# J2ME : System Rumah Sakit Rawat Inap

# 📋 Overview

Dokumentasi teknis lengkap untuk aplikasi **J2ME — Sistem Rawat Inap Rumah Sakit**. Dibangun menggunakan Java MIDP 2.0 / CLDC 1.1 dengan arsitektur OOP berlapis 5 layer, berjalan secara offline menggunakan RMS (Record Management System) sebagai penyimpanan lokal pada perangkat Nokia/Sony Ericsson era klasik.

> ⚠️ **Target Platform:** MIDP 2.0 · CLDC 1.1 · Sun WTK 2.5.2 · Offline-only (RMS)
> 

---

# 🏗️ Build Architecture

Arsitektur sistem mengikuti pola **5-Layer OOP** dengan pemisahan tanggung jawab yang ketat antar lapisan. Setiap layer hanya boleh berkomunikasi dengan layer di bawahnya — tidak boleh melewati layer.

```
┌─────────────────────────────────────────────────────┐
│              PRESENTATION LAYER (UI)                │
│  LoginScreen · DashboardScreen · PasienScreen       │
│  DokterScreen · RuanganScreen · AdmisiScreen        │
│                 ScreenManager (Singleton)           │
├─────────────────────────────────────────────────────┤
│              CONTROLLER LAYER                       │
│  AuthController · PasienController                  │
│  DokterController · RuanganController               │
│  AdmisiController · KunjunganController             │
├─────────────────────────────────────────────────────┤
│              SERVICE LAYER (Business Logic)         │
│  AuthService · PasienService · DokterService        │
│  RuanganService · AdmisiService · KunjunganService  │
│       [DateUtil · Validator · NoRMGenerator]        │
├─────────────────────────────────────────────────────┤
│              DOMAIN MODEL LAYER                     │
│  Pasien · Dokter · Ruangan · Penyakit               │
│  Asuransi · Admisi · Kunjungan · User               │
│  IPasienRepo · IDokterRepo · IRuanganRepo (iface)   │
├─────────────────────────────────────────────────────┤
│              INFRASTRUCTURE LAYER (RMS)             │
│  PasienDB · DokterDB · RuanganDB                    │
│  AdmisiDB · UserDB · RMSUtil                        │
└─────────────────────────────────────────────────────┘
```

## Prinsip Arsitektur

- **Dependency Rule:** Layer atas hanya boleh memanggil layer langsung di bawahnya
- **Interface Segregation:** Service hanya mengenal interface repository, bukan implementasi konkretnya
- **Singleton Navigator:** `ScreenManager` mengelola seluruh navigasi antar layar
- **Offline-first:** Semua data tersimpan di RMS — tidak ada koneksi jaringan
- **No `SimpleDateFormat`:** CLDC 1.1 tidak mendukung — gunakan `DateUtil` custom

---

# 🔄 Workflow Sistem

## Alur Umum Admin

```
Buka Aplikasi
    │
    ▼
LoginScreen
    │ input username + password
    ▼
AuthController → AuthService → UserDB.verify()
    │
    ├── [GAGAL] → tampil pesan error → kembali ke LoginScreen
    │
    └── [BERHASIL]
            │
            ▼
       DashboardScreen
            │
            ├── [Pasien Baru]      → PasienController
            ├── [Cari Pasien]      → PasienController
            ├── [Admisi / Rawat]   → AdmisiController
            ├── [Manajemen Dokter] → DokterController
            ├── [Manajemen Kamar]  → RuanganController
            └── [Riwayat]         → KunjunganController
```

## Alur Keluar Rawat Inap (Discharge)

```
Admin pilih menu Keluar Pasien
    │
    ▼
Cari admisi aktif by No. RM
    │
    ▼
Input tanggal keluar + diagnosa akhir + kode ICD-10
    │
    ▼
KunjunganService.tutupKunjungan()
    │
    ▼
AdmisiDB.updateStatus(SELESAI)
    │
    ▼
RuanganDB.updateStatus(KOSONG)
    │
    ▼
Tampil ringkasan perawatan ✓
```

---

# 📊 Flowchart Detail

## 1 · Login & Autentikasi

```
[START]
   │
   ▼
[LoginScreen]
   Input: username, password
   │
   ▼
[AuthController.login(username, password)]
   │
   ▼
[AuthService.verifikasi()]
   │
   ▼
[UserDB.findByUsername(username)]
   │
   ├─── NULL? ──► error: "Akun tidak ditemukan" ──► kembali LoginScreen
   │
   └─── FOUND
         │
         ▼
      [cek password hash]
         │
         ├─── SALAH ──► error: "Password salah" ──► kembali LoginScreen
         │
         └─── BENAR
               │
               ▼
            [simpan sesi admin]
               │
               ▼
            [ScreenManager.show(DashboardScreen)]
               │
               ▼
            [END — Dashboard tampil]
```

