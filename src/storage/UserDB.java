package storage;

import java.util.Vector;
import model.User;
import model.repository.IUserRepository;
import util.IDGenerator;
import util.RMSUtil;

/**
 * UserDB — Implementasi IUserRepository menggunakan RMS.
 */
public class UserDB extends BaseDB implements IUserRepository {

    protected String getStoreName() { return "user_store"; }

    protected String getEntityId(Object e) { return ((User) e).getUsername(); }

    protected void setRecordId(Object e, int rid) { ((User) e).setRecordId(rid); }
    
    protected int getRecordIdFromObject(Object e) { return ((User) e).getRecordId(); }

    public void save(User u) throws Exception { saveEntity(u); }

    public User findByUsername(String username) throws Exception {
        return (User) findByStringId(username);
    }

    public void inisialisasiDefault() throws Exception {
        if (!adaDataUser()) {
            save(new User(IDGenerator.generateUserId(), "admin", 
                         User.hashPassword("admin123"), "Administrator", User.ROLE_ADMIN));
        }
    }

    public boolean adaDataUser() throws Exception {
        return getAllEntities().size() > 0;
    }

    public boolean verify(String username, String password) throws Exception {
        User user = findByUsername(username);
        if (user != null) {
            return user.verifikasiPassword(password);
        }
        return false;
    }

    protected byte[] serialize(Object obj) throws Exception {
        User u = (User) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(u.getId());
        dos.writeUTF(u.getUsername());
        dos.writeUTF(u.getPasswordHash());
        dos.writeUTF(u.getNamaLengkap());
        dos.writeUTF(u.getRole());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
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
