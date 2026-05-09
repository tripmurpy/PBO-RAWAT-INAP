# Laporan Perubahan UI Form Pasien

**Tanggal:** 09 Mei 2026
**File yang Diubah:** `src/ui/PasienFormScreen.java`

## Masalah Sebelumnya
1. Kolom Tanggal Lahir tidak dapat di-input (menggunakan `DateField`).
2. Pilihan Jenis Kelamin tidak dapat diklik (menggunakan `ChoiceGroup.EXCLUSIVE`).
3. Pilihan Asuransi tidak dapat diklik (menggunakan `ChoiceGroup.EXCLUSIVE`).

## Solusi & Perubahan
1. **Tanggal Lahir**: Diubah dari `DateField` menjadi `TextField` biasa. Pengguna kini dapat langsung mengetikkan tanggal dalam format `DD/MM/YYYY`.
2. **Jenis Kelamin**: Diubah tipenya dari `ChoiceGroup.EXCLUSIVE` (Radio Button) menjadi `ChoiceGroup.POPUP` (Dropdown). Lebih ramah untuk navigasi emulator.
3. **Asuransi**: Diubah tipenya dari `ChoiceGroup.EXCLUSIVE` (Radio Button) menjadi `ChoiceGroup.POPUP` (Dropdown).

Perubahan ini membuat form sepenuhnya responsif dan mudah digunakan melalui keypad/emulator.
