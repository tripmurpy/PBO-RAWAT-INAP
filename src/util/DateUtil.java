package util;

import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil — Utilitas penanganan tanggal untuk CLDC 1.1.
 *
 * CATATAN PENTING:
 * CLDC 1.1's java.util.Calendar HANYA mendukung getInstance(), getTime(),
 * setTime(Date), get(int), setTimeZone(), dan getTimeZone().
 * Method set(int,int) TIDAK tersedia. Karena itu pembuatan tanggal dari
 * (year, month, day) dilakukan manual via aritmatika hari-sejak-epoch.
 */
public class DateUtil {

    private static final long MS_PER_DAY = 24L * 60L * 60L * 1000L;

    private static final int[] HARI_DALAM_BULAN = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    /** Format tanggal: DD/MM/YYYY */
    public static String formatTanggal(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));
        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH) + 1;
        int tahun = cal.get(Calendar.YEAR);

        StringBuffer sb = new StringBuffer(10);
        sb.append(padDuaDigit(hari)).append('/')
          .append(padDuaDigit(bulan)).append('/')
          .append(tahun);
        return sb.toString();
    }

    /** Format tanggal kompak: YYYYMMDD */
    public static String formatTanggalKompak(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));
        int hari = cal.get(Calendar.DAY_OF_MONTH);
        int bulan = cal.get(Calendar.MONTH) + 1;
        int tahun = cal.get(Calendar.YEAR);

        StringBuffer sb = new StringBuffer(8);
        sb.append(tahun).append(padDuaDigit(bulan)).append(padDuaDigit(hari));
        return sb.toString();
    }

    /** Format tahun saja: YYYY */
    public static String formatTahun(long waktu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(waktu));
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * Parsing string DD/MM/YYYY menjadi millisecond UTC.
     *
     * Implementasi manual karena Calendar.set() TIDAK ada di CLDC 1.1.
     * @return millisecond, atau -1 jika format/nilai tidak valid
     */
    public static long parseTanggal(String teks) {
        if (teks == null || teks.length() != 10) return -1;
        if (teks.charAt(2) != '/' || teks.charAt(5) != '/') return -1;

        try {
            int hari = Integer.parseInt(teks.substring(0, 2));
            int bulan = Integer.parseInt(teks.substring(3, 5));
            int tahun = Integer.parseInt(teks.substring(6, 10));

            if (tahun < 1900 || tahun > 2100) return -1;
            if (bulan < 1 || bulan > 12) return -1;
            if (hari < 1 || hari > hariDalamBulan(bulan, tahun)) return -1;

            return komputasiMillis(tahun, bulan, hari);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Waktu sekarang dalam millisecond. */
    public static long sekarang() {
        return System.currentTimeMillis();
    }

    /** Selisih hari antara dua waktu (selesai - mulai). */
    public static int hitungSelisihHari(long mulai, long selesai) {
        long selisih = selesai - mulai;
        return (int) (selisih / MS_PER_DAY);
    }

    /** Tanggal hari ini sebagai DD/MM/YYYY. */
    public static String tanggalHariIni() {
        return formatTanggal(sekarang());
    }

    // ========== INTERNAL ==========

    /**
     * Hitung milidetik UTC dari tahun/bulan/hari (1-based).
     * Independen dari Calendar.set() yang tidak ada di CLDC 1.1.
     */
    private static long komputasiMillis(int tahun, int bulan, int hari) {
        long days = 0;
        for (int y = 1970; y < tahun; y++) {
            days += isTahunKabisat(y) ? 366 : 365;
        }
        for (int m = 1; m < bulan; m++) {
            days += HARI_DALAM_BULAN[m - 1];
            if (m == 2 && isTahunKabisat(tahun)) days += 1;
        }
        days += hari - 1;
        return days * MS_PER_DAY;
    }

    private static boolean isTahunKabisat(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }

    private static int hariDalamBulan(int bulan, int tahun) {
        int d = HARI_DALAM_BULAN[bulan - 1];
        if (bulan == 2 && isTahunKabisat(tahun)) d = 29;
        return d;
    }

    private static String padDuaDigit(int angka) {
        return (angka < 10)
            ? new StringBuffer(2).append('0').append(angka).toString()
            : String.valueOf(angka);
    }
}
