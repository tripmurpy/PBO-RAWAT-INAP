**BLUEPRINT**

**Sistem Rawat Inap Rumah Sakit**

*Java J2ME (MIDP 2.0 / CLDC 1.1)*

Production-Ready Architecture Document

Pasien • Dokter • Ruangan • Obat • Rekam Medis • Laporan

Versi 1.0  |  Mei 2026

# **Daftar Isi**

1\. Ringkasan Eksekutif

2\. Analisis Requirements

3\. Edge Cases & Risiko

4\. Arsitektur Sistem (5-Layer)

5\. Domain Model & Penerapan OOP

6\. Strategi Penyimpanan (RMS)

7\. Struktur File Project

8\. Flowchart Sistem

9\. Spesifikasi Fitur per Modul

10\. Rencana Implementasi (Coding Order)

11\. Strategi Testing

12\. Checklist Production-Ready

# **1\. Ringkasan Eksekutif**

Dokumen ini adalah blueprint produksi untuk Sistem Rawat Inap Rumah Sakit berbasis Java J2ME, dirancang untuk perangkat klasik MIDP 2.0 / CLDC 1.1 (Nokia, Sony Ericsson era). Sistem berfungsi penuh secara offline menggunakan Record Management System (RMS) sebagai persistence layer.

### **Tujuan Sistem**

* Mendigitalkan proses administrasi rawat inap di rumah sakit kecil/menengah.

* Menyediakan single source of truth untuk data pasien, dokter, ruangan, dan obat.

* Mendukung alur lengkap: admisi → perawatan → rekam medis → resep → laporan → discharge.

* Menjamin integritas data lewat referential integrity di lapis service.

* Memberi laporan ringkas (occupancy, stok, diagnosa) yang dapat diakses langsung di handset.

### **Karakteristik Teknis**

* Platform target: MIDP 2.0 \+ CLDC 1.1.

* Storage: RMS (RecordStore terpisah per entitas).

* UI: LCDUI native (Form, List, Alert) — bukan Canvas custom.

* Heap budget: aman di 512 KB; record size dijaga di bawah 4 KB per entry.

* Output build: file .jar \+ .jad \+ MANIFEST.MF.

* Toolchain: Sun WTK 2.5.2, JDK 8, Apache Ant, MicroEmu, ProGuard (opsional).

### **Prinsip Arsitektur**

* Separation of Concerns: 5 lapis (UI → Controller → Service → Model → Storage).

* OOP murni: inheritance, encapsulation, polymorphism diterapkan di domain model.

* Singleton ScreenManager untuk navigasi yang konsisten.

* Fail-safe RMS: setiap operasi I/O dibungkus try/catch dengan recovery path.

* Simplicity first: tidak ada framework eksternal, tidak ada library tambahan.

# **2\. Analisis Requirements**

## **2.1 Functional Requirements**

Sistem harus mendukung operasi-operasi inti berikut, dikelompokkan per modul:

### **Modul Autentikasi**

* Login dengan username \+ password (role: Admin).

* Session sederhana di memory (logout \= clear session).

* Initial seeding admin default pada first run.

### **Modul Pasien**

* CRUD pasien (NIK, nama, tanggal lahir, jenis kelamin, alamat, no. HP, golongan darah).

* Validasi NIK unik (16 digit).

* Search pasien berdasarkan nama / NIK.

### **Modul Dokter (RMS)**

* CRUD dokter (kode, nama, spesialisasi, no. STR, jadwal praktek).

* Status aktif/non-aktif (soft delete jika masih punya pasien aktif).

### **Modul Ruangan/Kamar (RMS)**

* CRUD ruangan (kode, nama, kelas: VIP/I/II/III, kapasitas, tarif/hari).

* Tracking occupancy real-time (slot terisi vs kapasitas).

* Penanda status: tersedia, penuh, maintenance.

### **Modul Obat (RMS)**

* CRUD obat (kode, nama, bentuk: tablet/sirup/injeksi, satuan, stok, harga).

* Penyesuaian stok otomatis saat resep ditambahkan.

* Alert stok minimum (low-stock warning).

### **Modul Admisi & Rekam Medis**

* Admisi: assign pasien ke kamar \+ dokter penanggung jawab (DPJP).

* Generate nomor rekam medis: RM-YYYYMMDD-XXX.

* Catatan harian (vital signs, keluhan, diagnosa, tindakan).

* Resep obat per kunjungan (link ke entitas Obat, kurangi stok).

* Discharge: catat tanggal pulang, hitung total tarif kamar \+ obat.

### **Modul Laporan & Charts**

* Laporan occupancy ruangan (per hari).

