package controller;

import java.util.Vector;
import service.LaporanService;
import util.ServiceFactory;

/**
 * LaporanController — Aggregasi data laporan dari LaporanService.
 */
public class LaporanController {

    private LaporanService laporanService;

    public LaporanController() {
        this.laporanService = ServiceFactory.getInstance().getLaporanService();
    }

    public Vector getLaporanRuangan() throws Exception {
        return laporanService.getLaporanRuangan();
    }

    public Vector getLaporanStokObat() throws Exception {
        return laporanService.getLaporanStokObat();
    }

    public Vector getLowStockObat() throws Exception {
        return laporanService.getLowStockObat();
    }

    public Vector getPasienAktif() throws Exception {
        return laporanService.getPasienAktif();
    }

    public Vector getTopDiagnosa(int topN) throws Exception {
        return laporanService.getTopDiagnosa(topN);
    }
}
