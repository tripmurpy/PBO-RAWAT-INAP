package util.ui;

import javax.microedition.lcdui.*;
import model.Pasien;
import model.Dokter;
import model.Ruangan;
import service.DokterService;
import service.RuanganService;
import util.DateUtil;
import util.ServiceFactory;
import java.util.Vector;

/**
 * PasienDetailScreen — Menampilkan detail lengkap data pasien.
 * INHERITANCE: Extends Canvas (J2ME LCDUI).
 */
public class PasienDetailScreen extends Canvas {

    private Pasien pasien;
    private Dokter dokter;
    private Ruangan ruangan;
    private int scrollY = 0;
    private int contentHeight = 0;

    private int btnKeluarX, btnKeluarY, btnKeluarW, btnKeluarH;
    private boolean showBtnKeluar = false;

    // Pro Colors
    private static final int WARNA_BG = 0xF0F4F8;
    private static final int WARNA_CARD = 0xFFFFFF;
    private static final int WARNA_HEADER = 0x1A1A2E;
    private static final int WARNA_TEKS = 0x333333;
    private static final int WARNA_TEKS_TERANG = 0xFFFFFF;
    private static final int WARNA_TEKS_REDUP = 0x999999;
    private static final int WARNA_LABEL = 0x777777;
    private static final int WARNA_AKSEN = 0x4A90E2;
    private static final int WARNA_GARIS = 0xE8E8E8;
    private static final int WARNA_STATUS_AKTIF = 0x27AE60;
    private static final int WARNA_STATUS_DIRAWAT = 0xE67E22;
    private static final int WARNA_STATUS_PULANG = 0x95A5A6;
    private static final int WARNA_DOKTER_BG = 0xEBF5FB;
    private static final int WARNA_DOKTER_BORDER = 0x3498DB;
    private static final int WARNA_RUANGAN_BG = 0xFDF2E9;
    private static final int WARNA_RUANGAN_BORDER = 0xE67E22;

    public PasienDetailScreen(Pasien pasien) {
        this.pasien = pasien;
        setFullScreenMode(true);
        muatDataTerkait();
    }

    private void muatDataTerkait() {
        String namaDokter = pasien.getDokterPenanggungJawab();
        if (namaDokter != null && namaDokter.length() > 0) {
            try {
                Vector v = ServiceFactory.getInstance().getDokterService().getSemuaDokter();
                for (int i = 0; i < v.size(); i++) {
                    Dokter d = (Dokter) v.elementAt(i);
                    if (d.getNama().equals(namaDokter)) { this.dokter = d; break; }
                }
            } catch (Exception e) {}
        }
        String namaKamar = pasien.getKamarRawat();
        if (namaKamar != null && namaKamar.length() > 0) {
            try {
                Vector v = ServiceFactory.getInstance().getRuanganService().getSemuaRuangan();
                for (int i = 0; i < v.size(); i++) {
                    Ruangan r = (Ruangan) v.elementAt(i);
                    if (r.getNamaRuangan().equals(namaKamar)) { this.ruangan = r; break; }
                }
            } catch (Exception e) {}
        }
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedangBold = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fontKecilBold = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        int barH = 38;
        g.setColor(WARNA_HEADER);
        g.fillRect(0, 0, w, barH);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontSedangBold);
        g.drawString("Detail Pasien", w / 2, (barH - fontSedangBold.getHeight()) / 2, Graphics.TOP | Graphics.HCENTER);
        g.setFont(fontKecil);
        g.drawString("<", 10, (barH - fontKecil.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);

        int margin = 12;
        int cardW = w - (margin * 2);
        int y = barH + 8 - scrollY;

        // CARD 1: Identitas
        int rowH = fontKecil.getHeight() + 8;
        int infoRows = 9; // Added 2 for Wali info
        int card1H = 60 + (rowH * infoRows) + 20;

        g.setColor(WARNA_CARD);
        g.fillRoundRect(margin, y, cardW, card1H, 10, 10);

        int cx = margin + 12;
        int cw = cardW - 24;
        
        g.setColor(WARNA_AKSEN);
        g.fillRoundRect(cx, y + 10, 40, 40, 40, 40);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontBesar);
        g.drawString(pasien.getNama().substring(0, 1).toUpperCase(), cx + 20, y + 10 + (40 - fontBesar.getHeight()) / 2, Graphics.TOP | Graphics.HCENTER);

        g.setColor(WARNA_TEKS);
        g.setFont(fontSedangBold);
        g.drawString(pasien.getNama(), cx + 50, y + 12, Graphics.TOP | Graphics.LEFT);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("No. RM: " + pasien.getNoRM(), cx + 50, y + 12 + fontSedangBold.getHeight() + 2, Graphics.TOP | Graphics.LEFT);

