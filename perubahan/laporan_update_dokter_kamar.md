# Laporan Update: Fitur Pemilihan Dokter & Kamar
**Tanggal:** 2026-05-09  
**Modul:** Form Pendaftaran Pasien (PasienFormScreen)

---

## Ringkasan Perubahan

Penambahan dua fitur baru pada form pendaftaran pasien:
1. **Pemilihan Dokter Penanggung Jawab**
2. **Pemilihan & Manajemen Kamar Rawat**

---

## 1. Pemilihan Dokter

### File yang diubah
| File | Perubahan |
|------|-----------|
| `src/ui/PasienFormScreen.java` | Tambah `ChoiceGroup cgDokter` dropdown dari DB |
| `src/model/Pasien.java` | Tambah field `dokterPenanggungJawab` |
| `src/storage/PasienDB.java` | Update serialisasi dengan backward-compat |
| `src/service/PasienService.java` | Update parameter metode `daftarPasienBaru` |
| `src/controller/PasienController.java` | Teruskan parameter dokter ke service |
| `src/util/SeedData.java` | Seed 10 dokter spesialis RS Eka Hospital |

### Data Dokter Eka Hospital (10 Dokter)
Dokter spesialis yang di-seed meliputi: Spesialis Jantung, Neurologi, Ortopedi, Penyakit Dalam, Bedah, Anak, Kandungan, Radiologi, Anestesi, dan Urologi.

---

## 2. Pemilihan & Manajemen Kamar

### File yang diubah
| File | Perubahan |
|------|-----------|
| `src/model/Ruangan.java` | Tambah field `harga`, `fasilitas`, `namaPenanggungJawab`; tambah tipe VVIP; tambah konstruktor 6-arg |
| `src/model/repository/IRuanganRepository.java` | Update signature `updateStatus` dengan param `namaPenanggungJawab` |
| `src/storage/RuanganDB.java` | Update serialisasi/deserialisasi field baru; update `updateStatus` |
| `src/service/RuanganService.java` | Update `isiKamar(...)` dan `kosongkanKamar(...)` |
| `src/service/AdmisiService.java` | Update pemanggilan `isiKamar` |
| `src/ui/KamarSelectionScreen.java` | Screen baru untuk memilih kamar kosong |
| `src/ui/PasienFormScreen.java` | Tambah `StringItem` tampilan kamar + tombol "Pilih Kamar" |
| `src/util/SeedData.java` | Seed 25 kamar (Gedung A, Lantai 1–7, tipe VIP & VVIP) |

### Struktur Kamar
- **Jumlah:** 25 kamar
- **Gedung:** A
- **Lantai:** 1–7
- **Format Nomor:** `A[lantai][no]` (contoh: A11 = Gedung A, Lantai 1, Kamar 1)
- **Tipe:** VIP & VVIP
- **Data per kamar:** Nomor, Tipe, Harga/malam, Fasilitas, Status (Kosong/Terisi), Nama Pasien, Nama Penanggung Jawab

---

## 3. Alur Registrasi Pasien Baru (Setelah Update)

```
Buka Form Pendaftaran
    → Isi data dasar (Nama, TTL, Jenis Kelamin, Alamat, Telp)
    → Pilih Asuransi (BPJS / Eka Hospital / Swasta)
    → Pilih Dokter dari dropdown (data dari DokterDB)
    → Tekan "Pilih Kamar" → Buka KamarSelectionScreen
        → List kamar dengan status KOSONG
        → Pilih kamar → kembali ke form
    → Tekan "Daftar"
        → Pasien tersimpan dengan dokter & kamar
        → Status kamar otomatis berubah → TERISI
```

---

## 4. Catatan Teknis

- **Backward Compatibility:** Deserialisasi `PasienDB` dan `RuanganDB` menggunakan `dis.available() > 0` agar data lama tidak korup saat field baru ditambah.
- **Lint Warning:** `Vector` raw type di `SeedData.java` dan `KamarSelectionScreen.java` — ini warning biasa di J2ME, tidak memengaruhi kompilasi.
- **Lint Note:** Error tipe J2ME (`ChoiceGroup`, `StringItem`, dll.) di IDE adalah false positive karena IDE tidak punya classpath J2ME. Build via `ant` tetap berhasil.
