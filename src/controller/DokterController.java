package controller;

import java.util.Vector;
import model.Dokter;
import service.DokterService;
import util.ServiceFactory;

/**
 * DokterController — Validasi ringan + routing ke DokterService.
 */
public class DokterController {

    private DokterService dokterService;

    public DokterController() {
        this.dokterService = ServiceFactory.getInstance().getDokterService();
    }

    public Dokter tambahDokter(String nama, String spesialisasi, String jadwal) throws Exception {
        return dokterService.tambahDokter(nama, spesialisasi, jadwal);
    }

    public Dokter cariById(String id) throws Exception {
        return dokterService.cariById(id);
    }

    public Vector getDokterAktif() throws Exception {
        return dokterService.getDokterAktif();
    }

    public Vector getSemuaDokter() throws Exception {
        return dokterService.getSemuaDokter();
    }

    public void nonAktifkan(String id) throws Exception {
        dokterService.nonAktifkan(id);
    }

    public void updateDokter(Dokter dokter) throws Exception {
        dokterService.updateDokter(dokter);
    }
}
