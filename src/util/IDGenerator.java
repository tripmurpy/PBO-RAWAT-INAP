package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;

/**
 * IDGenerator — Generator ID otomatis untuk berbagai entitas.
 *
 * Counter persisten di RMS terpisah per kategori sehingga konsisten antar sesi.
 *
 * Format:
 *   Admisi    : ADM-YYYY-XXXX
 *   Dokter    : DKT-XXXX
 *   Ruangan   : RNG-XXXX
 *   Kunjungan : KNJ-XXXX
 *   Pasien    : RM-YYYYMMDD-XXX
 *   User      : USR-XXXX
 *
 * Catatan: J2ME single-threaded UI, namun method ini di-synchronized untuk
 * keamanan jika ada thread service di masa depan.
 */
public class IDGenerator {

    private static final String STORE_PREFIX = "id_gen_";
    private static final Object LOCK = new Object();

    public static String generateAdmisiId() {
        int counter = ambilDanNaikkanCounter("admisi");
        String tahun = DateUtil.formatTahun(DateUtil.sekarang());
        return new StringBuffer(14)
            .append("ADM-").append(tahun).append('-')
            .append(padEmpatDigit(counter)).toString();
    }

    public static String generateDokterId() {
        return prefixCounter("DKT-", "dokter", 4);
    }

    public static String generateRuanganId() {
        return prefixCounter("RNG-", "ruangan", 4);
    }

    public static String generateKunjunganId() {
        return prefixCounter("KNJ-", "kunjungan", 4);
    }

    public static String generateUserId() {
        return prefixCounter("USR-", "user", 4);
    }

    public static String generateNoRM() {
        int counter = ambilDanNaikkanCounter("norm");
        String tanggal = DateUtil.formatTanggalKompak(DateUtil.sekarang());
        return new StringBuffer(16)
            .append("RM-").append(tanggal).append('-')
            .append(padTigaDigit(counter)).toString();
    }

    // ========== INTERNAL ==========

    private static String prefixCounter(String prefix, String kategori, int digits) {
        int counter = ambilDanNaikkanCounter(kategori);
        StringBuffer sb = new StringBuffer(prefix.length() + digits);
        sb.append(prefix);
        if (digits == 4) sb.append(padEmpatDigit(counter));
        else sb.append(padTigaDigit(counter));
        return sb.toString();
    }

    /**
     * Atomik (per-store): baca counter, naikkan, tulis kembali.
     * Stream ditutup dengan benar via try/finally.
     */
    private static int ambilDanNaikkanCounter(String kategori) {
        synchronized (LOCK) {
            RecordStore rs = null;
            int counter = 1;
            try {
                rs = RMSUtil.bukaStore(STORE_PREFIX + kategori);
                if (rs.getNumRecords() > 0) {
                    counter = bacaCounter(rs) + 1;
                    tulisCounter(rs, counter, true);
                } else {
                    tulisCounter(rs, counter, false);
                }
            } catch (Exception e) {
                // Fallback aman: timestamp modulo. Tidak ideal, tapi mencegah
                // kegagalan total. Kemungkinan tabrakan kecil pada single device.
                counter = (int) (System.currentTimeMillis() % 9000) + 1000;
            } finally {
                RMSUtil.tutupStore(rs);
            }
            return counter;
        }
    }

    private static int bacaCounter(RecordStore rs) throws Exception {
        DataInputStream dis = null;
        try {
            byte[] data = rs.getRecord(1);
            dis = RMSUtil.buatInputStream(data);
            return dis.readInt();
        } finally {
            if (dis != null) try { dis.close(); } catch (Exception ignore) {}
        }
    }

    private static void tulisCounter(RecordStore rs, int counter, boolean update) throws Exception {
        DataOutputStream dos = null;
        try {
            Object[] stream = RMSUtil.buatOutputStream();
            dos = (DataOutputStream) stream[1];
            dos.writeInt(counter);
            byte[] data = RMSUtil.ambilBytes(stream);
            if (update) RMSUtil.updateRecord(rs, 1, data);
            else RMSUtil.simpanRecord(rs, data);
        } finally {
            if (dos != null) try { dos.close(); } catch (Exception ignore) {}
        }
    }

    private static String padEmpatDigit(int n) {
        if (n < 10)   return "000" + n;
        if (n < 100)  return "00" + n;
        if (n < 1000) return "0" + n;
        return String.valueOf(n);
    }

    private static String padTigaDigit(int n) {
        if (n < 10)  return "00" + n;
        if (n < 100) return "0" + n;
        return String.valueOf(n);
    }
}
