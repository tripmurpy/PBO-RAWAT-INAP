package controller;

import java.util.Vector;
import model.Ruangan;
import service.RuanganService;

/**
 * RuanganController — Menangani aksi manajemen kamar dari UI.
 */
public class RuanganController {

    private RuanganService ruanganService;

    public RuanganController() {
        this.ruanganService = new RuanganService();
    }

    public Ruangan tambahRuangan(String nama, String tipe, 
                                  int kapasitas) throws Exception {
        return ruanganService.tambahRuangan(nama, tipe, kapasitas);
    }

    public Vector getSemuaRuangan() throws Exception {
        return ruanganService.getSemuaRuangan();
    }

    public Vector cariKamarTersedia(String tipeKamar) throws Exception {
        return ruanganService.cariKamarTersedia(tipeKamar);
    }

    public Ruangan cariById(String id) throws Exception {
        return ruanganService.cariById(id);
    }

    /** Statistik: [total, terisi, kosong] */
    public int[] hitungStatistik() throws Exception {
        return ruanganService.hitungStatistik();
    }
}
