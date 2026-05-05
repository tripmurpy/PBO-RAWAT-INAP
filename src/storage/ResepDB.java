package storage;

import java.util.Vector;
import model.Resep;
import model.ItemObat;
import model.repository.IResepRepository;
import util.RMSUtil;

/**
 * ResepDB — Implementasi IResepRepository menggunakan RMS.
 * Menyimpan Resep sebagai komposit dengan ItemObat embedded.
 */
public class ResepDB extends BaseDB implements IResepRepository {

    protected String getStoreName() { return "resep_store"; }

    protected String getEntityId(Object e) { return ((Resep) e).getId(); }

    protected void setRecordId(Object e, int rid) { ((Resep) e).setRecordId(rid); }

    protected int getRecordIdFromObject(Object e) { return ((Resep) e).getRecordId(); }

    public void save(Resep r) throws Exception { saveEntity(r); }

    public Resep findById(String id) throws Exception {
        return (Resep) findByStringId(id);
    }

    public Vector findByRekamMedisId(String rekamMedisId) throws Exception {
        Vector hasil = new Vector();
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Resep r = (Resep) all.elementAt(i);
            if (rekamMedisId.equals(r.getRekamMedisId()))
                hasil.addElement(r);
        }
        return hasil;
    }

    public void update(Resep r) throws Exception {
        updateEntity(r, r.getRecordId());
    }

    public void delete(int recordId) throws Exception { deleteEntity(recordId); }

    protected byte[] serialize(Object obj) throws Exception {
        Resep r = (Resep) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(r.getId());
        dos.writeUTF(r.getRekamMedisId());
        dos.writeLong(r.getTanggal());

        Vector items = r.getListItem();
        int count = items == null ? 0 : items.size();
        dos.writeInt(count);
        for (int i = 0; i < count; i++) {
            ItemObat item = (ItemObat) items.elementAt(i);
            dos.writeUTF(item.getObatId());
            dos.writeUTF(item.getNamaObat());
            dos.writeInt(item.getQuantity());
            dos.writeUTF(item.getDosis() != null ? item.getDosis() : "");
        }

        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        Resep r = new Resep();
        r.setId(dis.readUTF());
        r.setRekamMedisId(dis.readUTF());
        r.setTanggal(dis.readLong());

        int count = dis.readInt();
        for (int i = 0; i < count; i++) {
            ItemObat item = new ItemObat();
            item.setObatId(dis.readUTF());
            item.setNamaObat(dis.readUTF());
            item.setQuantity(dis.readInt());
            item.setDosis(dis.readUTF());
            r.tambahItem(item);
        }

        dis.close();
        return r;
    }
}
