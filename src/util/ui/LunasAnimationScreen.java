package util.ui;

import javax.microedition.lcdui.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * LunasAnimationScreen — Animasi fullscreen "LUNAS" setelah pembayaran berhasil.
 *
 * INHERITANCE: Extends Canvas.
 * Menampilkan animasi check mark + teks LUNAS dengan efek scale/fade,
 * lalu otomatis kembali ke PasienListScreen setelah 3 detik.
 */
public class LunasAnimationScreen extends Canvas {

    private int totalBiaya;
    private String tipePembayaran;

    // Animasi state
    private int frame = 0;
    private static final int MAX_FRAMES = 60; // ~3 detik di 20fps
    private Timer animTimer;

    // Warna
    private static final int C_BG_START = 0x1E8449;
    private static final int C_BG_END = 0x27AE60;
    private static final int C_WHITE = 0xFFFFFF;
    private static final int C_GOLD = 0xF1C40F;
    private static final int C_LIGHT_GREEN = 0x82E0AA;
    private static final int C_DARK_GREEN = 0x145A32;

    public LunasAnimationScreen(int totalBiaya, String tipePembayaran) {
        this.totalBiaya = totalBiaya;
        this.tipePembayaran = tipePembayaran;
        setFullScreenMode(true);
        startAnimation();
    }

