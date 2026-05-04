# 🏥 Architecture Review — PBO-RAWAT-INAP
> **Senior Engineer Code Review & Refactoring Guide**  
> Project: J2ME Sistem Rawat Inap | Platform: MIDP 2.0 / CLDC 1.1  
> Repo: `https://github.com/tripmurpy/PBO-RAWAT-INAP`

---

## 1. Executive Summary

Proyek ini adalah aplikasi J2ME untuk manajemen rawat inap rumah sakit. Secara umum, struktur layer-nya cukup rapi dan mengikuti prinsip OOP yang benar. Namun setelah deep-dive ke seluruh 46 file, ditemukan beberapa **structural problems**, **critical bugs**, **duplicate code**, dan **maintainability risk** yang perlu segera diperbaiki.

| Kategori | Status | Severity |
|---|---|---|
| Layer Separation | ✅ Ada, tapi bocor di satu titik | Medium |
| Duplicate Code | ❌ Sangat tinggi di storage layer | High |
| Performance Bottleneck | ❌ Full-scan setiap query | High |
| Security Issue | ❌ Password hash lemah + hardcoded cred | Critical |
| Controller Layer Redundant | ❌ Pass-through 100%, no added value | Medium |
| ScreenManager Navigation Stack | ❌ Hanya simpan 1 layar sebelumnya | High |
| Kunjungan tidak dipersist | ❌ Data kunjungan tidak disimpan ke RMS | High |
| Service instantiation | ❌ Setiap panggil UI buat object baru | Medium |

---

## 2. Current Architecture (As-Is)

```
┌─────────────────────────────────────────────┐
│  PRESENTATION LAYER (ui/)                   │
│  LoginScreen, DashboardScreen, ...          │
│  Extends Canvas (J2ME LCDUI)                │
└────────────────┬────────────────────────────┘
                 │ creates new XxxController()
                 ▼
┌─────────────────────────────────────────────┐
│  CONTROLLER LAYER (controller/)             │
│  AuthController, PasienController, ...      │
│  [⚠ Semua method hanya forward ke Service] │
└────────────────┬────────────────────────────┘
                 │ creates new XxxService()
                 ▼
┌─────────────────────────────────────────────┐
│  SERVICE LAYER (service/)                   │
│  AuthService, PasienService, ...            │
│  Logika bisnis ada di sini ✅               │
└────────────────┬────────────────────────────┘
                 │ creates new XxxDB() — concrete class!
                 ▼
┌─────────────────────────────────────────────┐
│  STORAGE LAYER (storage/)                   │
│  PasienDB, DokterDB, ...                    │
│  Implements I*Repository                    │
└────────────────┬────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────┐
│  RMS (Record Management System)             │
│  J2ME built-in persistent storage          │
└─────────────────────────────────────────────┘
```

---

## 3. Problem Catalog

### 3.1 🔴 CRITICAL — Security: Password Hash Lemah

**File:** `src/model/User.java`

```java
// CURRENT — sangat lemah
public static String hashPassword(String password) {
    int hash = 7;
    for (int i = 0; i < password.length(); i++) {
        hash = hash * 31 + password.charAt(i);
    }
    return "H" + Integer.toHexString(hash & 0x7FFFFFFF);
}
```

**Masalah:**
- Java's `hashCode()` pattern, sangat mudah di-reverse dengan rainbow table
- Hash yang sama untuk string yang sama (tidak ada salt)
- Output hanya 8 hex chars = ruang 32-bit, bruteforceable

**Fix — Gunakan PBKDF2-inspired approach yang feasible di CLDC 1.1:**

