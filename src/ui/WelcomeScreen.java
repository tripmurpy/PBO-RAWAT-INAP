package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * WelcomeScreen — Halaman sambutan sebelum Login.
 *
 * INHERITANCE: Extends Canvas (J2ME LCDUI).
 * Tampil pertama kali saat aplikasi dibuka.
 * Pengguna dapat menekan tombol apapun atau mengetuk layar
 * untuk melanjutkan ke LoginScreen.
 */
public class WelcomeScreen extends Canvas {

    // =============================================
    // WARNA — Minimalist Bright Palette
    // =============================================
    private static final int BG           = 0xFFFFFF; // Putih bersih
    private static final int ACCENT       = 0x0052CC; // Biru laut (aksen utama)
    private static final int TEXT_MAIN    = 0x1A1A2E; // Gelap hampir hitam
    private static final int TEXT_SUB     = 0x5A6A8A; // Abu-abu biru
    private static final int TEXT_HINT    = 0xADB5BD; // Abu-abu sangat terang
    private static final int LINE_COLOR   = 0xE8ECF0; // Garis pemisah halus
    private static final int DOT_COLOR    = 0xDDE8F7; // Lingkaran dekoratif

    // =============================================
    // STATE FLASH untuk teks "Ketuk"
    // =============================================
    private boolean flashVisible = true;
    private long lastFlash = 0;
    private static final int FLASH_INTERVAL = 600; // ms

    // =============================================
    // Flag transisi: cegah double-trigger
    // =============================================
    private boolean sudahBerpindah = false;

    public WelcomeScreen() {
        setFullScreenMode(true);
        // Jadwalkan repaint berkala untuk efek berkedip
        new Thread(new Runnable() {
            public void run() {
                while (!sudahBerpindah) {
                    long now = System.currentTimeMillis();
                    if (now - lastFlash >= FLASH_INTERVAL) {
                        flashVisible = !flashVisible;
                        lastFlash = now;
                        repaint();
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                }
            }
        }).start();
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;

        // ── Latar belakang putih bersih ──────────────────────────────
        g.setColor(BG);
        g.fillRect(0, 0, w, h);

        // ── Dekorasi: Lingkaran besar di latar belakang ──────────────
        g.setColor(DOT_COLOR);
        // Lingkaran besar di pojok kiri atas
        g.fillArc(-60, -60, 180, 180, 0, 360);
        // Lingkaran kecil di pojok kanan bawah
        g.fillArc(w - 60, h - 60, 140, 140, 0, 360);

        // ── Font ─────────────────────────────────────────────────────
        Font fJudul  = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fSub    = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fKecil  = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fBold   = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

        // ── Posisi vertikal utama ─────────────────────────────────────
        int baseY = h / 5;

        // ── Label kecil di atas ───────────────────────────────────────
        g.setColor(ACCENT);
        g.setFont(fKecil);
        String labelAtas = "SISTEM ADMINISTRASI";
        g.drawString(labelAtas, cx, baseY,
                Graphics.TOP | Graphics.HCENTER);
        baseY += fKecil.getHeight() + 6;

        // ── Garis aksen pendek ────────────────────────────────────────
        int lineW = 36;
        g.setColor(ACCENT);
        g.fillRect(cx - lineW / 2, baseY, lineW, 3);
        baseY += 16;

        // ── Nama Rumah Sakit (judul utama) ───────────────────────────
        g.setColor(TEXT_MAIN);
        g.setFont(fJudul);
        g.drawString("RS Rasuna Said 5", cx, baseY,
                Graphics.TOP | Graphics.HCENTER);
        baseY += fJudul.getHeight() + 8;

        // ── Garis pemisah horizontal ─────────────────────────────────
        int margin = w / 5;
        g.setColor(LINE_COLOR);
        g.drawLine(margin, baseY, w - margin, baseY);
        baseY += 16;

        // ── Teks "Selamat Datang" ────────────────────────────────────
        g.setColor(TEXT_SUB);
        g.setFont(fBold);
        g.drawString("Selamat Datang", cx, baseY,
                Graphics.TOP | Graphics.HCENTER);
        baseY += fBold.getHeight() + 8;

        // ── Tagline ──────────────────────────────────────────────────
        g.setColor(TEXT_HINT);
        g.setFont(fKecil);
        g.drawString("Rawat Inap Terpadu", cx, baseY,
                Graphics.TOP | Graphics.HCENTER);

        // ── Teks berkedip di bagian bawah ────────────────────────────
        if (flashVisible) {
            int hintY = h - (h / 5);
            g.setColor(TEXT_SUB);
            g.setFont(fSub);
            g.drawString("Ketuk layar untuk mulai", cx, hintY,
                    Graphics.TOP | Graphics.HCENTER);

            // Titik-titik animasi
            g.setColor(ACCENT);
            int dotSpacing = 10;
            int dotY = hintY + fSub.getHeight() + 8;
            g.fillArc(cx - dotSpacing * 2 - 3, dotY, 6, 6, 0, 360);
            g.fillArc(cx - 3, dotY, 6, 6, 0, 360);
            g.fillArc(cx + dotSpacing * 2 - 3, dotY, 6, 6, 0, 360);
        }
    }

    // ── Pindah ke LoginScreen saat tombol apapun ditekan ─────────────
    protected void keyPressed(int keyCode) {
        navigasiKeLogin();
    }

    // ── Pindah ke LoginScreen saat layar disentuh ────────────────────
    protected void pointerPressed(int x, int y) {
        navigasiKeLogin();
    }

    private void navigasiKeLogin() {
        if (sudahBerpindah) return;
        sudahBerpindah = true;
        ScreenManager.getInstance().tampilkanLayar(new LoginScreen());
    }
}
