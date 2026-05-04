package ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * HospitalMIDlet — Entry point aplikasi J2ME.
 * 
 * INHERITANCE: Extends MIDlet (wajib untuk J2ME app).
 * Menginisialisasi ScreenManager dan menampilkan LoginScreen.
 */
public class HospitalMIDlet extends MIDlet {

    private boolean sudahDiinisialisasi = false;

    /**
     * Dipanggil saat aplikasi pertama kali dijalankan.
     * Inisialisasi ScreenManager dan tampilkan layar login.
     */
    protected void startApp() throws MIDletStateChangeException {
        if (!sudahDiinisialisasi) {
            // Inisialisasi Singleton ScreenManager
            ScreenManager sm = ScreenManager.getInstance();
            sm.init(this);

            // Tampilkan layar login
            sm.tampilkanLayar(new LoginScreen());

            sudahDiinisialisasi = true;
        }
    }

    /**
     * Dipanggil saat aplikasi di-pause (minimize).
     */
    protected void pauseApp() {
        // Tidak ada aksi khusus saat pause
    }

    /**
     * Dipanggil saat aplikasi ditutup.
     * Pastikan semua resource dibebaskan.
     */
    protected void destroyApp(boolean unconditional) 
            throws MIDletStateChangeException {
        // Cleanup jika diperlukan
        notifyDestroyed();
    }
}
