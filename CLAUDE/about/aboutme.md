# PROJECT OVERVIEW: J2ME Sistem Rumah Sakit Rawat Inap

## Deskripsi
Aplikasi manajemen **Rawat Inap** untuk rumah sakit berbasis platform Java J2ME. Fokus utama adalah pada fungsi administrasi (Admin) untuk mengelola data pasien, dokter, ruangan, dan proses admisi/discharge.

## Target Platform
- **Runtime:** Java MIDP 2.0 / CLDC 1.1
- **Emulator:** Sun WTK 2.5.2 / MicroEmu
- **Penyimpanan:** RMS (Record Management System) - Full Offline

## Peran Pengguna (Role)
1. **Admin:** Memiliki akses penuh untuk pendaftaran pasien, pencarian data, manajemen admisi (masuk/keluar), manajemen dokter, manajemen kamar, dan melihat riwayat kunjungan.

## Batasan Teknis (Constraints)
- **Bahasa:** Semua UI, variabel, dan dokumentasi menggunakan **Bahasa Indonesia**.
- **Utility:** Tidak ada `SimpleDateFormat` (menggunakan `DateUtil` custom).
- **Serialisasi:** Menggunakan `DataOutputStream` / `DataInputStream` via `RMSUtil`.
- **Navigation:** Menggunakan `ScreenManager` (Singleton).
