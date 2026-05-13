package service;

import java.util.Vector;
import model.Pasien;
import model.repository.IPasienRepository;
import util.IDGenerator;
import util.Validator;

/**
 * PasienService — Logika bisnis pengelolaan data pasien.
 * ENCAPSULATION: Aturan bisnis terisolasi dari UI dan storage.
 */
public class PasienService {

    private IPasienRepository pasienRepo;

    public PasienService(IPasienRepository pasienRepo) {
        this.pasienRepo = pasienRepo;
    }

    /**
     * Mendaftarkan pasien baru.
     * Validasi → Generate No.RM → Simpan ke RMS.
     * @return Pasien yang berhasil didaftarkan (dengan No.RM)
     */
    public Pasien daftarPasienBaru(String nama, String tglLahirStr,
            String jenisKelamin, String alamat, 
            String noTelp, String asuransi,
            String dokter, String kamar, String keluhan,
            String namaWali, String noTelpWali) throws Exception {

        // Validasi input
        String error = Validator.validasiPasien(nama, tglLahirStr, 
                                                 jenisKelamin, alamat, noTelp, keluhan);
        if (error != null) {
            throw new Exception(error);
        }

        // Parse tanggal
        long tglLahir = util.DateUtil.parseTanggal(tglLahirStr);

        // Generate No. RM
        String noRM = IDGenerator.generateNoRM();

        // Buat objek pasien
        Pasien pasien = new Pasien(noRM, nama.trim(), tglLahir,
                                    jenisKelamin, alamat.trim(), 
                                    noTelp.trim(), asuransi,
                                    namaWali.trim(), noTelpWali.trim());
        pasien.setDokterPenanggungJawab(dokter);
        pasien.setKamarRawat(kamar);
        pasien.setKeluhan(keluhan);

        // Simpan
        pasienRepo.save(pasien);
        return pasien;
    }

    /** Mencari pasien berdasarkan No. RM */
    public Pasien cariByRM(String noRM) throws Exception {
        Pasien p = pasienRepo.findByRM(noRM);
        if (p == null) {
            throw new Exception("Pasien tidak ditemukan");
        }
        return p;
    }

    /** Mencari pasien berdasarkan nama */
    public Vector cariByNama(String keyword) throws Exception {
        return pasienRepo.cariByNama(keyword);
    }

    /** Mendapatkan semua data pasien */
    public Vector getSemuaPasien() throws Exception {
        return pasienRepo.getAll();
    }

    /** Memperbarui data pasien */
    public void updatePasien(Pasien pasien) throws Exception {
        pasienRepo.update(pasien);
    }

    /** Menghapus data pasien */
    public void hapusPasien(int recordId) throws Exception {
        pasienRepo.delete(recordId);
    }

    /** Mengeluarkan pasien (status → PULANG) */
    public void keluarkanPasien(Pasien pasien) throws Exception {
        pasien.setStatus(Pasien.STATUS_PULANG);
        pasienRepo.update(pasien);
    }
}
