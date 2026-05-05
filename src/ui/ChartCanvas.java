package ui;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import controller.LaporanController;

/**
 * ChartCanvas — Bar chart diagnosa terbanyak (text-based).
 * Menampilkan 5 diagnosa teratas secara visual.
 */
public class ChartCanvas extends Canvas {

    private LaporanController controller;
    private Vector topDiagnosa;
    private String pesanError = "";

    private static final int WARNA_BG     = 0x1A1A2E;
    private static final int WARNA_HEADER = 0x533483;
    private static final int WARNA_BAR    = 0x0F3460;
    private static final int WARNA_TEKS   = 0xFFFFFF;
    private static final int WARNA_REDUP  = 0x888888;
    private static final int WARNA_ERR    = 0xE94560;

    private static final int[] BAR_COLORS = {
        0x9B59B6, 0x3498DB, 0x27AE60, 0xE67E22, 0xE74C3C
    };

    public ChartCanvas() {
        controller = new LaporanController();
        setFullScreenMode(true);
        muatData();
    }

    private void muatData() {
        try {
            topDiagnosa = controller.getTopDiagnosa(5);
            pesanError = "";
        } catch (Exception e) {
            topDiagnosa = new Vector();
            pesanError = e.getMessage() != null ? e.getMessage() : "Error";
        }
        repaint();
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        int cx = w / 2;

        // Header
        g.setColor(WARNA_HEADER);
        g.fillRect(0, 0, w, 36);
        g.setColor(WARNA_TEKS);
        g.setFont(fBesar);
        g.drawString("TOP 5 DIAGNOSA", cx, 6, Graphics.TOP | Graphics.HCENTER);

        int y = 44;

        if (pesanError.length() > 0) {
            g.setColor(WARNA_ERR);
            g.setFont(fKecil);
            g.drawString(pesanError, cx, y, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        if (topDiagnosa == null || topDiagnosa.size() == 0) {
            g.setColor(WARNA_REDUP);
            g.setFont(fKecil);
            g.drawString("Belum ada data diagnosa", cx, y + 20, Graphics.TOP | Graphics.HCENTER);
            return;
        }

        // Find max count for scaling
        int maxCount = 1;
        for (int i = 0; i < topDiagnosa.size(); i++) {
            String[] entry = (String[]) topDiagnosa.elementAt(i);
            int cnt = Integer.parseInt(entry[1]);
            if (cnt > maxCount) maxCount = cnt;
        }

        int maxBarW = w - 20;
        int barH = 24;
        int gap = 8;

        for (int i = 0; i < topDiagnosa.size(); i++) {
            String[] entry = (String[]) topDiagnosa.elementAt(i);
            String diagnosa = entry[0];
            int count = Integer.parseInt(entry[1]);

            int barW = (maxBarW * count) / maxCount;
            if (barW < 4) barW = 4;

            // Bar
            int color = BAR_COLORS[i % BAR_COLORS.length];
            g.setColor(color);
            g.fillRoundRect(10, y, barW, barH, 4, 4);

            // Label
            g.setColor(WARNA_TEKS);
            g.setFont(fKecil);
            StringBuffer labelBuf = new StringBuffer();
            labelBuf.append(i + 1).append(". ").append(diagnosa);
            String label = labelBuf.toString();
            if (label.length() > 20) {
                StringBuffer truncBuf = new StringBuffer();
                truncBuf.append(label.substring(0, 20)).append("..");
                label = truncBuf.toString();
            }
            g.drawString(label, 14, y + 4, Graphics.TOP | Graphics.LEFT);

            // Count on right
            StringBuffer countBuf = new StringBuffer();
            countBuf.append(String.valueOf(count)).append("x");
            g.drawString(countBuf.toString(),
                    w - 4, y + 4, Graphics.TOP | Graphics.RIGHT);

            y += barH + gap;
        }

        // Back hint
        g.setColor(WARNA_REDUP);
        g.setFont(fKecil);
        g.drawString("[0] Kembali", cx, h - fKecil.getHeight() - 4,
                Graphics.TOP | Graphics.HCENTER);
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == KEY_NUM0 || keyCode == -2) {
            ScreenManager.getInstance().kembali();
        }
    }
}
