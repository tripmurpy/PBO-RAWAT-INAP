package ui;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import model.Obat;
import controller.ObatController;

/**
 * ObatScreen — Manajemen data obat dan stok.
 * Menampilkan list obat, indikator low-stock, navigasi tambah.
 */
public class ObatScreen extends Canvas {

    private ObatController controller;
    private Vector listObat;
    private int selectedIndex = 0;
    private int scrollOffset = 0;
    private String pesanError = "";
    private static final int MAX_VISIBLE = 6;

    private static final int WARNA_BG = 0xF0F4F8;
    private static final int WARNA_HEADER = 0x4A90E2;
    private static final int WARNA_CARD = 0xFFFFFF;
    private static final int WARNA_SELECTED = 0xD0E1F9;
    private static final int WARNA_TEKS = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_REDUP = 0x888888;
    private static final int WARNA_WARNING = 0xE94560;
    private static final int WARNA_OK = 0x27AE60;

    public ObatScreen() {
        controller = new ObatController();
        setFullScreenMode(true);
        muatData();
    }

    private void muatData() {
        try {
            listObat = controller.getSemuaObat();
            pesanError = "";
        } catch (Exception e) {
            listObat = new Vector();
            pesanError = e.getMessage();
        }
        repaint();
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

        // Header
        g.setColor(WARNA_HEADER);
        g.fillRect(0, 0, w, 36);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fBesar);
        g.drawString("MANAJEMEN OBAT", cx, 6, Graphics.TOP | Graphics.HCENTER);

        int y = 44;
        g.setFont(fKecil);
        g.setColor(WARNA_REDUP);
        StringBuffer totalBuf = new StringBuffer();
        totalBuf.append("Total: ").append(listObat != null ? listObat.size() : 0).append(" obat  [Tgl: tambah, #: low-stock]");
        g.drawString(totalBuf.toString(),
                cx, y, Graphics.TOP | Graphics.HCENTER);
        y += fKecil.getHeight() + 6;

        if (pesanError.length() > 0) {
            g.setColor(WARNA_WARNING);
            g.drawString(pesanError, cx, y, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        if (listObat == null || listObat.size() == 0) {
            g.setColor(WARNA_REDUP);
            g.drawString("Belum ada data obat", cx, y + 20, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        // List obat
        int itemH = 38;
        int itemX = 6;
        int itemW = w - 12;

        int end = Math.min(scrollOffset + MAX_VISIBLE, listObat.size());
        for (int i = scrollOffset; i < end; i++) {
            Obat o = (Obat) listObat.elementAt(i);
            boolean sel = (i == selectedIndex);

            g.setColor(sel ? WARNA_SELECTED : WARNA_CARD);
            g.fillRoundRect(itemX, y, itemW, itemH, 6, 6);

            // Indikator low-stock
            g.setColor(o.isLowStock() ? WARNA_WARNING : WARNA_OK);
            g.fillRoundRect(itemX + itemW - 8, y + 12, 6, 14, 3, 3);

            g.setColor(WARNA_TEKS);
            g.setFont(fSedang);
            g.drawString(o.getNama(), itemX + 8, y + 4, Graphics.TOP | Graphics.LEFT);

            g.setFont(fKecil);
            g.setColor(WARNA_REDUP);
            StringBuffer sb = new StringBuffer();
            sb.append(o.getBentuk()).append(" | Stok: ").append(o.getStok()).append(" ").append(o.getSatuan());
            g.drawString(sb.toString(), itemX + 8, y + 4 + fSedang.getHeight() + 2, Graphics.TOP | Graphics.LEFT);

            y += itemH + 4;
        }

        // Scroll hint
        if (listObat.size() > MAX_VISIBLE) {
            g.setColor(WARNA_REDUP);
            g.setFont(fKecil);
            StringBuffer pageBuf = new StringBuffer();
            pageBuf.append(selectedIndex + 1).append("/").append(listObat.size());
            g.drawString(pageBuf.toString(), cx, h - fKecil.getHeight() - 4,
                    Graphics.TOP | Graphics.HCENTER);
        }
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == UP) {
            if (selectedIndex > 0) {
                selectedIndex--;
                if (selectedIndex < scrollOffset) scrollOffset = selectedIndex;
            }
        } else if (action == DOWN) {
            if (listObat != null && selectedIndex < listObat.size() - 1) {
                selectedIndex++;
                if (selectedIndex >= scrollOffset + MAX_VISIBLE)
                    scrollOffset = selectedIndex - MAX_VISIBLE + 1;
            }
        } else if (action == FIRE || keyCode == KEY_NUM5) {
            if (listObat != null && selectedIndex >= 0 && selectedIndex < listObat.size()) {
                Obat selectedObat = (Obat) listObat.elementAt(selectedIndex);
                ScreenManager.getInstance().getDisplay().setCurrent(new ObatPemberianScreen(selectedObat));
            }
        } else if (keyCode == KEY_NUM0 || keyCode == -2) {
            ScreenManager.getInstance().kembali();
            return;
        } else if (keyCode == KEY_STAR) {
            // Filter low-stock
            muatLowStock();
            return;
        }
        repaint();
    }

    private void muatLowStock() {
        try {
            listObat = controller.getLowStock();
            selectedIndex = 0;
            scrollOffset = 0;
            pesanError = "";
        } catch (Exception e) {
            pesanError = e.getMessage();
        }
        repaint();
    }
}
