package storage;

import java.util.Vector;
import model.Admisi;
import model.repository.IAdmisiRepository;
import util.RMSUtil;

/**
 * AdmisiDB — Implementasi IAdmisiRepository menggunakan RMS.
 */
public class AdmisiDB extends BaseDB implements IAdmisiRepository {

    protected String getStoreName() { return "admisi_store"; }

    protected String getEntityId(Object e) { return ((Admisi) e).getIdAdmisi(); }

    protected void setRecordId(Object e, int rid) { ((Admisi) e).setRecordId(rid); }

    public void save(Admisi a) throws Exception { saveEntity(a); }

    public Admisi findById(String id) throws Exception {
        return (Admisi) findByStringId(id);
    }

    public Vector findByPasien(String noRM) throws Exception {
        Vector result = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Admisi a = (Admisi) all.elementAt(i);
            if (a.getNoRM().equals(noRM)) {
                result.addElement(a);
            }
        }
        return result;
    }

    public Vector findAktif() throws Exception {
        Vector result = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Admisi a = (Admisi) all.elementAt(i);
            if (a.getStatus().equals("AKTIF")) {
                result.addElement(a);
            }
        }
        return result;
    }

    public Vector getAll() throws Exception { return getAllEntities(); }

    public void update(Admisi a) throws Exception {
        updateEntity(a, a.getRecordId());
    }

    protected int getRecordIdFromObject(Object e) {
        return ((Admisi) e).getRecordId();
    }

    protected byte[] serialize(Object obj) throws Exception {
        Admisi a = (Admisi) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(a.getIdAdmisi());
        dos.writeUTF(a.getNoRM());
        dos.writeUTF(a.getIdDokter());
        dos.writeUTF(a.getIdRuangan());
        dos.writeUTF(a.getDiagnosisAwal());
        dos.writeLong(a.getTglMasuk());
        dos.writeUTF(a.getStatus());
        dos.writeLong(a.getTglKeluar());
        dos.writeInt(a.getBiayaTotal());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Admisi a = new Admisi();
        a.setIdAdmisi(dis.readUTF());
        a.setNoRM(dis.readUTF());
        a.setIdDokter(dis.readUTF());
        a.setIdRuangan(dis.readUTF());
        a.setDiagnosisAwal(dis.readUTF());
        a.setTglMasuk(dis.readLong());
        a.setStatus(dis.readUTF());
        a.setTglKeluar(dis.readLong());
        a.setBiayaTotal(dis.readInt());
        dis.close();
        return a;
    }
}
