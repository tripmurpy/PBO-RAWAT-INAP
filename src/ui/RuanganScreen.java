package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import service.RuanganService;
import model.Ruangan;
import util.ServiceFactory;

/**
 * RuanganScreen — Menampilkan daftar ruangan dengan layout list.
 * INHERITANCE: Extends Canvas.
 */
public class RuanganScreen extends Canvas implements CommandListener {

    private RuanganService service;
    private Vector daftarRuangan;
    private int selectedIndex = 0;
    private int scrollOffset = 0;

    private Command cmdKembali, cmdBaru;

    private static final int WARNA_BG = 0xF0F4F8;
    private static final int WARNA_CARD = 0xFFFFFF;
    private static final int WARNA_TEKS = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x888888;
    private static final int WARNA_AKSEN = 0x4A90E2;
    private static final int WARNA_SELECTED = 0xD0E1F9;
    
    private static final int WARNA_KOSONG = 0x27AE60; // Green
    private static final int WARNA_TERISI = 0xE74C3C; // Red
    private static final int WARNA_MAINTENANCE = 0xF39C12; // Orange

    public RuanganScreen() {
        this.service = ServiceFactory.getInstance().getRuanganService();
        setFullScreenMode(true);
        muatData();

        cmdKembali = new Command("Kembali", Command.BACK, 1);
        cmdBaru = new Command("Tambah Kamar", Command.ITEM, 2);
        
        addCommand(cmdKembali);
        addCommand(cmdBaru);
        setCommandListener(this);
    }

    private void muatData() {
        try {
            daftarRuangan = service.getSemuaRuangan();
        } catch (Exception e) {
            daftarRuangan = new Vector();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdBaru) {
            ScreenManager.getInstance().tampilkanLayar(new RuanganFormScreen());
        }
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        // Title bar
        g.setColor(WARNA_AKSEN);
        g.fillRect(0, 0, w, 40);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontBesar);
        g.drawString("STATUS KAMAR", w / 2, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 50;

        if (daftarRuangan == null || daftarRuangan.size() == 0) {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontSedang);
            g.drawString("Belum ada data kamar", w / 2, h / 2, Graphics.TOP | Graphics.HCENTER);
        } else {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            int[] stat = new int[]{0, 0, 0};
            try {
                stat = service.hitungStatistik();
            } catch (Exception e) {}
            g.drawString("Total: " + stat[0] + " | Terisi: " + stat[1] + " | Kosong: " + stat[2], 10, y, Graphics.TOP | Graphics.LEFT);
            y += fontKecil.getHeight() + 8;

            int itemH = 65;
            int maxVisible = (h - y - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarRuangan.size() && i < scrollOffset + maxVisible; i++) {
                Ruangan r = (Ruangan) daftarRuangan.elementAt(i);

                g.setColor(i == selectedIndex ? WARNA_SELECTED : WARNA_CARD);
                g.fillRoundRect(10, y, w - 20, itemH, 8, 8);

                // Indicator Color Block on Left
                int statusColor = WARNA_KOSONG;
                if (r.getStatusKamar().equals(Ruangan.STATUS_TERISI)) statusColor = WARNA_TERISI;
                else if (r.getStatusKamar().equals(Ruangan.STATUS_MAINTENANCE)) statusColor = WARNA_MAINTENANCE;

                g.setColor(statusColor);
                g.fillRoundRect(10, y, 15, itemH, 8, 8);
                g.fillRect(18, y, 7, itemH); // square off right side

                int textX = 35;
                g.setColor(WARNA_TEKS);
                g.setFont(fontBesar);
                g.drawString(r.getNamaRuangan() + " (" + r.getTipeKamar() + ")", textX, y + 5, Graphics.TOP | Graphics.LEFT);

                g.setColor(WARNA_TEKS_REDUP);
                g.setFont(fontKecil);
                String info = r.getStatusKamar();
                if (r.isTerisi()) info += " - " + r.getNamaPasien();
                g.drawString("Status: " + info, textX, y + 5 + fontBesar.getHeight() + 2, Graphics.TOP | Graphics.LEFT);

                // Tombol Edit
                int btnW = 50;
                int btnH = 25;
                int btnX = w - 10 - btnW - 5;
                int btnY = y + (itemH - btnH) / 2;

                g.setColor(WARNA_AKSEN);
                g.fillRoundRect(btnX, btnY, btnW, btnH, 6, 6);
                g.setColor(WARNA_TEKS_TERANG);
                g.drawString("EDIT", btnX + btnW / 2, btnY + 5, Graphics.TOP | Graphics.HCENTER);

                y += itemH + 4;
            }
        }
        
        // Footer (Label bantuan)
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 25, w, 25);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Pilih item & tekan OK untuk Edit", 10, h - 20, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == Canvas.UP && selectedIndex > 0) {
            selectedIndex--;
            if (selectedIndex < scrollOffset) scrollOffset = selectedIndex;
        } else if (action == Canvas.DOWN && daftarRuangan != null && selectedIndex < daftarRuangan.size() - 1) {
            selectedIndex++;
            int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
            int maxVisible = (getHeight() - startY - 30) / (65 + 4);
            if (selectedIndex >= scrollOffset + maxVisible) scrollOffset = selectedIndex - maxVisible + 1;
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
        } else if (action == Canvas.FIRE || keyCode == Canvas.KEY_NUM5) {
            if (daftarRuangan != null && selectedIndex >= 0 && selectedIndex < daftarRuangan.size()) {
                ScreenManager.getInstance().tampilkanLayar(new RuanganEditScreen((Ruangan)daftarRuangan.elementAt(selectedIndex)));
            }
        }
        repaint();
    }
    
    protected void pointerPressed(int x, int y) {
        if (daftarRuangan == null) return;
        int w = getWidth();
        int h = getHeight();
        int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
        int itemH = 65;
        int maxVisible = (h - startY - 30) / (itemH + 4);

        for (int i = scrollOffset; i < daftarRuangan.size() && i < scrollOffset + maxVisible; i++) {
            int itemY = startY + (i - scrollOffset) * (itemH + 4);
            if (y >= itemY && y <= itemY + itemH) {
                selectedIndex = i;
                if (x >= w - 70) {
                    ScreenManager.getInstance().tampilkanLayar(new RuanganEditScreen((Ruangan)daftarRuangan.elementAt(i)));
                }
                repaint();
                return;
            }
        }
    }
}