    private void startAnimation() {
        animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                frame++;
                if (frame >= MAX_FRAMES) {
                    animTimer.cancel();
                    // Kembali ke daftar pasien
                    navigateBack();
                } else {
                    repaint();
                }
            }
        }, 50, 50); // 20fps
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // === BACKGROUND: Gradient hijau ===
        // Simulasi gradient dengan bands
        int bands = 20;
        int bandH = h / bands + 1;
        for (int i = 0; i < bands; i++) {
            int r1 = (C_BG_START >> 16) & 0xFF;
            int g1 = (C_BG_START >> 8) & 0xFF;
            int b1 = C_BG_START & 0xFF;
            int r2 = (C_BG_END >> 16) & 0xFF;
            int g2 = (C_BG_END >> 8) & 0xFF;
            int b2 = C_BG_END & 0xFF;

            int r = r1 + (r2 - r1) * i / bands;
            int gr = g1 + (g2 - g1) * i / bands;
            int b = b1 + (b2 - b1) * i / bands;

            g.setColor((r << 16) | (gr << 8) | b);
            g.fillRect(0, i * bandH, w, bandH);
        }

        // Fase animasi
        float progress = (float) frame / MAX_FRAMES;
        float entryProgress = Math.min(1.0f, (float) frame / 15); // 0-15 frame = masuk
        float pulseProgress = 0;
        if (frame > 15) {
            pulseProgress = (float) (frame - 15) / (MAX_FRAMES - 15);
        }

        int centerX = w / 2;
        int centerY = h / 2 - 30;

        // === LINGKARAN CAHAYA (glow effect) ===
        if (entryProgress > 0.3f) {
            int glowAlpha = (int) (Math.min(1.0f, (entryProgress - 0.3f) * 2) * 255);
            int glowSize = (int) (80 * entryProgress);
            // Outer glow ring
            g.setColor(C_LIGHT_GREEN);
            g.fillRoundRect(centerX - glowSize, centerY - glowSize,
                    glowSize * 2, glowSize * 2, glowSize * 2, glowSize * 2);
        }

        // === LINGKARAN UTAMA (scale up) ===
        int circleSize = (int) (60 * entryProgress);
        if (circleSize > 0) {
            g.setColor(C_WHITE);
            g.fillRoundRect(centerX - circleSize, centerY - circleSize,
                    circleSize * 2, circleSize * 2, circleSize * 2, circleSize * 2);

            // === CHECK MARK ===
            if (entryProgress > 0.5f) {
                float checkProgress = Math.min(1.0f, (entryProgress - 0.5f) * 3);
                g.setColor(C_BG_START);

                // Checkmark menggunakan thick lines
                int cxOff = centerX;
                int cyOff = centerY;
                int size = (int) (circleSize * 0.5f);

                // Line 1: dari kiri-tengah ke bawah-tengah
                if (checkProgress > 0) {
                    float p1 = Math.min(1.0f, checkProgress * 2);
                    int x1 = cxOff - size;
                    int y1 = cyOff - (int)(size * 0.1f);
                    int x2 = cxOff - (int)(size * 0.2f);
                    int y2 = cyOff + (int)(size * 0.6f);
                    int ex = x1 + (int)((x2 - x1) * p1);
                    int ey = y1 + (int)((y2 - y1) * p1);
                    drawThickLine(g, x1, y1, ex, ey, 4);
                }
                // Line 2: dari bawah-tengah ke kanan-atas
                if (checkProgress > 0.5f) {
                    float p2 = Math.min(1.0f, (checkProgress - 0.5f) * 2);
                    int x2 = cxOff - (int)(size * 0.2f);
                    int y2 = cyOff + (int)(size * 0.6f);
                    int x3 = cxOff + size;
                    int y3 = cyOff - (int)(size * 0.5f);
                    int ex = x2 + (int)((x3 - x2) * p2);
                    int ey = y2 + (int)((y3 - y2) * p2);
                    drawThickLine(g, x2, y2, ex, ey, 4);
                }
            }
        }

        // === TEKS "LUNAS" ===
        if (entryProgress > 0.7f) {
            Font fBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
            Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
            Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

            float textProgress = Math.min(1.0f, (entryProgress - 0.7f) * 4);
            int textY = centerY + circleSize + 20;

            // Pulse effect
            int bounceOff = 0;
            if (frame > 15 && frame < 25) {
                bounceOff = (int) (Math.sin((frame - 15) * 0.6f) * 5);
            }

            // LUNAS text
            g.setColor(C_GOLD);
            g.setFont(fBesar);
            g.drawString("L U N A S", centerX, textY + bounceOff,
                    Graphics.TOP | Graphics.HCENTER);
            textY += fBesar.getHeight() + 8;

            // Sparkle decorations
            if (frame > 20) {
                drawSparkle(g, centerX - 70, textY - 40 + bounceOff, frame);
                drawSparkle(g, centerX + 60, textY - 50 + bounceOff, frame + 5);
                drawSparkle(g, centerX - 50, textY - 60 + bounceOff, frame + 10);
                drawSparkle(g, centerX + 40, textY - 30 + bounceOff, frame + 15);
            }

            // Total biaya
            g.setColor(C_WHITE);
            g.setFont(fSedang);
            g.drawString(new StringBuffer().append("Rp ").append(formatHarga(totalBiaya)).toString(),
                    centerX, textY, Graphics.TOP | Graphics.HCENTER);
            textY += fSedang.getHeight() + 4;

            // Tipe pembayaran
            g.setColor(C_LIGHT_GREEN);
            g.setFont(fKecil);
            g.drawString(tipePembayaran, centerX, textY,
                    Graphics.TOP | Graphics.HCENTER);
            textY += fKecil.getHeight() + 16;

            // "Pembayaran berhasil" subtitle
            g.setColor(C_LIGHT_GREEN);
            g.setFont(fKecil);
            g.drawString("Pembayaran berhasil dicatat", centerX, textY,
                    Graphics.TOP | Graphics.HCENTER);
        }

        // === PROGRESS BAR (bawah) ===
        int barH = 3;
        int barW = (int) (w * progress);
        g.setColor(C_GOLD);
        g.fillRect(0, h - barH, barW, barH);
    }

    /**
     * Menggambar garis tebal (karena J2ME tidak punya setStroke).
     */
    private void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {
        for (int i = -thickness / 2; i <= thickness / 2; i++) {
            g.drawLine(x1, y1 + i, x2, y2 + i);
            g.drawLine(x1 + i, y1, x2 + i, y2);
        }
    }

    /**
     * Menggambar efek sparkle/bintang kecil.
     */
    private void drawSparkle(Graphics g, int x, int y, int animFrame) {
        int size = 2 + (animFrame % 4);
        // Blink effect
        if ((animFrame / 3) % 2 == 0) {
            g.setColor(C_GOLD);
        } else {
            g.setColor(C_WHITE);
        }
        g.drawLine(x - size, y, x + size, y);
        g.drawLine(x, y - size, x, y + size);
    }

    protected void keyPressed(int keyCode) {
        // Tekan tombol apa saja → langsung kembali
        if (animTimer != null) {
            animTimer.cancel();
        }
        navigateBack();
    }

    protected void pointerPressed(int px, int py) {
        // Tap di mana saja → langsung kembali
        if (animTimer != null) {
            animTimer.cancel();
        }
        navigateBack();
    }

    /**
     * Kembali ke PasienListScreen (skip history stack, langsung replace).
     */
    private void navigateBack() {
        // Kembali 2 level: PasienKeluarScreen → PasienDetailScreen
        // Lalu tampilkan PasienListScreen baru (data sudah direfresh)
        ScreenManager sm = ScreenManager.getInstance();
        sm.kembali(); // ke PasienDetailScreen
        sm.kembali(); // ke PasienListScreen
        // Buka list baru (refresh data)
        sm.tampilkanLayar(new PasienListScreen());
    }

    private String formatHarga(int harga) {
        String s = String.valueOf(harga);
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (i > 0 && (len - i) % 3 == 0) {
                sb.append('.');
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }
}