* Laporan stok obat & list low-stock.

* Laporan pasien aktif (sedang dirawat).

* Statistik diagnosa terbanyak (text-based bar chart sederhana di Canvas).

## **2.2 Non-Functional Requirements**

| Aspek | Target | Strategi |
| :---- | :---- | :---- |
| Performa | Operasi list \< 1 detik untuk 100 record | Pre-load index, lazy detail fetch |
| Memori | Heap usage \< 512 KB | Stream record, hindari load-all |
| Persistensi | Data tidak hilang saat aplikasi tertutup | RMS commit per operasi |
| Reliability | Tidak crash saat RMS error | try/catch \+ fallback ke Alert |
| Usability | Operasional dengan keypad numeric | List \+ Form native LCDUI |
| Maintainability | Modular, mudah diuji unit-per-unit | 5-layer architecture, DAO pattern |
| Portability | Berjalan di emulator & device asli | Hindari class non-CLDC 1.1 |

# **3\. Edge Cases & Risiko**

Identifikasi edge cases adalah kunci sistem production-ready. Berikut katalog lengkap kasus tepi yang sudah dipetakan beserta strategi penanganannya.

## **3.1 Edge Cases Data Integrity**

| Kategori | Edge Case | Penanganan |
| :---- | :---- | :---- |
| Duplikasi | NIK pasien sudah ada | Validasi di service layer sebelum write; tampilkan Alert error |
| Duplikasi | Kode dokter/obat duplikat | Auto-generate unik (DKT-001, OBT-001) di IdGenerator |
| Referential | Hapus dokter yang masih DPJP pasien aktif | Soft delete (status=NON\_AKTIF), block hard delete |
| Referential | Hapus obat yang sudah masuk resep historis | Soft delete; obat tetap muncul di rekam medis lama |
| Referential | Hapus ruangan yang ada pasien dirawat | Tolak operasi; tampilkan list pasien yang masih okupansi |
| Validasi | Tanggal pulang \< tanggal masuk | Validasi di AdmisiService.discharge() |
| Validasi | Field kosong/null | Required-field check di Controller sebelum forward ke Service |
| Validasi | NIK bukan 16 digit | Regex sederhana via String.length() \+ char-by-char digit check |

## **3.2 Edge Cases Bisnis**

| Edge Case | Penanganan |
| :---- | :---- |
| Kamar penuh saat admisi | RuanganService.cekKapasitas() — return error & sarankan kamar lain |
| Stok obat 0 saat resep ditambah | ObatService.kurangiStok() throw StokHabisException; controller tampil Alert |
| Stok obat menjadi negatif | Atomic check-and-decrement; tolak jika stok \< quantity |
| Resep \> batas wajar (mis. \> 1000 unit) | Validasi maksimum quantity di form input |
| Pasien sudah discharge tapi rekam medis open | discharge() set status REKAM\_MEDIS=CLOSED \+ lock edit |
| Dokter assigned ke 2 pasien di waktu sama (jam praktek) | Out of scope versi 1; flagged untuk v2 |
| Tanggal masuk di masa depan | Validasi: tanggal masuk ≤ today |
| Pasien yang sama admisi 2x bersamaan | Cek status pasien (DIRAWAT) sebelum admisi baru |

## **3.3 Edge Cases Teknis (J2ME-Specific)**

| Edge Case | Penanganan |
| :---- | :---- |
| RMS belum ada (first run) | RMSUtil.openOrCreate() — auto-create \+ seed default data |
| RecordStore corrupt (RecordStoreException) | Catch \+ tawarkan reset RMS via menu admin |
| Record size \> 4 KB (rekam medis panjang) | Truncate text field di Controller, max 500 char per catatan |
| RMS quota habis (RecordStoreFullException) | Alert \+ sarankan archive/export data lama |
| OutOfMemoryError saat load list besar | Paging: load 20 record per halaman |
| SimpleDateFormat tidak ada di CLDC 1.1 | DateUtil custom (format manual yyyy-MM-dd) |
| Long → byte array conversion | DataOutputStream.writeLong() di RMSUtil |
| Encoding karakter non-ASCII (nama pasien) | Pakai UTF-8 explicit di DataInputStream/OutputStream |
| Aplikasi force-close saat write RMS | Operasi atomik per record; tidak ada partial write multi-record |
| MIDlet pause/resume | Re-open RMS di startApp(); close di destroyApp() |

## **3.4 Risk Matrix**

