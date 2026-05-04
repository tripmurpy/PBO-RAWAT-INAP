package model.repository;

import model.User;

/**
 * IUserRepository — Interface repository untuk data User.
 * POLYMORPHISM: Kontrak yang diimplementasikan oleh UserDB.
 */
public interface IUserRepository {

    void save(User user) throws Exception;
    User findByUsername(String username) throws Exception;
    boolean verify(String username, String password) throws Exception;
    boolean adaDataUser() throws Exception;
    void inisialisasiDefault() throws Exception;
}
