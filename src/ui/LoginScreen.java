package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Displayable;
import controller.AuthController;
import model.User;

/**
 * LoginScreen — Layar login admin.
 * 
 * INHERITANCE: Extends Canvas (J2ME LCDUI).
 * Menggunakan custom painting untuk tampilan login.
 */
public class LoginScreen extends Canvas {

    private AuthController authController;
    private String username = "";
    private String password = "";
    private int fieldAktif = 0; // 0=username, 1=password, 2=login button
    private String pesanError = "";
    private boolean sedangInput = false;

    // Konstanta warna
    private static final int WARNA_BG = 0x1A1A2E;
    private static final int WARNA_CARD = 0x16213E;
    private static final int WARNA_AKSEN = 0x0F3460;
    private static final int WARNA_TEKS = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x888888;
    private static final int WARNA_ERROR = 0xE94560;
    private static final int WARNA_TOMBOL = 0x533483;
    private static final int WARNA_FIELD_BG = 0x0A0A1A;
    private static final int WARNA_FIELD_AKTIF = 0x0F3460;
    private static final int WARNA_SELECTED = 0x0F3460;

    public LoginScreen() {
        this.authController = new AuthController();
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // Background
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        int centerX = w / 2;
        int y = h / 8;

        // Judul
        g.setColor(WARNA_TEKS);
        g.setFont(fontBesar);
        g.drawString("RUMAH SAKIT XYZ", centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontBesar.getHeight() + 4;

        g.setFont(fontKecil);
        g.setColor(WARNA_TEKS_REDUP);
        g.drawString("Sistem Rawat Inap", centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontKecil.getHeight() + 20;

        // Card login
        int cardX = 10;
        int cardW = w - 20;
        int cardH = 160;
        g.setColor(WARNA_CARD);
        g.fillRoundRect(cardX, y, cardW, cardH, 12, 12);

        int fieldX = cardX + 10;
        int fieldW = cardW - 20;
        int fieldH = 28;
        int fy = y + 15;

        g.setFont(fontKecil);

        // Label username
        g.setColor(WARNA_TEKS_REDUP);
        g.drawString("Username", fieldX, fy, Graphics.TOP | Graphics.LEFT);
        fy += fontKecil.getHeight() + 2;

        // Field username
        g.setColor(fieldAktif == 0 ? WARNA_FIELD_AKTIF : WARNA_FIELD_BG);
        g.fillRoundRect(fieldX, fy, fieldW, fieldH, 6, 6);
        g.setColor(WARNA_TEKS);
        g.setFont(fontSedang);
        g.drawString(username.length() > 0 ? username : "", 
                     fieldX + 8, fy + 4, Graphics.TOP | Graphics.LEFT);
        fy += fieldH + 10;

        // Label password
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Password", fieldX, fy, Graphics.TOP | Graphics.LEFT);
        fy += fontKecil.getHeight() + 2;

        // Field password
        g.setColor(fieldAktif == 1 ? WARNA_FIELD_AKTIF : WARNA_FIELD_BG);
        g.fillRoundRect(fieldX, fy, fieldW, fieldH, 6, 6);
        g.setColor(WARNA_TEKS);
        g.setFont(fontSedang);
        // Tampilkan password sebagai ****
        String mask = "";
        for (int i = 0; i < password.length(); i++) mask += "*";
        g.drawString(mask, fieldX + 8, fy + 4, Graphics.TOP | Graphics.LEFT);
        fy += fieldH + 15;

        // Tombol LOGIN
        int btnY = y + cardH + 15;
        int btnH = 35;
        g.setColor(fieldAktif == 2 ? WARNA_SELECTED : WARNA_TOMBOL);
        g.fillRoundRect(fieldX, btnY, fieldW, btnH, 8, 8);
        
        // Border if selected
        if (fieldAktif == 2) {
            g.setColor(WARNA_TEKS);
            g.drawRoundRect(fieldX, btnY, fieldW, btnH, 8, 8);
        }
        
        g.setColor(WARNA_TEKS);
        g.setFont(fontBesar);
        g.drawString("LOGIN", centerX, btnY + 6, Graphics.TOP | Graphics.HCENTER);

        // Pesan error
        if (pesanError.length() > 0) {
            g.setColor(WARNA_ERROR);
            g.setFont(fontKecil);
            g.drawString(pesanError, centerX, btnY + btnH + 10, 
                         Graphics.TOP | Graphics.HCENTER);
        }

        // Info default
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Default: admin / admin123", centerX, h - 20, 
                     Graphics.TOP | Graphics.HCENTER);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);

        if (action == UP) {
            fieldAktif = (fieldAktif - 1 + 3) % 3;
        } else if (action == DOWN) {
            fieldAktif = (fieldAktif + 1) % 3;
        } else if (action == FIRE) {
            if (fieldAktif == 2) {
                prosesLogin();
            } else {
                fieldAktif = (fieldAktif + 1) % 3;
            }
        } else if (keyCode == KEY_STAR || keyCode == -8) {
            // Hapus karakter terakhir
            if (fieldAktif == 0 && username.length() > 0) {
                username = username.substring(0, username.length() - 1);
            } else if (fieldAktif == 1 && password.length() > 0) {
                password = password.substring(0, password.length() - 1);
            }
        } else if (keyCode >= KEY_NUM0 && keyCode <= KEY_NUM9) {
            if (fieldAktif == 2 && keyCode == KEY_NUM5) {
                prosesLogin();
            } else if (fieldAktif < 2) {
                // Input karakter
                char c = mapKeyToChar(keyCode);
                if (c != 0) {
                    if (fieldAktif == 0) {
                        username += c;
                    } else {
                        password += c;
                    }
                }
            }
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        int w = getWidth();
        int h = getHeight();
        int cardY = h / 8 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, 
                    Font.SIZE_LARGE).getHeight() + 4 +
                    Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, 
                    Font.SIZE_SMALL).getHeight() + 20;
        int btnY = cardY + 160 + 15;
        int btnH = 35;
        int fieldX = 20;
        int fieldW = w - 40;

