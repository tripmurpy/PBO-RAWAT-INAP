package controller;

import java.util.Vector;
import model.Obat;
import service.ObatService;
import util.ServiceFactory;

/**
 * ObatController — Validasi ringan + routing ke ObatService.
 */
public class ObatController {

    private ObatService obatService;

    public ObatController() {
        this.obatService = ServiceFactory.getInstance().getObatService();
    }

    public Obat tambahObat(String nama, String bentuk, String satuan,
                            int stok, int stokMin, int harga) throws Exception {
        return obatService.tambahObat(nama, bentuk, satuan, stok, stokMin, harga);
    }

    public Obat cariById(String id) throws Exception {
        return obatService.cariById(id);
    }

    public Vector getSemuaObat() throws Exception {
        return obatService.getSemuaObat();
    }

    public Vector cariByNama(String keyword) throws Exception {
        return obatService.cariByNama(keyword);
    }

    public Vector getLowStock() throws Exception {
        return obatService.getLowStock();
    }

    public void tambahStok(String id, int qty) throws Exception {
        obatService.tambahStok(id, qty);
    }

    public void updateObat(Obat obat) throws Exception {
        obatService.updateObat(obat);
    }

    public void hapusObat(int recordId) throws Exception {
        obatService.hapusObat(recordId);
    }
}
