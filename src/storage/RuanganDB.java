package storage;

import java.util.Vector;
import model.Ruangan;
import model.repository.IRuanganRepository;
import util.RMSUtil;

/**
 * RuanganDB — Implementasi IRuanganRepository menggunakan RMS.
 */
public class RuanganDB extends BaseDB implements IRuanganRepository {

    protected String getStoreName() { return "ruangan_store"; }

    protected String getEntityId(Object e) { return ((Ruangan) e).getId(); }

    protected void setRecordId(Object e, int rid) { ((Ruangan) e).setRecordId(rid); }

    public void save(Ruangan r) throws Exception { saveEntity(r); }

    public Ruangan findById(String id) throws Exception {
        return (Ruangan) findByStringId(id);
    }

    public void update(Ruangan r) throws Exception {
        updateEntity(r, r.getRecordId());
    }

    public Vector findAll() throws Exception { return getAllEntities(); }

    public Vector findAvailable(String tipeKamar) throws Exception {
        Vector result = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Ruangan r = (Ruangan) all.elementAt(i);
            if (r.isKosong() && (tipeKamar == null || r.getTipeKamar().equalsIgnoreCase(tipeKamar))) {
                result.addElement(r);
            }
        }
        return result;
    }

    public void updateStatus(String id, String status, String namaPasien, String namaPenanggungJawab) throws Exception {
        Ruangan r = findById(id);
        if (r != null) {
            r.setKosong(status.equalsIgnoreCase("KOSONG"));
            r.setNamaPasien(namaPasien);
            r.setNamaPenanggungJawab(namaPenanggungJawab);
            update(r);
        }
    }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    protected int getRecordIdFromObject(Object e) {
        return ((Ruangan) e).getRecordId();
    }

    protected byte[] serialize(Object obj) throws Exception {
        Ruangan r = (Ruangan) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(r.getId());
        dos.writeUTF(r.getNamaRuangan());
        dos.writeUTF(r.getTipeKamar());
        dos.writeInt(r.getKapasitas());
        dos.writeBoolean(r.isKosong());
        dos.writeUTF(r.getNoRM() == null ? "" : r.getNoRM());
        dos.writeUTF(r.getNamaPasien() == null ? "" : r.getNamaPasien());
        dos.writeDouble(r.getHarga());
        dos.writeUTF(r.getFasilitas() == null ? "" : r.getFasilitas());
        dos.writeUTF(r.getNamaPenanggungJawab() == null ? "" : r.getNamaPenanggungJawab());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Ruangan r = new Ruangan();
        r.setId(dis.readUTF());
        r.setNamaRuangan(dis.readUTF());
        r.setTipeKamar(dis.readUTF());
        r.setKapasitas(dis.readInt());
        r.setKosong(dis.readBoolean());
        r.setNoRM(dis.readUTF());
        r.setNamaPasien(dis.readUTF());
        if (dis.available() > 0) {
            r.setHarga(dis.readDouble());
            r.setFasilitas(dis.readUTF());
            r.setNamaPenanggungJawab(dis.readUTF());
        } else {
            r.setHarga(0);
            r.setFasilitas("");
            r.setNamaPenanggungJawab("");
        }
        dis.close();
        return r;
    }
}
