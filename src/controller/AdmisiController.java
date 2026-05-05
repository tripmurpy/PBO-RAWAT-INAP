package controller;

import java.util.Vector;
import model.Admisi;
import service.AdmisiService;
import util.ServiceFactory;

/**
 * AdmisiController — Validasi ringan + routing ke AdmisiService.
 */
public class AdmisiController {

    private AdmisiService admisiService;

    public AdmisiController() {
        this.admisiService = ServiceFactory.getInstance().getAdmisiService();
    }

    public Admisi buatAdmisi(String noRM, String idDokter,
            String idRuangan, String diagnosisAwal,
            String tglMasukStr) throws Exception {
        return admisiService.buatAdmisi(noRM, idDokter, idRuangan, diagnosisAwal, tglMasukStr);
    }

    public Admisi discharge(String idAdmisi, String diagnosisAkhir,
            String kodeICD10, String tglKeluarStr,
            String catatan) throws Exception {
        return admisiService.discharge(idAdmisi, diagnosisAkhir, kodeICD10, tglKeluarStr, catatan);
    }

    public Admisi cariAdmisiAktifByRM(String noRM) throws Exception {
        return admisiService.cariAdmisiAktifByRM(noRM);
    }

    public Vector getAdmisiAktif() throws Exception {
        return admisiService.getAdmisiAktif();
    }

    public Vector getSemuaAdmisi() throws Exception {
        return admisiService.getSemuaAdmisi();
    }

    public int hitungLamaRawat(Admisi admisi) {
        return admisiService.hitungLamaRawat(admisi);
    }
}