| Risiko | Probabilitas | Dampak | Mitigasi |
| :---- | :---- | :---- | :---- |
| RMS corruption | Rendah | Tinggi | Backup export ke file (jika FileConnection optional API tersedia) |
| Memori penuh | Sedang | Tinggi | Paging \+ soft-delete archive |
| Data race (multi-MIDlet) | Sangat rendah | Sedang | Single-instance MIDlet (J2ME default behavior) |
| Salah input clinical (overdose) | Sedang | Tinggi | Validasi range quantity \+ double-check Alert |
| Lupa logout (session aktif) | Tinggi | Rendah | Auto-logout setelah idle (timer 5 menit) |

# **4\. Arsitektur Sistem (5-Layer)**

Sistem mengikuti pola arsitektur 5-layer yang konsisten dengan project rawat jalan, untuk menjaga kohesi dan kemudahan rotasi developer antar project.

## **4.1 Diagram Lapisan**

\+------------------------------------------------------------+  
|  LAYER 1: UI (View)                                        |  
|  Form, List, Alert, Canvas (chart)                         |  
|  Tugas: render screen, capture user input                  |  
\+------------------------------------------------------------+  
                          |  
                          v  
\+------------------------------------------------------------+  
|  LAYER 2: CONTROLLER                                       |  
|  PasienController, AdmisiController, ObatController, ...   |  
|  Tugas: handle CommandAction, validasi input ringan,       |  
|         rute ke Service, navigasi via ScreenManager        |  
\+------------------------------------------------------------+  
                          |  
                          v  
\+------------------------------------------------------------+  
|  LAYER 3: SERVICE (Business Logic)                         |  
|  AdmisiService, ResepService, LaporanService, ...          |  
|  Tugas: orchestration, transaksi multi-entity, business    |  
|         rules (cek kapasitas, kurangi stok, dll)           |  
\+------------------------------------------------------------+  
                          |  
                          v  
\+------------------------------------------------------------+  
|  LAYER 4: MODEL (Domain)                                   |  
|  Person (abstract) \-\> Pasien, Dokter, Perawat              |  
|  Entity, RekamMedis, Resep, Admisi, Ruangan, Obat          |  
|  Tugas: representasi data \+ perilaku domain                |  
\+------------------------------------------------------------+  
                          |  
                          v  
\+------------------------------------------------------------+  
|  LAYER 5: STORAGE (DAO \+ RMS)                              |  
|  PasienDB, DokterDB, RuanganDB, ObatDB, AdmisiDB, ...      |  
|  RMSUtil (helper serialize/deserialize)                    |  
|  Tugas: persistensi via RecordStore                        |  
\+------------------------------------------------------------+

## **4.2 Aturan Komunikasi Antar Lapisan**

* UI hanya bicara ke Controller — TIDAK PERNAH langsung ke Service/DAO.

* Controller bicara ke Service (atau langsung DAO untuk read sederhana).

* Service bicara ke DAO dan model lain — tidak pernah ke UI.

* Model bersih dari I/O; hanya state \+ perilaku domain.

* Storage tidak tahu ada Service atau Controller; murni teknis I/O.

* Singleton ScreenManager dipanggil dari Controller untuk navigasi.

## **4.3 Komponen Cross-Cutting**

| Komponen | Lokasi | Tanggung Jawab |
| :---- | :---- | :---- |
| ScreenManager | util/ | Singleton stack-based navigation antar Displayable |
| RMSUtil | storage/ | Serialize/deserialize byte\[\] generik via DataInputStream/OutputStream |
| DateUtil | util/ | Format & parse tanggal manual (CLDC 1.1 friendly) |
| IdGenerator | util/ | Generate kode unik (RM-YYYYMMDD-XXX, DKT-001, dst) |
| Session | util/ | Holder user yang sedang login \+ role |
| Validator | util/ | Validasi NIK, tanggal, range numerik |
| AppException | exception/ | Hierarki: ValidationException, NotFoundException, StokHabisException, ... |

# **5\. Domain Model & Penerapan OOP**

Bagian ini menjelaskan bagaimana inheritance, encapsulation, dan polymorphism diterapkan secara konkret di domain model — bukan hanya teori.

## **5.1 Class Hierarchy (Inheritance)**

                    \+---------------+  
                    |   Entity      |  \<-- abstract root  
                    | \- id : String |  
                    | \+ getId()     |  
                    | \+ toBytes()   |  abstract  
                    | \+ fromBytes() |  abstract  
                    \+-------+-------+  
                            |  
         \+------------------+------------------+  
         |                  |                  |  
    \+----v----+        \+----v----+        \+----v----+  
    | Person  |        | Ruangan |        |  Obat   |  
    \+----+----+        \+---------+        \+---------+  
         |  
    \+----+--------+--------+  
    |             |        |  
  \+-v----+    \+---v---+ \+--v-----+  
  |Pasien|    |Dokter | |Perawat |  
  \+------+    \+-------+ \+--------+  
   
       \+------------+  
       | RekamMedis |  \-- composes \--\>  List\<CatatanHarian\>, List\<Resep\>  
       \+------------+  
   
       \+---------+  
       | Admisi  |  \-- references \--\>  Pasien, Dokter, Ruangan  
       \+---------+

