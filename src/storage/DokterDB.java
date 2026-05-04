package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import model.Dokter;
import model.repository.IDokterRepository;
import util.RMSUtil;

/**
 * DokterDB — Implementasi IDokterRepository menggunakan RMS.
 * POLYMORPHISM: implements IDokterRepository.
 */
public class DokterDB implements IDokterRepository {

    private static final String STORE_NAME = "dokter_store";

    public void save(Dokter dokter) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(dokter);
            int recordId = RMSUtil.simpanRecord(rs, data);
            dokter.setRecordId(recordId);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public Dokter findById(String id) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Dokter d = deserialize(data);
                d.setRecordId(rid);
                if (id.equals(d.getId())) return d;
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
                Dokter d = deserialize(data);
                d.setRecordId(rid);
                hasil.addElement(d);
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    public Vector findBySpesialis(String spesialisasi) throws Exception {
        Vector hasil = new Vector();
        String keyLower = spesialisasi.toLowerCase();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Dokter d = deserialize(data);
                d.setRecordId(rid);
                if (d.getSpesialisasi().toLowerCase().indexOf(keyLower) >= 0) {
                    hasil.addElement(d);
                }
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    public void update(Dokter dokter) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(dokter);
            RMSUtil.updateRecord(rs, dokter.getRecordId(), data);
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

    private byte[] serialize(Dokter d) throws Exception {
        Object[] stream = RMSUtil.buatOutputStream();
        DataOutputStream dos = (DataOutputStream) stream[1];
        dos.writeUTF(d.getId());
        dos.writeUTF(d.getNama());
        dos.writeUTF(d.getSpesialisasi());
        dos.writeUTF(d.getJadwal());
        dos.writeBoolean(d.isAktif());
        byte[] result = RMSUtil.ambilBytes(stream);
        dos.close();
        return result;
    }

    private Dokter deserialize(byte[] data) throws Exception {
        DataInputStream dis = RMSUtil.buatInputStream(data);
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
