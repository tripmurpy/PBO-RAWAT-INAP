package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Displayable;
import model.User;
import controller.LoginController;

/**
 * LoginScreen — Layar login admin.
 * 
 * INHERITANCE: Extends Canvas (J2ME LCDUI).
 * Menggunakan custom painting untuk tampilan login.
 */
public class LoginScreen extends Canvas {

    private LoginController loginController;
    private StringBuffer username = new StringBuffer();
    private StringBuffer password = new StringBuffer();
    private int fieldAktif = 0; // 0=username, 1=password, 2=login button
    private String pesanError = "";
    private boolean sedangInput = false;

    // Multi-tap state for Nokia-style keyboard
    private static final String[] KEY_MAP = {
        "0 ", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9"
    };
    private int lastKeyCode = -1;
    private long lastKeyTime = 0;
    private int tapCount = 0;
    private static final int MULTI_TAP_TIMEOUT = 800; // ms

    // Layout coordinates - calculated in paint(), used in pointerPressed()
    private int cardX, cardY, cardW, cardH;
    private int fieldX, fieldW, fieldH;
    private int usernameFieldY, passwordFieldY;
    private int btnY, btnH;

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
        this.loginController = new LoginController();
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
        this.cardX = 10;
        this.cardW = w - 20;
        this.cardH = 160;
        this.cardY = y;
        g.setColor(WARNA_CARD);
        g.fillRoundRect(cardX, cardY, cardW, cardH, 12, 12);

        this.fieldX = cardX + 10;
        this.fieldW = cardW - 20;
        this.fieldH = 28;
        int fy = cardY + 15;

        g.setFont(fontKecil);

        // Label username
        g.setColor(WARNA_TEKS_REDUP);
        g.drawString("Username", fieldX, fy, Graphics.TOP | Graphics.LEFT);
        fy += fontKecil.getHeight() + 2;
        this.usernameFieldY = fy;

        // Field username
        g.setColor(fieldAktif == 0 ? WARNA_FIELD_AKTIF : WARNA_FIELD_BG);
        g.fillRoundRect(fieldX, usernameFieldY, fieldW, fieldH, 6, 6);
        g.setColor(WARNA_TEKS);
        g.setFont(fontSedang);
        g.drawString(username.length() > 0 ? username.toString() : "",
                fieldX + 8, usernameFieldY + 4, Graphics.TOP | Graphics.LEFT);
        fy += fieldH + 10;

        // Label password
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Password", fieldX, fy, Graphics.TOP | Graphics.LEFT);
        fy += fontKecil.getHeight() + 2;
        this.passwordFieldY = fy;

        // Field password
        g.setColor(fieldAktif == 1 ? WARNA_FIELD_AKTIF : WARNA_FIELD_BG);
        g.fillRoundRect(fieldX, passwordFieldY, fieldW, fieldH, 6, 6);
        g.setColor(WARNA_TEKS);
        g.setFont(fontSedang);
        // Tampilkan password sebagai **** (StringBuffer agar tidak alokasi N kali)
        int pwLen = password.length();
        StringBuffer maskBuf = new StringBuffer(pwLen);
        for (int i = 0; i < pwLen; i++) maskBuf.append('*');
        g.drawString(maskBuf.toString(), fieldX + 8, passwordFieldY + 4, Graphics.TOP | Graphics.LEFT);
        fy += fieldH + 15;

        // Tombol LOGIN
        this.btnY = cardY + cardH + 15;
        this.btnH = 35;
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
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        long currentTime = System.currentTimeMillis();

        if (action == Canvas.UP) {
            fieldAktif = (fieldAktif - 1 + 3) % 3;
            resetMultiTap();
        } else if (action == Canvas.DOWN) {
            fieldAktif = (fieldAktif + 1) % 3;
            resetMultiTap();
        } else if (action == Canvas.FIRE) {
            resetMultiTap();
            if (fieldAktif == 2) {
                prosesLogin();
            } else {
                fieldAktif = (fieldAktif + 1) % 3;
            }
        } else if (keyCode == Canvas.KEY_STAR || keyCode == -8 || keyCode == 8) {
            // Hapus karakter terakhir (Backspace)
            resetMultiTap();
            if (fieldAktif == 0 && username.length() > 0) {
                username.deleteCharAt(username.length() - 1);
            } else if (fieldAktif == 1 && password.length() > 0) {
                password.deleteCharAt(password.length() - 1);
            }
        } else if (keyCode >= Canvas.KEY_NUM0 && keyCode <= Canvas.KEY_NUM9) {
            if (fieldAktif == 2) {
                if (keyCode == Canvas.KEY_NUM5) prosesLogin();
            } else {
                handleMultiTap(keyCode, currentTime);
            }
        } else if (keyCode > 31 && keyCode < 127) {
            // Support for PC keyboard (direct ASCII input)
            resetMultiTap();
            if (fieldAktif == 0) {
                username.append((char) keyCode);
            } else if (fieldAktif == 1) {
                password.append((char) keyCode);
            }
        }
        repaint();
    }

    private void resetMultiTap() {
        lastKeyCode = -1;
        tapCount = 0;
    }

    private void handleMultiTap(int keyCode, long currentTime) {
        int index = keyCode - Canvas.KEY_NUM0;
        if (index < 0 || index >= KEY_MAP.length) return;

        String chars = KEY_MAP[index];
        StringBuffer activeBuffer = (fieldAktif == 0) ? username : password;

        if (keyCode == lastKeyCode && (currentTime - lastKeyTime) < MULTI_TAP_TIMEOUT) {
            // Cycle character
            if (activeBuffer.length() > 0) {
                activeBuffer.deleteCharAt(activeBuffer.length() - 1);
            }
            tapCount = (tapCount + 1) % chars.length();
        } else {
            // New character
            tapCount = 0;
        }

        activeBuffer.append(chars.charAt(tapCount));
        lastKeyCode = keyCode;
        lastKeyTime = currentTime;
    }

    protected void pointerPressed(int x, int y) {
        // Gunakan field yang sudah dihitung paint()
        // Cek klik tombol LOGIN
        if (x >= fieldX && x <= fieldX + fieldW && y >= btnY && y <= btnY + btnH) {
            fieldAktif = 2;
            prosesLogin();
        }

        // Cek klik field username
        if (x >= fieldX && x <= fieldX + fieldW && y >= usernameFieldY && y <= usernameFieldY + fieldH) {
            fieldAktif = 0;
        }

        // Cek klik field password
        if (x >= fieldX && x <= fieldX + fieldW && y >= passwordFieldY && y <= passwordFieldY + fieldH) {
            fieldAktif = 1;
        }
        repaint();
    }

    private void prosesLogin() {
        try {
            User user = loginController.login(username.toString(), password.toString());
            pesanError = "";
            // Navigasi ke Dashboard
            ScreenManager.getInstance().tampilkanLayar(new DashboardScreen(user));
        } catch (Exception e) {
            pesanError = e.getMessage();
        }
        repaint();
    }

}