## **5.2 Encapsulation**

Setiap class domain menerapkan encapsulation ketat:

* Semua field private.

* Akses lewat getter/setter; setter melakukan validasi (mis. setNIK() cek 16 digit).

* Object construction lewat constructor lengkap — tidak ada setter publik untuk ID setelah dibuat.

* Internal state seperti totalTarif di Admisi adalah computed (method), bukan field — supaya tidak bisa drift dari kenyataan.

### **Contoh: class Pasien**

public class Pasien extends Person {  
    private String nik;          // 16 digit  
    private String alamat;  
    private String golDarah;  
    private String noHP;  
    private String status;       // AKTIF, DIRAWAT, PULANG, NON\_AKTIF  
   
    public Pasien(String id, String nama, String tglLahir,  
                  char jk, String nik, String alamat,  
                  String golDarah, String noHP) {  
        super(id, nama, tglLahir, jk);  
        setNIK(nik);             // validasi di setter  
        this.alamat \= alamat;  
        this.golDarah \= golDarah;  
        this.noHP \= noHP;  
        this.status \= "AKTIF";  
    }  
   
    public void setNIK(String nik) {  
        if (nik \== null || nik.length() \!= 16\)  
            throw new IllegalArgumentException("NIK harus 16 digit");  
        this.nik \= nik;  
    }  
   
    // getter lain ... toBytes() / fromBytes() override  
}

## **5.3 Polymorphism**

Polymorphism dimanfaatkan di tiga titik utama:

### **(1) Serialisasi Generik via Entity**

Method abstrak toBytes() / fromBytes() di Entity di-override per subclass. RMSUtil bisa menyimpan tipe apapun selama mengimplementasikan kontrak Entity:

public abstract class Entity {  
    protected String id;  
    public String getId() { return id; }  
    public abstract byte\[\] toBytes() throws IOException;  
    public abstract void fromBytes(byte\[\] data) throws IOException;  
}  
   
// di RMSUtil:  
public static int simpan(String storeName, Entity e) throws ... {  
    RecordStore rs \= RecordStore.openRecordStore(storeName, true);  
    byte\[\] data \= e.toBytes();           // polimorfik  
    int recId \= rs.addRecord(data, 0, data.length);  
    rs.closeRecordStore();  
    return recId;  
}

### **(2) Method Override: tampilkan() pada Person**

// Person  
public String tampilkan() {  
    return nama \+ " (" \+ id \+ ")";  
}  
   
// Dokter override \-\> tambah spesialisasi  
public String tampilkan() {  
    return "dr. " \+ getNama() \+ " \- Sp." \+ spesialisasi;  
}  
   
// Pasien override \-\> tambah status  
public String tampilkan() {  
    return getNama() \+ " \[" \+ status \+ "\]";  
}

### **(3) Polymorphic List di UI**

List Person bisa dirender seragam tanpa peduli subclass-nya — UI cukup memanggil tampilkan().

## **5.4 Composition (lebih kuat dari inheritance untuk relasi)**

* RekamMedis composes List\<CatatanHarian\> dan List\<Resep\>.

* Admisi tidak meng-extend Pasien; ia menyimpan referensi pasienId, dokterId, ruanganId (foreign-key style).

* Resep composes List\<ItemObat\> (Obat \+ jumlah \+ dosis).

* Alasan: relasi 'punya' bukan 'adalah'. Inheritance disalahgunakan \= fragile design.

# **6\. Strategi Penyimpanan (RMS)**

## **6.1 Pola: One RecordStore per Entity**

Setiap entitas punya RecordStore terpisah. Pola ini sederhana, mudah di-reset per modul, dan menghindari problem schema migration.

| RecordStore | Entitas | Estimasi Size/Record | Strategi Akses |
| :---- | :---- | :---- | :---- |
| UserDB | User (admin/dokter/perawat) | \~80 byte | Load all (max \~20 user) |
| PasienDB | Pasien | \~250 byte | Index by NIK; paging 20 |
| DokterDB | Dokter | \~200 byte | Load all (max \~50) |
| RuanganDB | Ruangan/Kamar | \~120 byte | Load all (max \~30) |
| ObatDB | Obat | \~180 byte | Load all (max \~200) |
| AdmisiDB | Admisi (header) | \~150 byte | Filter by status |
| RekamMedisDB | RekamMedis | \~3 KB (komposit) | Load by admisiId |
| ResepDB | Resep | \~500 byte | Load by rekamMedisId |

