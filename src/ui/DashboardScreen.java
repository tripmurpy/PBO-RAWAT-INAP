package ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import model.User;
import controller.DashboardController;

/**
 * DashboardScreen — Menu utama setelah login.
 *
 * INHERITANCE: Extends Canvas.
 * Uses DashboardController for navigation routing (5-layer architecture).
 */
public class DashboardScreen extends Canvas {

    private User currentUser;
    private DashboardController controller;
    private int selectedIndex = 0;

    private static final int WARNA_BG       = 0xF0F4F8;
    private static final int WARNA_CARD     = 0xFFFFFF;
    private static final int WARNA_TEKS     = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_REDUP    = 0x888888;
    private static final int WARNA_AKSEN    = 0x4A90E2;
    private static final int WARNA_SELECTED = 0xD0E1F9;

    private static final String[] MENU = {
        "1. Daftarkan Pasien Baru",
        "2. Cari Pasien",
        "3. Rawat Inap Baru",
        "4. Keluar Pasien (Discharge)",
        "5. Manajemen Dokter",
        "6. Manajemen Kamar",
        "7. Manajemen Obat",
        "8. Laporan",
        "9. Riwayat Kunjungan",
        "0. Logout"
    };

    public DashboardScreen(User user) {
        this.currentUser = user;
        this.controller = new DashboardController();
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

        // Title bar
        g.setColor(WARNA_AKSEN);
        g.fillRect(0, 0, w, 40);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontBesar);
        g.drawString("DASHBOARD ADMIN", centerX, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 48;
        g.setColor(WARNA_REDUP);
        g.setFont(fontKecil);
        String nama = (currentUser != null) ? currentUser.getNamaLengkap() : "Admin";
        StringBuffer welcomeBuf = new StringBuffer();
        welcomeBuf.append("Selamat datang, ").append(nama);
        g.drawString(welcomeBuf.toString(), centerX, y, Graphics.TOP | Graphics.HCENTER);
        y += fontKecil.getHeight() + 6;

        // Menu items (scrollable if needed)
        int itemH = 28;
        int itemX = 10;
        int itemW = w - 20;

        for (int i = 0; i < MENU.length; i++) {
            if (y + itemH > h - 4) break; // clip if screen too small
            if (i == selectedIndex) {
                g.setColor(WARNA_SELECTED);
            } else {
                g.setColor(WARNA_CARD);
            }
            g.fillRoundRect(itemX, y, itemW, itemH, 8, 8);
            g.setColor(WARNA_TEKS);
            g.setFont(fontSedang);
            g.drawString(MENU[i], itemX + 12, y + 4, Graphics.TOP | Graphics.LEFT);
            y += itemH + 4;
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
        } else if (keyCode == Canvas.KEY_NUM0) {
            eksekusiMenu(9); // Logout
        } else {
            int num = keyCode - Canvas.KEY_NUM1;
            if (num >= 0 && num < MENU.length - 1) {
                eksekusiMenu(num);
            }
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int startY = 48 + fontKecil.getHeight() + 6;
        int itemH = 28;
        int gap = 4;

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
        switch (index) {
            case 0: controller.navigatePasienBaru();  break;
            case 1: controller.navigateCariPasien();  break;
            case 2: controller.navigateAdmisi();      break;
            case 3: controller.navigateDischarge();   break;
            case 4: controller.navigateDokter();      break;
            case 5: controller.navigateRuangan();     break;
            case 6: controller.navigateObat();        break;
            case 7: controller.navigateLaporan();     break;
            case 8: controller.navigateRiwayat();     break;
            case 9: controller.logout();              break;
        }
    }
}
