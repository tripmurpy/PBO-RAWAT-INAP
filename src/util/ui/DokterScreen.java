package util.ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import service.DokterService;
import model.Dokter;
import util.ServiceFactory;

/**
 * DokterScreen — Menampilkan daftar dokter dalam format card dengan foto dummy.
 * INHERITANCE: Extends Canvas.
 */
public class DokterScreen extends Canvas implements CommandListener {

    private DokterService service;
    private Vector daftarDokter;
    private int selectedIndex = 0;
    private int scrollOffset = 0;

    private Command cmdKembali, cmdBaru, cmdHapus;

    private static final int WARNA_BG = 0xF0F4F8;
    private static final int WARNA_CARD = 0xFFFFFF;
    private static final int WARNA_TEKS = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x888888;
    private static final int WARNA_AKSEN = 0x4A90E2;
    private static final int WARNA_SELECTED = 0xD0E1F9;
    private static final int WARNA_HAPUS = 0xE94560;

    public DokterScreen() {
        this.service = ServiceFactory.getInstance().getDokterService();
        setFullScreenMode(true);
        muatData();

        cmdBaru = new Command("Tambah", Command.ITEM, 1);
        cmdHapus = new Command("Hapus", Command.ITEM, 2);
        cmdKembali = new Command("Kembali", Command.BACK, 3);
        
        addCommand(cmdBaru);
        addCommand(cmdHapus);
        addCommand(cmdKembali);
        setCommandListener(this);
    }

    private void muatData() {
        try {
            daftarDokter = service.getSemuaDokter();
        } catch (Exception e) {
            daftarDokter = new Vector();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdBaru) {
            ScreenManager.getInstance().tampilkanLayar(new DokterFormScreen());
        } else if (c == cmdHapus) {
            if (daftarDokter != null && selectedIndex >= 0 && selectedIndex < daftarDokter.size()) {
                konfirmasiHapus((Dokter) daftarDokter.elementAt(selectedIndex));
            }
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
        g.drawString("MANAJEMEN DOKTER", w / 2, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 50;

        if (daftarDokter == null || daftarDokter.size() == 0) {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontSedang);
            g.drawString("Belum ada data dokter", w / 2, h / 2, Graphics.TOP | Graphics.HCENTER);
        } else {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            g.drawString("Total: " + daftarDokter.size() + " dokter", 10, y, Graphics.TOP | Graphics.LEFT);
            y += fontKecil.getHeight() + 8;

            int itemH = 75;
            int maxVisible = (h - y - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarDokter.size() && i < scrollOffset + maxVisible; i++) {
                Dokter dk = (Dokter) daftarDokter.elementAt(i);

                g.setColor(i == selectedIndex ? WARNA_SELECTED : WARNA_CARD);
                g.fillRoundRect(10, y, w - 20, itemH, 8, 8);

                // Draw Dummy Photo Box
                g.setColor(0xBDC3C7); // Silver
                g.fillRoundRect(15, y + 10, 50, 55, 10, 10);
                // Draw icon shape (head and shoulders)
                g.setColor(WARNA_CARD);
                g.fillArc(25, y + 15, 30, 30, 0, 360);
                g.fillArc(15, y + 45, 50, 40, 0, 180);

                int textX = 75;
                g.setColor(WARNA_TEKS);
                g.setFont(fontBesar);
                g.drawString(dk.getNama(), textX, y + 8, Graphics.TOP | Graphics.LEFT);

                g.setColor(WARNA_AKSEN);
                g.setFont(fontKecil);
                g.drawString(dk.getSpesialisasi(), textX, y + 8 + fontBesar.getHeight() + 2, Graphics.TOP | Graphics.LEFT);

                g.setColor(WARNA_TEKS_REDUP);
                g.drawString("Jadwal: " + dk.getJadwal(), textX, y + 8 + fontBesar.getHeight() + fontKecil.getHeight() + 4, Graphics.TOP | Graphics.LEFT);

                // Tombol Hapus di kanan card
                int btnW = 50;
                int btnH = 25;
                int btnX = w - 10 - btnW - 5;
                int btnY = y + (itemH - btnH) / 2;

                g.setColor(WARNA_HAPUS);
                g.fillRoundRect(btnX, btnY, btnW, btnH, 6, 6);
                g.setColor(WARNA_TEKS_TERANG);
                g.drawString("HAPUS", btnX + btnW / 2, btnY + 5, Graphics.TOP | Graphics.HCENTER);

                y += itemH + 4;
            }
        }
        
        // Footer (Label bantuan)
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 25, w, 25);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Pilih item & tekan OK untuk Hapus", 10, h - 20, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == Canvas.UP && selectedIndex > 0) {
            selectedIndex--;
            if (selectedIndex < scrollOffset) scrollOffset = selectedIndex;
        } else if (action == Canvas.DOWN && daftarDokter != null && selectedIndex < daftarDokter.size() - 1) {
            selectedIndex++;
            int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
            int maxVisible = (getHeight() - startY - 30) / (75 + 4);
            if (selectedIndex >= scrollOffset + maxVisible) scrollOffset = selectedIndex - maxVisible + 1;
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
        } else if (action == Canvas.FIRE || keyCode == Canvas.KEY_NUM5) {
            if (daftarDokter != null && selectedIndex >= 0 && selectedIndex < daftarDokter.size()) {
                konfirmasiHapus((Dokter) daftarDokter.elementAt(selectedIndex));
            }
        }
        repaint();
    }
    
    protected void pointerPressed(int x, int y) {
        if (daftarDokter == null) return;
        int w = getWidth();
        int h = getHeight();
        int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
        int itemH = 75;
        int maxVisible = (h - startY - 30) / (itemH + 4);

        for (int i = scrollOffset; i < daftarDokter.size() && i < scrollOffset + maxVisible; i++) {
            int itemY = startY + (i - scrollOffset) * (itemH + 4);
            if (y >= itemY && y <= itemY + itemH) {
                selectedIndex = i;
                if (x >= w - 70) {
                    konfirmasiHapus((Dokter) daftarDokter.elementAt(i));
                }
                repaint();
                return;
            }
        }
    }

    private void konfirmasiHapus(final Dokter d) {
        Alert alert = new Alert("KONFIRMASI",
                "Hapus dokter " + d.getNama() + "?",
                null, AlertType.CONFIRMATION);
        final Command cmdYa = new Command("Ya", Command.OK, 1);
        final Command cmdTidak = new Command("Tidak", Command.CANCEL, 2);
        alert.addCommand(cmdYa);
        alert.addCommand(cmdTidak);
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable disp) {
                if (c == cmdYa) {
                    try {
                        service.hapusDokter(d.getRecordId());
                        muatData();
                        repaint();
                        ScreenManager.getInstance().getDisplay().setCurrent(DokterScreen.this);
                    } catch (Exception e) {}
                } else {
                    ScreenManager.getInstance().getDisplay().setCurrent(DokterScreen.this);
                }
            }
        });
        ScreenManager.getInstance().getDisplay().setCurrent(alert);
    }
}