## **6.2 Format Serialisasi**

Pakai DataOutputStream / DataInputStream — format binary kompak, ada di MIDP 2.0:

// toBytes() pada Pasien:  
ByteArrayOutputStream baos \= new ByteArrayOutputStream();  
DataOutputStream dos \= new DataOutputStream(baos);  
dos.writeUTF(id);  
dos.writeUTF(nama);  
dos.writeUTF(tglLahir);  
dos.writeChar(jk);  
dos.writeUTF(nik);  
dos.writeUTF(alamat);  
dos.writeUTF(golDarah);  
dos.writeUTF(noHP);  
dos.writeUTF(status);  
dos.flush();  
return baos.toByteArray();

## **6.3 Versi Schema**

Tambahkan satu byte versi di awal setiap record. Saat load, cek versi dulu — jika tidak match, jalankan migrator atau skip record.

private static final byte SCHEMA\_VERSION \= 1;  
   
// toBytes:  
dos.writeByte(SCHEMA\_VERSION);  
// ... field lain  
   
// fromBytes:  
byte ver \= dis.readByte();  
if (ver \!= SCHEMA\_VERSION)  
    throw new IOException("Schema mismatch");

## **6.4 Indexing Sederhana**

RMS tidak punya index. Strategi:

* Untuk lookup by ID: simpan map in-memory String id → int recordId saat startup.

* Untuk search by nama: linear scan dengan RecordEnumeration; OK karena dataset kecil.

* Untuk filter status: RecordFilter custom yang baca byte\[\] dan cek field status.

* Hindari sort di sisi RMS — load lalu sort di memory pakai bubble sort sederhana.

## **6.5 Seeding Default Data**

Pada first run (RecordStore belum ada), MainMIDlet menjalankan SeedData.run() yang mengisi:

* 1 admin: username=admin, password=admin

* 3 dokter contoh \+ 2 perawat contoh

* 5 ruangan (1 VIP, 2 kelas I, 2 kelas II)

* 10 obat umum (paracetamol, amoxicillin, dll)

# **7\. Struktur File Project**

rawat-inap/  
├── build.xml                       \# Apache Ant build script  
├── MANIFEST.MF  
├── res/                            \# ikon, splash  
│   └── icon.png  
└── src/  
    └── com/rs/rawatinap/  
        ├── MainMIDlet.java         \# entry point  
        │  
        ├── model/  
        │   ├── Entity.java         \# abstract root  
        │   ├── Person.java         \# abstract, extends Entity  
        │   ├── Pasien.java  
        │   ├── Dokter.java  
        │   ├── Perawat.java  
        │   ├── User.java  
        │   ├── Ruangan.java  
        │   ├── Obat.java  
        │   ├── Admisi.java  
        │   ├── RekamMedis.java  
        │   ├── CatatanHarian.java  
        │   ├── Resep.java  
        │   └── ItemObat.java  
        │  
        ├── storage/  
        │   ├── RMSUtil.java        \# helper generic  
        │   ├── UserDB.java  
        │   ├── PasienDB.java  
        │   ├── DokterDB.java  
        │   ├── RuanganDB.java  
        │   ├── ObatDB.java  
        │   ├── AdmisiDB.java  
        │   ├── RekamMedisDB.java  
        │   └── ResepDB.java  
        │  
        ├── service/  
        │   ├── AuthService.java  
        │   ├── PasienService.java  
        │   ├── DokterService.java  
        │   ├── RuanganService.java  
        │   ├── ObatService.java  
        │   ├── AdmisiService.java  
        │   ├── RekamMedisService.java  
        │   ├── ResepService.java  
        │   └── LaporanService.java  
        │  
        ├── controller/  
        │   ├── LoginController.java  
        │   ├── DashboardController.java  
        │   ├── PasienController.java  
        │   ├── DokterController.java  
        │   ├── RuanganController.java  
        │   ├── ObatController.java  
        │   ├── AdmisiController.java  
        │   ├── RekamMedisController.java  
        │   └── LaporanController.java  
        │  
        ├── ui/  
        │   ├── LoginForm.java  
        │   ├── DashboardList.java  
        │   ├── PasienListUI.java  
        │   ├── PasienFormUI.java  
        │   ├── ObatListUI.java  
        │   ├── AdmisiFormUI.java  
        │   ├── RekamMedisUI.java  
        │   ├── LaporanMenuUI.java  
        │   └── ChartCanvas.java    \# bar chart sederhana  
        │  
        ├── util/  
        │   ├── ScreenManager.java  \# singleton  
        │   ├── DateUtil.java  
        │   ├── IdGenerator.java  
        │   ├── Session.java  
        │   ├── Validator.java  
        │   └── SeedData.java  
        │  
        └── exception/  
            ├── AppException.java  
            ├── ValidationException.java  
            ├── NotFoundException.java  
            ├── DuplicateException.java  
            ├── KamarPenuhException.java  
            └── StokHabisException.java  
