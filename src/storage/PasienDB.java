package storage;

import java.util.Vector;
import model.Pasien;
import model.repository.IPasienRepository;
import util.RMSUtil;

/**
 * PasienDB — Implementasi IPasienRepository menggunakan RMS.
 */
public class PasienDB extends BaseDB implements IPasienRepository {

    protected String getStoreName() { return "pasien_store"; }

    protected String getEntityId(Object e) { return ((Pasien) e).getNoRM(); }
    
    protected void setRecordId(Object e, int rid) { ((Pasien) e).setRecordId(rid); }

    public void save(Pasien p) throws Exception { saveEntity(p); }

    public Pasien findByRM(String noRM) throws Exception {
        return (Pasien) findByStringId(noRM);
    }

    public Vector getAll() throws Exception { return getAllEntities(); }

    public void update(Pasien p) throws Exception {
        updateEntity(p, p.getRecordId());
    }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    public Pasien findById(int recordId) throws Exception {
        return (Pasien) findByRecordId(recordId);
    }

    protected int getRecordIdFromObject(Object e) {
        return ((Pasien) e).getRecordId();
    }

    public Vector cariByNama(String keyword) throws Exception {
        Vector hasil = new Vector();
        Vector all = getAllEntities();
        String keyLower = keyword.toLowerCase();
        for (int i = 0; i < all.size(); i++) {
            Pasien p = (Pasien) all.elementAt(i);
            if (p.getNama().toLowerCase().indexOf(keyLower) >= 0)
                hasil.addElement(p);
        }
        return hasil;
    }

    protected byte[] serialize(Object obj) throws Exception {
        Pasien p = (Pasien) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(p.getNoRM()); dos.writeUTF(p.getNama());
        dos.writeLong(p.getTglLahir()); dos.writeUTF(p.getJenisKelamin());
        dos.writeUTF(p.getAlamat()); dos.writeUTF(p.getNoTelp());
        dos.writeUTF(p.getAsuransi());
        dos.writeUTF(p.getDokterPenanggungJawab() == null ? "" : p.getDokterPenanggungJawab());
        dos.writeUTF(p.getKamarRawat() == null ? "" : p.getKamarRawat());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Pasien p = new Pasien();
        p.setNoRM(dis.readUTF()); p.setNama(dis.readUTF());
        p.setTglLahir(dis.readLong()); p.setJenisKelamin(dis.readUTF());
        p.setAlamat(dis.readUTF()); p.setNoTelp(dis.readUTF());
        p.setAsuransi(dis.readUTF());
        if (dis.available() > 0) {
            p.setDokterPenanggungJawab(dis.readUTF());
            p.setKamarRawat(dis.readUTF());
        } else {
            p.setDokterPenanggungJawab("");
            p.setKamarRawat("");
        }
        dis.close();
        return p;
    }
}