package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import model.Pasien;
import util.DateUtil;
import service.PasienService;
import util.ServiceFactory;

/**
 * PasienListScreen — Menampilkan daftar semua pasien.
 * INHERITANCE: Extends Canvas.
 */
public class PasienListScreen extends Canvas implements CommandListener {

    private PasienService service;
    private Vector daftarPasien;
    private int selectedIndex = 0;
    private int scrollOffset = 0;
    private String pesanError = "";

    private Command cmdHapus, cmdKembali;

    private static final int WARNA_BG = 0xF0F4F8;
    private static final int WARNA_CARD = 0xFFFFFF;
    private static final int WARNA_TEKS = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x888888;
    private static final int WARNA_AKSEN = 0x4A90E2;
    private static final int WARNA_SELECTED = 0xD0E1F9;
    private static final int WARNA_HAPUS = 0xE94560;
    private static final int WARNA_QUEUE = 0x27AE60; // Bright green for queue number

    public PasienListScreen() {
        this.service = ServiceFactory.getInstance().getPasienService();
        setFullScreenMode(true);
        muatData();

        cmdHapus = new Command("Hapus", Command.ITEM, 1);
        cmdKembali = new Command("Kembali", Command.BACK, 2);
        addCommand(cmdHapus);
        addCommand(cmdKembali);
        setCommandListener(this);
    }

