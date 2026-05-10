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
 * 
 * INHERITANCE: Extends Canvas (J2ME LCDUI).
 * Menampilkan kartu detail pasien dengan info dokter dan ruangan.
 */
public class PasienDetailScreen extends Canvas {

    private Pasien pasien;
    private Dokter dokter;
    private Ruangan ruangan;
    private int scrollY = 0;
    private int contentHeight = 0;

    // Posisi tombol Pasien Keluar (untuk deteksi tap)
    private int btnKeluarX, btnKeluarY, btnKeluarW, btnKeluarH;
    private boolean showBtnKeluar = false;

    // Warna — Clean minimal palette
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
    private static final int WARNA_BADGE_BG = 0xE8F8F5;
    private static final int WARNA_BADGE_TEKS = 0x1ABC9C;

    public PasienDetailScreen(Pasien pasien) {
        this.pasien = pasien;
        setFullScreenMode(true);
        muatDataTerkait();
    }

    /**
     * Memuat data dokter dan ruangan terkait pasien.
     */
    private void muatDataTerkait() {
        // Cari dokter berdasarkan nama yang disimpan di pasien
        String namaDokter = pasien.getDokterPenanggungJawab();
        if (namaDokter != null && namaDokter.length() > 0) {
            try {
                DokterService ds = ServiceFactory.getInstance().getDokterService();
                Vector semuaDokter = ds.getSemuaDokter();
                for (int i = 0; i < semuaDokter.size(); i++) {
                    Dokter d = (Dokter) semuaDokter.elementAt(i);
                    if (d.getNama().equals(namaDokter)) {
                        this.dokter = d;
                        break;
                    }
                }
            } catch (Exception e) {
                // Gagal memuat data dokter — lanjut tanpa
            }
        }

        // Cari ruangan berdasarkan nama kamar yang disimpan di pasien
        String namaKamar = pasien.getKamarRawat();
        if (namaKamar != null && namaKamar.length() > 0) {
            try {
                RuanganService rs = ServiceFactory.getInstance().getRuanganService();
                Vector semuaRuangan = rs.getSemuaRuangan();
                for (int i = 0; i < semuaRuangan.size(); i++) {
                    Ruangan r = (Ruangan) semuaRuangan.elementAt(i);
                    if (r.getNamaRuangan().equals(namaKamar)) {
                        this.ruangan = r;
                        break;
                    }
                }
            } catch (Exception e) {
                // Gagal memuat data ruangan — lanjut tanpa
            }
        }
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        Font fontBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fontSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fontSedangBold = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        Font fontKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fontKecilBold = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

        // Background
        g.setColor(WARNA_BG);
        g.fillRect(0, 0, w, h);

        // === TOP BAR ===
        int barH = 38;
        g.setColor(WARNA_HEADER);
        g.fillRect(0, 0, w, barH);

        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontSedangBold);
        g.drawString("Detail Pasien", w / 2, (barH - fontSedangBold.getHeight()) / 2,
                Graphics.TOP | Graphics.HCENTER);

        // Back indicator
        g.setFont(fontKecil);
        g.drawString("<", 10, (barH - fontKecil.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);

        int margin = 12;
        int cardW = w - (margin * 2);
        int y = barH + 8 - scrollY;

        // ======================================================
        // CARD 1: Identitas Pasien (main card)
        // ======================================================
        int card1StartY = y;
        int card1ContentH = 0;

        // Avatar circle + Name section
        int avatarSize = 40;
        int nameSecH = avatarSize + 16;
        card1ContentH += nameSecH;

        // Info rows: NoRM, TTL, JK, Alamat, Telp, Asuransi = 6 rows
        int rowH = fontKecil.getHeight() + 8;
        int infoRows = 6;
        card1ContentH += (rowH * infoRows) + 8; // +8 padding
        // Status badge
        card1ContentH += 20;

        int card1H = card1ContentH + 12;

        // Draw card background
        g.setColor(WARNA_CARD);
        g.fillRoundRect(margin, y, cardW, card1H, 10, 10);

        int cx = margin + 12; // content x
        int cw = cardW - 24; // content width
        y += 10;

        // --- Avatar circle ---
        int avatarX = cx;
        int avatarY = y;
        g.setColor(WARNA_AKSEN);
        g.fillRoundRect(avatarX, avatarY, avatarSize, avatarSize, avatarSize, avatarSize);

        // Initial letter in avatar
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontBesar);
        String initial = pasien.getNama() != null && pasien.getNama().length() > 0
                ? pasien.getNama().substring(0, 1).toUpperCase()
                : "?";
        g.drawString(initial, avatarX + avatarSize / 2,
                avatarY + (avatarSize - fontBesar.getHeight()) / 2,
                Graphics.TOP | Graphics.HCENTER);

