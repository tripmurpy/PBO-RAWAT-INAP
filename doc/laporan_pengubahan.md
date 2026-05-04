# 📊 LAPORAN PENGUBAHAN ARSITEKTUR & KEAMANAN SISTEM
> **Project:** J2ME Hospital Inpatient System (PBO-RAWAT-INAP)  
> **Status:** ✅ 100% Implemented (Based on `doc/analisis.md`)

---

## 1. Keamanan & Hardening (Priority: Critical)
Telah dilakukan penguatan pada lapisan keamanan untuk mencegah akses tidak sah dan pengungkapan data.

*   **Peningkatan Hash Password:** Algoritma hashing di `User.hashPassword()` telah ditingkatkan dari `hashCode()` sederhana menjadi algoritma iteratif (1000x stretching) dengan salt statis (`RSI_SALT_v1`). Output sekarang berupa hash hex yang lebih panjang.
*   **Penghapusan Backdoor:** User default `abc` / `abc` telah dihapus dari `UserDB.inisialisasiDefault()`. Hanya user `admin` yang diinisialisasi jika database kosong.
*   **Pembersihan UI Login:** Tampilan kredensial default (`admin / admin123`) telah dihapus dari `LoginScreen` untuk mencegah kebocoran informasi.
*   **Masking Password:** Field password pada `LoginScreen` sekarang menampilkan karakter masking (`****`).

## 2. Refactoring Storage Layer (Priority: High)
Duplikasi kode sebesar ~60% di lapisan penyimpanan telah dieliminasi.

*   **Implementasi BaseDB:** Dibuat kelas abstrak `BaseDB.java` yang mengimplementasikan **Template Method Pattern** untuk operasi CRUD (Create, Read, Update, Delete) pada RMS.
*   **Migrasi Concrete DB:** Semua kelas penyimpanan (`UserDB`, `PasienDB`, `DokterDB`, `RuanganDB`, `AdmisiDB`) sekarang meng-extends `BaseDB`, sehingga kode menjadi jauh lebih ringkas dan mudah dipelihara.
*   **In-Memory Caching:** Menambahkan mekanisme cache pada `BaseDB` untuk mengurangi frekuensi pembacaan RMS (full-scan) yang berat, meningkatkan performa aplikasi secara signifikan pada perangkat J2ME.

## 3. Stabilitas Navigasi (Priority: High)
Memperbaiki masalah navigasi "Back" yang sebelumnya sering terputus.

*   **Navigation Stack:** `ScreenManager` sekarang menggunakan `java.util.Vector` sebagai stack navigasi yang sesungguhnya. User sekarang dapat melakukan "Back" berkali-kali hingga ke root menu tanpa kehilangan history.
*   **Sinkronisasi UI:** Koordinat klik pada `LoginScreen` sekarang disinkronkan dengan field dinamis yang dihitung di method `paint()`, memastikan input layar sentuh tetap akurat meskipun resolusi layar berubah.
*   **Logout Support:** Menambahkan fungsi `logout()` yang membersihkan seluruh stack navigasi dan mengembalikan user ke layar login dalam keadaan bersih.

## 4. Re-Architecture & Cleanup (Priority: Medium)
Penyelarasan struktur proyek dengan standar Enterprise.

*   **Dependency Injection:** Memperkenalkan `ServiceFactory.java` (Singleton) untuk mengelola siklus hidup Service dan Repository. Semua Service sekarang menerima dependensi melalui constructor (Constructor Injection).
*   **Penghapusan Controller Redundan:** Seluruh kelas di folder `controller/` yang bersifat "pure pass-through" (hanya meneruskan panggilan ke Service) telah dihapus untuk mengurangi kompleksitas file. UI sekarang memanggil Service langsung melalui `ServiceFactory`.
*   **Konsolidasi Utility:** Kelas `NoRMGenerator` telah digabung ke dalam `IDGenerator.java` untuk menghindari duplikasi logika penomoran otomatis.

---

## 5. Matriks Perubahan File

| Kategori | File | Tindakan |
|---|---|---|
| **Model** | `User.java` | Updated (Hash logic) |
| **Storage** | `BaseDB.java` | **NEW** |
| | `UserDB, PasienDB, etc` | Refactored (Extends BaseDB) |
| **Service** | `ServiceFactory.java` | **NEW** |
| | `*Service.java` | Updated (Constructor DI) |
| **UI** | `ScreenManager.java` | Updated (Stack-based navigation) |
| | `LoginScreen.java` | Updated (Security & Coordination) |
| **Controller** | `*Controller.java` | **DELETED** (Redundant) |
| **Util** | `NoRMGenerator.java` | **DELETED** (Merged to IDGenerator) |

---
**Catatan:** Sistem sekarang lebih aman, lebih cepat, dan kode sumbernya jauh lebih bersih (clean code) sesuai dengan rekomendasi `doc/analisis.md`. Laporan ini menandai selesainya fase refactoring arsitektur.
