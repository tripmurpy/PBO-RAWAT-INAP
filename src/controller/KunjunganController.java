package controller;

import java.util.Vector;
import model.Kunjungan;
import service.KunjunganService;

/**
 * KunjunganController — Menangani aksi riwayat kunjungan dari UI.
 */
public class KunjunganController {

    private KunjunganService kunjunganService;

    public KunjunganController() {
        this.kunjunganService = new KunjunganService();
    }

    /** Ambil semua riwayat kunjungan */
    public Vector getRiwayatKunjungan() throws Exception {
        return kunjunganService.getRiwayatKunjungan();
    }
}
