package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import model.Pasien;
import model.repository.IPasienRepository;
import util.RMSUtil;

/**
 * PasienDB — Implementasi konkret IPasienRepository menggunakan RMS.
 * 
 * POLYMORPHISM: Mengimplementasikan interface IPasienRepository.
 * Service layer cukup mengenal interface, tidak perlu tahu detail RMS.
 * 
 * ENCAPSULATION: Detail serialisasi/deserialisasi tersembunyi
 * di metode private serialize() dan deserialize().
 */
public class PasienDB implements IPasienRepository {

    private static final String STORE_NAME = "pasien_store";

    // ========== IMPLEMENTASI INTERFACE (POLYMORPHISM) ==========

    public void save(Pasien pasien) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(pasien);
            int recordId = RMSUtil.simpanRecord(rs, data);
            pasien.setRecordId(recordId);
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public Pasien findByRM(String noRM) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                byte[] data = rs.getRecord(id);
                Pasien p = deserialize(data);
                p.setRecordId(id);
                if (noRM.equals(p.getNoRM())) {
                    return p;
                }
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return null;
    }

    public Pasien findById(int recordId) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = RMSUtil.ambilRecord(rs, recordId);
            Pasien p = deserialize(data);
            p.setRecordId(recordId);
            return p;
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    public Vector getAll() throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                byte[] data = rs.getRecord(id);
                Pasien p = deserialize(data);
                p.setRecordId(id);
                hasil.addElement(p);
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    public void update(Pasien pasien) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(pasien);
            RMSUtil.updateRecord(rs, pasien.getRecordId(), data);
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

    public Vector cariByNama(String keyword) throws Exception {
        Vector hasil = new Vector();
        String keyLower = keyword.toLowerCase();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                byte[] data = rs.getRecord(id);
                Pasien p = deserialize(data);
                p.setRecordId(id);
                if (p.getNama().toLowerCase().indexOf(keyLower) >= 0) {
                    hasil.addElement(p);
                }
            }
        } finally {
            RMSUtil.tutupStore(rs);
        }
        return hasil;
    }

    // ========== SERIALISASI PRIVATE (ENCAPSULATION) ==========

    private byte[] serialize(Pasien p) throws Exception {
        Object[] stream = RMSUtil.buatOutputStream();
        DataOutputStream dos = (DataOutputStream) stream[1];
        dos.writeUTF(p.getNoRM());
        dos.writeUTF(p.getNama());
        dos.writeLong(p.getTglLahir());
        dos.writeUTF(p.getJenisKelamin());
        dos.writeUTF(p.getAlamat());
        dos.writeUTF(p.getNoTelp());
        dos.writeUTF(p.getAsuransi());
        byte[] result = RMSUtil.ambilBytes(stream);
        dos.close();
        return result;
    }

    private Pasien deserialize(byte[] data) throws Exception {
        DataInputStream dis = RMSUtil.buatInputStream(data);
        Pasien p = new Pasien();
        p.setNoRM(dis.readUTF());
        p.setNama(dis.readUTF());
        p.setTglLahir(dis.readLong());
        p.setJenisKelamin(dis.readUTF());
        p.setAlamat(dis.readUTF());
        p.setNoTelp(dis.readUTF());
        p.setAsuransi(dis.readUTF());
        dis.close();
        return p;
    }
}
