package controller;

import java.util.Vector;
import model.Ruangan;
import service.RuanganService;
import util.ServiceFactory;

/**
 * RuanganController — Validasi ringan + routing ke RuanganService.
 */
public class RuanganController {

    private RuanganService ruanganService;

    public RuanganController() {
        this.ruanganService = ServiceFactory.getInstance().getRuanganService();
    }

    public Ruangan tambahRuangan(String nama, String tipe, int lantai, double harga, String fasilitas) throws Exception {
        return ruanganService.tambahRuangan(nama, tipe, lantai, harga, fasilitas);
    }

    public Ruangan cariById(String id) throws Exception {
        return ruanganService.cariById(id);
    }

    public Vector getSemuaRuangan() throws Exception {
        return ruanganService.getSemuaRuangan();
    }

    public Vector getRuanganTersedia() throws Exception {
        return ruanganService.getRuanganTersedia();
    }

    public void updateRuangan(Ruangan ruangan) throws Exception {
        ruanganService.updateRuangan(ruangan);
    }
}
