package controller;

import java.util.Vector;
import model.Pasien;
import service.PasienService;

/**
 * PasienController — Menangani aksi CRUD pasien dari UI.
 */
public class PasienController {

    private PasienService pasienService;

    public PasienController() {
        this.pasienService = new PasienService();
    }

    /** Daftarkan pasien baru */
    public Pasien daftarPasienBaru(String nama, String tglLahir,
            String jenisKelamin, String alamat,
            String noTelp, String asuransi) throws Exception {
        return pasienService.daftarBaru(nama, tglLahir, jenisKelamin,
                                        alamat, noTelp, asuransi);
    }

    /** Cari pasien berdasarkan No. RM */
    public Pasien cariByRM(String noRM) throws Exception {
        return pasienService.cariByRM(noRM);
    }

    /** Cari pasien berdasarkan nama */
    public Vector cariByNama(String keyword) throws Exception {
        return pasienService.cariByNama(keyword);
    }

    /** Ambil semua pasien */
    public Vector getSemuaPasien() throws Exception {
        return pasienService.getSemuaPasien();
    }

    /** Update data pasien */
    public void updatePasien(Pasien pasien) throws Exception {
        pasienService.updatePasien(pasien);
    }

    /** Hapus pasien */
    public void hapusPasien(int recordId) throws Exception {
        pasienService.hapusPasien(recordId);
    }
}
