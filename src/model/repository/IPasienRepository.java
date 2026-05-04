package model.repository;

import java.util.Vector;
import model.Pasien;

/**
 * IPasienRepository — Interface repository untuk data Pasien.
 * 
 * POLYMORPHISM: Interface ini mendefinisikan kontrak yang HARUS
 * diimplementasikan oleh class konkret (PasienDB).
 * Service layer hanya mengenal interface ini, bukan implementasinya.
 */
public interface IPasienRepository {

    /** Menyimpan pasien baru ke penyimpanan. */
    void save(Pasien pasien) throws Exception;

    /** Mencari pasien berdasarkan No. RM. */
    Pasien findByRM(String noRM) throws Exception;

    /** Mencari pasien berdasarkan record ID internal. */
    Pasien findById(int recordId) throws Exception;

    /** Mengambil semua data pasien. */
    Vector getAll() throws Exception;

    /** Memperbarui data pasien yang sudah ada. */
    void update(Pasien pasien) throws Exception;

    /** Menghapus pasien berdasarkan record ID. */
    void delete(int recordId) throws Exception;

    /** Mencari pasien berdasarkan nama (pencarian parsial). */
    Vector cariByNama(String keyword) throws Exception;
}
