package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * ScreenManager — Singleton untuk navigasi antar layar.
 * 
 * ENCAPSULATION: Instance tunggal, akses via getInstance().
 * Mengelola display dan stack navigasi sederhana.
 */
public class ScreenManager {

    private static ScreenManager instance;
    private Display display;
    private MIDlet midlet;
    private java.util.Vector historyStack;

    private ScreenManager() {
        historyStack = new java.util.Vector();
    }

    /** Mendapatkan instance Singleton */
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /** Inisialisasi dengan MIDlet dan Display */
    public void init(MIDlet midlet) {
        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);
    }

    /** Menampilkan layar baru */
    public void tampilkanLayar(Displayable layar) {
        Displayable current = display.getCurrent();
        if (current != null) {
            historyStack.addElement(current);
        }
        display.setCurrent(layar);
    }

    /** Kembali ke layar sebelumnya */
    public void kembali() {
        if (!historyStack.isEmpty()) {
            int last = historyStack.size() - 1;
            Displayable prev = (Displayable) historyStack.elementAt(last);
            historyStack.removeElementAt(last);
            display.setCurrent(prev);
        }
    }

    /** Logout: reset state and show LoginScreen */
    public void logout() {
        historyStack.removeAllElements();
        display.setCurrent(new LoginScreen());
    }

    public boolean bisaKembali() {
        return !historyStack.isEmpty();
    }

    /** Mendapatkan Display */
    public Display getDisplay() {
        return display;
    }

    /** Mendapatkan MIDlet */
    public MIDlet getMIDlet() {
        return midlet;
    }

    /** Mendapatkan layar yang sedang aktif */
    public Displayable getLayarAktif() {
        return display.getCurrent();
    }
}