```java
// IMPROVED — di User.java
public static String hashPassword(String password) {
    // Gabungkan password + salt statis + iterasi
    String salted = "RSI_SALT_v1_" + password + "_" + password.length();
    int hash1 = 17;
    int hash2 = 31;
    for (int i = 0; i < salted.length(); i++) {
        char c = salted.charAt(i);
        hash1 = hash1 * 37 + c;
        hash2 = hash2 * 53 + (c ^ (i + 7));
    }
    // Iterasi stretching (1000x)
    for (int round = 0; round < 1000; round++) {
        hash1 = hash1 * 31 ^ hash2;
        hash2 = hash2 * 37 ^ hash1;
    }
    String h1 = Long.toHexString(hash1 & 0xFFFFFFFFL);
    String h2 = Long.toHexString(hash2 & 0xFFFFFFFFL);
    return "S2$" + h1 + h2; // prefix S2$ = versi hash
}
```

---

### 3.2 🔴 CRITICAL — Hardcoded Default Credentials

**File:** `src/storage/UserDB.java`

```java
// CURRENT — jangan pernah hardcode credential!
public void inisialisasiDefault() throws Exception {
    if (findByUsername("abc") == null) {
        User abc = new User("USR-0002", "abc",
            User.hashPassword("abc"), "User ABC", User.ROLE_ADMIN);
        save(abc);
    }
}
```

**Masalah:**
- User `abc` dengan password `abc` adalah backdoor yang obvious
- Info default credentials tampil di UI Login Screen (`Default: admin / admin123`)

**Fix:**

```java
// IMPROVED — hapus user "abc", simpan hanya admin
public void inisialisasiDefault() throws Exception {
    if (!adaDataUser()) {
        User admin = new User(
            IDGenerator.generateUserId(), "admin",
            User.hashPassword("admin123"),
            "Administrator", User.ROLE_ADMIN
        );
        save(admin);
    }
}
```

Dan hapus baris ini dari `LoginScreen.java`:
```java
// HAPUS baris ini dari UI — expose credential!
g.drawString("Default: admin / admin123", ...);
```

---

### 3.3 🔴 HIGH — Duplicate Code di Semua Storage Class

Setiap `*DB.java` memiliki pattern yang **identik secara struktural**:

```java
// Pattern yang sama di PasienDB, DokterDB, RuanganDB, AdmisiDB, UserDB:
public XxxEntity findById(String id) throws Exception {
    RecordStore rs = null;
    try {
        rs = RMSUtil.bukaStore(STORE_NAME);
        RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
        while (re.hasNextElement()) {
            int rid = re.nextRecordId();
            XxxEntity e = deserialize(rs.getRecord(rid));
            e.setRecordId(rid);
            if (id.equals(e.getId())) return e;
        }
    } finally { RMSUtil.tutupStore(rs); }
    return null;
}
```

**Solusi:** Buat abstract base class `BaseDB` yang handle boilerplate:

```java
// BARU: src/storage/BaseDB.java
package storage;

import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import util.RMSUtil;

/**
 * BaseDB — Abstract template untuk semua *DB class.
 * Eliminasi 80% duplicate code di storage layer.
 * Template Method Pattern.
 */
public abstract class BaseDB {

    protected abstract String getStoreName();
    protected abstract byte[] serialize(Object entity) throws Exception;
    protected abstract Object deserialize(byte[] data) throws Exception;
    protected abstract void setRecordId(Object entity, int rid);
    protected abstract String getEntityId(Object entity);

    /** Simpan entity baru, set recordId setelah simpan */
    protected void saveEntity(Object entity) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            byte[] data = serialize(entity);
            int rid = RMSUtil.simpanRecord(rs, data);
            setRecordId(entity, rid);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /** Ambil semua entity sebagai Vector */
    protected Vector getAllEntities() throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                Object e = deserialize(rs.getRecord(rid));
                setRecordId(e, rid);
                hasil.addElement(e);
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    /** Cari entity berdasarkan string ID */
    protected Object findByStringId(String targetId) throws Exception {
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Object e = all.elementAt(i);
            if (targetId.equals(getEntityId(e))) return e;
        }
        return null;
    }

    /** Update entity berdasarkan recordId */
    protected void updateEntity(Object entity, int recordId) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            RMSUtil.updateRecord(rs, recordId, serialize(entity));
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /** Hapus entity berdasarkan recordId */
    protected void deleteEntity(int recordId) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            RMSUtil.hapusRecord(rs, recordId);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }
}
```

