package service;

import java.util.Vector;
import model.Pasien;
import storage.PasienDB;
import util.NoRMGenerator;
import util.Validator;

/**
 * PasienService — Logika bisnis pengelolaan data pasien.
 * ENCAPSULATION: Aturan bisnis terisolasi dari UI dan storage.
 */
public class PasienService {

    private PasienDB pasienDB;

    public PasienService() {
        this.pasienDB = new PasienDB();
    }

    /**
     * Mendaftarkan pasien baru.
     * Validasi → Generate No.RM → Simpan ke RMS.
     * @return Pasien yang berhasil didaftarkan (dengan No.RM)
     */
    public Pasien daftarBaru(String nama, String tglLahirStr,
            String jenisKelamin, String alamat, 
            String noTelp, String asuransi) throws Exception {

        // Validasi input
        String error = Validator.validasiPasien(nama, tglLahirStr, 
                                                 jenisKelamin, alamat, noTelp);
        if (error != null) {
            throw new Exception(error);
        }

        // Parse tanggal
        long tglLahir = util.DateUtil.parseTanggal(tglLahirStr);

        // Generate No. RM
        String noRM = NoRMGenerator.generate();

        // Buat objek pasien
        Pasien pasien = new Pasien(noRM, nama.trim(), tglLahir,
                                    jenisKelamin, alamat.trim(), 
                                    noTelp.trim(), asuransi);

        // Simpan
        pasienDB.save(pasien);
        return pasien;
    }

    /** Mencari pasien berdasarkan No. RM */
    public Pasien cariByRM(String noRM) throws Exception {
        Pasien p = pasienDB.findByRM(noRM);
        if (p == null) {
            throw new Exception("Pasien tidak ditemukan");
        }
        return p;
    }

    /** Mencari pasien berdasarkan nama */
    public Vector cariByNama(String keyword) throws Exception {
        return pasienDB.cariByNama(keyword);
    }

    /** Mendapatkan semua data pasien */
    public Vector getSemuaPasien() throws Exception {
        return pasienDB.getAll();
    }

    /** Memperbarui data pasien */
    public void updatePasien(Pasien pasien) throws Exception {
        pasienDB.update(pasien);
    }

    /** Menghapus data pasien */
    public void hapusPasien(int recordId) throws Exception {
        pasienDB.delete(recordId);
    }
}
