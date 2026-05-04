package controller;

import java.util.Vector;
import model.Dokter;
import service.DokterService;

/**
 * DokterController — Menangani aksi manajemen dokter dari UI.
 */
public class DokterController {

    private DokterService dokterService;

    public DokterController() {
        this.dokterService = new DokterService();
    }

    public Dokter tambahDokter(String nama, String spesialisasi, 
                                String jadwal) throws Exception {
        return dokterService.tambahDokter(nama, spesialisasi, jadwal);
    }

    public Dokter cariById(String id) throws Exception {
        return dokterService.cariById(id);
    }

    public Vector getSemuaDokter() throws Exception {
        return dokterService.getSemuaDokter();
    }

    public Vector cariBySpesialis(String spesialisasi) throws Exception {
        return dokterService.cariBySpesialis(spesialisasi);
    }

    public void updateDokter(Dokter dokter) throws Exception {
        dokterService.updateDokter(dokter);
    }

    public void hapusDokter(int recordId) throws Exception {
        dokterService.hapusDokter(recordId);
    }
}
