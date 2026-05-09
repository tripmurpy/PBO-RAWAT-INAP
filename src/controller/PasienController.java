package controller;

import java.util.Vector;
import model.Pasien;
import service.PasienService;
import util.ServiceFactory;

/**
 * PasienController — Validasi ringan + routing ke PasienService.
 */
public class PasienController {

    private PasienService pasienService;

    public PasienController() {
        this.pasienService = ServiceFactory.getInstance().getPasienService();
    }

    public Pasien daftarPasienBaru(String nama, String tglLahirStr,
            String jenisKelamin, String alamat,
            String noTelp, String asuransi,
            String dokter, String kamar) throws Exception {
        return pasienService.daftarPasienBaru(nama, tglLahirStr,
                jenisKelamin, alamat, noTelp, asuransi, dokter, kamar);
    }

    public Pasien cariByRM(String noRM) throws Exception {
        return pasienService.cariByRM(noRM);
    }

    public Vector cariByNama(String keyword) throws Exception {
        return pasienService.cariByNama(keyword);
    }

    public Vector getSemuaPasien() throws Exception {
        return pasienService.getSemuaPasien();
    }

    public void updatePasien(Pasien pasien) throws Exception {
        pasienService.updatePasien(pasien);
    }

    public void hapusPasien(int recordId) throws Exception {
        pasienService.hapusPasien(recordId);
    }
}
