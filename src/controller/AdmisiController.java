package controller;

import java.util.Vector;
import model.Admisi;
import service.AdmisiService;

/**
 * AdmisiController — Menangani aksi rawat inap masuk & keluar dari UI.
 */
public class AdmisiController {

    private AdmisiService admisiService;

    public AdmisiController() {
        this.admisiService = new AdmisiService();
    }

    /** Proses admisi rawat inap baru */
    public Admisi buatAdmisi(String noRM, String idDokter,
            String idRuangan, String diagnosisAwal,
            String tglMasuk) throws Exception {
        return admisiService.buatAdmisi(noRM, idDokter, idRuangan,
                                         diagnosisAwal, tglMasuk);
    }

    /** Proses discharge (keluar pasien) */
    public Admisi discharge(String idAdmisi, String diagnosisAkhir,
            String kodeICD10, String tglKeluar,
            String catatan) throws Exception {
        return admisiService.discharge(idAdmisi, diagnosisAkhir,
                                        kodeICD10, tglKeluar, catatan);
    }

    /** Cari admisi aktif berdasarkan No. RM */
    public Admisi cariAdmisiAktifByRM(String noRM) throws Exception {
        return admisiService.cariAdmisiAktifByRM(noRM);
    }

    /** Semua admisi aktif */
    public Vector getAdmisiAktif() throws Exception {
        return admisiService.getAdmisiAktif();
    }

    /** Semua admisi */
    public Vector getSemuaAdmisi() throws Exception {
        return admisiService.getSemuaAdmisi();
    }

    /** Hitung lama rawat */
    public int hitungLamaRawat(Admisi admisi) {
        return admisiService.hitungLamaRawat(admisi);
    }
}