Dengan `BaseDB`, implementasi `PasienDB` menjadi jauh lebih ringkas:

```java
// IMPROVED: PasienDB.java — dari ~120 baris jadi ~60 baris
public class PasienDB extends BaseDB implements IPasienRepository {

    protected String getStoreName() { return "pasien_store"; }

    protected String getEntityId(Object e) { return ((Pasien) e).getNoRM(); }
    
    protected void setRecordId(Object e, int rid) { ((Pasien) e).setRecordId(rid); }

    public void save(Pasien p) throws Exception { saveEntity(p); }

    public Pasien findByRM(String noRM) throws Exception {
        return (Pasien) findByStringId(noRM);
    }

    public Vector getAll() throws Exception { return getAllEntities(); }

    public void update(Pasien p) throws Exception {
        updateEntity(p, p.getRecordId());
    }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    public Vector cariByNama(String keyword) throws Exception {
        Vector hasil = new Vector();
        Vector all = getAllEntities();
        String keyLower = keyword.toLowerCase();
        for (int i = 0; i < all.size(); i++) {
            Pasien p = (Pasien) all.elementAt(i);
            if (p.getNama().toLowerCase().indexOf(keyLower) >= 0)
                hasil.addElement(p);
        }
        return hasil;
    }

    protected byte[] serialize(Object obj) throws Exception {
        Pasien p = (Pasien) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(p.getNoRM()); dos.writeUTF(p.getNama());
        dos.writeLong(p.getTglLahir()); dos.writeUTF(p.getJenisKelamin());
        dos.writeUTF(p.getAlamat()); dos.writeUTF(p.getNoTelp());
        dos.writeUTF(p.getAsuransi());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Pasien p = new Pasien();
        p.setNoRM(dis.readUTF()); p.setNama(dis.readUTF());
        p.setTglLahir(dis.readLong()); p.setJenisKelamin(dis.readUTF());
        p.setAlamat(dis.readUTF()); p.setNoTelp(dis.readUTF());
        p.setAsuransi(dis.readUTF());
        dis.close();
        return p;
    }
}
```

---

### 3.4 🔴 HIGH — Performance: Full-Scan pada Setiap Query

**File:** Semua `*DB.java`

```java
// Setiap findById → buka store → enumerate semua → tutup
// Kompleksitas: O(n) setiap operasi read
// Pada data besar, ini sangat lambat di device J2ME
```

**Masalah:** RMS tidak punya indexing. Setiap query harus scan semua record.

**Fix — In-Memory Cache dengan Lazy Loading:**

```java
// Tambahkan di BaseDB atau masing-masing *DB yang sering diakses
private Vector cache = null;
private boolean cacheValid = false;

protected Vector getAllEntities() throws Exception {
    if (cacheValid && cache != null) return cache; // cache hit
    cache = loadFromRMS(); // baru baca RMS kalau cache miss
    cacheValid = true;
    return cache;
}

protected void invalidateCache() {
    cacheValid = false;
    cache = null;
}

// Panggil invalidateCache() setelah save, update, delete
protected void saveEntity(Object entity) throws Exception {
    // ... RMS write ...
    invalidateCache();
}
```

Ini mengurangi jumlah pembacaan RMS secara drastis untuk operasi yang berulang dalam satu sesi.

---

### 3.5 🟡 MEDIUM — Controller Layer Redundant (Pass-Through 100%)

**File:** Semua `controller/*.java`

```java
// CONTOH AuthController.java
public User login(String username, String password) throws Exception {
    String error = Validator.validasiLogin(username, password);   // ← satu-satunya tambahan
    if (error != null) throw new Exception(error);
    return authService.login(username.trim(), password);
}

// Tapi AuthService.login() juga langsung bisa dipanggil dari UI
// PasienController, DokterController, RuanganController = 100% pass-through
```

**Opsi A — Hapus Controller, merge validasi ke Service (lebih clean):**

