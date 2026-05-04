package storage;

import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import util.RMSUtil;

/**
 * BaseDB — Kelas dasar abstrak untuk semua repositori berbasis RMS.
 * Mengimplementasikan Template Method Pattern untuk mengurangi duplikasi kode CRUD.
 * 
 * DESIGN PATTERN: Template Method.
 */
public abstract class BaseDB {

    // Cache sederhana untuk meningkatkan performa (Full-Scan avoidance)
    protected Vector cache = null;
    protected boolean cacheDirty = true;

    // Metod abstrak yang harus diimplementasikan oleh subclass
    protected abstract String getStoreName();
    protected abstract String getEntityId(Object e);
    protected abstract void setRecordId(Object e, int rid);
    protected abstract byte[] serialize(Object obj) throws Exception;
    protected abstract Object deserialize(byte[] data) throws Exception;

    /**
     * Menyimpan entitas baru ke RMS.
     */
    protected void saveEntity(Object e) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            byte[] data = serialize(e);
            int rid = RMSUtil.simpanRecord(rs, data);
            setRecordId(e, rid);
            cacheDirty = true;
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /**
     * Memperbarui entitas yang sudah ada.
     */
    protected void updateEntity(Object e, int rid) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            byte[] data = serialize(e);
            RMSUtil.updateRecord(rs, rid, data);
            cacheDirty = true;
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /**
     * Mengambil semua entitas dari RMS dengan dukungan caching.
     */
    protected Vector getAllEntities() throws Exception {
        if (!cacheDirty && cache != null) {
            return cache;
        }

        Vector list = new Vector();
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            re = RMSUtil.ambilSemuaRecord(rs);
            while (re.hasNextElement()) {
                int rid = re.nextRecordId();
                byte[] data = rs.getRecord(rid);
                Object obj = deserialize(data);
                setRecordId(obj, rid);
                list.addElement(obj);
            }
            cache = list;
            cacheDirty = false;
        } finally {
            if (re != null) re.destroy();
            RMSUtil.tutupStore(rs);
        }
        return list;
    }

    /**
     * Mencari entitas berdasarkan ID string (Primary Key logic).
     */
    protected Object findByStringId(String id) throws Exception {
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Object e = all.elementAt(i);
            if (id.equals(getEntityId(e))) {
                return e;
            }
        }
        return null;
    }

    /**
     * Menghapus entitas berdasarkan recordId.
     */
    protected void deleteEntity(int rid) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            RMSUtil.hapusRecord(rs, rid);
            cacheDirty = true;
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /**
     * Mencari entitas berdasarkan recordId (RMS ID).
     */
    protected Object findByRecordId(int rid) throws Exception {
        Vector all = getAllEntities();
        for (int i = 0; i < all.size(); i++) {
            Object e = all.elementAt(i);
            // Kita perlu cara untuk mendapatkan recordId dari objek
            // Karena recordId tidak diwajibkan ada di level BaseDB (hanya rid RMS)
            // Namun setiap entity di sistem ini memiliki setRecordId/getRecordId
            // Untuk amannya, kita scan cache yang sudah di-setRecordId-kan di getAllEntities
            if (rid == getRecordIdFromObject(e)) {
                return e;
            }
        }
        return null;
    }

    // Helper untuk subclass mendapatkan recordId (karena setRecordId ada, getRecordId mungkin beda-beda nama)
    // Tapi di model kita rata-rata getRecordId(). Jika tidak ada, subclass bisa override ini.
    protected abstract int getRecordIdFromObject(Object e);
}
