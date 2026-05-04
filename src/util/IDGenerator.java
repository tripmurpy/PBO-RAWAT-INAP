package util;

import javax.microedition.rms.RecordStore;

/**
 * IDGenerator — Generator ID otomatis untuk berbagai entitas.
 * 
 * Menyediakan auto-increment ID yang tersimpan di RMS
 * agar konsisten antar sesi aplikasi.
 * 
 * Format yang dihasilkan:
 * - Admisi  : ADM-YYYY-XXXX  (contoh: ADM-2025-0042)
 * - Dokter  : DKT-XXXX       (contoh: DKT-0001)
 * - Ruangan : RNG-XXXX       (contoh: RNG-0001)
 * 
 * ENCAPSULATION: Mekanisme counter internal tersembunyi,
 * class lain hanya memanggil generateXxx().
 */
public class IDGenerator {

    private static final String STORE_PREFIX = "id_gen_";

    /**
     * Generate ID Admisi: ADM-YYYY-XXXX
     */
    public static String generateAdmisiId() {
        int counter = ambilDanNaikkanCounter("admisi");
        String tahun = DateUtil.formatTahun(DateUtil.sekarang());
        return new StringBuffer().append("ADM-").append(tahun).append("-").append(padEmpatDigit(counter)).toString();
    }

    /**
     * Generate ID Dokter: DKT-XXXX
     */
    public static String generateDokterId() {
        int counter = ambilDanNaikkanCounter("dokter");
        return new StringBuffer().append("DKT-").append(padEmpatDigit(counter)).toString();
    }

    /**
     * Generate ID Ruangan: RNG-XXXX
     */
    public static String generateRuanganId() {
        int counter = ambilDanNaikkanCounter("ruangan");
        return new StringBuffer().append("RNG-").append(padEmpatDigit(counter)).toString();
    }

    /**
     * Generate ID Kunjungan: KNJ-XXXX
     */
    public static String generateKunjunganId() {
        int counter = ambilDanNaikkanCounter("kunjungan");
        return new StringBuffer().append("KNJ-").append(padEmpatDigit(counter)).toString();
    }

    /**
     * Generate ID User: USR-XXXX
     */
    public static String generateUserId() {
        int counter = ambilDanNaikkanCounter("user");
        return new StringBuffer().append("USR-").append(padEmpatDigit(counter)).toString();
    }

    // ========== INTERNAL ==========

    /**
     * Mengambil counter dari RMS dan menaikkan nilainya.
     */
    private static int ambilDanNaikkanCounter(String kategori) {
        RecordStore rs = null;
        int counter = 1;

        try {
            rs = RMSUtil.bukaStore(new StringBuffer().append(STORE_PREFIX).append(kategori).toString());

            if (rs.getNumRecords() > 0) {
                byte[] data = rs.getRecord(1);
                java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
                counter = dis.readInt() + 1;
                dis.close();

                Object[] stream = RMSUtil.buatOutputStream();
                java.io.DataOutputStream dos = (java.io.DataOutputStream) stream[1];
                dos.writeInt(counter);
                byte[] newData = RMSUtil.ambilBytes(stream);
                RMSUtil.updateRecord(rs, 1, newData);
                dos.close();
            } else {
                Object[] stream = RMSUtil.buatOutputStream();
                java.io.DataOutputStream dos = (java.io.DataOutputStream) stream[1];
                dos.writeInt(counter);
                byte[] newData = RMSUtil.ambilBytes(stream);
                RMSUtil.simpanRecord(rs, newData);
                dos.close();
            }
        } catch (Exception e) {
            counter = (int) (System.currentTimeMillis() % 10000);
        } finally {
            RMSUtil.tutupStore(rs);
        }

        return counter;
    }

    /**
     * Padding angka menjadi 4 digit.
     */
    private static String padEmpatDigit(int angka) {
        if (angka < 10) return new StringBuffer().append("000").append(angka).toString();
        if (angka < 100) return new StringBuffer().append("00").append(angka).toString();
        if (angka < 1000) return new StringBuffer().append("0").append(angka).toString();
        return String.valueOf(angka);
    }
}
