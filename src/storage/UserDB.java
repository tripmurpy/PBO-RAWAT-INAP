package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import model.User;
import model.repository.IUserRepository;
import util.RMSUtil;

/**
 * UserDB — Implementasi IUserRepository menggunakan RMS.
 * POLYMORPHISM: implements IUserRepository.
 * Mengelola data akun admin dengan password di-hash.
 */
public class UserDB implements IUserRepository {

    private static final String STORE_NAME = "user_store";

    public void save(User user) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(user);
            int rid = RMSUtil.simpanRecord(rs, data);
            user.setRecordId(rid);
        } finally { RMSUtil.tutupStore(rs); }
    }

    public User findByUsername(String username) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                User u = deserialize(rs.getRecord(rid));
                u.setRecordId(rid);
                if (username.equals(u.getUsername())) return u;
            }
        } finally { RMSUtil.tutupStore(rs); }
        return null;
    }

    public boolean verify(String username, String password) throws Exception {
        User user = findByUsername(username);
        if (user == null) return false;
        return user.verifikasiPassword(password);
    }

    public boolean adaDataUser() throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            return rs.getNumRecords() > 0;
        } finally { RMSUtil.tutupStore(rs); }
    }

    /**
     * Inisialisasi akun admin default jika belum ada data.
     * Username: admin, Password: admin123
     */
    public void inisialisasiDefault() throws Exception {
        if (findByUsername("admin") == null) {
            User admin = new User(
                "USR-0001", "admin",
                User.hashPassword("admin123"),
                "Administrator", User.ROLE_ADMIN
            );
            save(admin);
        }
        if (findByUsername("abc") == null) {
            User abc = new User(
                "USR-0002", "abc",
                User.hashPassword("abc"),
                "User ABC", User.ROLE_ADMIN
            );
            save(abc);
        }
    }

    private byte[] serialize(User u) throws Exception {
        Object[] s = RMSUtil.buatOutputStream();
        DataOutputStream dos = (DataOutputStream) s[1];
        dos.writeUTF(u.getId());
        dos.writeUTF(u.getUsername());
        dos.writeUTF(u.getPasswordHash());
        dos.writeUTF(u.getNamaLengkap());
        dos.writeUTF(u.getRole());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    private User deserialize(byte[] data) throws Exception {
        DataInputStream dis = RMSUtil.buatInputStream(data);
        User u = new User();
        u.setId(dis.readUTF());
        u.setUsername(dis.readUTF());
        u.setPasswordHash(dis.readUTF());
        u.setNamaLengkap(dis.readUTF());
        u.setRole(dis.readUTF());
        dis.close();
        return u;
    }
}
