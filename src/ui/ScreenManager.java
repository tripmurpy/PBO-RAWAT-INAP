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
    private Displayable layarSebelumnya;

    private ScreenManager() {
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
        this.layarSebelumnya = display.getCurrent();
        display.setCurrent(layar);
    }

    /** Kembali ke layar sebelumnya */
    public void kembali() {
        if (layarSebelumnya != null) {
            display.setCurrent(layarSebelumnya);
            layarSebelumnya = null; // Clear after use in this simple manager
        }
    }

    /** Logout: reset state and show LoginScreen */
    public void logout() {
        this.layarSebelumnya = null;
        display.setCurrent(new LoginScreen());
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
