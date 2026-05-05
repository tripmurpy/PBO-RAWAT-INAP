package storage;

import java.util.Vector;
import model.RekamMedis;
import model.CatatanHarian;
import model.Resep;
import model.ItemObat;
import model.repository.IRekamMedisRepository;
import util.RMSUtil;

/**
 * RekamMedisDB — Implementasi IRekamMedisRepository menggunakan RMS.
 * Menyimpan RekamMedis sebagai komposit dengan CatatanHarian.
 * Resep disimpan di ResepDB terpisah.
 */
public class RekamMedisDB extends BaseDB implements IRekamMedisRepository {

    protected String getStoreName() { return "rekammedis_store"; }

    protected String getEntityId(Object e) { return ((RekamMedis) e).getId(); }

    protected void setRecordId(Object e, int rid) { ((RekamMedis) e).setRecordId(rid); }

    protected int getRecordIdFromObject(Object e) { return ((RekamMedis) e).getRecordId(); }

    public void save(RekamMedis rm) throws Exception { saveEntity(rm); }

    public RekamMedis findById(String id) throws Exception {
        return (RekamMedis) findByStringId(id);
    }

    public RekamMedis findByAdmisiId(String admisiId) throws Exception {
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            RekamMedis rm = (RekamMedis) all.elementAt(i);
            if (admisiId.equals(rm.getAdmisiId())) return rm;
        }
        return null;
    }

    public void update(RekamMedis rm) throws Exception {
        updateEntity(rm, rm.getRecordId());
    }

    public Vector getAll() throws Exception { return getAllEntities(); }

    protected byte[] serialize(Object obj) throws Exception {
        RekamMedis rm = (RekamMedis) obj;
        Object[] s = RMSUtil.buatOutputStream();
        java.io.DataOutputStream dos = (java.io.DataOutputStream) s[1];
        dos.writeUTF(rm.getId());
        dos.writeUTF(rm.getAdmisiId());
        dos.writeUTF(rm.getStatus());
        dos.writeLong(rm.getTanggalBuka());
        dos.writeLong(rm.getTanggalTutup());

        // Serialize CatatanHarian list
        Vector catatan = rm.getListCatatan();
        int catatanCount = catatan == null ? 0 : catatan.size();
        dos.writeInt(catatanCount);
        for (int i = 0; i < catatanCount; i++) {
            CatatanHarian c = (CatatanHarian) catatan.elementAt(i);
            dos.writeUTF(c.getId() != null ? c.getId() : "");
            dos.writeLong(c.getTanggal());
            dos.writeUTF(c.getVitalSigns() != null ? c.getVitalSigns() : "");
            dos.writeUTF(c.getKeluhan() != null ? c.getKeluhan() : "");
            dos.writeUTF(c.getDiagnosa() != null ? c.getDiagnosa() : "");
            dos.writeUTF(c.getTindakan() != null ? c.getTindakan() : "");
        }

        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    protected Object deserialize(byte[] data) throws Exception {
        java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
        RekamMedis rm = new RekamMedis();
        rm.setId(dis.readUTF());
        rm.setAdmisiId(dis.readUTF());
        rm.setStatus(dis.readUTF());
        rm.setTanggalBuka(dis.readLong());
        rm.setTanggalTutup(dis.readLong());

        int catatanCount = dis.readInt();
        for (int i = 0; i < catatanCount; i++) {
            CatatanHarian c = new CatatanHarian();
            c.setId(dis.readUTF());
            c.setTanggal(dis.readLong());
            c.setVitalSigns(dis.readUTF());
            c.setKeluhan(dis.readUTF());
            c.setDiagnosa(dis.readUTF());
            c.setTindakan(dis.readUTF());
            rm.tambahCatatan(c);
        }

        dis.close();
        return rm;
    }
}