        // --- Name + Status beside avatar ---
        int nameX = avatarX + avatarSize + 10;
        g.setColor(WARNA_TEKS);
        g.setFont(fontSedangBold);
        g.drawString(pasien.getNama() != null ? pasien.getNama() : "-", nameX, avatarY + 2,
                Graphics.TOP | Graphics.LEFT);

        // NoRM below name
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString(new StringBuffer().append("No. RM: ").append(
                pasien.getNoRM() != null ? pasien.getNoRM() : "-").toString(),
                nameX, avatarY + fontSedangBold.getHeight() + 4,
                Graphics.TOP | Graphics.LEFT);

        // Status badge below NoRM
        String statusText = pasien.getStatus() != null ? pasien.getStatus() : "AKTIF";
        int statusColor;
        if (Pasien.STATUS_DIRAWAT.equals(statusText)) {
            statusColor = WARNA_STATUS_DIRAWAT;
        } else if (Pasien.STATUS_PULANG.equals(statusText)) {
            statusColor = WARNA_STATUS_PULANG;
        } else {
            statusColor = WARNA_STATUS_AKTIF;
        }
        int badgeW = fontKecil.stringWidth(statusText) + 14;
        int badgeH = fontKecil.getHeight() + 4;
        int badgeY = avatarY + fontSedangBold.getHeight() + fontKecil.getHeight() + 6;
        g.setColor(statusColor);
        g.fillRoundRect(nameX, badgeY, badgeW, badgeH, badgeH, badgeH);
        g.setColor(WARNA_TEKS_TERANG);
        g.setFont(fontKecil);
        g.drawString(statusText, nameX + 7, badgeY + 2, Graphics.TOP | Graphics.LEFT);

        y = avatarY + avatarSize + 14;

        // --- Divider ---
        g.setColor(WARNA_GARIS);
        g.drawLine(cx, y, cx + cw, y);
        y += 6;

        // --- Info rows ---
        y = drawInfoRow(g, fontKecilBold, fontKecil, cx, cw, y, "Tanggal Lahir",
                pasien.getTglLahir() > 0 ? DateUtil.formatTanggal(pasien.getTglLahir()) : "-");
        y = drawInfoRow(g, fontKecilBold, fontKecil, cx, cw, y, "Jenis Kelamin",
                safeStr(pasien.getJenisKelamin(), "-"));
        y = drawInfoRow(g, fontKecilBold, fontKecil, cx, cw, y, "Alamat",
                safeStr(pasien.getAlamat(), "-"));
        y = drawInfoRow(g, fontKecilBold, fontKecil, cx, cw, y, "Telepon",
                safeStr(pasien.getNoTelp(), "-"));
        y = drawInfoRow(g, fontKecilBold, fontKecil, cx, cw, y, "Asuransi",
                safeStr(pasien.getAsuransi(), "-"));

        y += 6; // end padding of card 1

        y = card1StartY + card1H + 8;

        // ======================================================
        // CARD 2: Dokter Penanggung Jawab
        // ======================================================
        int card2H;
        if (dokter != null) {
            card2H = 70;
        } else {
            card2H = 44;
        }

        // Card background with left border accent
        g.setColor(WARNA_DOKTER_BG);
        g.fillRoundRect(margin, y, cardW, card2H, 10, 10);
        // Left accent bar
        g.setColor(WARNA_DOKTER_BORDER);
        g.fillRoundRect(margin, y, 4, card2H, 4, 4);

        int dcy = y + 8;
        g.setColor(WARNA_DOKTER_BORDER);
        g.setFont(fontKecilBold);
        g.drawString("Dokter Penanggung Jawab", cx + 4, dcy, Graphics.TOP | Graphics.LEFT);
        dcy += fontKecilBold.getHeight() + 4;

