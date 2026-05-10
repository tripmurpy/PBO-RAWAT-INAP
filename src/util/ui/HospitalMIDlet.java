package util.ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import util.ServiceFactory;
import util.SeedData;

/**
 * HospitalMIDlet — Entry point aplikasi J2ME Rawat Inap.
 *
 * INHERITANCE: Extends MIDlet (wajib untuk J2ME app).
 * Inisialisasi ServiceFactory, SeedData, ScreenManager, lalu LoginScreen.
 */
public class HospitalMIDlet extends MIDlet {

    private boolean sudahDiinisialisasi = false;

    protected void startApp() throws MIDletStateChangeException {
        if (!sudahDiinisialisasi) {
            // Init ServiceFactory (singleton — juga init default admin)
            ServiceFactory.getInstance();

            // Seed data default jika first run
            SeedData.run();

            // Init ScreenManager dan tampilkan login
            ScreenManager sm = ScreenManager.getInstance();
            sm.init(this);
            sm.tampilkanLayar(new WelcomeScreen());

            sudahDiinisialisasi = true;
        }
    }

    protected void pauseApp() {
        // Tidak ada aksi khusus saat pause
    }

    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        notifyDestroyed();
    }
}
