package model.repository;

import java.util.Vector;
import model.Resep;

public interface IResepRepository {
    void save(Resep r) throws Exception;
    Resep findById(String id) throws Exception;
    Vector findByRekamMedisId(String rekamMedisId) throws Exception;
    void update(Resep r) throws Exception;
    void delete(int recordId) throws Exception;
}
