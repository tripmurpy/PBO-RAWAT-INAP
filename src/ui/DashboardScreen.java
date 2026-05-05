package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import model.User;

/**
 * DashboardScreen — Menu utama setelah login.
 * 
 * INHERITANCE: Extends Canvas.
 * Menampilkan 8 menu navigasi untuk admin.
 */
public class DashboardScreen extends Canvas {

    private User currentUser;
    private int selectedIndex = 0;

    private static final int WARNA_BG = 0x1A1A2E;
    private static final int WARNA_CARD = 0x16213E;
    private static final int WARNA_TEKS = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x888888;
    private static final int WARNA_AKSEN = 0x533483;
    private static final int WARNA_SELECTED = 0x0F3460;

    private static final String[] MENU = {
        "1. Daftarkan Pasien Baru",
        "2. Cari Pasien",
        "3. Rawat Inap Baru",
        "4. Keluar Pasien (Discharge)",
        "5. Manajemen Dokter",
        "6. Manajemen Kamar",
        "7. Riwayat Kunjungan",
        "8. Logout"
    };

    public DashboardScreen(User user) {
        this.currentUser = user;
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        int centerX = w / 2;
        int y = 10;

        // Title bar
        g.setColor(WARNA_AKSEN);
        g.fillRect(0, 0, w, 40);
        g.setColor(WARNA_TEKS);
        g.setFont(fontBesar);
        g.drawString("DASHBOARD ADMIN", centerX, 8, Graphics.TOP | Graphics.HCENTER);

        // Selamat datang
        y = 48;
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        String nama = (currentUser != null) ? currentUser.getNamaLengkap() : "Admin";
        g.drawString(new StringBuffer().append("Selamat datang, ").append(nama).toString(), centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontKecil.getHeight() + 10;

        // Menu items
        int itemH = 30;
        int itemX = 10;
        int itemW = w - 20;

        for (int i = 0; i < MENU.length; i++) {
            // Background item
            if (i == selectedIndex) {
                g.setColor(WARNA_SELECTED);
            } else {
                g.setColor(WARNA_CARD);
            }
            g.fillRoundRect(itemX, y, itemW, itemH, 8, 8);

            // Teks menu
            g.setColor(WARNA_TEKS);
            g.setFont(fontSedang);
            g.drawString(MENU[i], itemX + 12, y + 6, Graphics.TOP | Graphics.LEFT);

            y += itemH + 5;
        }
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);

        if (action == Canvas.UP) {
            selectedIndex = (selectedIndex - 1 + MENU.length) % MENU.length;
        } else if (action == Canvas.DOWN) {
            selectedIndex = (selectedIndex + 1) % MENU.length;
        } else if (action == Canvas.FIRE) {
            eksekusiMenu(selectedIndex);
        } else {
            // Shortcut angka 1-8
            int num = keyCode - Canvas.KEY_NUM1;
            if (num >= 0 && num < MENU.length) {
                eksekusiMenu(num);
            }
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int startY = 48 + fontKecil.getHeight() + 10;
        int itemH = 30;
        int gap = 5;

        for (int i = 0; i < MENU.length; i++) {
            int itemY = startY + i * (itemH + gap);
            if (y >= itemY && y <= itemY + itemH) {
                selectedIndex = i;
                eksekusiMenu(i);
                return;
            }
        }
        repaint();
    }

    private void eksekusiMenu(int index) {
        ScreenManager sm = ScreenManager.getInstance();
        switch (index) {
            case 0: // Daftar Pasien Baru
                sm.tampilkanLayar(new PasienFormScreen());
                break;
            case 1: // Cari Pasien
                sm.tampilkanLayar(new PasienListScreen());
                break;
            case 2: // Rawat Inap Baru
                sm.tampilkanLayar(new AdmisiScreen());
                break;
            case 3: // Discharge
                sm.tampilkanLayar(new DischargeScreen());
                break;
            case 4: // Manajemen Dokter
                sm.tampilkanLayar(new DokterScreen());
                break;
            case 5: // Manajemen Kamar
                sm.tampilkanLayar(new RuanganScreen());
                break;
            case 6: // Riwayat
                sm.tampilkanLayar(new KunjunganScreen());
                break;
            case 7: // Logout
                sm.logout();
                break;
        }
    }
}
