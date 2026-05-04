package storage;

import java.util.Vector;
import model.Dokter;
import model.repository.IDokterRepository;
import util.RMSUtil;

/**
 * DokterDB — Implementasi IDokterRepository menggunakan RMS.
 */
public class DokterDB extends BaseDB implements IDokterRepository {

    protected String getStoreName() { return "dokter_store"; }

    protected String getEntityId(Object e) { return ((Dokter) e).getId(); }

    protected void setRecordId(Object e, int rid) { ((Dokter) e).setRecordId(rid); }

    public void save(Dokter d) throws Exception { saveEntity(d); }

    public Dokter findById(String id) throws Exception {
        return (Dokter) findByStringId(id);
    }

    public Vector findAll() throws Exception { return getAllEntities(); }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    public Vector findBySpesialis(String spesialisasi) throws Exception {
        Vector result = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Dokter d = (Dokter) all.elementAt(i);
            if (d.getSpesialisasi().equalsIgnoreCase(spesialisasi)) {
                result.addElement(d);
            }
        }
        return result;
    }

    public void update(Dokter d) throws Exception {
        updateEntity(d, d.getRecordId());
    }

    protected int getRecordIdFromObject(Object e) {
        return ((Dokter) e).getRecordId();
    }

    protected byte[] serialize(Object obj) throws Exception {
        Dokter d = (Dokter) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(d.getId());
        dos.writeUTF(d.getNama());
        dos.writeUTF(d.getSpesialisasi());
        dos.writeUTF(d.getJadwal());
        dos.writeBoolean(d.isAktif());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Dokter d = new Dokter();
        d.setId(dis.readUTF());
        d.setNama(dis.readUTF());
        d.setSpesialisasi(dis.readUTF());
        d.setJadwal(dis.readUTF());
        d.setAktif(dis.readBoolean());
        dis.close();
        return d;
    }
}