Total estimasi: \~60 file Java. Project size .jar setelah ProGuard target di bawah 200 KB.

# **8\. Flowchart Sistem**

## **8.1 Flow Utama: Startup → Login → Dashboard**

          \[START\]  
             |  
             v  
    \+-----------------+  
    | MainMIDlet.start|  
    \+-----------------+  
             |  
             v  
    \+-----------------+  
    | Buka semua RMS  |  
    | RMS exists?     |--NO--\> \[SeedData.run()\]  
    \+-----------------+              |  
             | YES                   |  
             v \<---------------------+  
    \+-----------------+  
    |   LoginForm     |  
    \+-----------------+  
             |  
             v  
    \+-----------------+  
    | Input user/pass |  
    \+-----------------+  
             |  
             v  
    \+-----------------+  
    | AuthService     |  
    | .login()        |--FAIL--\> \[Alert: Login gagal\]  
    \+-----------------+              |  
             | OK                    v  
             v               (kembali ke LoginForm)  
    \+-----------------+  
    | Session.set()   |  
    \+-----------------+  
             |  
             v  
    \+-----------------+  
    | DashboardList   |  
    | (menu by role)  |  
    \+-----------------+

## **8.2 Flow Admisi Pasien Baru**

  \[Pilih: Admisi Baru\]  
          |  
          v  
  \+-----------------+  
  | Pilih pasien    |  
  | dari list       |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | Cek status      |  
  | pasien==AKTIF?  |--TIDAK--\> \[Alert: pasien sudah dirawat\]  
  \+-----------------+  
          | YA  
          v  
  \+-----------------+  
  | Pilih dokter    |  
  | (DPJP) dari list|  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | Pilih ruangan   |  
  | yang TERSEDIA   |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | RuanganService  |  
  | cekKapasitas    |--PENUH--\> \[Alert \+ sarankan kamar lain\]  
  \+-----------------+  
          | OK  
          v  
  \+-----------------+  
  | Generate noRM   |  
  | RM-YYYYMMDD-XXX |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | AdmisiService   |  
  | .admisi()       |  
  | \- simpan Admisi |  
  | \- update kamar  |  
  | \- update pasien |  
  | \- buat RekamMd  |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | Tampil detail   |  
  | admisi \+ noRM   |  
  \+-----------------+

## **8.3 Flow Resep Obat & Stok**

  \[Buka RekamMedis pasien\]  
          |  
          v  
  \[Pilih: Tambah Resep\]  
          |  
          v  
  \+-----------------+  
  | Pilih obat dari |  
  | ObatList        |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | Input quantity  |  
  | \+ dosis         |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | qty \<= stok?    |--NO--\> \[Alert: stok tidak cukup,  
  \+-----------------+         tampil stok aktual\]  
          | YES  
          v  
  \+-----------------+  
  | qty \> 1000?     |--YA--\> \[Alert: konfirmasi qty besar\]  
  \+-----------------+  
          | TIDAK  
          v  
  \+-----------------+  
  | ResepService    |  
  | .tambahItem()   |  
  | \- kurangi stok  |  
  | \- simpan resep  |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | stok \< minStok? |--YA--\> \[Tampil warning low-stock di  
  \+-----------------+         status bar\]  
          | TIDAK  
          v  
  \[Kembali ke detail rekam medis\]

## **8.4 Flow Discharge**

  \[RekamMedis aktif\]  
          |  
          v  
  \[Pilih: Pulangkan Pasien\]  
          |  
          v  
  \+-----------------+  
  | Input tgl pulang|  
  | (default: today)|  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | tglPulang \>=    |  
  | tglMasuk?       |--NO--\> \[Alert: tanggal invalid\]  
  \+-----------------+  
          | YES  
          v  
  \+-----------------+  
  | Hitung tarif:   |  
  | hari \* tarif    |  
  | \+ total obat    |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | AdmisiService   |  
  | .discharge()    |  
  | \- update admisi |  
  | \- bebaskan kamar|  
  | \- close RM      |  
  | \- status=PULANG |  
  \+-----------------+  
          |  
          v  
  \+-----------------+  
  | Tampil ringkasan|  
  | \+ total tagihan |  
  \+-----------------+

