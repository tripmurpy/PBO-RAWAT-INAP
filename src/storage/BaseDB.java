package storage;

import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import util.RMSUtil;

/**
 * BaseDB — Kelas dasar abstrak untuk repositori berbasis RMS.
 *
 * DESIGN PATTERN: Template Method.
 *
 * Caching:
 *  - cache invalidasi otomatis pada save/update/delete
 *  - getAllEntities() mengembalikan SALINAN Vector untuk mencegah caller
 *    memodifikasi cache internal
 *
 * Resource:
 *  - RecordEnumeration & RecordStore selalu ditutup di blok finally
 */
public abstract class BaseDB {

    protected Vector cache = null;
    protected boolean cacheDirty = true;

    // Subclass HARUS implement
    protected abstract String getStoreName();
    protected abstract String getEntityId(Object e);
    protected abstract void setRecordId(Object e, int rid);
    protected abstract int getRecordIdFromObject(Object e);
    protected abstract byte[] serialize(Object obj) throws Exception;
    protected abstract Object deserialize(byte[] data) throws Exception;

    /** Simpan entitas baru → set recordId hasil RMS ke entitas. */
    protected void saveEntity(Object e) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            byte[] data = serialize(e);
            int rid = RMSUtil.simpanRecord(rs, data);
            setRecordId(e, rid);
            invalidateCache();
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /** Update entitas berdasarkan recordId. */
    protected void updateEntity(Object e, int rid) throws Exception {
        if (rid <= 0) {
            throw new Exception(new StringBuffer().append("RecordId tidak valid: ").append(rid).toString());
        }
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            byte[] data = serialize(e);
            RMSUtil.updateRecord(rs, rid, data);
            invalidateCache();
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /** Hapus entitas by recordId. */
    protected void deleteEntity(int rid) throws Exception {
        RecordStore rs = null;
        try {
            rs = RMSUtil.bukaStore(getStoreName());
            RMSUtil.hapusRecord(rs, rid);
            invalidateCache();
        } finally {
            RMSUtil.tutupStore(rs);
        }
    }

    /**
     * Ambil semua entitas. Mengembalikan SALINAN Vector — caller bebas
     * memodifikasi tanpa merusak cache internal.
     */
    protected Vector getAllEntities() throws Exception {
        if (cacheDirty || cache == null) {
            muatUlangCache();
        }
        // Defensive copy: cegah modifikasi external pada cache
        Vector copy = new Vector(cache.size());
        for (int i = 0; i < cache.size(); i++) {
            copy.addElement(cache.elementAt(i));
        }
        return copy;
    }

    /** Cari entitas berdasarkan ID string (PK logic). */
    protected Object findByStringId(String id) throws Exception {
        if (id == null) return null;
        if (cacheDirty || cache == null) muatUlangCache();
        for (int i = 0; i < cache.size(); i++) {
            Object e = cache.elementAt(i);
            if (id.equals(getEntityId(e))) return e;
        }
        return null;
    }

    /** Cari entitas berdasarkan recordId RMS. */
    protected Object findByRecordId(int rid) throws Exception {
        if (cacheDirty || cache == null) muatUlangCache();
        for (int i = 0; i < cache.size(); i++) {
            Object e = cache.elementAt(i);
            if (rid == getRecordIdFromObject(e)) return e;
        }
        return null;
    }

    /** Reset cache — dipanggil otomatis setelah mutasi. */
    protected void invalidateCache() {
        cacheDirty = true;
        cache = null; // bebaskan memori segera (penting di J2ME)
    }

    /** Muat ulang cache dari RMS (full scan). */
    private void muatUlangCache() throws Exception {
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
            if (re != null) {
                try { re.destroy(); } catch (Exception ignore) {}
            }
            RMSUtil.tutupStore(rs);
        }
    }
}
