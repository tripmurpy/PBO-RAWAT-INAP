# Laporan Ringkasan Sistem (PBO-SYSTEM-RAWA-INAP)

Berikut adalah informasi penting mengenai kredensial, keamanan, dan struktur data dalam sistem.

## 1. Kredensial Default
Sistem secara otomatis membuat akun administrator jika belum ada data pengguna.

| Atribut | Detail |
| :--- | :--- |
| **Username** | `admin` |
| **Password** | `admin123` |
| **Role** | `ADMIN` (Administrator) |
| **Nama Lengkap** | `Administrator` |

> [!IMPORTANT]
> Akun ini diinisialisasi melalui method `inisialisasiDefault()` di dalam `UserDB.java`.

## 2. Keamanan Data
Sistem menerapkan enkapsulasi dan hashing untuk melindungi informasi sensitif.

- **Hashing Password**: Menggunakan algoritma kustom (karena batasan J2ME).
    - **Salt**: `RSI_SALT_v1_`
    - **Metode**: Salted + Password Length + Iterasi Stretching (1000x).
    - **Penyimpanan**: Password tidak pernah disimpan dalam bentuk teks biasa (*plaintext*).
- **Penyimpanan (RMS)**: Data disimpan dalam Record Management System J2ME. Nama store untuk user adalah `user_store`.

## 3. Format Penomoran (ID Generator)
Sistem menggunakan generator ID otomatis untuk memastikan konsistensi data.

| Entitas | Format ID | Contoh |
| :--- | :--- | :--- |
| **Admisi** | `ADM-YYYY-XXXX` | `ADM-2026-0001` |
| **Pasien** | `RM-YYYYMMDD-XXX` | `RM-20260506-001` |
| **Dokter** | `DKT-XXXX` | `DKT-0001` |
| **User** | `USR-XXXX` | `USR-0001` |
| **Obat** | `OBT-XXXX` | `OBT-0001` |
| **Resep** | `RSP-XXXX` | `RSP-0001` |

## 4. Struktur Penting Lainnya
- **Service Factory**: Sentralisasi akses ke semua layanan (Service) melalui `ServiceFactory.getInstance()`.
- **BaseDB**: Kelas dasar untuk semua operasi database (RMS) untuk menjaga kebersihan kode (CRUD terpusat).
- **DateUtil**: Digunakan untuk standarisasi format tanggal di seluruh aplikasi.

---
*Laporan ini dibuat berdasarkan analisis kode sumber terbaru pada 6 Mei 2026.*
