package model.repository;

import java.util.Vector;
import model.Dokter;

/**
 * IDokterRepository — Interface repository untuk data Dokter.
 * POLYMORPHISM: Kontrak yang diimplementasikan oleh DokterDB.
 */
public interface IDokterRepository {

    void save(Dokter dokter) throws Exception;
    Dokter findById(String id) throws Exception;
    Vector findAll() throws Exception;
    Vector findBySpesialis(String spesialisasi) throws Exception;
    void update(Dokter dokter) throws Exception;
    void delete(int recordId) throws Exception;
}
