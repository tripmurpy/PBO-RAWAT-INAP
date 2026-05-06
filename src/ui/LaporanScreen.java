package ui;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import model.Ruangan;
import model.Obat;
import model.Admisi;
import controller.LaporanController;

/**
 * LaporanScreen — Menu dan tampilan laporan sistem.
 * Menampilkan occupancy, stok, pasien aktif, top diagnosa.
 */
public class LaporanScreen extends Canvas {

    private static final int MODE_MENU      = 0;
    private static final int MODE_RUANGAN   = 1;
    private static final int MODE_STOK      = 2;
    private static final int MODE_PASIEN    = 3;
    private static final int MODE_DIAGNOSA  = 4;

    private LaporanController controller;
    private int mode = MODE_MENU;
    private int selectedIndex = 0;
    private Vector dataList;
    private String pesanError = "";

    private static final String[] MENU_LAPORAN = {
        "1. Occupancy Ruangan",
        "2. Stok Obat",
        "3. Pasien Aktif (Dirawat)",
        "4. Top 5 Diagnosa",
        "5. Billing & Laporan Pasien",
        "6. Kembali"
    };

    private static final int WARNA_BG       = 0x1A1A2E;
    private static final int WARNA_HEADER   = 0x533483;
    private static final int WARNA_CARD     = 0x16213E;
    private static final int WARNA_SELECTED = 0x0F3460;
    private static final int WARNA_TEKS     = 0xFFFFFF;
    private static final int WARNA_REDUP    = 0x888888;
    private static final int WARNA_WARNING  = 0xE94560;
    private static final int WARNA_OK       = 0x27AE60;

    public LaporanScreen() {
        controller = new LaporanController();
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fBesar  = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fKecil  = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int cx = w / 2;

        g.setColor(WARNA_HEADER);
        g.fillRect(0, 0, w, 36);
        g.setColor(WARNA_TEKS);
        g.setFont(fBesar);
        g.drawString("LAPORAN", cx, 6, Graphics.TOP | Graphics.HCENTER);

        if (mode == MODE_MENU) {
            paintMenu(g, w, fSedang, fKecil);
        } else {
            paintData(g, w, h, fSedang, fKecil, cx);
        }
    }

    private void paintMenu(Graphics g, int w, Font fSedang, Font fKecil) {
        int y = 44;
        int itemH = 30;
        int itemX = 10;
        int itemW = w - 20;

        for (int i = 0; i < MENU_LAPORAN.length; i++) {
            g.setColor(i == selectedIndex ? WARNA_SELECTED : WARNA_CARD);
            g.fillRoundRect(itemX, y, itemW, itemH, 8, 8);
            g.setColor(WARNA_TEKS);
            g.setFont(fSedang);
            g.drawString(MENU_LAPORAN[i], itemX + 12, y + 6, Graphics.TOP | Graphics.LEFT);
            y += itemH + 5;
        }
    }