```java
// Di PasienService.daftarBaru() — tambahkan validasi yang tadinya di Controller:
public Pasien daftarBaru(String nama, ...) throws Exception {
    String error = Validator.validasiPasien(nama, ...); // sudah ada ini!
    if (error != null) throw new Exception(error);
    // ...
}
// → UI bisa langsung panggil Service, Controller dihapus
```

**Opsi B — Biarkan Controller tapi berikan tanggung jawab nyata:**

Jadikan Controller tempat untuk:
- Agregasi data dari beberapa Service
- Transform/mapping data untuk UI
- Logging/audit trail

Dalam konteks J2ME akademis, **Opsi A** lebih pragmatis — kurangi jumlah file tanpa mengorbankan kualitas.

---

### 3.6 🟡 MEDIUM — ScreenManager Navigation Stack Dangkal

**File:** `src/ui/ScreenManager.java`

```java
// CURRENT — hanya simpan 1 layar sebelumnya
private Displayable layarSebelumnya; // ← bukan stack!

public void kembali() {
    if (layarSebelumnya != null) {
        display.setCurrent(layarSebelumnya);
        layarSebelumnya = null; // setelah balik, history hilang
    }
}
```

**Masalah:** Jika user navigasi: Login → Dashboard → PasienList → PasienForm, lalu tekan "Back", mereka akan kembali ke PasienList. Tapi kalau tekan "Back" lagi, `layarSebelumnya` sudah `null` — user terjebak.

**Fix — Gunakan Stack sederhana (J2ME-compatible):**

```java
// IMPROVED: ScreenManager.java
public class ScreenManager {

    private static ScreenManager instance;
    private Display display;
    private MIDlet midlet;
    private Vector historyStack; // Stack navigasi!

    private ScreenManager() {
        historyStack = new Vector();
    }

    public void tampilkanLayar(Displayable layar) {
        Displayable current = display.getCurrent();
        if (current != null) {
            historyStack.addElement(current); // push ke stack
        }
        display.setCurrent(layar);
    }

    public void kembali() {
        if (historyStack.isEmpty()) return;
        int last = historyStack.size() - 1;
        Displayable prev = (Displayable) historyStack.elementAt(last);
        historyStack.removeElementAt(last); // pop dari stack
        display.setCurrent(prev);
    }

    public void logout() {
        historyStack.removeAllElements(); // clear seluruh history
        display.setCurrent(new LoginScreen());
    }

    public boolean bisaKembali() {
        return !historyStack.isEmpty();
    }
}
```

---

### 3.7 🟡 MEDIUM — KunjunganService Tidak Persist Data

**File:** `src/service/KunjunganService.java`

```java
// CURRENT — Kunjungan hanya dibuat di memory, tidak ada storage layer!
public Kunjungan buatKunjungan(Admisi admisi, ...) {
    String id = IDGenerator.generateKunjunganId();
    Kunjungan knj = new Kunjungan(id, ...);
    return knj; // tidak disimpan ke RMS!
}

// getRiwayatKunjungan() pun hanya konversi Admisi jadi Kunjungan
// Tidak ada IKunjunganRepository, tidak ada KunjunganDB
```

**Dampak:** Data kunjungan (catatan perawatan harian, observasi dokter) tidak persisten. Tidak bisa dipanggil kembali di sesi berikutnya.

**Fix — Tambahkan KunjunganDB (atau simplify dengan remove entity Kunjungan):**

Karena Kunjungan pada dasarnya adalah view dari data Admisi, pertimbangkan dua opsi:

**Opsi A (Pragmatis):** Hapus model `Kunjungan` dan `KunjunganService`. Tampilkan riwayat langsung dari `AdmisiService.getSemuaAdmisi()` + lookup Pasien/Dokter. Kurangi kompleksitas.

**Opsi B (Full Implementation):** Buat `IKunjunganRepository` + `KunjunganDB` yang lengkap untuk menyimpan catatan perawatan harian.

---

### 3.8 🟡 MEDIUM — Service Dependency Hard-Coded (No DI)

**File:** `src/service/AdmisiService.java`