        int curY = y + 60;
        g.setColor(WARNA_GARIS);
        g.drawLine(cx, curY, cx + cw, curY);
        curY += 8;

        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Tgl Lahir", DateUtil.formatTanggal(pasien.getTglLahir()));
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Gender", pasien.getJenisKelamin());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Alamat", pasien.getAlamat());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Telepon", pasien.getNoTelp());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Nama Wali", pasien.getNamaWali());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Telp Wali", pasien.getNoTelpWali());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Asuransi", pasien.getAsuransi());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Keluhan", pasien.getKeluhan());
        curY = drawRow(g, fontKecilBold, fontKecil, cx, cw, curY, "Status", pasien.getStatus());

        y += card1H + 8;

        // CARD 2: Dokter
        int card2H = 50;
        g.setColor(WARNA_DOKTER_BG);
        g.fillRoundRect(margin, y, cardW, card2H, 8, 8);
        g.setColor(WARNA_DOKTER_BORDER);
        g.fillRect(margin, y, 4, card2H);
        g.setFont(fontKecilBold);
        g.drawString("Dokter Penanggung Jawab", cx + 4, y + 6, Graphics.TOP | Graphics.LEFT);
        g.setColor(WARNA_TEKS);
        g.setFont(fontKecil);
        g.drawString(pasien.getDokterPenanggungJawab(), cx + 4, y + 6 + fontKecilBold.getHeight() + 4, Graphics.TOP | Graphics.LEFT);

        y += card2H + 8;

        // CARD 3: Ruangan
        int card3H = 50;
        g.setColor(WARNA_RUANGAN_BG);
        g.fillRoundRect(margin, y, cardW, card3H, 8, 8);
        g.setColor(WARNA_RUANGAN_BORDER);
        g.fillRect(margin, y, 4, card3H);
        g.setFont(fontKecilBold);
        g.drawString("Ruangan / Kamar", cx + 4, y + 6, Graphics.TOP | Graphics.LEFT);
        g.setColor(WARNA_TEKS);
        g.setFont(fontKecil);
        String rInfo = pasien.getKamarRawat();
        if (ruangan != null) rInfo += " (" + ruangan.getTipeKamar() + ") - Rp " + formatHarga(ruangan.getHarga());
        g.drawString(rInfo, cx + 4, y + 6 + fontKecilBold.getHeight() + 4, Graphics.TOP | Graphics.LEFT);

        y += card3H + 12;

        // BUTTON: Discharge
        if (!Pasien.STATUS_PULANG.equals(pasien.getStatus())) {
            showBtnKeluar = true;
            btnKeluarX = margin; btnKeluarY = y; btnKeluarW = cardW; btnKeluarH = 40;
            g.setColor(0xE74C3C);
            g.fillRoundRect(btnKeluarX, btnKeluarY, btnKeluarW, btnKeluarH, 10, 10);
            g.setColor(WARNA_TEKS_TERANG);
            g.setFont(fontSedangBold);
            g.drawString("PASIEN KELUAR", btnKeluarX + btnKeluarW / 2, btnKeluarY + 10, Graphics.TOP | Graphics.HCENTER);
            y += 50;
        } else {
            showBtnKeluar = false;
        }

        contentHeight = y + scrollY;
    }

    private int drawRow(Graphics g, Font fL, Font fV, int x, int w, int y, String label, String val) {
        g.setColor(WARNA_LABEL); g.setFont(fL); g.drawString(label, x, y, Graphics.TOP | Graphics.LEFT);
        g.setColor(WARNA_TEKS); g.setFont(fV); g.drawString(val != null ? val : "-", x + w / 2, y, Graphics.TOP | Graphics.LEFT);
        return y + fL.getHeight() + 6;
    }

    private String formatHarga(double harga) {
        String s = String.valueOf((long) harga);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (s.length() - i) % 3 == 0) sb.append('.');
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        if (action == Canvas.UP) scrollY = Math.max(0, scrollY - 30);
        else if (action == Canvas.DOWN) scrollY = Math.min(contentHeight - getHeight(), scrollY + 30);
        else if (keyCode == Canvas.KEY_STAR) ScreenManager.getInstance().kembali();
        else if (action == Canvas.FIRE && showBtnKeluar) bukaPasienKeluar();
        repaint();
    }

    protected void pointerPressed(int px, int py) {
        if (py < 38) ScreenManager.getInstance().kembali();
        else if (showBtnKeluar && px >= btnKeluarX && px <= btnKeluarX + btnKeluarW && py >= btnKeluarY && py <= btnKeluarY + btnKeluarH) bukaPasienKeluar();
    }

    private void bukaPasienKeluar() {
        ScreenManager.getInstance().tampilkanLayar(new PasienKeluarScreen(pasien, dokter, ruangan));
    }
}
