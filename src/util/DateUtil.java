package util;

import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil — Utilitas penanganan tanggal untuk CLDC 1.1.
 * 
 * CLDC 1.1 TIDAK memiliki SimpleDateFormat, sehingga semua
 * operasi format dan parsing tanggal harus dilakukan secara manual.
 * 
 * ENCAPSULATION: Detail parsing dan formatting tanggal
 * disembunyikan di dalam metode-metode statis.
 */
public class DateUtil {

    /**
     * Format tanggal ke string: DD/MM/YYYY
     * 
     * @param waktu waktu dalam millisecond
     * @return string format DD/MM/YYYY
     */
    public static String formatTanggal(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));

        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH) + 1;
        int tahun = cal.get(Calendar.YEAR);

        return new StringBuffer().append(padDuaDigit(hari)).append("/").append(padDuaDigit(bulan)).append("/").append(tahun).toString();
    }

    /**
     * Format tanggal ke string: YYYYMMDD (untuk ID generator).
     * 
     * @param waktu waktu dalam millisecond
     * @return string format YYYYMMDD
     */
    public static String formatTanggalKompak(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));

        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH) + 1;
        int tahun = cal.get(Calendar.YEAR);

        return new StringBuffer().append(tahun).append(padDuaDigit(bulan)).append(padDuaDigit(hari)).toString();
    }

    /**
     * Format tanggal ke string: YYYY (untuk ID admisi).
     * 
     * @param waktu waktu dalam millisecond
     * @return string format YYYY
     */
    public static String formatTahun(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * Parsing string DD/MM/YYYY menjadi long (millisecond).
     * 
     * @param teks string format DD/MM/YYYY
     * @return waktu dalam millisecond, atau -1 jika format salah
     */
    public static long parseTanggal(String teks) {
        if (teks == null || teks.length() != 10) {
            return -1;
        }

        try {
            int hari = Integer.parseInt(teks.substring(0, 2));
            int bulan = Integer.parseInt(teks.substring(3, 5));
            int tahun = Integer.parseInt(teks.substring(6, 10));

            // Validasi karakter pemisah
            if (teks.charAt(2) != '/' || teks.charAt(5) != '/') {
                return -1;
            }

            // Validasi range
            if (bulan < 1 || bulan > 12 || hari < 1 || hari > 31) {
                return -1;
            }

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, tahun);
            cal.set(Calendar.MONTH, bulan - 1);
            cal.set(Calendar.DAY_OF_MONTH, hari);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return cal.getTime().getTime();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Mendapatkan waktu saat ini dalam millisecond.
     */
    public static long sekarang() {
        return System.currentTimeMillis();
    }

    /**
     * Menghitung selisih hari antara dua waktu.
     * 
     * @param mulai   waktu mulai (ms)
     * @param selesai waktu selesai (ms)
     * @return jumlah hari
     */
    public static int hitungSelisihHari(long mulai, long selesai) {
        long selisih = selesai - mulai;
        return (int) (selisih / (24L * 60L * 60L * 1000L));
    }

    /**
     * Menambahkan angka 0 di depan jika angka < 10.
     */
    private static String padDuaDigit(int angka) {
        return (angka < 10) ? new StringBuffer().append("0").append(angka).toString() : String.valueOf(angka);
    }

    /**
     * Mendapatkan tanggal hari ini dalam format DD/MM/YYYY.
     */
    public static String tanggalHariIni() {
        return formatTanggal(sekarang());
    }
}