    private void paintData(Graphics g, int w, int h, Font fSedang, Font fKecil, int cx) {
        int y = 44;
        g.setFont(fKecil);
        g.setColor(WARNA_REDUP);
        g.drawString("[0/Back] kembali ke menu", cx, y, Graphics.TOP | Graphics.HCENTER);
        y += fKecil.getHeight() + 6;

        if (pesanError.length() > 0) {
            g.setColor(WARNA_WARNING);
            g.drawString(pesanError, cx, y, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        if (dataList == null || dataList.size() == 0) {
            g.setColor(WARNA_REDUP);
            g.drawString("Tidak ada data", cx, y + 20, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        int itemH = 28;
        int itemX = 6;
        int itemW = w - 12;

        if (mode == MODE_RUANGAN) {
            for (int i = 0; i < dataList.size() && y + itemH < h; i++) {
                Ruangan r = (Ruangan) dataList.elementAt(i);
                g.setColor(WARNA_CARD);
                g.fillRoundRect(itemX, y, itemW, itemH, 4, 4);
                g.setColor(r.isKosong() ? WARNA_OK : WARNA_WARNING);
                g.fillRoundRect(itemX + itemW - 8, y + 8, 6, 12, 3, 3);
                g.setColor(WARNA_TEKS);
                g.setFont(fSedang);
                StringBuffer sb = new StringBuffer();
                sb.append(r.getNamaRuangan()).append(" [").append(r.getStatusKamar()).append("]");
                g.drawString(sb.toString(), itemX + 8, y + 5, Graphics.TOP | Graphics.LEFT);
                y += itemH + 3;
            }
        } else if (mode == MODE_STOK) {
            for (int i = 0; i < dataList.size() && y + itemH < h; i++) {
                Obat o = (Obat) dataList.elementAt(i);
                g.setColor(WARNA_CARD);
                g.fillRoundRect(itemX, y, itemW, itemH, 4, 4);
                g.setColor(o.isLowStock() ? WARNA_WARNING : WARNA_OK);
                g.fillRoundRect(itemX + itemW - 8, y + 8, 6, 12, 3, 3);
                g.setColor(WARNA_TEKS);
                g.setFont(fKecil);
                StringBuffer sb = new StringBuffer();
                sb.append(o.getNama()).append(" - Stok: ").append(o.getStok());
                g.drawString(sb.toString(), itemX + 8, y + 7, Graphics.TOP | Graphics.LEFT);
                y += itemH + 3;
            }
        } else if (mode == MODE_PASIEN) {
            for (int i = 0; i < dataList.size() && y + itemH < h; i++) {
                Admisi a = (Admisi) dataList.elementAt(i);
                g.setColor(WARNA_CARD);
                g.fillRoundRect(itemX, y, itemW, itemH, 4, 4);
                g.setColor(WARNA_TEKS);
                g.setFont(fKecil);
                StringBuffer pasienBuf = new StringBuffer();
                pasienBuf.append(a.getNoRMPasien()).append(" - ").append(a.getDiagnosisAwal());
                g.drawString(pasienBuf.toString(),
                        itemX + 8, y + 7, Graphics.TOP | Graphics.LEFT);
                y += itemH + 3;
            }
        } else if (mode == MODE_DIAGNOSA) {
            // Render bar chart via ChartCanvas or simple text
            for (int i = 0; i < dataList.size() && y + itemH < h; i++) {
                String[] entry = (String[]) dataList.elementAt(i);
                g.setColor(WARNA_CARD);
                g.fillRoundRect(itemX, y, itemW, itemH, 4, 4);
                g.setColor(WARNA_TEKS);
                g.setFont(fKecil);
                StringBuffer sb = new StringBuffer();
                sb.append((i + 1)).append(". ").append(entry[0]).append(" (").append(entry[1]).append("x)");
                g.drawString(sb.toString(), itemX + 8, y + 7, Graphics.TOP | Graphics.LEFT);
                y += itemH + 3;
            }
        }
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (mode == MODE_MENU) {
            if (action == UP) {
                selectedIndex = (selectedIndex - 1 + MENU_LAPORAN.length) % MENU_LAPORAN.length;
            } else if (action == DOWN) {
                selectedIndex = (selectedIndex + 1) % MENU_LAPORAN.length;
            } else if (action == FIRE) {
                eksekusiMenu(selectedIndex);
            } else {
                int num = keyCode - KEY_NUM1;
                if (num >= 0 && num < MENU_LAPORAN.length) eksekusiMenu(num);
            }
        } else {
            if (keyCode == KEY_NUM0 || keyCode == -2) {
                mode = MODE_MENU;
                dataList = null;
                pesanError = "";
            }
        }
        repaint();
    }

    private void eksekusiMenu(int idx) {
        try {
            switch (idx) {
                case 0:
                    dataList = controller.getLaporanRuangan();
                    mode = MODE_RUANGAN;
                    break;
                case 1:
                    dataList = controller.getLaporanStokObat();
                    mode = MODE_STOK;
                    break;
                case 2:
                    dataList = controller.getPasienAktif();
                    mode = MODE_PASIEN;
                    break;
                case 3:
                    dataList = controller.getTopDiagnosa(5);
                    mode = MODE_DIAGNOSA;
                    break;
                case 4:
                    ScreenManager.getInstance().tampilkanLayar(new BillingScreen());
                    return;
                case 5:
                    ScreenManager.getInstance().kembali();
                    return;
            }
            pesanError = "";
        } catch (Exception e) {
            pesanError = e.getMessage() != null ? e.getMessage() : "Error";
            mode = MODE_RUANGAN; // show error in data mode
            dataList = null;
        }
        repaint();
    }
}
