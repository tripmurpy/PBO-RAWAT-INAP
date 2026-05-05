package storage;

import java.util.Vector;
import model.Obat;
import model.repository.IObatRepository;
import util.RMSUtil;

/**
 * ObatDB — Implementasi IObatRepository menggunakan RMS.
 */
public class ObatDB extends BaseDB implements IObatRepository {

    protected String getStoreName() { return "obat_store"; }

    protected String getEntityId(Object e) { return ((Obat) e).getId(); }

    protected void setRecordId(Object e, int rid) { ((Obat) e).setRecordId(rid); }

    protected int getRecordIdFromObject(Object e) { return ((Obat) e).getRecordId(); }

    public void save(Obat o) throws Exception { saveEntity(o); }

    public Obat findById(String id) throws Exception {
        return (Obat) findByStringId(id);
    }

    public Vector getAll() throws Exception { return getAllEntities(); }

    public void update(Obat o) throws Exception {
        updateEntity(o, o.getRecordId());
    }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    public Vector cariByNama(String keyword) throws Exception {
        Vector hasil = new Vector();
        Vector all = getAllEntities();
        String keyLower = keyword.toLowerCase();
        for (int i = 0; i < all.size(); i++) {
            Obat o = (Obat) all.elementAt(i);
            if (o.getNama().toLowerCase().indexOf(keyLower) >= 0)
                hasil.addElement(o);
        }
        return hasil;
    }

    public Vector getLowStock() throws Exception {
        Vector hasil = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Obat o = (Obat) all.elementAt(i);
            if (o.isLowStock()) hasil.addElement(o);
        }
        return hasil;
    }

    protected byte[] serialize(Object obj) throws Exception {
        Obat o = (Obat) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(o.getId());
        dos.writeUTF(o.getNama());
        dos.writeUTF(o.getBentuk());
        dos.writeUTF(o.getSatuan());
        dos.writeInt(o.getStok());
        dos.writeInt(o.getStokMinimum());
        dos.writeInt(o.getHargaPerUnit());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Obat o = new Obat();
        o.setId(dis.readUTF());
        o.setNama(dis.readUTF());
        o.setBentuk(dis.readUTF());
        o.setSatuan(dis.readUTF());
        o.setStok(dis.readInt());
        o.setStokMinimum(dis.readInt());
        o.setHargaPerUnit(dis.readInt());
        dis.close();
        return o;
    }
}
