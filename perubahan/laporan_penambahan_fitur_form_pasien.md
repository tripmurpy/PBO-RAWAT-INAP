# Laporan Penambahan Fitur Form Pendaftaran Pasien

**Tanggal:** 09 Mei 2026

Dokumen ini berisi rancangan data dan fitur baru yang akan ditambahkan ke dalam sistem (Form Pendaftaran Pasien). Perubahan ini mencakup pemilihan dokter penanggung jawab dari RS Eka Hospital dan pemilihan kamar perawatan.

---

## 1. Fitur Pemilihan Dokter
Akan ditambahkan fitur / dropdown bagi pasien untuk memilih dokter yang akan menangani. Berikut adalah daftar 10 Dokter dari RS Eka Hospital (berdasarkan data pencarian) yang akan dimasukkan ke dalam database:

| No | Nama Dokter | Spesialisasi | Gelar / Jabatan / Kebisaan |
|----|-------------|--------------|----------------------------|
| 1 | dr. Bayushi Eka Putra, Sp.JP (K), FIHA | Jantung & Pembuluh Darah | Konsultan Kardiologi Intervensi, Penanganan Serangan Jantung |
| 2 | dr. Sidhi Laksono, Sp.JP, Subsp.KI(K), FIHA | Jantung & Pembuluh Darah | Subspesialis Kardiologi Intervensi |
| 3 | dr. Radhiyatam Mardhiyah, Sp.PD, Subsp.R.(K) | Penyakit Dalam | Konsultan Reumatologi, Penanganan Autoimun |
| 4 | dr. Anggun Mekar Kusuma, Sp.PD-KGer | Penyakit Dalam | Konsultan Geriatri (Penyakit Dalam Lansia) |
| 5 | dr. Johannes Ridwan T S, Sp.A, FPPS, DAA | Anak (Pediatri) | Ahli Alergi dan Imunologi Anak |
| 6 | dr. Christine Natalita, Sp.A | Anak (Pediatri) | Tumbuh Kembang Anak dan Vaksinasi |
| 7 | dr. Agus Heriyanto, Sp.OG, Subsp.FER, MARS, MM | Kebidanan & Kandungan | Konsultan Fertilitas Endokrinologi Reproduksi |
| 8 | dr. Merwin Tjahjadi, Sp.OG | Kebidanan & Kandungan | Ahli Kandungan, Pemeriksaan Kehamilan & Persalinan |
| 9 | dr. Lia Natalia, Sp.THT-KL | THT-KL | Bedah Telinga, Hidung, Tenggorokan, Kepala & Leher |
| 10 | dr. Hilda Sasdyanita, Sp.OT, AIFO-K | Orthopedi & Traumatologi | Penanganan Cedera Tulang, Sendi, dan Otot Olahraga |

---

## 2. Fitur Pemilihan Kamar
Akan ditambahkan tombol / menu untuk masuk ke form pemilihan kamar. Form ini akan menampilkan daftar kamar yang tersedia untuk di-*choose* oleh pengguna. 

Berikut adalah rancangan 25 kamar yang tersebar di 7 lantai pada Gedung A. Format penomoran menggunakan **[Kode Gedung][Nomor Lantai][Nomor Kamar]** (contoh: A29).

### Spesifikasi Tipe Kamar
*   **VIP**: Harga Rp 1.500.000 / Malam. Fasilitas: AC, TV 32 Inch, 1 Bed Pasien Elektrik, Sofa Penunggu, Kamar Mandi Dalam.
*   **VVIP**: Harga Rp 2.500.000 / Malam. Fasilitas: AC, TV 42 Inch, 1 Bed Pasien Elektrik Premium, Ruang Tamu Khusus Keluarga, Kulkas Kecil, Microwave, Kamar Mandi Dalam VIP.

### Data Kamar (25 Kamar)

| No Ruangan | Tipe | Lantai | Harga / Malam | Status | Nama Pasien | Nama Penanggung Jawab |
|------------|------|--------|---------------|--------|-------------|-----------------------|
| **A11** | VIP | Lantai 1 | Rp 1.500.000 | Terpakai | Budi Santoso | Andi Santoso |
| **A12** | VIP | Lantai 1 | Rp 1.500.000 | Kosong | - | - |
| **A13** | VIP | Lantai 1 | Rp 1.500.000 | Kosong | - | - |
| **A14** | VVIP | Lantai 1 | Rp 2.500.000 | Terpakai | Siti Aminah | Rahmat Hidayat |
| **A21** | VIP | Lantai 2 | Rp 1.500.000 | Kosong | - | - |
| **A22** | VIP | Lantai 2 | Rp 1.500.000 | Kosong | - | - |
| **A23** | VIP | Lantai 2 | Rp 1.500.000 | Terpakai | Antonius Wijaya | Maria Kusuma |
| **A24** | VVIP | Lantai 2 | Rp 2.500.000 | Kosong | - | - |
| **A31** | VIP | Lantai 3 | Rp 1.500.000 | Kosong | - | - |
| **A32** | VIP | Lantai 3 | Rp 1.500.000 | Kosong | - | - |
| **A33** | VIP | Lantai 3 | Rp 1.500.000 | Terpakai | Rina Melati | Dodi Hermawan |
| **A34** | VVIP | Lantai 3 | Rp 2.500.000 | Kosong | - | - |
| **A41** | VIP | Lantai 4 | Rp 1.500.000 | Kosong | - | - |
| **A42** | VIP | Lantai 4 | Rp 1.500.000 | Terpakai | Hendra Gunawan | Lina Marlina |
| **A43** | VIP | Lantai 4 | Rp 1.500.000 | Kosong | - | - |
| **A44** | VVIP | Lantai 4 | Rp 2.500.000 | Kosong | - | - |
| **A51** | VIP | Lantai 5 | Rp 1.500.000 | Kosong | - | - |
| **A52** | VIP | Lantai 5 | Rp 1.500.000 | Kosong | - | - |
| **A53** | VIP | Lantai 5 | Rp 1.500.000 | Terpakai | Wahyu Saputra | Nina Agustina |
| **A54** | VVIP | Lantai 5 | Rp 2.500.000 | Kosong | - | - |
| **A61** | VIP | Lantai 6 | Rp 1.500.000 | Kosong | - | - |
| **A62** | VIP | Lantai 6 | Rp 1.500.000 | Kosong | - | - |
| **A63** | VVIP | Lantai 6 | Rp 2.500.000 | Terpakai | Eka Pertiwi | Bima Aryo |
| **A71** | VIP | Lantai 7 | Rp 1.500.000 | Kosong | - | - |
| **A72** | VVIP | Lantai 7 | Rp 2.500.000 | Kosong | - | - |

---

### Catatan Implementasi UI
1. **Dropdown Dokter**: Menggunakan komponen `ChoiceGroup.POPUP` agar menghemat tempat di layar J2ME.
2. **Menu Pilih Kamar**: Disediakan tombol khusus (`Command` atau tombol UI) di bawah form pendaftaran yang jika ditekan akan membuka `List` atau Form baru berisi pilihan kamar yang berstatus **Kosong**. Kamar dengan status **Terpakai** akan didisable / tidak dimunculkan dalam pilihan.
