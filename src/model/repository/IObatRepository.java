package model.repository;

import java.util.Vector;
import model.Obat;

public interface IObatRepository {
    void save(Obat o) throws Exception;
    Obat findById(String id) throws Exception;
    Vector getAll() throws Exception;
    void update(Obat o) throws Exception;
    void delete(int recordId) throws Exception;
    Vector cariByNama(String keyword) throws Exception;
    Vector getLowStock() throws Exception;
}