## 2 · Pendaftaran Pasien Baru

```
[DashboardScreen → Pasien Baru]
   │
   ▼
[PasienFormScreen]
   Input: nama, tglLahir, jenisKelamin, alamat, noTelp, asuransi
   │
   ▼
[PasienController.daftarPasienBaru(data)]
   │
   ▼
[Validator.validasiPasien(data)]
   │
   ├─── INVALID ──► highlight field error ──► kembali form
   │
   └─── VALID
         │
         ▼
      [PasienService.daftarBaru(data)]
         │
         ▼
      [NoRMGenerator.generate()]
         format: RM-YYYYMMDD-XXX
         │
         ▼
      [PasienDB.save(pasien)]
         RMS RecordStore persist
         │
         ▼
      [Tampil: No RM berhasil dibuat]
         │
         ▼
      [END]
```

## 3 · Admisi Rawat Inap

```
[DashboardScreen → Rawat Inap]
   │
   ▼
[AdmisiScreen]
   Input: No. RM pasien
   │
   ▼
[PasienDB.findByRM(noRM)]
   │
   ├─── NULL ──► error: "Pasien tidak ditemukan"
   │
   └─── FOUND
         │
         ▼
      [Tampil data pasien]
         │
         ▼
      [Pilih dokter penanggung jawab]
         │
         ▼
      [RuanganService.cariKamarTersedia(tipe)]
         │
         ├─── PENUH ──► notif kamar penuh
         │
         └─── ADA
               │
               ▼
            [Input tglMasuk, diagnosisAwal, tipeKamar]
               │
               ▼
            [AdmisiService.buatAdmisi(pasien, dokter, kamar, data)]
               │
               ▼
            [AdmisiDB.save(admisi)]
               │
               ▼
            [RuanganDB.updateStatus(kamar, TERISI)]
               │
               ▼
            [Tampil ID Admisi ✓]
               │
               ▼
            [END]
```

---

# 📁 Struktur Folder & File

## Direktori Lengkap

```
hospital-rawat-inap/
│
├── build.xml                        ← Ant build script (compile→preverify→package)
├── MANIFEST.MF                      ← MIDlet manifest
├── hospital-rawat-inap.jad          ← Java Application Descriptor
│
├── src/
│   │
│   ├── ui/                          ← [PRESENTATION LAYER]
│   │   ├── HospitalMIDlet.java      ← Entry point, startApp/destroyApp
│   │   ├── ScreenManager.java       ← Singleton, navigasi antar layar
│   │   ├── LoginScreen.java         ← Form login admin
│   │   ├── DashboardScreen.java     ← Menu utama post-login
│   │   ├── PasienListScreen.java    ← Daftar semua pasien (List MIDP)
│   │   ├── PasienFormScreen.java    ← Form tambah / edit pasien
│   │   ├── DokterScreen.java        ← List & form dokter
│   │   ├── RuanganScreen.java       ← Status kamar & kapasitas
│   │   ├── AdmisiScreen.java        ← Form rawat inap baru
│   │   ├── DischargeScreen.java     ← Form keluar pasien
│   │   └── KunjunganScreen.java     ← Riwayat kunjungan / admisi
│   │
│   ├── controller/                  ← [CONTROLLER LAYER]
│   │   ├── AuthController.java      ← Handle login & validasi sesi
│   │   ├── PasienController.java    ← CRUD data pasien
│   │   ├── DokterController.java    ← Assign & manajemen dokter
│   │   ├── RuanganController.java   ← Cek & update status kamar
│   │   ├── AdmisiController.java    ← Proses rawat inap masuk & keluar
│   │   └── KunjunganController.java ← Riwayat & rekap kunjungan
│   │
│   ├── service/                     ← [SERVICE LAYER — Business Logic]
│   │   ├── AuthService.java         ← Verifikasi akun, cek kredensial
│   │   ├── PasienService.java       ← Aturan pendaftaran pasien
│   │   ├── DokterService.java       ← Aturan penjadwalan dokter
│   │   ├── RuanganService.java      ← Cek ketersediaan & kapasitas kamar
│   │   ├── AdmisiService.java       ← Validasi & proses admisi
│   │   └── KunjunganService.java    ← Rekap & tutup kunjungan
│   │
│   ├── model/                       ← [DOMAIN MODEL LAYER — Entities]
│   │   ├── Pasien.java              ← noRM, nama, tglLahir, asuransi
│   │   ├── Dokter.java              ← id, nama, spesialisasi, jadwal
│   │   ├── Ruangan.java             ← id, tipe, kapasitas, statusKamar
│   │   ├── Penyakit.java            ← kodeICD10, namaPenyakit
│   │   ├── Asuransi.java            ← id, nama, tipeKlaim
│   │   ├── Admisi.java              ← idAdmisi, pasien, dokter, ruangan, tgl
│   │   ├── Kunjungan.java           ← id, tglMasuk, tglKeluar, status
│   │   └── User.java                ← username, passwordHash, role
│   │
│   ├── model/repository/            ← [REPOSITORY INTERFACES]
│   │   ├── IPasienRepository.java   ← save, findById, findByRM, delete, getAll
│   │   ├── IDokterRepository.java   ← save, findAll, findBySpesialis
│   │   ├── IRuanganRepository.java  ← save, findAvailable, updateStatus
│   │   ├── IAdmisiRepository.java   ← save, findByPasien, findAktif, getAll
│   │   └── IUserRepository.java     ← findByUsername, verify
│   │
│   ├── storage/                     ← [INFRASTRUCTURE LAYER — RMS impl]
│   │   ├── PasienDB.java            ← implements IPasienRepository
│   │   ├── DokterDB.java            ← implements IDokterRepository
│   │   ├── RuanganDB.java           ← implements IRuanganRepository
│   │   ├── AdmisiDB.java            ← implements IAdmisiRepository
│   │   └── UserDB.java              ← implements IUserRepository
│   │
│   └── util/                        ← [SHARED UTILITIES]
│       ├── RMSUtil.java             ← serialize/deserialize byte[]
│       ├── DateUtil.java            ← CLDC 1.1 date workaround
│       ├── Validator.java           ← Field validation rules
│       ├── NoRMGenerator.java       ← Format: RM-YYYYMMDD-XXX
│       └── IDGenerator.java         ← Auto-increment ID helper
│
├── bin/                             ← Compiled .class files (generated)
├── preverified/                     ← WTK preverified classes (generated)
├── dist/
│   ├── hospital-rawat-inap.jar      ← Final JAR distributable
│   └── hospital-rawat-inap.jad      ← Final JAD untuk OTA deploy
```