        if (dokter != null) {
            g.setColor(WARNA_TEKS);
            g.setFont(fontSedangBold);
            g.drawString(new StringBuffer().append("dr. ").append(dokter.getNama()).toString(),
                    cx + 4, dcy, Graphics.TOP | Graphics.LEFT);
            dcy += fontSedangBold.getHeight() + 2;

            g.setColor(WARNA_LABEL);
            g.setFont(fontKecil);
            StringBuffer spBuf = new StringBuffer();
            spBuf.append("Sp. ").append(dokter.getSpesialisasi());
            if (dokter.getJadwal() != null && dokter.getJadwal().length() > 0
                    && !dokter.getJadwal().equals("-")) {
                spBuf.append(" | ").append(dokter.getJadwal());
            }
            g.drawString(spBuf.toString(), cx + 4, dcy, Graphics.TOP | Graphics.LEFT);
        } else {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            String dLabel = safeStr(pasien.getDokterPenanggungJawab(), "");
            if (dLabel.length() > 0) {
                g.drawString(dLabel, cx + 4, dcy, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawString("Belum ditentukan", cx + 4, dcy, Graphics.TOP | Graphics.LEFT);
            }
        }

        y += card2H + 8;

        // ======================================================
        // CARD 3: Ruangan / Kamar
        // ======================================================
        int card3H;
        if (ruangan != null) {
            card3H = 70;
        } else {
            card3H = 44;
        }

        g.setColor(WARNA_RUANGAN_BG);
        g.fillRoundRect(margin, y, cardW, card3H, 10, 10);
        // Left accent bar
        g.setColor(WARNA_RUANGAN_BORDER);
        g.fillRoundRect(margin, y, 4, card3H, 4, 4);

        int rcy = y + 8;
        g.setColor(WARNA_RUANGAN_BORDER);
        g.setFont(fontKecilBold);
        g.drawString("Ruangan / Kamar", cx + 4, rcy, Graphics.TOP | Graphics.LEFT);
        rcy += fontKecilBold.getHeight() + 4;

        if (ruangan != null) {
            g.setColor(WARNA_TEKS);
            g.setFont(fontSedangBold);
            g.drawString(ruangan.getNamaRuangan(), cx + 4, rcy, Graphics.TOP | Graphics.LEFT);
            rcy += fontSedangBold.getHeight() + 2;

            g.setColor(WARNA_LABEL);
            g.setFont(fontKecil);
            StringBuffer rBuf = new StringBuffer();
            rBuf.append(ruangan.getTipeKamar());
            if (ruangan.getHarga() > 0) {
                rBuf.append(" | Rp ").append(formatHarga(ruangan.getHarga()));
                rBuf.append("/malam");
            }
            g.drawString(rBuf.toString(), cx + 4, rcy, Graphics.TOP | Graphics.LEFT);
        } else {
            g.setColor(WARNA_TEKS_REDUP);
            g.setFont(fontKecil);
            String kLabel = safeStr(pasien.getKamarRawat(), "");
            if (kLabel.length() > 0) {
                g.drawString(kLabel, cx + 4, rcy, Graphics.TOP | Graphics.LEFT);
            } else {
                g.drawString("Belum ditentukan", cx + 4, rcy, Graphics.TOP | Graphics.LEFT);
            }
        }

        y += card3H + 8;

        // ======================================================
        // BUTTON: Pasien Keluar (hanya jika belum PULANG)
        // ======================================================
        String currentStatus = pasien.getStatus() != null ? pasien.getStatus() : "AKTIF";
        if (!Pasien.STATUS_PULANG.equals(currentStatus)) {
            showBtnKeluar = true;
            int btnH = 40;
            int btnW = cardW;
            int btnX = margin;
            int btnY = y;

            // Store for tap detection
            this.btnKeluarX = btnX;
            this.btnKeluarY = btnY;
            this.btnKeluarW = btnW;
            this.btnKeluarH = btnH;

            // Red gradient button
            g.setColor(0xE74C3C);
            g.fillRoundRect(btnX, btnY, btnW, btnH, 12, 12);
            // Darker bottom edge for depth
            g.setColor(0xC0392B);
            g.fillRoundRect(btnX, btnY + btnH - 8, btnW, 8, 0, 0);
            g.fillRoundRect(btnX, btnY + btnH - 12, btnW, 12, 12, 12);
            // Recolor main area
            g.setColor(0xE74C3C);
            g.fillRoundRect(btnX, btnY, btnW, btnH - 6, 12, 12);

            // Button text
            g.setColor(WARNA_TEKS_TERANG);
            g.setFont(fontSedangBold);
            g.drawString("PASIEN KELUAR", btnX + btnW / 2,
                    btnY + (btnH - fontSedangBold.getHeight()) / 2 - 2,
                    Graphics.TOP | Graphics.HCENTER);

            y += btnH + 10;
        } else {
            showBtnKeluar = false;
            // Tampilkan badge SUDAH PULANG
            int badgeW2 = fontSedangBold.stringWidth("SUDAH PULANG") + 20;
            int badgeH2 = fontSedangBold.getHeight() + 10;
            int badgeX2 = margin + (cardW - badgeW2) / 2;
            g.setColor(WARNA_STATUS_PULANG);
            g.fillRoundRect(badgeX2, y, badgeW2, badgeH2, badgeH2, badgeH2);
            g.setColor(WARNA_TEKS_TERANG);
            g.setFont(fontSedangBold);
            g.drawString("SUDAH PULANG", badgeX2 + badgeW2 / 2,
                    y + 5, Graphics.TOP | Graphics.HCENTER);
            y += badgeH2 + 10;
        }

        // Store total content height for scrolling
        this.contentHeight = y + scrollY;

        // === FOOTER ===
        g.setColor(WARNA_CARD);
        g.fillRect(0, h - 22, w, 22);
        g.setColor(WARNA_TEKS_REDUP);
        g.setFont(fontKecil);
        g.drawString("Tekan * untuk kembali", w / 2, h - 18,
                Graphics.TOP | Graphics.HCENTER);
    }

