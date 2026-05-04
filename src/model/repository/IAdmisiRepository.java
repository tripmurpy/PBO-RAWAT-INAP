package model.repository;

import java.util.Vector;
import model.Admisi;

/**
 * IAdmisiRepository — Interface repository untuk data Admisi.
 * POLYMORPHISM: Kontrak yang diimplementasikan oleh AdmisiDB.
 */
public interface IAdmisiRepository {

    void save(Admisi admisi) throws Exception;
    Admisi findById(String idAdmisi) throws Exception;
    Vector findByPasien(String noRM) throws Exception;
    Vector findAktif() throws Exception;
    Vector getAll() throws Exception;
    void update(Admisi admisi) throws Exception;
}