# **9\. Spesifikasi Fitur per Modul**

## **9.1 Modul Pasien**

| Method (Service) | Input | Output / Effect | Validasi |
| :---- | :---- | :---- | :---- |
| tambah(Pasien p) | object Pasien | id baru | NIK unik, NIK 16 digit, nama tidak kosong |
| update(Pasien p) | object Pasien | void | Pasien exists; status \!= DIRAWAT untuk field tertentu |
| hapus(String id) | id | void | Status \!= DIRAWAT |
| cariByNIK(String nik) | nik | Pasien atau null | — |
| cariByNama(String q) | query | List\<Pasien\> | — |

## **9.2 Modul Dokter**

| Method | Effect |
| :---- | :---- |
| tambah(Dokter) | Generate kode DKT-XXX, simpan |
| nonAktifkan(id) | Set status=NON\_AKTIF (soft delete) |
| listAktif() | Filter status=AKTIF, sort by nama |
| assignKePasien(...) | Validasi dokter aktif sebelum admisi |

## **9.3 Modul Ruangan**

| Method | Effect |
| :---- | :---- |
| cekKapasitas(idRuangan) | Return slotKosong \= kapasitas \- terisi |
| isiSlot(idRuangan) | \++terisi; throw KamarPenuhException jika full |
| bebaskanSlot(idRuangan) | \--terisi (saat discharge) |
| listTersedia() | Filter slotKosong \> 0 AND status=TERSEDIA |

## **9.4 Modul Obat**

| Method | Effect |
| :---- | :---- |
| kurangiStok(id, qty) | Atomic check \+ decrement; throw StokHabis jika kurang |
| tambahStok(id, qty) | Penambahan stok (restock) |
| listLowStock() | Filter stok \< minStok |
| cari(nama) | Linear scan; return List\<Obat\> |

## **9.5 Modul Admisi & Rekam Medis**

| Method | Effect |
| :---- | :---- |
| admisi(pasienId, dokterId, ruanganId) | Transaksi: simpan admisi, isi slot kamar, update status pasien=DIRAWAT, buat rekamMedis kosong |
| tambahCatatan(rmId, catatan) | Append CatatanHarian dengan timestamp |
| tambahResep(rmId, obatId, qty, dosis) | Lewat ResepService — kurangi stok, simpan resep |
| discharge(admisiId, tglPulang) | Transaksi: bebaskan kamar, status pasien=PULANG, RM=CLOSED, hitung total tarif |

## **9.6 Modul Laporan & Charts**

| Laporan | Sumber Data | Tampilan |
| :---- | :---- | :---- |
| Occupancy ruangan | RuanganDB | List: nama kamar | terisi/kapasitas | % |
| Stok obat | ObatDB | List: nama obat | stok | status (LOW jika \< min) |
| Pasien aktif | PasienDB filter DIRAWAT | List: nama | kamar | dokter |
| Top diagnosa | RekamMedisDB aggregate | Bar chart Canvas: 5 diagnosa terbanyak |

# **10\. Rencana Implementasi (Coding Order)**

Urutan ini WAJIB diikuti — backend solid dulu sebelum frontend. Ini menghemat refactor besar di akhir.

## **Fase 1: Foundation (Hari 1-2)**

1. Setup project: build.xml, MANIFEST.MF, struktur folder.

2. util/DateUtil.java — format YYYY-MM-DD, hitung selisih hari.

3. util/IdGenerator.java — counter persisten \+ format kode.

4. util/Validator.java — cek NIK, tanggal, range.

5. util/Session.java — holder user login.

6. exception/ — semua AppException \+ turunannya.

## **Fase 2: Model Layer (Hari 2-3)**

7. model/Entity.java (abstract).

8. model/Person.java (abstract, extends Entity).

9. model/Pasien, Dokter, Perawat, User.

10. model/Ruangan, Obat.

11. model/Admisi, RekamMedis, CatatanHarian, Resep, ItemObat.

12. Implementasi toBytes() / fromBytes() per class — uji round-trip.

## **Fase 3: Storage Layer (Hari 3-4)**

13. storage/RMSUtil.java — generic open/save/load/delete.

14. Per-entity DB class: UserDB, PasienDB, DokterDB, RuanganDB, ObatDB, AdmisiDB, RekamMedisDB, ResepDB.

15. util/SeedData.java — first-run data default.

