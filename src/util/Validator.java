package util;

/**
 * Validator — Utilitas validasi input field.
 * 
 * Menyediakan metode statis untuk memvalidasi berbagai jenis input
 * sebelum data disimpan ke RMS.
 * 
 * ENCAPSULATION: Aturan validasi terpusat di satu class,
 * tidak tersebar di seluruh kode.
 */
public class Validator {

    /**
     * Mengecek apakah string kosong atau null.
     * @param teks string yang akan dicek
     * @return true jika kosong/null
     */
    public static boolean kosong(String teks) {
        return teks == null || teks.trim().length() == 0;
    }

    /**
     * Validasi data pasien baru.
     * @return pesan error, atau null jika valid
     */
    public static String validasiPasien(String nama, String tglLahir, 
            String jenisKelamin, String alamat, String noTelp) {

        if (kosong(nama)) {
            return "Nama lengkap wajib diisi";
        }
        if (nama.trim().length() < 3) {
            return "Nama minimal 3 karakter";
        }
        if (kosong(tglLahir)) {
            return "Tanggal lahir wajib diisi";
        }
        if (DateUtil.parseTanggal(tglLahir) == -1) {
            return "Format tanggal lahir salah (DD/MM/YYYY)";
        }
        if (kosong(jenisKelamin)) {
            return "Jenis kelamin wajib dipilih";
        }
        if (!jenisKelamin.equals("L") && !jenisKelamin.equals("P")) {
            return "Jenis kelamin harus L atau P";
        }
        if (kosong(alamat)) {
            return "Alamat wajib diisi";
        }
        if (kosong(noTelp)) {
            return "No. telepon wajib diisi";
        }
        if (!adalahNomorTelpon(noTelp)) {
            return "No. telepon tidak valid";
        }

        return null; // Valid
    }

    /**
     * Validasi data dokter.
     * @return pesan error, atau null jika valid
     */
    public static String validasiDokter(String nama, String spesialisasi) {
        if (kosong(nama)) {
            return "Nama dokter wajib diisi";
        }
        if (nama.trim().length() < 3) {
            return "Nama dokter minimal 3 karakter";
        }
        if (kosong(spesialisasi)) {
            return "Spesialisasi wajib diisi";
        }
        return null;
    }

    /**
     * Validasi data ruangan.
     * @return pesan error, atau null jika valid
     */
    public static String validasiRuangan(String namaRuangan, String tipeKamar, int kapasitas) {
        if (kosong(namaRuangan)) {
            return "Nama ruangan wajib diisi";
        }
        if (kosong(tipeKamar)) {
            return "Tipe kamar wajib diisi";
        }
        if (kapasitas <= 0) {
            return "Kapasitas harus lebih dari 0";
        }
        return null;
    }

    /**
     * Validasi data admisi.
     * @return pesan error, atau null jika valid
     */
    public static String validasiAdmisi(String noRM, String idDokter, 
            String idRuangan, String diagnosisAwal, String tglMasuk) {

        if (kosong(noRM)) {
            return "No. RM pasien wajib diisi";
        }
        if (kosong(idDokter)) {
            return "Dokter penanggung jawab wajib dipilih";
        }
        if (kosong(idRuangan)) {
            return "Ruangan wajib dipilih";
        }
        if (kosong(diagnosisAwal)) {
            return "Diagnosis awal wajib diisi";
        }
        if (kosong(tglMasuk)) {
            return "Tanggal masuk wajib diisi";
        }
        if (DateUtil.parseTanggal(tglMasuk) == -1) {
            return "Format tanggal masuk salah (DD/MM/YYYY)";
        }
        return null;
    }

    /**
     * Validasi data discharge.
     * @return pesan error, atau null jika valid
     */
    public static String validasiDischarge(String diagnosisAkhir, 
            String kodeICD10, String tglKeluar) {

        if (kosong(diagnosisAkhir)) {
            return "Diagnosis akhir wajib diisi";
        }
        if (kosong(kodeICD10)) {
            return "Kode ICD-10 wajib diisi";
        }
        if (kosong(tglKeluar)) {
            return "Tanggal keluar wajib diisi";
        }
        if (DateUtil.parseTanggal(tglKeluar) == -1) {
            return "Format tanggal keluar salah (DD/MM/YYYY)";
        }
        return null;
    }

    /**
     * Validasi login credentials.
     * @return pesan error, atau null jika valid
     */
    public static String validasiLogin(String username, String password) {
        if (kosong(username)) {
            return "Username wajib diisi";
        }
        if (kosong(password)) {
            return "Password wajib diisi";
        }
        if (password.length() < 4) {
            return "Password minimal 4 karakter";
        }
        return null;
    }

    // ========== HELPER PRIVATE ==========

    /**
     * Mengecek apakah string hanya berisi digit dan panjang wajar.
     */
    private static boolean adalahNomorTelpon(String teks) {
        if (teks == null || teks.length() < 8 || teks.length() > 15) {
            return false;
        }
        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i);
            if (i == 0 && c == '+') continue; // Kode negara
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
