# Laporan Perubahan - 13 Mei 2026
## Peningkatan Fitur, Bug Fixing, dan Modernisasi UI

Hari ini telah dilakukan serangkaian pembaruan pada System Rawat Inap Rasuna Said 5 untuk meningkatkan fungsionalitas medis dan pengalaman pengguna.

### 1. Penambahan Fitur Keluhan Pasien
*   **Model (`Pasien.java`)**: Penambahan atribut `keluhan` untuk menyimpan alasan utama pasien masuk.
*   **Form Pendaftaran (`PasienFormScreen.java`)**: Penambahan field input keluhan yang ditempatkan secara strategis sebelum pemilihan dokter.
*   **Detail Pasien (`PasienDetailScreen.java`)**: Menampilkan informasi keluhan dalam kartu detail pasien.

### 2. Modernisasi UI & UX
*   **Urutan Input yang Logis**: Mengatur ulang urutan field pada form pendaftaran sehingga keluhan diisi sebelum memilih dokter penanggung jawab.
*   **Modern Card Visit History (`KunjunganScreen.java`)**: Mengubah tampilan riwayat kunjungan yang sebelumnya list sederhana menjadi tampilan berbasis kartu (card) yang lebih informatif dan estetik.
*   **Optimalisasi Input (`ChoiceGroup`)**: Mengganti input manual teks untuk 'Asuransi' dan 'Dokter Penanggung Jawab' dengan komponen pilihan (ChoiceGroup) untuk memastikan validitas data.

### 3. Perbaikan Bug (Bug Fixing)
*   **Fix Missing Guardian Info**: Memperbaiki masalah di mana Nama Wali dan Nomor Wali tidak muncul pada layar detail pasien meskipun sudah diisi saat pendaftaran.
*   **Sinkronisasi RMS**: Penyesuaian pada `PasienDB` untuk memastikan field baru (keluhan) tersimpan dan terbaca dengan benar dari penyimpanan permanen.

### 4. Analisis Performa
*   **Diagnosa Lag**: Melakukan pengecekan pada proses startup dan manipulasi data di RMS untuk mengidentifikasi bottleneck yang menyebabkan aplikasi terasa lambat pada perangkat tertentu.
*   **Optimasi Seed Data**: Menyederhanakan proses inisialisasi data awal untuk mempercepat waktu loading aplikasi.

---
**Status: Selesai & Terintegrasi**
