package util.ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import model.Ruangan;
import util.ServiceFactory;

/**
 * KamarSelectionScreen — Tampilan identik dengan RuanganScreen di Dashboard,
 * namun berfungsi sebagai selector: klik kamar kosong = pilih & kembali ke form.
 * INHERITANCE: Extends Canvas.
 */
public class KamarSelectionScreen extends Canvas implements CommandListener {

    private PasienFormScreen parent;
    private Vector daftarRuangan;   // semua kamar (untuk ditampilkan)
    private int selectedIndex = 0;
    private int scrollOffset = 0;

    private Command cmdBatal;

    // Warna identik dengan RuanganScreen
    private static final int WARNA_BG            = 0xF0F4F8;
    private static final int WARNA_CARD          = 0xFFFFFF;
    private static final int WARNA_TEKS          = 0x333333;
    private static final int WARNA_TEKS_TERANG   = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP    = 0x888888;
    private static final int WARNA_AKSEN         = 0x4A90E2;
    private static final int WARNA_SELECTED      = 0xD0E1F9;
    private static final int WARNA_KOSONG        = 0x27AE60;   // Hijau
    private static final int WARNA_TERISI        = 0xE74C3C;   // Merah
    private static final int WARNA_MAINTENANCE   = 0xF39C12;   // Oranye
    private static final int WARNA_DISABLED_TEXT = 0xBBBBBB;   // Abu (kamar tidak kosong)

    public KamarSelectionScreen(PasienFormScreen parent) {
        this.parent = parent;
        setFullScreenMode(true);
        muatData();

        cmdBatal = new Command("Kembali", Command.BACK, 1);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    private void muatData() {
        try {
            daftarRuangan = ServiceFactory.getInstance().getRuanganService().getSemuaRuangan();
        } catch (Exception e) {
            daftarRuangan = new Vector();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().kembali();
        }
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        // Latar belakang
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

        // --- Title bar (identik dengan RuanganScreen) ---
        g.setColor(WARNA_AKSEN);
        g.fillRect(0, 0, w, 40);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontBesar);
        g.drawString("PILIH KAMAR", w / 2, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 50;

        if (daftarRuangan == null || daftarRuangan.size() == 0) {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontBesar);
            g.drawString("Belum ada data kamar", w / 2, h / 2, Graphics.TOP | Graphics.HCENTER);
        } else {
            // Info statistik (identik RuanganScreen)
            int kosong = 0, terisi = 0;
            for (int k = 0; k < daftarRuangan.size(); k++) {
                Ruangan r = (Ruangan) daftarRuangan.elementAt(k);
                if (r.isKosong()) kosong++;
                else terisi++;
            }
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            g.drawString("Total: " + daftarRuangan.size() + " | Terisi: " + terisi + " | Kosong: " + kosong,
                    10, y, Graphics.TOP | Graphics.LEFT);
            y += fontKecil.getHeight() + 8;

            int itemH = 65;
            int maxVisible = (h - y - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarRuangan.size() && i < scrollOffset + maxVisible; i++) {
                Ruangan r = (Ruangan) daftarRuangan.elementAt(i);
                boolean isKosong = r.isKosong();
                boolean isSelected = (i == selectedIndex);

                // Card background
                g.setColor(isSelected ? WARNA_SELECTED : WARNA_CARD);
                g.fillRoundRect(10, y, w - 20, itemH, 8, 8);

                // Indicator color block kiri (identik RuanganScreen)
                int statusColor = WARNA_KOSONG;
                if (r.getStatusKamar().equals(Ruangan.STATUS_TERISI)) statusColor = WARNA_TERISI;
                else if (r.getStatusKamar().equals(Ruangan.STATUS_MAINTENANCE)) statusColor = WARNA_MAINTENANCE;

                g.setColor(statusColor);
                g.fillRoundRect(10, y, 15, itemH, 8, 8);
                g.fillRect(18, y, 7, itemH);

                int textX = 35;

                // Nama + tipe
                g.setColor(isKosong ? WARNA_TEKS : WARNA_DISABLED_TEXT);
                g.setFont(fontBesar);
                g.drawString(r.getNamaRuangan() + " (" + r.getTipeKamar() + ")", textX, y + 5, Graphics.TOP | Graphics.LEFT);

                // Status line
                g.setColor(WARNA_TEKS_REDUP);
                g.setFont(fontKecil);
                String info = r.getStatusKamar();
                if (r.isTerisi()) info += " - " + r.getNamaPasien();
                g.drawString("Status: " + info, textX, y + 5 + fontBesar.getHeight() + 2, Graphics.TOP | Graphics.LEFT);

                // Badge "PILIH" hanya untuk kamar kosong
                if (isKosong) {
                    int btnW = 50;
                    int btnH = 25;
                    int btnX = w - 10 - btnW - 5;
                    int btnY = y + (itemH - btnH) / 2;

                    g.setColor(WARNA_KOSONG);
                    g.fillRoundRect(btnX, btnY, btnW, btnH, 6, 6);
                    g.setColor(WARNA_TEKS_TERANG);
                    g.setFont(fontKecil);
                    g.drawString("PILIH", btnX + btnW / 2, btnY + 5, Graphics.TOP | Graphics.HCENTER);
                }

                y += itemH + 4;
            }
        }

        // Footer hint (identik RuanganScreen)
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 25, w, 25);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        g.drawString("Ketuk kamar KOSONG untuk memilih", 10, h - 20, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (daftarRuangan == null) return;

        if (action == Canvas.UP && selectedIndex > 0) {
            selectedIndex--;
            if (selectedIndex < scrollOffset) scrollOffset = selectedIndex;
        } else if (action == Canvas.DOWN && selectedIndex < daftarRuangan.size() - 1) {
            selectedIndex++;
            int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
            int maxVisible = (getHeight() - startY - 30) / (65 + 4);
            if (selectedIndex >= scrollOffset + maxVisible) scrollOffset = selectedIndex - maxVisible + 1;
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
        } else if (action == Canvas.FIRE || keyCode == Canvas.KEY_NUM5) {
            pilihKamar(selectedIndex);
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        if (daftarRuangan == null) return;
        int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
        int itemH = 65;
        int maxVisible = (getHeight() - startY - 30) / (itemH + 4);

        for (int i = scrollOffset; i < daftarRuangan.size() && i < scrollOffset + maxVisible; i++) {
            int itemY = startY + (i - scrollOffset) * (itemH + 4);
            if (y >= itemY && y <= itemY + itemH) {
                selectedIndex = i;
                pilihKamar(i);
                repaint();
                return;
            }
        }
    }

    /** Pilih kamar jika statusnya KOSONG, lalu kembali ke form */
    private void pilihKamar(int index) {
        if (index < 0 || index >= daftarRuangan.size()) return;
        Ruangan r = (Ruangan) daftarRuangan.elementAt(index);
        if (!r.isKosong()) {
            // Kamar sudah terisi — tampilkan peringatan singkat
            Alert alert = new Alert("Kamar Tidak Tersedia",
                    r.getNamaRuangan() + " sudah terisi.\nSilakan pilih kamar lain.",
                    null, AlertType.WARNING);
            alert.setTimeout(2000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            return;
        }
        parent.setKamarTerpilih(r.getId(), r.getNamaRuangan() + " (" + r.getTipeKamar() + ")");
        ScreenManager.getInstance().kembali();
    }
}
