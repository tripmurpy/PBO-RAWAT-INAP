package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import model.Admisi;
import model.Pasien;
import model.Dokter;
import model.Ruangan;
import service.AdmisiService;
import util.ServiceFactory;
import util.DateUtil;

/**
 * AdmisiScreen — Menampilkan daftar pasien rawat inap.
 * INHERITANCE: Extends Canvas.
 */
public class AdmisiScreen extends Canvas implements CommandListener {

    private AdmisiService service;
    private Vector daftarAdmisiDetail;
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
    private static final int WARNA_INFO = 0x16A085;

    private class AdmisiDetail {
        String namaPasien;
        String noRM;
        String jk;
        String tlp;
        String tglMasuk;
        String kamar;
        String dokter;
        String noAntrian;
    }

    public AdmisiScreen() {
        this.service = ServiceFactory.getInstance().getAdmisiService();
        setFullScreenMode(true);
        daftarAdmisiDetail = new Vector();
        muatData();

        cmdKembali = new Command("Kembali", Command.BACK, 1);
        cmdBaru = new Command("Baru", Command.ITEM, 2);
        addCommand(cmdKembali);
        addCommand(cmdBaru);
        setCommandListener(this);
    }

    private void muatData() {
        daftarAdmisiDetail.removeAllElements();
        try {
            Vector admisiList = service.getAdmisiAktif();
            for (int i = 0; i < admisiList.size(); i++) {
                Admisi a = (Admisi) admisiList.elementAt(i);
                AdmisiDetail ad = new AdmisiDetail();
                
                Pasien p = null;
                Dokter dk = null;
                Ruangan rn = null;
                try {
                    p = ServiceFactory.getInstance().getPasienService().cariByRM(a.getNoRM());
                    dk = ServiceFactory.getInstance().getDokterService().cariById(a.getIdDokter());
                    rn = ServiceFactory.getInstance().getRuanganService().cariById(a.getIdRuangan());
                } catch (Exception ex) {}

                ad.noAntrian = "INP-" + (i < 9 ? "0" + (i+1) : String.valueOf(i+1));
                ad.namaPasien = p != null ? p.getNama() : "Unknown";
                ad.noRM = p != null ? p.getNoRM() : a.getNoRM();
                ad.jk = p != null ? p.getJenisKelamin() : "-";
                ad.tlp = p != null ? p.getNoTelp() : "-";
                ad.kamar = rn != null ? rn.getNamaRuangan() + " (" + rn.getTipeKamar() + ")" : "-";
                ad.dokter = dk != null ? dk.getNama() : "-";
                ad.tglMasuk = DateUtil.formatTanggal(a.getTglMasuk());

                daftarAdmisiDetail.addElement(ad);
            }
        } catch (Exception e) {}
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdBaru) {
            ScreenManager.getInstance().tampilkanLayar(new AdmisiBaruScreen());
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
        g.drawString("PASIEN RAWAT INAP", w / 2, 8, Graphics.TOP | Graphics.HCENTER);

        int y = 50;

        if (daftarAdmisiDetail.size() == 0) {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontSedang);
            g.drawString("Tidak ada pasien dirawat", w / 2, h / 2, Graphics.TOP | Graphics.HCENTER);
        } else {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            g.drawString("Total: " + daftarAdmisiDetail.size() + " pasien", 10, y, Graphics.TOP | Graphics.LEFT);
            y += fontKecil.getHeight() + 8;

            int itemH = 115; // Tall enough for all data
            int maxVisible = (h - y - 30) / (itemH + 4);

            for (int i = scrollOffset; i < daftarAdmisiDetail.size() && i < scrollOffset + maxVisible; i++) {
                AdmisiDetail ad = (AdmisiDetail) daftarAdmisiDetail.elementAt(i);

                g.setColor(i == selectedIndex ? WARNA_SELECTED : WARNA_CARD);
                g.fillRoundRect(10, y, w - 20, itemH, 8, 8);

                // Queue Box / Tag
                g.setColor(WARNA_INFO);
                g.fillRoundRect(10, y, 65, itemH, 8, 8);
                g.fillRect(65, y, 10, itemH); // square off right side
                
                g.setColor(WARNA_TEKS_TERANG);
                g.setFont(fontSedang);
                g.drawString(ad.noAntrian, 40, y + (itemH - fontSedang.getHeight())/2, Graphics.TOP | Graphics.HCENTER);

                // Info Pasien
                int startX = 85;
                g.setColor(WARNA_TEKS);
                g.setFont(fontBesar);
                g.drawString(ad.namaPasien + " (" + ad.jk + ")", startX, y + 5, Graphics.TOP | Graphics.LEFT);

                int textY = y + 5 + fontBesar.getHeight() + 4;

                g.setColor(WARNA_TEKS_REDUP);
                g.setFont(fontKecil);
                g.drawString("RM: " + ad.noRM + " | Telp: " + ad.tlp, startX, textY, Graphics.TOP | Graphics.LEFT);
                textY += fontKecil.getHeight() + 2;

                g.drawString("Tgl Masuk: " + ad.tglMasuk, startX, textY, Graphics.TOP | Graphics.LEFT);
                textY += fontKecil.getHeight() + 2;

                g.setColor(WARNA_INFO);
                g.drawString("Kamar: " + ad.kamar, startX, textY, Graphics.TOP | Graphics.LEFT);
                textY += fontKecil.getHeight() + 2;
                
                g.setColor(WARNA_AKSEN);
                g.drawString("Dokter Jaga: " + ad.dokter, startX, textY, Graphics.TOP | Graphics.LEFT);

                y += itemH + 4;
            }
        }
        
        // Footer (Label bantuan)
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 25, w, 25);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Pilih menu 'Baru' untuk mendaftarkan admisi", 10, h - 20, Graphics.TOP | Graphics.LEFT);
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == Canvas.UP && selectedIndex > 0) {
            selectedIndex--;
            if (selectedIndex < scrollOffset) scrollOffset = selectedIndex;
        } else if (action == Canvas.DOWN && daftarAdmisiDetail.size() > 0 && selectedIndex < daftarAdmisiDetail.size() - 1) {
            selectedIndex++;
            int startY = 50 + Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL).getHeight() + 8;
            int maxVisible = (getHeight() - startY - 30) / (115 + 4);
            if (selectedIndex >= scrollOffset + maxVisible) scrollOffset = selectedIndex - maxVisible + 1;
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
        }
        repaint();
    }
}
