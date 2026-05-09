package model.repository;

import java.util.Vector;
import model.Ruangan;

/**
 * IRuanganRepository — Interface repository untuk data Ruangan.
 * POLYMORPHISM: Kontrak yang diimplementasikan oleh RuanganDB.
 */
public interface IRuanganRepository {

    void save(Ruangan ruangan) throws Exception;
    Ruangan findById(String id) throws Exception;
    Vector findAll() throws Exception;
    Vector findAvailable(String tipeKamar) throws Exception;
    void updateStatus(String id, String status, String namaPasien, String namaPenanggungJawab) throws Exception;
    void update(Ruangan ruangan) throws Exception;
    void delete(int recordId) throws Exception;
}