## Jumlah File per Layer

| Layer | Folder | Jumlah File |
| --- | --- | --- |
| Presentation | `ui/` | 11 file |
| Controller | `controller/` | 6 file |
| Service | `service/` | 6 file |
| Domain Model | `model/` | 8 file |
| Repository Interface | `model/repository/` | 5 file |
| Infrastructure / RMS | `storage/` | 5 file |
| Utility | `util/` | 5 file |
| **Total** |  | **46 file** |

---

# 📱 Text Flow Aplikasi Admin

Berikut gambaran lengkap alur tampilan layar dari sudut pandang admin yang mengoperasikan aplikasi.

## Skenario 1 — Masuk ke Sistem

```
══════════════════════════════
       RUMAH SAKIT XYZ
     Sistem Rawat Inap
══════════════════════════════
Username : [____________]
Password : [____________]

        [ LOGIN ]
══════════════════════════════

    ↓ (berhasil login)

══════════════════════════════
   DASHBOARD ADMIN
   Selamat datang, Admin
══════════════════════════════
 1. Daftarkan Pasien Baru
 2. Cari Pasien
 3. Rawat Inap Baru
 4. Keluar Pasien (Discharge)
 5. Manajemen Dokter
 6. Manajemen Kamar
 7. Riwayat Kunjungan
 8. Logout
══════════════════════════════
```

## Skenario 2 — Daftarkan Pasien Baru

```
══════════════════════════════
   PENDAFTARAN PASIEN BARU
══════════════════════════════
Nama Lengkap : [____________]
Tgl Lahir    : [DD/MM/YYYY  ]
Jenis Kelamin: [L / P       ]
Alamat       : [____________]
No. Telpon   : [____________]
Asuransi     : [BPJS/Mandiri]

  [ DAFTAR ]   [ BATAL ]
══════════════════════════════

    ↓ (validasi OK, disimpan)

══════════════════════════════
   PASIEN BERHASIL DIDAFTAR
══════════════════════════════
 No. RM : RM-20250502-001
 Nama   : Budi Santoso
 Status : Terdaftar
══════════════════════════════
        [ OK ]
══════════════════════════════
```

## Skenario 3 — Admisi Rawat Inap

