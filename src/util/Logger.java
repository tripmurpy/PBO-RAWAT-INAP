package util;

/**
 * Logger — Logging ringan untuk J2ME.
 *
 * J2ME tidak punya java.util.logging. Class ini membungkus System.out/err
 * dengan level filter sederhana sehingga log dapat dimatikan di build rilis.
 *
 * Pemakaian:
 *   Logger.info("PasienService", "menyimpan pasien " + noRM);
 *   Logger.error("AdmisiDB", "gagal serialize", e);
 */
public class Logger {

    public static final int LEVEL_DEBUG = 0;
    public static final int LEVEL_INFO  = 1;
    public static final int LEVEL_WARN  = 2;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_OFF   = 99;

    /** Default INFO; ubah ke LEVEL_OFF untuk build rilis. */
    private static int currentLevel = LEVEL_INFO;

    public static void setLevel(int level) { currentLevel = level; }
    public static int  getLevel()           { return currentLevel; }

    public static void debug(String tag, String msg) {
        if (currentLevel <= LEVEL_DEBUG) tulis("DEBUG", tag, msg, null);
    }

    public static void info(String tag, String msg) {
        if (currentLevel <= LEVEL_INFO) tulis("INFO ", tag, msg, null);
    }

    public static void warn(String tag, String msg) {
        if (currentLevel <= LEVEL_WARN) tulis("WARN ", tag, msg, null);
    }

    public static void error(String tag, String msg) {
        if (currentLevel <= LEVEL_ERROR) tulis("ERROR", tag, msg, null);
    }

    public static void error(String tag, String msg, Throwable t) {
        if (currentLevel <= LEVEL_ERROR) tulis("ERROR", tag, msg, t);
    }

    private static void tulis(String level, String tag, String msg, Throwable t) {
        StringBuffer sb = new StringBuffer();
        sb.append('[').append(level).append("] ")
          .append(tag).append(": ").append(msg);
        System.out.println(sb.toString());
        if (t != null) {
            System.out.println("  cause: " + t.toString());
            t.printStackTrace();
        }
    }
}
