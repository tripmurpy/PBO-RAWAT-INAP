package service;

import java.util.Vector;
import model.Dokter;
import storage.DokterDB;
import util.IDGenerator;
import util.Validator;

/**
 * DokterService — Logika bisnis pengelolaan data dokter.
 */
public class DokterService {

    private DokterDB dokterDB;

    public DokterService() {
        this.dokterDB = new DokterDB();
    }

    /** Menambah dokter baru */
    public Dokter tambahDokter(String nama, String spesialisasi, 
                                String jadwal) throws Exception {
        String error = Validator.validasiDokter(nama, spesialisasi);
        if (error != null) throw new Exception(error);

        String id = IDGenerator.generateDokterId();
        Dokter dokter = new Dokter(id, nama.trim(), spesialisasi.trim(), 
                                    jadwal != null ? jadwal.trim() : "-");
        dokterDB.save(dokter);
        return dokter;
    }

    /** Mencari dokter berdasarkan ID */
    public Dokter cariById(String id) throws Exception {
        Dokter d = dokterDB.findById(id);
        if (d == null) throw new Exception("Dokter tidak ditemukan");
        return d;
    }

    /** Mendapatkan semua dokter */
    public Vector getSemuaDokter() throws Exception {
        return dokterDB.findAll();
    }

    /** Mencari dokter berdasarkan spesialisasi */
    public Vector cariBySpesialis(String spesialisasi) throws Exception {
        return dokterDB.findBySpesialis(spesialisasi);
    }

    /** Memperbarui data dokter */
    public void updateDokter(Dokter dokter) throws Exception {
        dokterDB.update(dokter);
    }

    /** Menghapus dokter */
    public void hapusDokter(int recordId) throws Exception {
        dokterDB.delete(recordId);
    }
}
