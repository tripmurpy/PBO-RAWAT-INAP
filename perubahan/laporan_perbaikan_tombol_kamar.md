# Laporan Perbaikan: Tombol & Halaman Kamar Rawat

**Tanggal:** 09 Mei 2026  
**Versi:** PBO-RAWAT-INAP

---

## Masalah

1. Tombol "Kamar" di dashboard tidak langsung membuka halaman kamar yang sama dengan tampilan di Dashboard.
2. Field "Kamar Rawat" di form pendaftaran pasien tidak bisa diklik langsung (harus lewat menu).

---

## Perubahan

### 1. `KamarSelectionScreen.java` — Diubah total

- **Sebelum:** Extends `List` (tampilan teks polos, berbeda dengan Dashboard).
- **Sesudah:** Extends `Canvas` dengan layout **identik** dengan `RuanganScreen` di Dashboard:
  - Header biru dengan judul "PILIH KAMAR"
  - Card per kamar dengan indikator warna (Hijau = Kosong, Merah = Terisi, Oranye = Maintenance)
  - Badge **PILIH** hijau di kamar yang tersedia
  - Klik card kamar kosong → langsung terpilih & kembali ke form

### 2. `PasienFormScreen.java` — Field Kamar diperbarui

- **Sebelum:** `StringItem` biasa → tidak bisa diklik langsung.
- **Sesudah:** Ditambah `StringItem` dengan mode **`Item.BUTTON`**:
  - Label `[ Pilih Kamar ]` → bisa diklik / ditap langsung
  - Setelah dipilih, label berubah jadi `[ Ganti Kamar ]` dan nama kamar tampil di atas tombol

---

## Hasil

- Build: ✅ `BUILD SUCCESSFUL`
- Tombol kamar di form pendaftaran **langsung bisa diklik**
- Halaman pilih kamar **identik** dengan halaman Kamar di Dashboard
