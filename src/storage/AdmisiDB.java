package storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import model.Admisi;
import model.repository.IAdmisiRepository;
import util.RMSUtil;

/**
 * AdmisiDB — Implementasi IAdmisiRepository menggunakan RMS.
 * POLYMORPHISM: implements IAdmisiRepository.
 */
public class AdmisiDB implements IAdmisiRepository {

    private static final String STORE_NAME = "admisi_store";

    public void save(Admisi admisi) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            byte[] data = serialize(admisi);
            int rid = RMSUtil.simpanRecord(rs, data);
            admisi.setRecordId(rid);
        } finally { RMSUtil.tutupStore(rs); }
    }

    public Admisi findById(String idAdmisi) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                Admisi a = deserialize(rs.getRecord(rid));
                a.setRecordId(rid);
                if (idAdmisi.equals(a.getIdAdmisi())) return a;
            }
        } finally { RMSUtil.tutupStore(rs); }
        return null;
    }

    public Vector findByPasien(String noRM) throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                Admisi a = deserialize(rs.getRecord(rid));
                a.setRecordId(rid);
                if (noRM.equals(a.getNoRMPasien())) hasil.addElement(a);
            }
        } finally { RMSUtil.tutupStore(rs); }
        return hasil;
    }

    public Vector findAktif() throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                Admisi a = deserialize(rs.getRecord(rid));
                a.setRecordId(rid);
                if (a.isAktif()) hasil.addElement(a);
            }
        } finally { RMSUtil.tutupStore(rs); }
        return hasil;
    }

    public Vector getAll() throws Exception {
        Vector hasil = new Vector();
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RecordEnumeration re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                Admisi a = deserialize(rs.getRecord(rid));
                a.setRecordId(rid);
                hasil.addElement(a);
            }
        } finally { RMSUtil.tutupStore(rs); }
        return hasil;
    }

    public void update(Admisi admisi) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(STORE_NAME);
            RMSUtil.updateRecord(rs, admisi.getRecordId(), serialize(admisi));
        } finally { RMSUtil.tutupStore(rs); }
    }

    private byte[] serialize(Admisi a) throws Exception {
        Object[] s = RMSUtil.buatOutputStream();
        DataOutputStream dos = (DataOutputStream) s[1];
        dos.writeUTF(a.getIdAdmisi());
        dos.writeUTF(a.getNoRMPasien());
        dos.writeUTF(a.getIdDokter());
        dos.writeUTF(a.getIdRuangan());
        dos.writeLong(a.getTglMasuk());
        dos.writeLong(a.getTglKeluar());
        dos.writeUTF(a.getDiagnosisAwal());
        dos.writeUTF(a.getDiagnosisAkhir() != null ? a.getDiagnosisAkhir() : "");
        dos.writeUTF(a.getKodeICD10() != null ? a.getKodeICD10() : "");
        dos.writeUTF(a.getCatatan() != null ? a.getCatatan() : "");
        dos.writeUTF(a.getStatus());
        byte[] result = RMSUtil.ambilBytes(s);
        dos.close();
        return result;
    }

    private Admisi deserialize(byte[] data) throws Exception {
        DataInputStream dis = RMSUtil.buatInputStream(data);
        Admisi a = new Admisi();
        a.setIdAdmisi(dis.readUTF());
        a.setNoRMPasien(dis.readUTF());
        a.setIdDokter(dis.readUTF());
        a.setIdRuangan(dis.readUTF());
        a.setTglMasuk(dis.readLong());
        a.setTglKeluar(dis.readLong());
        a.setDiagnosisAwal(dis.readUTF());
        a.setDiagnosisAkhir(dis.readUTF());
        a.setKodeICD10(dis.readUTF());
        a.setCatatan(dis.readUTF());
        a.setStatus(dis.readUTF());
        dis.close();
        return a;
    }
}