    /**
     * Draw a label: value row.
     */
    private int drawInfoRow(Graphics g, Font fontLabel, Font fontValue,
                            int x, int width, int y, String label, String value) {
        g.setColor(WARNA_LABEL);
        g.setFont(fontLabel);
        g.drawString(label, x, y, Graphics.TOP | Graphics.LEFT);

        g.setColor(WARNA_TEKS);
        g.setFont(fontValue);
        g.drawString(value, x + width / 2, y, Graphics.TOP | Graphics.LEFT);

        return y + fontLabel.getHeight() + 6;
    }

    private String safeStr(String s, String def) {
        return (s != null && s.length() > 0) ? s : def;
    }

    private String formatHarga(double harga) {
        long h = (long) harga;
        String s = String.valueOf(h);
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (i > 0 && (len - i) % 3 == 0) {
                sb.append('.');
            }
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        int h = getHeight();

        if (action == Canvas.UP) {
            scrollY = Math.max(0, scrollY - 30);
        } else if (action == Canvas.DOWN) {
            int maxScroll = contentHeight - h;
            if (maxScroll > 0) {
                scrollY = Math.min(maxScroll, scrollY + 30);
            }
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
            return;
        } else if (action == Canvas.FIRE) {
            // FIRE → buka Pasien Keluar jika tersedia
            if (showBtnKeluar) {
                bukaPasienKeluar();
                return;
            }
            ScreenManager.getInstance().kembali();
            return;
        }
        repaint();
    }

    protected void pointerPressed(int px, int py) {
        int w = getWidth();
        // Back button area (top-left)
        if (py < 38 && px < 50) {
            ScreenManager.getInstance().kembali();
            return;
        }
        // Footer area tap => go back
        if (py > getHeight() - 22) {
            ScreenManager.getInstance().kembali();
            return;
        }
        // Cek tap pada tombol Pasien Keluar (koordinat relatif ke scrollY)
        if (showBtnKeluar) {
            int realBtnY = btnKeluarY; // sudah di-render relative to scroll
            if (px >= btnKeluarX && px <= btnKeluarX + btnKeluarW
                    && py >= realBtnY && py <= realBtnY + btnKeluarH) {
                bukaPasienKeluar();
                return;
            }
        }
    }

    /** Navigasi ke layar Pasien Keluar (form pembayaran). */
    private void bukaPasienKeluar() {
        ScreenManager.getInstance().tampilkanLayar(
                new PasienKeluarScreen(pasien, dokter, ruangan));
    }
}
