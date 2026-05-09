# Laporan Analisis File Tidak Terpakai (Dead Code)
**Project:** PBO-SYSTEM-RAWA-INAP (J2ME Hospital System)
**Tanggal:** 9 Mei 2026

## 1. Pendahuluan
Berdasarkan pembaruan Dashboard pada `DashboardScreen.java`, beberapa fitur telah dihapus untuk menyederhanakan antarmuka pengguna. Akibatnya, banyak file source code yang sebelumnya mendukung fitur tersebut kini tidak lagi memiliki akses masuk (entry point) dan menjadi kode mati (*dead code*).

Laporan ini merinci file-file yang aman untuk dihapus guna meringankan ukuran aplikasi (JAR) dan menjaga kebersihan struktur proyek.

---

## 2. Daftar File Tidak Terpakai Berdasarkan Modul

### A. Modul Laporan & Visualisasi
Fitur statistik dan grafik sudah tidak ada di menu utama.
- `src/controller/LaporanController.java`
- `src/service/LaporanService.java`
- `src/ui/LaporanScreen.java`
- `src/ui/ChartCanvas.java`

### B. Modul Obat & Manajemen Resep
Seluruh sistem inventori obat dan pemberian resep sudah tidak digunakan.
- `src/controller/ObatController.java`
- `src/service/ObatService.java`
- `src/service/ResepService.java`
- `src/storage/ObatDB.java`
- `src/storage/ResepDB.java`
- `src/ui/ObatScreen.java`
- `src/ui/ObatPemberianScreen.java`
- `src/model/Obat.java`
- `src/model/ItemObat.java`
- `src/model/Resep.java`
- `src/model/repository/IObatRepository.java`
- `src/model/repository/IResepRepository.java`

### C. Modul Antarmuka Admisi & Discharge
UI lama untuk proses pendaftaran dan pemulangan pasien tidak lagi dipanggil oleh Dashboard.
- `src/controller/AdmisiController.java`
- `src/service/AdmisiService.java`
- `src/ui/AdmisiScreen.java`
- `src/ui/AdmisiBaruScreen.java`
- `src/ui/DischargeScreen.java`
- `src/ui/BillingScreen.java`

### D. Modul Rekam Medis
- `src/controller/RekamMedisController.java`
- `src/service/RekamMedisService.java`
- `src/storage/RekamMedisDB.java`
- `src/model/RekamMedis.java`
- `src/model/CatatanHarian.java`
- `src/model/repository/IRekamMedisRepository.java`

### E. Model & Entitas Lainnya
- `src/model/Perawat.java` (Tidak ada fitur manajemen perawat)
- `src/model/Penyakit.java` (Tidak ada fitur katalog penyakit)

---

## 3. Pengecualian (PENTING)
Beberapa file **TIDAK BOLEH** dihapus meskipun UI-nya tidak ada:
1.  **`src/model/Admisi.java`**: Masih digunakan oleh `KunjunganService` untuk menampilkan menu **Riwayat**.
2.  **`src/storage/AdmisiDB.java`**: Dibutuhkan untuk menyimpan data riwayat tersebut.
3.  **`src/model/repository/IAdmisiRepository.java`**: Interface yang dibutuhkan untuk akses data Riwayat.

---

## 4. Langkah Penghapusan yang Aman
Jika diputuskan untuk menghapus file-file di atas, langkah berikut wajib diikuti:
1.  **Hapus Referensi di ServiceFactory**: Buka `src/util/ServiceFactory.java` dan hapus instansiasi service yang dihapus (misal `getObatService`).
2.  **Hapus Referensi di IDGenerator**: Jika ada referensi ke model yang dihapus.
3.  **Clean & Build**: Jalankan perintah `ant clean` kemudian `ant run` untuk memastikan tidak ada error kompilasi.

---
**Status:** Rekomendasi Pembersihan Proyek.
