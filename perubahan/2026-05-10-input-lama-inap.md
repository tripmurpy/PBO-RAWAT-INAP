# Laporan: Penambahan Fitur Input Lama Inap & Perbaikan Harga Kamar

**Tanggal:** 2026-05-10  
**Tujuan:** Memberikan kontrol manual atas durasi rawat inap dan memastikan harga kamar tidak bernilai nol pada saat penagihan.

---

## 1. Penambahan Input "Lama Rawat Inap"
Sebelumnya, sistem menghitung durasi rawat secara otomatis atau default 1 hari. Sekarang, staf dapat menyesuaikan jumlah hari secara manual di layar pembayaran.

### Perubahan Teknis:
- **Layar:** `PasienKeluarScreen.java`
- **Fitur Baru:** 
  - Section baru **"DURASI RAWAT INAP"** ditambahkan di bagian atas card pembayaran.
  - Kontrol navigasi ditingkatkan (Focus Section 0).
  - Menggunakan tombol **KIRI / KANAN** untuk menambah atau mengurangi hari.
  - **Auto-Update:** Setiap perubahan hari akan langsung memicu perhitungan ulang seluruh komponen biaya (Kamar, Makan, Obat) dan Total Bayar secara real-time.

---

## 2. Perbaikan Harga Kamar (Seed Data)
Ditemukan masalah di mana harga kamar muncul sebagai **Rp 0** karena data awal (seed) tidak memiliki nilai harga.

### Perubahan Teknis:
- **File:** `src/util/SeedData.java`
- **Penyesuaian Harga:**
  - **VIP (Lantai 1-7):** Di-set menjadi **Rp 1.500.000** per malam.
  - **VVIP (Lantai 1-7):** Di-set menjadi **Rp 2.500.000** per malam.
- **Penambahan Fasilitas:** Menambahkan deskripsi fasilitas pada setiap kamar agar tampilan detail lebih informatif.

---

## 3. Logika Perhitungan Biaya (Approach 2)
Sistem sekarang konsisten menggunakan logika kelas kamar:
- **Total Biaya** = `(Harga Kamar + Biaya Makan + Biaya Obat) * Lama Inap` + `Biaya Admin`.
- Biaya Makan & Obat disesuaikan secara otomatis berdasarkan tipe kamar (VIP/Regular).

---

## Hasil Akhir (Preview)
- Layar pembayaran kini memiliki 4 section fokus:
  1. Durasi Rawat Inap (+/- Hari)
  2. Tipe Pembayaran (Cash/QRIS/dll)
  3. Pilihan Bank (Jika non-cash)
  4. Tombol Bayar

**Status:** BERHASIL (Build OK, dist/hospital-rawat-inap.jar diperbarui).
