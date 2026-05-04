package service;

import java.util.Vector;
import model.Dokter;
import model.repository.IDokterRepository;
import util.IDGenerator;
import util.Validator;

/**
 * DokterService — Logika bisnis pengelolaan data dokter.
 */
public class DokterService {

    private IDokterRepository dokterRepo;

    public DokterService(IDokterRepository dokterRepo) {
        this.dokterRepo = dokterRepo;
    }

    /** Menambah dokter baru */
    public Dokter tambahDokter(String nama, String spesialisasi, 
                                String jadwal) throws Exception {
        String error = Validator.validasiDokter(nama, spesialisasi);
        if (error != null) throw new Exception(error);

        String id = IDGenerator.generateDokterId();
        Dokter dokter = new Dokter(id, nama.trim(), spesialisasi.trim(), 
                                    jadwal != null ? jadwal.trim() : "-");
        dokterRepo.save(dokter);
        return dokter;
    }

    /** Mencari dokter berdasarkan ID */
    public Dokter cariById(String id) throws Exception {
        Dokter d = dokterRepo.findById(id);
        if (d == null) throw new Exception("Dokter tidak ditemukan");
        return d;
    }

    /** Mendapatkan semua dokter */
    public Vector getSemuaDokter() throws Exception {
        return dokterRepo.findAll();
    }

    /** Mencari dokter berdasarkan spesialisasi */
    public Vector cariBySpesialis(String spesialisasi) throws Exception {
        return dokterRepo.findBySpesialis(spesialisasi);
    }

    /** Memperbarui data dokter */
    public void updateDokter(Dokter dokter) throws Exception {
        dokterRepo.update(dokter);
    }

    /** Menghapus dokter */
    public void hapusDokter(int recordId) throws Exception {
        dokterRepo.delete(recordId);
    }
}