    private void muatData() {
        try {
            daftarPasien = service.getSemuaPasien();
        } catch (Exception e) {
            daftarPasien = new Vector();
            pesanError = new StringBuffer().append("Gagal memuat data: ").append(e.getMessage()).toString();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdHapus) {
            if (daftarPasien != null && selectedIndex >= 0 && selectedIndex < daftarPasien.size()) {
                konfirmasiHapus((Pasien) daftarPasien.elementAt(selectedIndex));
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
        g.drawString("ANTRIAN PASIEN", w / 2, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 50;

        if (daftarPasien == null || daftarPasien.size() == 0) {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontSedang);
            g.drawString("Belum ada data pasien", w / 2, h / 2,
                    Graphics.TOP | Graphics.HCENTER);
        } else {
            // Info jumlah
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            g.drawString(new StringBuffer().append("Total: ").append(daftarPasien.size()).append(" pasien").toString(),
                    15, y,
                    Graphics.TOP | Graphics.LEFT);
            y += fontKecil.getHeight() + 8;

            // List pasien
            int itemH = 60; // Taller for queue card
            int maxVisible = (h - y - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarPasien.size() &&
                    i < scrollOffset + maxVisible; i++) {
                Pasien p = (Pasien) daftarPasien.elementAt(i);

                g.setColor(i == selectedIndex ? WARNA_SELECTED : WARNA_CARD);
                g.fillRoundRect(10, y, w - 20, itemH, 8, 8);

                // Draw Queue Number Box
                g.setColor(WARNA_QUEUE);
                g.fillRoundRect(10, y, 50, itemH, 8, 8);
                // Fix right corners of queue box to connect with card
                g.fillRect(50, y, 10, itemH);
                
                g.setColor(WARNA_TEKS_TERANG);
                g.setFont(fontBesar);
                String qNum = (i < 9) ? "A0" + (i + 1) : "A" + (i + 1);
                g.drawString(qNum, 35, y + (itemH - fontBesar.getHeight()) / 2, Graphics.TOP | Graphics.HCENTER);

                // Draw Patient Info
                g.setColor(WARNA_TEKS);
                g.setFont(fontSedang);
                g.drawString(p.getNama(), 70, y + 4, Graphics.TOP | Graphics.LEFT);

                g.setColor(WARNA_TEKS_REDUP);
                g.setFont(fontKecil);
                g.drawString(new StringBuffer().append("Telp: ").append(p.getNoTelp()).toString(),
                        70, y + 4 + fontSedang.getHeight(), Graphics.TOP | Graphics.LEFT);
                        
                // Info Dokter/Ruangan (Mockup)
                g.setColor(WARNA_AKSEN);
                g.drawString("Dokter: Belum | R. Poli",
                        70, y + 6 + fontSedang.getHeight() + fontKecil.getHeight(), Graphics.TOP | Graphics.LEFT);

                // Tombol Set Dokter di kanan
                int btnW = 55;
                int btnH = 25;
                int btnX = w - 10 - btnW - 5;
                int btnYPos = y + (itemH - btnH) / 2;

                g.setColor(WARNA_AKSEN);
                g.fillRoundRect(btnX, btnYPos, btnW, btnH, 6, 6);
                g.setColor(WARNA_TEKS_TERANG);
                g.setFont(fontKecil);
                g.drawString("DOKTER", btnX + btnW / 2, btnYPos + 5, Graphics.TOP | Graphics.HCENTER);

                y += itemH + 4;
            }
        }

        // Footer (Label bantuan)
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 25, w, 25);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Pilih item & tekan OK untuk Assign Dokter", 10, h - 20, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == Canvas.UP && selectedIndex > 0) {
            selectedIndex--;
            if (selectedIndex < scrollOffset)
                scrollOffset = selectedIndex;
        } else if (action == Canvas.DOWN && daftarPasien != null &&
                selectedIndex < daftarPasien.size() - 1) {
            selectedIndex++;
            // Scroll down if needed
            int h = getHeight();
            int itemH = 45;
            int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
            int maxVisible = (h - startY - 30) / (itemH + 4);
            if (selectedIndex >= scrollOffset + maxVisible) {
                scrollOffset = selectedIndex - maxVisible + 1;
            }
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
        } else if (action == Canvas.FIRE || keyCode == Canvas.KEY_NUM5) {
            if (daftarPasien != null && selectedIndex >= 0 && selectedIndex < daftarPasien.size()) {
                bukaFormDokter((Pasien) daftarPasien.elementAt(selectedIndex));
            }
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        int w = getWidth();
        int h = getHeight();

        // Cek klik pada list items
        int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
        int itemH = 45;
        int gap = 4;

        if (daftarPasien != null) {
            int h2 = getHeight();
            int maxVisible = (h2 - startY - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarPasien.size() && i < scrollOffset + maxVisible; i++) {
                int itemY = startY + (i - scrollOffset) * (itemH + gap);
                if (y >= itemY && y <= itemY + itemH) {
                    selectedIndex = i;

                    // Hit area tombol DOKTER lebih lebar (seluruh sisi kanan item)
                    int triggerX = w - 70; // Lebih lebar dari visual tombol
                    if (x >= triggerX) {
                        bukaFormDokter((Pasien) daftarPasien.elementAt(i));
                    }

                    repaint();
                    return;
                }
            }
        }
    }

    private void bukaFormDokter(final Pasien p) {
        final Form form = new Form("Assign Dokter - " + p.getNama());
        final ChoiceGroup cgDokter = new ChoiceGroup("Pilih Dokter", ChoiceGroup.EXCLUSIVE);
        
        try {
            java.util.Vector listDokter = ServiceFactory.getInstance().getDokterService().getSemuaDokter();
            for (int i = 0; i < listDokter.size(); i++) {
                model.Dokter dk = (model.Dokter) listDokter.elementAt(i);
                cgDokter.append(dk.getNama() + " (" + dk.getSpesialisasi() + ")", null);
            }
        } catch (Exception e) {}
        
        form.append(cgDokter);
        
        final Command cmdSimpan = new Command("Simpan", Command.OK, 1);
        final Command cmdBatalDokter = new Command("Batal", Command.BACK, 2);
        form.addCommand(cmdSimpan);
        form.addCommand(cmdBatalDokter);
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c == cmdSimpan) {
                    // Implementasi simpan (mockup visual)
                    ScreenManager.getInstance().getDisplay().setCurrent(PasienListScreen.this);
                } else if (c == cmdBatalDokter) {
                    ScreenManager.getInstance().getDisplay().setCurrent(PasienListScreen.this);
                }
            }
        });
        
        ScreenManager.getInstance().getDisplay().setCurrent(form);
    }

    private void konfirmasiHapus(final Pasien p) {
        Alert alert = new Alert("KONFIRMASI",
                new StringBuffer().append("Hapus pasien ").append(p.getNama()).append("?").toString(),
                null, AlertType.CONFIRMATION);
        final Command cmdYa = new Command("Ya", Command.OK, 1);
        final Command cmdTidak = new Command("Tidak", Command.CANCEL, 2);
        alert.addCommand(cmdYa);
        alert.addCommand(cmdTidak);
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c == cmdYa) {
                    try {
                        service.hapusPasien(p.getRecordId());
                        muatData();
                        repaint();
                        ScreenManager.getInstance().getDisplay().setCurrent(PasienListScreen.this);
                    } catch (Exception e) {
                        Alert errorAlert = new Alert("ERROR",
                                new StringBuffer().append("Gagal hapus: ").append(e.getMessage()).toString(), null,
                                AlertType.ERROR);
                        ScreenManager.getInstance().getDisplay().setCurrent(errorAlert, PasienListScreen.this);
                    }
                } else {
                    ScreenManager.getInstance().getDisplay().setCurrent(PasienListScreen.this);
                }
            }
        });
        ScreenManager.getInstance().getDisplay().setCurrent(alert);
    }
}
