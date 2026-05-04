package service;

import java.util.Vector;
import model.Ruangan;
import storage.RuanganDB;
import util.IDGenerator;
import util.Validator;

/**
 * RuanganService — Logika bisnis manajemen kamar/ruangan.
 */
public class RuanganService {

    private RuanganDB ruanganDB;

    public RuanganService() {
        this.ruanganDB = new RuanganDB();
    }

    /** Menambah ruangan baru */
    public Ruangan tambahRuangan(String namaRuangan, String tipeKamar, 
                                  int kapasitas) throws Exception {
        String error = Validator.validasiRuangan(namaRuangan, tipeKamar, kapasitas);
        if (error != null) throw new Exception(error);

        String id = IDGenerator.generateRuanganId();
        Ruangan ruangan = new Ruangan(id, namaRuangan.trim(), tipeKamar, kapasitas);
        ruanganDB.save(ruangan);
        return ruangan;
    }

    /** Mencari kamar tersedia berdasarkan tipe */
    public Vector cariKamarTersedia(String tipeKamar) throws Exception {
        return ruanganDB.findAvailable(tipeKamar);
    }

    /** Mendapatkan semua ruangan */
    public Vector getSemuaRuangan() throws Exception {
        return ruanganDB.findAll();
    }

    /** Mencari ruangan berdasarkan ID */
    public Ruangan cariById(String id) throws Exception {
        Ruangan r = ruanganDB.findById(id);
        if (r == null) throw new Exception("Ruangan tidak ditemukan");
        return r;
    }

    /** Update status kamar menjadi TERISI */
    public void isiKamar(String idRuangan, String namaPasien) throws Exception {
        ruanganDB.updateStatus(idRuangan, Ruangan.STATUS_TERISI, namaPasien);
    }

    /** Update status kamar menjadi KOSONG */
    public void kosongkanKamar(String idRuangan) throws Exception {
        ruanganDB.updateStatus(idRuangan, Ruangan.STATUS_KOSONG, "");
    }

    /** Menghitung statistik kamar */
    public int[] hitungStatistik() throws Exception {
        Vector semua = ruanganDB.findAll();
        int total = semua.size();
        int terisi = 0;
        for (int i = 0; i < semua.size(); i++) {
            Ruangan r = (Ruangan) semua.elementAt(i);
            if (r.isTerisi()) terisi++;
        }
        return new int[]{total, terisi, total - terisi};
    }
}