16. Test: write 50 record, baca ulang, hapus, count.

## **Fase 4: Service Layer (Hari 4-6)**

17. AuthService — login, logout.

18. PasienService, DokterService, RuanganService, ObatService — CRUD \+ business rules.

19. AdmisiService — orkestrasi multi-DAO.

20. RekamMedisService, ResepService.

21. LaporanService — aggregate read.

22. Unit test setiap method via console main() sederhana.

## **Fase 5: UI \+ Controller (Hari 6-9)**

23. util/ScreenManager singleton.

24. MainMIDlet — startApp/pauseApp/destroyApp.

25. LoginForm \+ LoginController.

26. DashboardList \+ DashboardController (menu by role).

27. Modul pasien: PasienListUI, PasienFormUI \+ PasienController.

28. Modul dokter, ruangan, obat (pola sama).

29. Modul admisi (paling kompleks) \+ rekam medis.

30. Modul laporan \+ ChartCanvas.

## **Fase 6: Integrasi & Polish (Hari 9-10)**

31. End-to-end test alur lengkap (login → admisi → resep → discharge).

32. Stress test: 100 pasien, 50 dokter, 200 obat.

33. ProGuard untuk shrink .jar.

34. Build .jad \+ .jar final, test di MicroEmu \+ device asli (jika ada).

# **11\. Strategi Testing**

## **11.1 Unit Test (J2SE Friendly)**

Sebagian besar class model & service tidak bergantung javax.microedition.\* — bisa diuji di JUnit/J2SE. Tulis stub untuk RMS.

## **11.2 Test Matrix**

| Skenario Test | Modul | Expected |
| :---- | :---- | :---- |
| Tambah pasien valid | PasienService | ID baru, count++ |
| Tambah pasien NIK duplikat | PasienService | DuplicateException |
| Admisi ke kamar penuh | AdmisiService | KamarPenuhException |
| Resep dengan stok \= 0 | ResepService | StokHabisException |
| Discharge tgl \< tglMasuk | AdmisiService | ValidationException |
| Hapus dokter aktif | DokterService | Tolak / soft delete |
| Login password salah | AuthService | return false |
| First run tanpa RMS | MainMIDlet | Seed data jalan, login admin/admin OK |

## **11.3 Manual Test di Emulator**

* MicroEmu / WTK 2.5.2 untuk smoke test full flow.

* Coba pause MIDlet di tengah operasi — pastikan RMS tidak corrupt.

* Stress: input 50 pasien manual, lihat performa list.

* Reset RMS via menu Admin — pastikan kembali ke first-run.

# **12\. Checklist Production-Ready**

## **Code Quality**

* Semua class punya javadoc minimal (deskripsi \+ param \+ return).

* Tidak ada System.out.println sisa di production build.

* Tidak ada hardcoded magic number — pakai constant di class Konstanta.

* Naming konsisten: bahasa Indonesia untuk domain (pasien, dokter), Inggris untuk teknis (Service, Util).

* Setiap method \< 50 baris; setiap class \< 300 baris.

## **Robustness**

* Setiap RMS operation di-wrap try/catch — tidak ada uncaught RecordStoreException.

* Setiap input form punya validasi sebelum panggil service.

* Semua AppException diubah jadi Alert user-friendly di Controller.

* First-run handled: SeedData jalan jika RMS kosong.

* Reset RMS tersedia via menu Admin (untuk recovery).

## **Performance**

* Tidak ada load-all-records pada dataset \> 50 (pakai paging).

* RecordStore di-close setelah operasi (atau dijaga single-open di RMSUtil).

* List render via lazy-fetch detail (item summary saja di list).

## **Security & Privacy**

* Password user disimpan hashed (SHA-1 via MessageDigest J2ME — mendekati cukup).

* Tidak ada PII (NIK, alamat) di-log.

* Auto-logout setelah idle 5 menit.

## **Build & Deploy**

* build.xml bisa generate .jar \+ .jad lengkap dengan ProGuard step.

* MANIFEST.MF berisi MIDlet-Name, Vendor, Version, MicroEdition-Profile=MIDP-2.0, Configuration=CLDC-1.1.

* .jar size \< 200 KB setelah ProGuard.

* Tested di minimal 2 emulator (MicroEmu \+ WTK).

* Versi & build number tertera di splash / About screen.

## **Dokumentasi**

* README.md cara build & run.

* Notion page: spesifikasi method per file (per pola project rawat jalan).

* Diagram: ER, class diagram, flow utama (bisa via Mermaid atau gambar).

* User manual singkat untuk admin RS.

*— Akhir Dokumen Blueprint —*