        // Cek klik tombol LOGIN
        if (x >= fieldX && x <= fieldX + fieldW && y >= btnY && y <= btnY + btnH) {
            prosesLogin();
        }

        // Cek klik field username
        int fy = cardY + 15 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, 
                 Font.SIZE_SMALL).getHeight() + 2;
        if (y >= fy && y <= fy + 28) {
            fieldAktif = 0;
        }

        // Cek klik field password
        fy += 28 + 10 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, 
             Font.SIZE_SMALL).getHeight() + 2;
        if (y >= fy && y <= fy + 28) {
            fieldAktif = 1;
        }
        repaint();
    }

    private void prosesLogin() {
        try {
            User user = authController.login(username, password);
            pesanError = "";
            // Navigasi ke Dashboard
            ScreenManager.getInstance().tampilkanLayar(new DashboardScreen(user));
        } catch (Exception e) {
            pesanError = e.getMessage();
        }
        repaint();
    }

    /** Map keyCode sederhana ke karakter */
    private char mapKeyToChar(int keyCode) {
        if (keyCode >= Canvas.KEY_NUM0 && keyCode <= Canvas.KEY_NUM9) {
            return (char) ('0' + (keyCode - Canvas.KEY_NUM0));
        }
        // Huruf sederhana via multi-tap (simplified)
        switch (keyCode) {
            case Canvas.KEY_NUM2: return 'a';
            case Canvas.KEY_NUM3: return 'd';
            case Canvas.KEY_NUM4: return 'g';
            case Canvas.KEY_NUM5: return 'j';
            case Canvas.KEY_NUM6: return 'm';
            case Canvas.KEY_NUM7: return 'p';
            case Canvas.KEY_NUM8: return 't';
            case Canvas.KEY_NUM9: return 'w';
        }
        return 0;
    }
}