```java
// CURRENT — konkret class, tidak bisa diganti saat testing
public AdmisiService() {
    this.admisiDB = new AdmisiDB();       // ← hard dependency
    this.pasienService = new PasienService(); // ← buat instance baru!
    this.dokterService = new DokterService();
    this.ruanganService = new RuanganService();
}
```

**Masalah:**
1. `AdmisiService` buat `PasienService` baru, yang buat `PasienDB` baru. Tapi di UI, juga ada `PasienController` yang buat `PasienService` baru lagi. Artinya ada **banyak instance** dari service yang sama — tidak ada singleton, cache tidak shared antar instance.
2. Tidak bisa inject mock untuk testing.

**Fix — Dependency Injection via Constructor:**

```java
// IMPROVED: AdmisiService.java
public class AdmisiService {

    private IAdmisiRepository admisiRepo;
    private IPasienRepository pasienRepo;
    private IDokterRepository dokterRepo;
    private IRuanganRepository ruanganRepo;

    // Constructor DI — gunakan interface, bukan concrete class
    public AdmisiService(IAdmisiRepository admisiRepo,
                         IPasienRepository pasienRepo,
                         IDokterRepository dokterRepo,
                         IRuanganRepository ruanganRepo) {
        this.admisiRepo = admisiRepo;
        this.pasienRepo = pasienRepo;
        this.dokterRepo = dokterRepo;
        this.ruanganRepo = ruanganRepo;
    }
}
```

Dan buat `ServiceFactory` singleton:

```java
// BARU: src/service/ServiceFactory.java
public class ServiceFactory {
    private static ServiceFactory instance;

    // Shared repository instances (satu per store)
    private IAdmisiRepository admisiRepo;
    private IPasienRepository pasienRepo;
    private IDokterRepository dokterRepo;
    private IRuanganRepository ruanganRepo;
    private IUserRepository userRepo;

    // Shared service instances
    private AdmisiService admisiService;
    private PasienService pasienService;

    private ServiceFactory() {
        admisiRepo  = new AdmisiDB();
        pasienRepo  = new PasienDB();
        dokterRepo  = new DokterDB();
        ruanganRepo = new RuanganDB();
        userRepo    = new UserDB();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) instance = new ServiceFactory();
        return instance;
    }

    public AdmisiService getAdmisiService() {
        if (admisiService == null)
            admisiService = new AdmisiService(admisiRepo, pasienRepo,
                                               dokterRepo, ruanganRepo);
        return admisiService;
    }

    public PasienService getPasienService() {
        if (pasienService == null)
            pasienService = new PasienService(pasienRepo);
        return pasienService;
    }
    // ... dst
}
```

---

### 3.9 🟡 LOW — IDGenerator: Duplicate Pattern dengan NoRMGenerator

**File:** `src/util/IDGenerator.java` & `src/util/NoRMGenerator.java`

Kedua file memiliki logika `ambilDanNaikkanCounter()` yang **identik secara struktural**. NoRMGenerator adalah subset dari IDGenerator.

**Fix — Merge ke IDGenerator:**

```java
// Di IDGenerator.java — tambahkan:
public static String generateNoRM() {
    int counter = ambilDanNaikkanCounter("noRM");
    String tanggal = DateUtil.formatTanggalKompak(DateUtil.sekarang());
    return new StringBuffer()
        .append("RM-").append(tanggal).append("-")
        .append(padTigaDigit(counter)).toString();
}

private static String padTigaDigit(int n) {
    if (n < 10) return "00" + n;
    if (n < 100) return "0" + n;
    return String.valueOf(n);
}
```

Hapus `NoRMGenerator.java`. Kurangi 1 file.

---

### 3.10 🟡 LOW — LoginScreen: Koordinat Hard-Coded di pointerPressed()

**File:** `src/ui/LoginScreen.java`

```java
protected void pointerPressed(int x, int y) {
    // Koordinat dihitung ulang manual! Harus sinkron dengan paint()
    int cardY = h / 8 + Font.getFont(...).getHeight() + 4 +
                Font.getFont(...).getHeight() + 20;
    int btnY = cardY + 160 + 15; // ← magic number 160!
```

