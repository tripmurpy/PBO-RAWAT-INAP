package util;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * NoRMGenerator — Generator Nomor Rekam Medis.
 * 
 * Format: RM-YYYYMMDD-XXX
 * Contoh: RM-20250502-001
 * 
 * Counter tersimpan di RMS store tersendiri agar
 * nomor urut tetap konsisten antar sesi.
 * 
 * ENCAPSULATION: Logika pembuatan nomor RM terisolasi,
 * class lain hanya memanggil generate().
 */
public class NoRMGenerator {

    private static final String STORE_COUNTER = "rm_counter";

    /**
     * Menghasilkan Nomor RM baru dengan format RM-YYYYMMDD-XXX.
     * Counter otomatis bertambah setiap pemanggilan.
     * @return Nomor RM baru
     */
    public static String generate() {
        int counter = ambilDanNaikkanCounter();
        String tanggal = DateUtil.formatTanggalKompak(DateUtil.sekarang());
        StringBuffer sb = new StringBuffer();
        sb.append("RM-");
        sb.append(tanggal);
        sb.append("-");
        sb.append(padTigaDigit(counter));
        return sb.toString();
    }

    /**
     * Mengambil counter saat ini dan menaikkan nilainya.
     * Jika belum ada, mulai dari 1.
     */
    private static int ambilDanNaikkanCounter() {
        RecordStore rs = null;
        int counter = 1;

        try {
            rs = RMSUtil.bukaStore(STORE_COUNTER);

            if (rs.getNumRecords() > 0) {
                // Ambil counter terakhir
                byte[] data = rs.getRecord(1);
                java.io.DataInputStream dis = RMSUtil.buatInputStream(data);
                counter = dis.readInt() + 1;
                dis.close();

                // Update counter
                Object[] stream = RMSUtil.buatOutputStream();
                java.io.DataOutputStream dos = (java.io.DataOutputStream) stream[1];
                dos.writeInt(counter);
                byte[] newData = RMSUtil.ambilBytes(stream);
                RMSUtil.updateRecord(rs, 1, newData);
                dos.close();
            } else {
                // Buat counter baru, mulai dari 1
                Object[] stream = RMSUtil.buatOutputStream();
                java.io.DataOutputStream dos = (java.io.DataOutputStream) stream[1];
                dos.writeInt(counter);
                byte[] newData = RMSUtil.ambilBytes(stream);
                RMSUtil.simpanRecord(rs, newData);
                dos.close();
            }
        } catch (Exception e) {
            // Fallback: gunakan timestamp sebagai counter
            counter = (int) (System.currentTimeMillis() % 1000);
        } finally {
            RMSUtil.tutupStore(rs);
        }

        return counter;
    }

    /**
     * Padding angka menjadi 3 digit (001, 012, 123).
     */
    private static String padTigaDigit(int angka) {
        if (angka < 10) return new StringBuffer().append("00").append(angka).toString();
        if (angka < 100) return new StringBuffer().append("0").append(angka).toString();
        return String.valueOf(angka);
    }
}
