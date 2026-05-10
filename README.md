# 🏥 System Rawat Inap Rasuna Said 5

[![Java Version](https://img.shields.io/badge/Java-1.3%20/%201.4-orange.svg)](https://www.oracle.com/java/technologies/javame.html)
[![Platform](https://img.shields.io/badge/Platform-J2ME%20/%20MIDP%202.0-blue.svg)](https://en.wikipedia.org/wiki/Java_Platform,_Micro_Edition)
[![Build System](https://img.shields.io/badge/Build-Ant-red.svg)](https://ant.apache.org/ )

**System Rawat Inap Rasuna Said 5** adalah aplikasi manajemen rumah sakit berbasis Java ME (Micro Edition) yang dirancang untuk efisiensi operasional di lingkungan dengan sumber daya terbatas. Aplikasi ini mencakup seluruh alur pelayanan rawat inap, mulai dari registrasi pasien hingga manajemen kamar dan penagihan.

---

## 🔄 Alur Sistem (Project Flow)

Sistem ini berjalan dengan mengikuti siklus hidup data pasien dan operasional rumah sakit:

1.  **Inisialisasi Data (Seed)**: Pada saat aplikasi pertama kali dijalankan, sistem secara otomatis mengisi data awal (Dokter, Kamar, Obat) ke dalam penyimpanan permanen (RMS).
2.  **Autentikasi**: Petugas (Admin/Staff) masuk melalui layar login untuk mengakses fungsi dashboard.
3.  **Manajemen Operasional**:
    *   **Registrasi**: Pendaftaran pasien baru dan alokasi kamar.
    *   **Monitoring**: Pemantauan status okupansi kamar secara real-time.
    *   **Pelayanan Medis**: Pencatatan kunjungan dokter dan pemberian obat.
4.  **Persistensi Data**: Setiap transaksi data (tambah/edit/hapus) langsung dipersistensikan ke dalam **Record Management System (RMS)** bawaan J2ME agar data tidak hilang saat aplikasi ditutup.

---

## 🚀 Cara Menjalankan Project (Step-by-Step)

Ikuti langkah-langkah berikut untuk menjalankan aplikasi di lingkungan pengembangan:

### 1. Persiapan Environment
*   Instalasi **Java Development Kit (JDK)** (Disarankan versi 8 atau yang kompatibel dengan WTK).
*   Instalasi **Oracle Java Wireless Toolkit (WTK) 2.5.2** atau versi terbaru.
*   Instalasi **Apache Ant**.

### 2. Konfigurasi Path
Buka file `build.properties` di root project dan sesuaikan path WTK Anda:
```properties
wtk.home=C:/path/to/WTK2.5.2
```

### 3. Build dan Run via Terminal
Buka terminal atau command prompt di direktori project, lalu jalankan perintah berikut:

| Langkah | Perintah | Deskripsi |
| :--- | :--- | :--- |
| **Clean** | `ant clean` | Menghapus artefak build lama |
| **Build** | `ant build` | Kompilasi, preverify, dan pembuatan JAR/JAD |
| **Run** | `ant run` | Menjalankan aplikasi di Emulator WTK |

---

## 🛠 Spesifikasi Aplikasi

### 3.1 Library & Framework
Aplikasi ini dibangun murni menggunakan standar **Java ME (J2ME)** tanpa library eksternal tambahan untuk menjaga performa dan kompatibilitas:
*   **CLDC 1.1**: Connected Limited Device Configuration.
*   **MIDP 2.0**: Mobile Information Device Profile.

### 3.2 User Interface (UI)
*   **Custom Graphics**: Menggunakan `javax.microedition.lcdui.Canvas` untuk merender UI yang modern, dinamis, dan premium (dashboard grid, tombol gradasi, dan animasi transisi).
*   **LCDUI Forms**: Digunakan pada bagian input data yang kompleks untuk memastikan kemudahan input pengguna.

### 3.3 Sistem Penyimpanan (Persistence)
*   **Record Management System (RMS)**: Menggunakan database internal J2ME untuk menyimpan data secara lokal dalam bentuk byte stream yang diindeks.

### 3.4 Penerapan OOP: Inheritance (Pewarisan)
Struktur kelas menggunakan hierarki yang ketat untuk reusability:
*   `Pasien`, `Dokter`, dan `Perawat` mewarisi properti dasar dari kelas abstract `Person`.
*   `Person` mewarisi identitas unik dari kelas root `Entity`.

### 3.5 Penerapan OOP: Encapsulation (Pengkapsulan)
*   Semua data member dalam model didefinisikan sebagai `protected` atau `private`.
*   Akses data dilakukan secara terkontrol melalui metode **Getter** dan **Setter** publik untuk menjaga integritas data.

### 3.6 Penerapan OOP: Polymorphism (Polimorfisme)
*   **Method Overriding**: Metode `toString()` dan `tampilkan()` diimplementasikan secara berbeda di setiap subclass (Pasien, Kamar, dll) untuk memberikan representasi data yang sesuai konteks.
*   **Generic Handling**: Penggunaan kelas abstract `Entity` memungkinkan sistem penyimpanan (Storage Layer) menangani berbagai tipe objek secara generik.

---

## 📋 Persyaratan (Requirements)

| Kategori | Spesifikasi Minimal |
| :--- | :--- |
| **Operating System** | Windows, Linux, atau macOS |
| **Java SDK** | JDK 1.8 (Java 8) |
| **Tooling** | Apache Ant 1.10+ |
| **Emulator** | Java Wireless Toolkit (WTK) 2.5.2 |
| **Versi Java** | Source 1.3 / Target 1.1 (Bytecode Compatibility) |

---

## 📂 Struktur Folder & Fungsi File

| Folder / File | Deskripsi & Fungsi |
| :--- | :--- |
| 📁 `src/model` | Berisi kelas entitas data (Pasien, Dokter, Kamar, dll). |
| 📁 `src/ui` | Berisi seluruh layar aplikasi dan komponen UI kustom. |
| 📁 `src/storage` | Layer akses data yang berinteraksi langsung dengan RMS. |
| 📁 `src/service` | Berisi logika bisnis (AuthService, PasienService). |
| 📁 `src/controller` | Mengatur navigasi antar layar dan alur logika aplikasi. |
| 📁 `src/util` | Helper class untuk penanganan Tanggal, ID, dan Seed Data. |
| 📄 `build.xml` | Skrip otomatisasi build menggunakan Apache Ant. |
| 📄 `MANIFEST.MF` | Konfigurasi atribut JAR untuk MIDlet. |
| 📄 `*.jad` | Descriptor file untuk instalasi aplikasi di perangkat/emulator. |


---

## 🛠 Fixing Problem

Bagian ini merangkum serangkaian perbaikan, optimasi, dan penambahan fitur yang dilakukan untuk meningkatkan kualitas sistem:

### 1. Modernisasi UI & UX
*   **Login Screen**: Redesain total menjadi tampilan minimalis premium dengan palet warna modern dan perbaikan visual pada field password.
*   **Pasien Detail Screen**: Implementasi kartu detail pasien yang mengintegrasikan data identitas, dokter penanggung jawab, dan rincian kamar rawat dalam satu tampilan elegan.
*   **Daftar Pasien**: Navigasi daftar pasien diperbarui agar langsung menuju layar detail, memberikan akses informasi yang lebih cepat bagi petugas.

### 2. Optimalisasi Form Pendaftaran
*   **Input Kontrol**: Mengubah input tanggal lahir menjadi `TextField` dan dropdown jenis kelamin/asuransi menjadi `POPUP` untuk mengatasi masalah unclickable pada emulator.
*   **Integrasi Medis**: Penambahan fitur pemilihan Dokter Penanggung Jawab (DPJP) dan Kamar Rawat langsung di dalam form pendaftaran.

### 3. Sistem Pemilihan Kamar Interaktif
*   **Visual Status**: Implementasi `KamarSelectionScreen` berbasis Canvas yang menampilkan status kamar secara visual (Warna Hijau/Merah/Oranye) sesuai dengan kondisi riil di database.
*   **Interactive Picking**: Tombol pemilihan kamar kini terintegrasi penuh antara form pendaftaran dan database ruangan.

### 4. Manajemen Data & Persistensi
*   **Seed Data Otomatis**: Inisialisasi otomatis untuk 10 Dokter Spesialis (RS Eka Hospital), 25 Kamar (Gedung A, Lantai 1-7), dan 6 Pasien awal untuk keperluan pengujian sistem.
*   **Sinkronisasi RMS**: Perbaikan logika penyimpanan pada RMS untuk memastikan status kamar dan data pasien selalu sinkron (Kamar otomatis TERISI saat pasien terdaftar).

### 5. Pembersihan & Arsitektur
*   **Analisis Dead Code**: Identifikasi modul-modul lama yang tidak terpakai (Laporan lama, Obat, Admisi UI lama) untuk langkah optimalisasi ukuran JAR di masa mendatang.
*   **Backward Compatibility**: Implementasi pengecekan `available()` pada stream data untuk memastikan sistem tetap bisa membaca data lama meskipun ada penambahan field baru pada model.

---
<p align="center">
  Dibuat dengan ❤️ untuk Sistem Kesehatan yang Lebih Baik<br>
  Built by <b>Anthropic</b>
</p>