**Masalah:** Koordinat layout di `paint()` dan `pointerPressed()` dihitung secara terpisah. Jika ada perubahan layout di `paint()`, `pointerPressed()` tidak akan sinkron → klik tidak akurat.

**Fix — Ekstrak koordinat sebagai field instance:**

```java
public class LoginScreen extends Canvas {
    // Layout coordinates — dihitung sekali di paint(), dipakai di pointer()
    private int btnY, btnH, usernameFieldY, passwordFieldY, fieldX, fieldW;

    protected void paint(Graphics g) {
        // Hitung dan simpan ke field
        this.btnY = cardY + cardH + 15;
        this.btnH = 35;
        // ... gunakan field ini untuk menggambar ...
    }

    protected void pointerPressed(int x, int y) {
        // Gunakan field yang sudah dihitung paint()
        if (x >= fieldX && x <= fieldX + fieldW && 
            y >= btnY && y <= btnY + btnH) {
            prosesLogin();
        }
    }
}
```

---

## 4. Improved Architecture (To-Be)

```
┌──────────────────────────────────────────────────────────────┐
│  PRESENTATION LAYER (ui/)                                    │
│  HospitalMIDlet, ScreenManager (full stack), *Screen        │
│  → Panggil langsung ke Service via ServiceFactory           │
└──────────────────────────────┬───────────────────────────────┘
                               │ ServiceFactory.getInstance()
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  SERVICE FACTORY (service/ServiceFactory.java) [NEW]         │
│  Singleton — kelola lifecycle semua Service dan Repository   │
└──────────────────────────────┬───────────────────────────────┘
                               │ DI via constructor
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  SERVICE LAYER (service/)                                    │
│  *Service menerima I*Repository via constructor              │
│  Logika bisnis bersih, tidak tahu implementasi DB            │
└──────────────────────────────┬───────────────────────────────┘
                               │ implements interface
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  REPOSITORY INTERFACES (model/repository/)                   │
│  I*Repository — kontrak CRUD yang jelas                     │
└──────────────────────────────┬───────────────────────────────┘
                               │ extends BaseDB
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  STORAGE LAYER (storage/)                                    │
│  BaseDB [NEW] + *DB extends BaseDB                          │
│  In-memory cache per store                                   │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────┐
│  RMS (javax.microedition.rms)                                │
└──────────────────────────────────────────────────────────────┘
```

### Perubahan Layer

| Layer | Before | After | Impact |
|---|---|---|---|
| Controller | 6 file pass-through | Dihapus atau digabung ke Service | -6 file |
| Storage | 5 file dengan ~80% duplikasi | 5 file extends BaseDB | -60% baris kode |
| Service | Hard-coded dependencies | DI via constructor + factory | Testable |
| ScreenManager | 1 layar history | Stack penuh | Navigation fixed |
| Utils | 2 ID generator terpisah | 1 IDGenerator + merged | -1 file |
| Security | Weak hash | Iterative hash | Lebih aman |

---

## 5. File-by-File Action Plan

### Files to CREATE

| File | Alasan |
|---|---|
| `src/storage/BaseDB.java` | Eliminate duplicate CRUD pattern |
| `src/service/ServiceFactory.java` | Centralized DI, single instance |

### Files to MODIFY

| File | Perubahan |
|---|---|
| `src/model/User.java` | Perkuat `hashPassword()` |
| `src/storage/UserDB.java` | Hapus user backdoor `abc` |
| `src/ui/LoginScreen.java` | Hapus tampilan credential; fix koordinat |
| `src/ui/ScreenManager.java` | Ganti `layarSebelumnya` dengan `Vector` stack |
| `src/storage/*.java` | Extends `BaseDB`, kurangi boilerplate |
| `src/service/*.java` | Tambah constructor DI |
| `src/util/IDGenerator.java` | Merge logika dari `NoRMGenerator` |

### Files to DELETE

