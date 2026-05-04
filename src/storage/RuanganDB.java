package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import model.Ruangan;
import model.repository.IRuanganRepository;
import util.RMSUtil;

/**
 * RuanganDB — Implementasi IRuanganRepository menggunakan RMS.
 * POLYMORPHISM: implements IRuanganRepository.
 */
public class RuanganDB implements IRuanganRepository {

    private static final String STORE_NAME = "ruangan_store";

    public void save(Ruangan ruangan) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(ruangan);
            int recordId = RMSUtil.simpanRecord(rs, data);
            ruangan.setRecordId(recordId);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public Ruangan findById(String id) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Ruangan r = deserialize(data);
                r.setRecordId(rid);
                if (id.equals(r.getId())) return r;
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return null;
    }

    public Vector findAll() throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Ruangan r = deserialize(data);
                r.setRecordId(rid);
                hasil.addElement(r);
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    public Vector findAvailable(String tipeKamar) throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Ruangan r = deserialize(data);
                r.setRecordId(rid);
                if (r.isKosong()) {
                    if (tipeKamar == null || tipeKamar.equals(r.getTipeKamar())) {
                        hasil.addElement(r);
                    }
                }
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    public void updateStatus(String id, String status, String namaPasien) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Ruangan r = deserialize(data);
                if (id.equals(r.getId())) {
                    r.setStatusKamar(status);
                    r.setNamaPasien(namaPasien != null ? namaPasien : "");
                    r.setRecordId(rid);
                    byte[] newData = serialize(r);
                    RMSUtil.updateRecord(rs, rid, newData);
                    return;
                }
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public void update(Ruangan ruangan) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(ruangan);
            RMSUtil.updateRecord(rs, ruangan.getRecordId(), data);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public void delete(int recordId) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RMSUtil.hapusRecord(rs, recordId);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    private byte[] serialize(Ruangan r) throws Exception {
        Object[] stream = RMSUtil.buatOutputStream();
        DataOutputStream dos = (DataOutputStream) stream[1];
        dos.writeUTF(r.getId());
        dos.writeUTF(r.getNamaRuangan());
        dos.writeUTF(r.getTipeKamar());
        dos.writeInt(r.getKapasitas());
        dos.writeUTF(r.getStatusKamar());
        dos.writeUTF(r.getNamaPasien() != null ? r.getNamaPasien() : "");
        byte[] result = RMSUtil.ambilBytes(stream);
        dos.close();
        return result;
    }

    private Ruangan deserialize(byte[] data) throws Exception {
        DataInputStream dis = RMSUtil.buatInputStream(data);
        Ruangan r = new Ruangan();
        r.setId(dis.readUTF());
        r.setNamaRuangan(dis.readUTF());
        r.setTipeKamar(dis.readUTF());
        r.setKapasitas(dis.readInt());
        r.setStatusKamar(dis.readUTF());
        r.setNamaPasien(dis.readUTF());
        dis.close();
        return r;
    }
}
