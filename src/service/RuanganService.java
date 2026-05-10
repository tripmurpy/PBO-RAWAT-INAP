package service;

import java.util.Vector;
import model.Ruangan;
import model.repository.IRuanganRepository;
import util.IDGenerator;
import util.Validator;

/**
 * RuanganService — Logika bisnis manajemen kamar/ruangan.
 */
public class RuanganService {

    private IRuanganRepository ruanganRepo;

    public RuanganService(IRuanganRepository ruanganRepo) {
        this.ruanganRepo = ruanganRepo;
    }

    /** Menambah ruangan baru */
    public Ruangan tambahRuangan(String namaRuangan, String tipeKamar, 
                                  int kapasitas) throws Exception {
        String error = Validator.validasiRuangan(namaRuangan, tipeKamar, kapasitas);
        if (error != null) throw new Exception(error);

        String id = IDGenerator.generateRuanganId();
        Ruangan ruangan = new Ruangan(id, namaRuangan.trim(), tipeKamar, kapasitas);
        ruanganRepo.save(ruangan);
        return ruangan;
    }

    /** Mencari kamar tersedia berdasarkan tipe */
    public Vector cariKamarTersedia(String tipeKamar) throws Exception {
        return ruanganRepo.findAvailable(tipeKamar);
    }

    /** Mendapatkan semua ruangan */
    public Vector getSemuaRuangan() throws Exception {
        return ruanganRepo.findAll();
    }

    /** Mencari ruangan berdasarkan ID */
    public Ruangan cariById(String id) throws Exception {
        Ruangan r = ruanganRepo.findById(id);
        if (r == null) throw new Exception("Ruangan tidak ditemukan");
        return r;
    }

    /** Update status kamar menjadi TERISI */
    public void isiKamar(String idRuangan, String namaPasien, String namaPenanggungJawab) throws Exception {
        ruanganRepo.updateStatus(idRuangan, Ruangan.STATUS_TERISI, namaPasien, namaPenanggungJawab);
    }

    /** Update status kamar menjadi KOSONG */
    public void kosongkanKamar(String idRuangan) throws Exception {
        ruanganRepo.updateStatus(idRuangan, Ruangan.STATUS_KOSONG, "", "");
    }

    /** Mendapatkan ruangan yang tersedia (kosong) */
    public Vector getRuanganTersedia() throws Exception {
        Vector semua = ruanganRepo.findAll();
        Vector tersedia = new Vector();
        for (int i = 0; i < semua.size(); i++) {
            Ruangan r = (Ruangan) semua.elementAt(i);
            if (r.isKosong()) tersedia.addElement(r);
        }
        return tersedia;
    }

    /** Update data ruangan */
    public void updateRuangan(Ruangan ruangan) throws Exception {
        ruanganRepo.update(ruangan);
    }

    /** Menghitung statistik kamar */
    public int[] hitungStatistik() throws Exception {
        Vector semua = ruanganRepo.findAll();
        int total = semua.size();
        int terisi = 0;
        for (int i = 0; i < semua.size(); i++) {
            Ruangan r = (Ruangan) semua.elementAt(i);
            if (r.isTerisi()) terisi++;
        }
        return new int[]{total, terisi, total - terisi};
    }

    /** Melepaskan kamar berdasarkan nama ruangan (set KOSONG) */
    public void lepaskanKamar(String namaKamar) throws Exception {
        if (namaKamar == null || namaKamar.length() == 0) return;
        Vector semua = ruanganRepo.findAll();
        for (int i = 0; i < semua.size(); i++) {
            Ruangan r = (Ruangan) semua.elementAt(i);
            if (namaKamar.equals(r.getNamaRuangan())) {
                r.setKosong(true);
                r.setNamaPenanggungJawab("");
                ruanganRepo.update(r);
                return;
            }
        }
    }
}
