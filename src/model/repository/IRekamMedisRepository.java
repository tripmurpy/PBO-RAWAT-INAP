package model.repository;

import java.util.Vector;
import model.RekamMedis;

public interface IRekamMedisRepository {
    void save(RekamMedis rm) throws Exception;
    RekamMedis findById(String id) throws Exception;
    RekamMedis findByAdmisiId(String admisiId) throws Exception;
    void update(RekamMedis rm) throws Exception;
    Vector getAll() throws Exception;
}