| File | Alasan |
|---|---|
| `src/controller/PasienController.java` | Pure pass-through |
| `src/controller/DokterController.java` | Pure pass-through |
| `src/controller/RuanganController.java` | Pure pass-through |
| `src/controller/KunjunganController.java` | Pure pass-through |
| `src/util/NoRMGenerator.java` | Duplicate, digabung ke IDGenerator |

> `AuthController` boleh dipertahankan karena ada validasi tambahan `validasiLogin()`.  
> `AdmisiController` boleh dipertahankan karena ada potensi logika agregasi discharge.

---

## 6. Code Quality Metrics

### Before vs After (Estimated)

| Metric | Before | After |
|---|---|---|
| Total file | 46 | 41 |
| Baris kode storage layer | ~700 | ~300 |
| Duplikasi kode | ~65% di storage | <10% |
| RMS open per query | 1x per query | 0x jika cache hit |
| Password hash strength | 32-bit | ~64-bit + stretching |
| Navigation depth | 1 | Unlimited |
| Service instances per action | N (baru setiap UI) | 1 (singleton factory) |

---

## 7. Quick Wins (Implementasi Prioritas)

Kalau waktu terbatas, implementasikan dalam urutan ini:

**Priority 1 (Security — harus sekarang):**
1. Hapus user `abc` dari `UserDB.inisialisasiDefault()`
2. Hapus tampilan credential di `LoginScreen`
3. Perkuat `hashPassword()` di `User`

**Priority 2 (Correctness — navigation broken):**
4. Fix `ScreenManager` dengan proper navigation stack

**Priority 3 (Maintainability — technical debt):**
5. Buat `BaseDB` dan refactor semua `*DB`
6. Merge `NoRMGenerator` ke `IDGenerator`
7. Fix koordinat `pointerPressed()` di `LoginScreen`

**Priority 4 (Architecture — long term):**
8. Buat `ServiceFactory`
9. Inject dependencies via constructor di semua Service
10. Hapus controller yang pure pass-through

---

## 8. What's Already Good ✅

Sebelum tutup, penting untuk acknowledge hal yang sudah benar:

- **Layer separation** — Model tidak tahu tentang RMS. Service tidak tahu tentang UI. Prinsip ini sudah diikuti.
- **Repository Interface** — Ada `I*Repository` di setiap entitas. Ini pattern yang tepat.
- **RMSUtil** — Helper class ini bersih dan reusable. Tidak perlu diubah.
- **Validator** — Logika validasi terpusat, bukan tersebar di UI.
- **DateUtil** — Manual parsing tanggal untuk CLDC 1.1 diimplementasikan dengan benar.
- **Error handling** — Semua operasi RMS dibungkus try-finally yang benar.
- **Model constants** — `Admisi.STATUS_AKTIF`, `Ruangan.STATUS_KOSONG` dll. sudah benar.
- **Komentar OOP** — Konsisten menandai ENCAPSULATION, POLYMORPHISM, INHERITANCE.

---

## 9. Summary Diagram — Problems & Fixes

```
CRITICAL  ██████████████████████████████████████████  HIGH
├── Weak password hash ──────────────────────► Iterative hash
├── Hardcoded backdoor credentials ──────────► Remove abc user
│
HIGH
├── Storage layer 80% duplicate ─────────────► BaseDB abstract class
├── O(n) full-scan every query ──────────────► In-memory cache
├── ScreenManager 1-deep history ────────────► Vector stack
├── Kunjungan tidak dipersist ───────────────► KunjunganDB / simplify
│
MEDIUM
├── Controller = pass-through ───────────────► Delete atau berikan responsibility
├── Service creates new deps setiap kali ────► ServiceFactory singleton + DI
│
LOW
├── IDGenerator dup NoRMGenerator ───────────► Merge ke IDGenerator
└── LoginScreen hardcoded coordinates ──────► Extract ke instance fields
```

---

*Review ini dibuat berdasarkan analisis lengkap terhadap 46 file source code proyek PBO-RAWAT-INAP. Semua saran mempertimbangkan constraint platform J2ME MIDP 2.0 / CLDC 1.1 yang tidak mendukung generics, lambda, atau Java standard library modern.*