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
 * Menggunakan custom painting untuk tampilan login minimalis.
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
    private int fieldX, fieldW, fieldH;
    private int usernameFieldY, passwordFieldY;
    private int btnX, btnY, btnW, btnH;

    // Konstanta warna — Minimalist palette
    private static final int WARNA_BG = 0xFFFFFF;
    private static final int WARNA_TEKS_UTAMA = 0x1A1A2E;
    private static final int WARNA_TEKS_SEKUNDER = 0x9E9E9E;
    private static final int WARNA_FIELD_BG = 0xF5F5F5;
    private static final int WARNA_FIELD_BORDER = 0xE0E0E0;
    private static final int WARNA_FIELD_AKTIF_BG = 0xF0F7FF;
    private static final int WARNA_FIELD_AKTIF_BORDER = 0x4A90E2;
    private static final int WARNA_TOMBOL = 0x1A1A2E;
    private static final int WARNA_TOMBOL_TEKS = 0xFFFFFF;
    private static final int WARNA_TOMBOL_SELECTED = 0x333355;
    private static final int WARNA_ERROR = 0xE53935;
    private static final int WARNA_GARIS = 0xEEEEEE;

    public LoginScreen() {
        this.loginController = new LoginController();
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // Clean white background
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fontLabel = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

        int centerX = w / 2;
        int margin = 24;
        this.fieldX = margin;
        this.fieldW = w - (margin * 2);
        this.fieldH = 32;

        // === HEADER SECTION ===
        int y = h / 6;

        // Hospital name — bold, dark, centered
        g.setColor(WARNA_TEKS_UTAMA);
        g.setFont(fontBesar);
        g.drawString("RS Rasuna Said 5", centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontBesar.getHeight() + 3;

        // Subtitle
        g.setFont(fontKecil);
        g.setColor(WARNA_TEKS_SEKUNDER);
        g.drawString("Sistem Rawat Inap", centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontKecil.getHeight() + 8;

        // Thin separator line
        g.setColor(WARNA_GARIS);
        int lineMargin = w / 4;
        g.drawLine(lineMargin, y, w - lineMargin, y);
        y += 20;

        // === USERNAME FIELD ===
        g.setFont(fontLabel);
        g.setColor(fieldAktif == 0 ? WARNA_FIELD_AKTIF_BORDER : WARNA_TEKS_SEKUNDER);
        g.drawString("Username", fieldX, y, Graphics.TOP | Graphics.LEFT);
        y += fontLabel.getHeight() + 4;
        this.usernameFieldY = y;

        // Field background
        if (fieldAktif == 0) {
            g.setColor(WARNA_FIELD_AKTIF_BG);
            g.fillRoundRect(fieldX, usernameFieldY, fieldW, fieldH, 6, 6);
            g.setColor(WARNA_FIELD_AKTIF_BORDER);
            g.drawRoundRect(fieldX, usernameFieldY, fieldW, fieldH, 6, 6);
        } else {
            g.setColor(WARNA_FIELD_BG);
            g.fillRoundRect(fieldX, usernameFieldY, fieldW, fieldH, 6, 6);
            g.setColor(WARNA_FIELD_BORDER);
            g.drawRoundRect(fieldX, usernameFieldY, fieldW, fieldH, 6, 6);
        }

        // Username text
        g.setColor(WARNA_TEKS_UTAMA);
        g.setFont(fontSedang);
        String uText = username.length() > 0 ? username.toString() : "";
        g.drawString(uText, fieldX + 10, usernameFieldY + (fieldH - fontSedang.getHeight()) / 2,
                Graphics.TOP | Graphics.LEFT);

        // Cursor blink indicator for active field
        if (fieldAktif == 0) {
            int cursorX = fieldX + 10 + fontSedang.stringWidth(uText);
            g.setColor(WARNA_FIELD_AKTIF_BORDER);
            g.fillRect(cursorX + 1, usernameFieldY + 7, 2, fieldH - 14);
        }

        y = usernameFieldY + fieldH + 14;

        // === PASSWORD FIELD ===
        g.setFont(fontLabel);
        g.setColor(fieldAktif == 1 ? WARNA_FIELD_AKTIF_BORDER : WARNA_TEKS_SEKUNDER);
        g.drawString("Password", fieldX, y, Graphics.TOP | Graphics.LEFT);
        y += fontLabel.getHeight() + 4;
        this.passwordFieldY = y;

        // Field background
        if (fieldAktif == 1) {
            g.setColor(WARNA_FIELD_AKTIF_BG);
            g.fillRoundRect(fieldX, passwordFieldY, fieldW, fieldH, 6, 6);
            g.setColor(WARNA_FIELD_AKTIF_BORDER);
            g.drawRoundRect(fieldX, passwordFieldY, fieldW, fieldH, 6, 6);
        } else {
            g.setColor(WARNA_FIELD_BG);
            g.fillRoundRect(fieldX, passwordFieldY, fieldW, fieldH, 6, 6);
            g.setColor(WARNA_FIELD_BORDER);
            g.drawRoundRect(fieldX, passwordFieldY, fieldW, fieldH, 6, 6);
        }

        // Password masked text
        g.setColor(WARNA_TEKS_UTAMA);
        g.setFont(fontSedang);
        int pwLen = password.length();
        StringBuffer maskBuf = new StringBuffer(pwLen);
        for (int i = 0; i < pwLen; i++) maskBuf.append('\u2022'); // bullet char
        String pText = maskBuf.toString();
        g.drawString(pText, fieldX + 10, passwordFieldY + (fieldH - fontSedang.getHeight()) / 2,
                Graphics.TOP | Graphics.LEFT);

        // Cursor for password
        if (fieldAktif == 1) {
            int cursorX = fieldX + 10 + fontSedang.stringWidth(pText);
            g.setColor(WARNA_FIELD_AKTIF_BORDER);
            g.fillRect(cursorX + 1, passwordFieldY + 7, 2, fieldH - 14);
        }

        y = passwordFieldY + fieldH + 20;

        // === LOGIN BUTTON ===
        this.btnW = fieldW;
        this.btnH = 36;
        this.btnX = fieldX;
        this.btnY = y;

        g.setColor(fieldAktif == 2 ? WARNA_TOMBOL_SELECTED : WARNA_TOMBOL);
        g.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);

        g.setColor(WARNA_TOMBOL_TEKS);
        g.setFont(fontLabel);
        g.drawString("Masuk", centerX, btnY + (btnH - fontLabel.getHeight()) / 2,
                Graphics.TOP | Graphics.HCENTER);

        // === ERROR MESSAGE ===
        if (pesanError.length() > 0) {
            g.setColor(WARNA_ERROR);
            g.setFont(fontKecil);
            g.drawString(pesanError, centerX, btnY + btnH + 12,
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
        // Cek klik tombol LOGIN
        if (x >= btnX && x <= btnX + btnW && y >= btnY && y <= btnY + btnH) {
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
            pesanError = e.getMessage() != null ? e.getMessage() : "Terjadi Kesalahan Sistem";
        }
        repaint();
    }

}
