# Laporan: Penambahan Seed Data Pasien ke RMS

**Tanggal:** 2026-05-09  
**File diubah:** `src/util/SeedData.java`

---

## Ringkasan

Ditambahkan method `seedPasien()` ke kelas `SeedData` sehingga 6 pasien awal
otomatis tersimpan ke RMS (`pasien_store`) saat aplikasi pertama kali dijalankan.
Setiap pasien juga memperbarui status kamar yang ditempati menjadi **TERISI**.

---

## Daftar Pasien yang Ditambahkan

| No | Nama                    | Tgl Lahir      | JK        | Asuransi        | Kamar | Tipe  | Dokter PJ                        | Spesialisasi     |
|----|-------------------------|----------------|-----------|-----------------|-------|-------|----------------------------------|------------------|
| 1  | Yosia Siahaan           | 14 Mar 1995    | Laki-laki | BPJS            | A21   | VIP   | dr. Bayushi Eka P, Sp.JP(K)      | Jantung          |
| 2  | Joachim Simson Ririhena | 7 Nov 1988     | Laki-laki | Asuransi Swasta | A14   | VVIP  | dr. Radhiyatam M, Sp.PD          | Penyakit Dalam   |
| 3  | Margaretha Susanti      | 22 Jun 2001    | Perempuan | BPJS            | A31   | VIP   | dr. Anggun Mekar K, Sp.PD        | Penyakit Dalam   |
| 4  | Bambang Prasetyo        | 10 Jan 1979    | Laki-laki | Umum            | A22   | VIP   | dr. Johannes R, Sp.A             | Anak             |
| 5  | Felicia Tanujaya        | 3 Ags 1992     | Perempuan | Asuransi Swasta | A44   | VVIP  | dr. Christine Natalita, Sp.A     | Anak             |
| 6  | Rudi Hartono            | 28 Feb 1985    | Laki-laki | BPJS            | A32   | VIP   | dr. Hilda Sasdyanita, Sp.OT      | Orthopedi        |

---

## Perubahan Teknis

### `src/util/SeedData.java`
- **Import ditambah:** `model.Pasien`, `storage.PasienDB`
- **`run()`:** Memanggil `seedPasien()` setelah `seedObat()`
- **`seedPasien()`** *(baru)*:
  - Mengecek jika `pasien_store` sudah berisi data → skip (idempoten)
  - Membuat 6 objek `Pasien` dengan `IDGenerator.generateNoRM()` untuk No. RM unik
  - Menyimpan setiap pasien ke `PasienDB` (RMS `pasien_store`)
  - Memanggil helper `isiKamar()` untuk menandai kamar menjadi TERISI di `ruangan_store`
- **`mkDate(int, int, int)`** *(baru)*: Konversi tanggal ke milidetik epoch (estimasi J2ME-safe)
- **`isiKamar(RuanganDB, String, String, String)`** *(baru)*: Update status ruangan ke TERISI

### Status Kamar yang Diperbarui
| Kamar | Status Baru | Pasien              |
|-------|-------------|---------------------|
| A21   | TERISI      | Yosia Siahaan       |
| A14   | TERISI      | Joachim Simson Ririhena |
| A31   | TERISI      | Margaretha Susanti  |
| A22   | TERISI      | Bambang Prasetyo    |
| A44   | TERISI      | Felicia Tanujaya    |
| A32   | TERISI      | Rudi Hartono        |

---

## Hasil Build

```
BUILD SUCCESSFUL
Total time: 4 seconds
dist/hospital-rawat-inap.jar (119052 bytes)
```

Tidak ada error kompilasi. Warning yang muncul (raw Vector, obsolete source 1.3)
adalah normal untuk proyek J2ME dan tidak mempengaruhi fungsionalitas.

---

## Catatan

- Data seed hanya dijalankan **sekali** (idempoten — cek `db.getAll().size() > 0`)
- Format No. RM mengikuti pola `RM-YYYYMMDD-XXX` dari `IDGenerator`
- Status pasien di-set ke `STATUS_DIRAWAT` agar konsisten dengan kamar TERISI