```
══════════════════════════════
   RAWAT INAP BARU
══════════════════════════════
No. RM Pasien: [____________]

  [ CARI PASIEN ]
══════════════════════════════

    ↓ (pasien ditemukan)

══════════════════════════════
 Nama    : Budi Santoso
 Tgl Lhr : 12/05/1990
 Asuransi: BPJS
══════════════════════════════
Dokter PJ    : [Dr. Ani / ...]
Tipe Kamar   : [VIP/Kelas I ]
Diagnosa Awal: [____________]
Tgl Masuk    : [DD/MM/YYYY  ]

  [ PROSES ADMISI ]  [ BATAL ]
══════════════════════════════

    ↓ (kamar tersedia, admisi dibuat)

══════════════════════════════
   ADMISI BERHASIL
══════════════════════════════
 ID Admisi : ADM-2025-0042
 Pasien    : Budi Santoso
 Kamar     : VIP-03
 Dokter    : Dr. Ani Sp.PD
 Tgl Masuk : 02/05/2025
══════════════════════════════
        [ OK ]
══════════════════════════════
```

## Skenario 4 — Discharge (Keluar Pasien)

```
══════════════════════════════
   KELUAR PASIEN
══════════════════════════════
No. RM / ID Admisi: [_______]

  [ CARI ]
══════════════════════════════

    ↓ (admisi aktif ditemukan)

══════════════════════════════
 Pasien   : Budi Santoso
 Kamar    : VIP-03
 Masuk    : 02/05/2025
══════════════════════════════
Diagnosa Akhir: [____________]
Kode ICD-10   : [____________]
Tgl Keluar    : [DD/MM/YYYY  ]
Catatan       : [____________]

  [ SELESAIKAN ]  [ BATAL ]
══════════════════════════════

    ↓ (admisi ditutup, kamar dibebaskan)

══════════════════════════════
   PASIEN BERHASIL KELUAR
══════════════════════════════
 Kamar VIP-03 kembali KOSONG
 Lama rawat : 5 hari
══════════════════════════════
        [ OK ]
══════════════════════════════
```

## Skenario 5 — Cek Status Kamar

```
══════════════════════════════
   STATUS KAMAR
══════════════════════════════
 VIP-01  : TERISI  (Rina S.)
 VIP-02  : KOSONG  ✓
 VIP-03  : TERISI  (Budi S.)
 Kls1-01 : TERISI  (Ahmad Z.)
 Kls1-02 : KOSONG  ✓
 Kls1-03 : KOSONG  ✓
══════════════════════════════
  Total Terisi : 3 / 6
  Total Kosong : 3 / 6
══════════════════════════════
 [ DETAIL ] [ REFRESH ] [ ← ]
══════════════════════════════
```

---

# 🛠️ Build Pipeline

```
Source .java
    │
    ▼
[javac] → compile ke .class
    │  (JDK 8, bootclasspath WTK)
    ▼
[preverify] → CLDC preverification
    │  (wajib untuk J2ME)
    ▼
[jar] → package jadi .jar + MANIFEST.MF
    │
    ▼
[.jad generator] → buat descriptor
    │
    ▼
[MicroEmu / WTK Emulator] → test run
    │
    ▼
[Deploy ke device]
```

**Tools yang digunakan:**

- JDK 8
- Sun WTK 2.5.2 (preverify + emulator)
- Apache Ant (otomasi build via `build.xml`)
- MicroEmu (emulator alternatif)
- ProGuard (opsional — obfuscation & shrink)

---

# 👥 Pembagian Kerja Tim

| Urutan | Developer | Layer | File yang Dikerjakan |
| --- | --- | --- | --- |
| 1 | Dev 1 | Model | `model/*.java` |
| 2 | Dev 1 | Interface | `model/repository/*.java` |
| 3 | Dev 1 | Utility | `util/*.java` |
| 4 | Dev 1 | Storage | `storage/*DB.java` |
| 5 | Dev 1 | Service | `service/*Service.java` |
| 6 | Dev 2 | Controller | `controller/*Controller.java` |
| 7 | Dev 2 | UI | `ui/*Screen.java` |

> Dev 2 **baru mulai** setelah Dev 1 menyelesaikan step 1–5. Backend harus selesai sebelum frontend dimulai untuk menghindari blocking dependency.
> 

---

# 📌 Catatan Teknis Penting

- `SimpleDateFormat` **tidak tersedia** di CLDC 1.1 — wajib pakai `DateUtil.java` custom
- Setiap entity punya `RecordStore` terpisah — jangan gabung dalam satu store
- Serialisasi menggunakan `DataOutputStream` / `DataInputStream` via `RMSUtil`
- Format No. RM: `RM-YYYYMMDD-XXX` (contoh: `RM-20250502-001`)
- Format ID Admisi: `ADM-YYYY-XXXX` (contoh: `ADM-2025-0042`)
- Password disimpan sebagai hash — jangan simpan plaintext di RMS
- `ScreenManager` adalah Singleton — instance hanya boleh satu selama runtime