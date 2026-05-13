package util.ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import model.Pasien;
import model.Dokter;
import model.Ruangan;
import model.Admisi;
import service.PasienService;
import service.RuanganService;
import service.KunjunganService;
import model.repository.IAdmisiRepository;
import util.DateUtil;
import util.IDGenerator;
import util.ServiceFactory;

/**
 * PasienKeluarScreen — Form pembayaran pasien keluar (discharge).
 *
 * INHERITANCE: Extends Canvas (custom rendered UI).
 * Menampilkan ringkasan data, biaya, dan opsi pembayaran.
 */
public class PasienKeluarScreen extends Canvas {

    private Pasien pasien;
    private Dokter dokter;
    private Ruangan ruangan;

    private int scrollY = 0;
    private int contentHeight = 0;

    // Pilihan pembayaran
    private static final String[] TIPE_BAYAR = {"CASH", "QRIS", "DEBIT", "KREDIT"};
    private static final String[] DAFTAR_BANK = {
        "BCA", "BRI", "BNI", "MANDIRI", "PERMATA BANK", "BANK MEGA"
    };
    private int selectedTipeBayar = 0;
    private int selectedBank = 0;
    private boolean showBankSelection = false;

    // Focus state: 0 = lama rawat, 1 = tipe bayar, 2 = bank, 3 = tombol bayar
    private int focusSection = 0;

    // Biaya calculation
    private int lamaRawat = 1;
    private int hargaPerMalam = 0;
    private int biayaRuangan = 0;
    private int biayaMakanan = 0;
    private int biayaObat = 0;
    private int biayaAdmin = 50000;
    private int totalBiaya = 0;

    // Admisi terkait
    private Admisi admisiAktif;

    // Posisi tombol bayar (untuk tap)
    private int btnBayarX, btnBayarY, btnBayarW, btnBayarH;

    // Warna palette — premium dark medical
    private static final int C_BG = 0xF0F4F8;
    private static final int C_CARD = 0xFFFFFF;
    private static final int C_HEADER = 0x1A1A2E;
    private static final int C_TEXT = 0x2D3436;
    private static final int C_TEXT_WHITE = 0xFFFFFF;
    private static final int C_TEXT_MUTED = 0x95A5A6;
    private static final int C_LABEL = 0x636E72;
    private static final int C_DIVIDER = 0xE8E8E8;
    private static final int C_ACCENT = 0x4A90E2;
    private static final int C_MONEY = 0x27AE60;
    private static final int C_SELECTED = 0x2ECC71;
    private static final int C_UNSELECTED = 0xECF0F1;
    private static final int C_UNSEL_TEXT = 0x7F8C8D;
    private static final int C_BAYAR = 0x27AE60;
    private static final int C_BAYAR_DARK = 0x1E8449;
    private static final int C_FOCUS_BORDER = 0xF39C12;

    public PasienKeluarScreen(Pasien pasien, Dokter dokter, Ruangan ruangan) {
        this.pasien = pasien;
        this.dokter = dokter;
        this.ruangan = ruangan;
        setFullScreenMode(true);
        hitungBiaya();
    }

    /**
     * Menghitung biaya rawat inap berdasarkan Admisi aktif.
     * Jika tidak ada Admisi, gunakan harga ruangan × 1 hari minimum.
     */
    private void hitungBiaya() {
        // Harga per malam dari ruangan
        if (ruangan != null) {
            hargaPerMalam = (int) ruangan.getHarga();
        }

        // Cari Admisi aktif untuk pasien ini
        try {
            IAdmisiRepository admisiRepo = ServiceFactory.getInstance().getAdmisiRepo();
            Vector admisiList = admisiRepo.findByPasien(pasien.getNoRM());
            for (int i = 0; i < admisiList.size(); i++) {
                Admisi a = (Admisi) admisiList.elementAt(i);
                if (a.isAktif()) {
                    admisiAktif = a;
                    break;
                }
            }
        } catch (Exception e) {
        }

        // Hitung lama rawat (Initial only)
        if (admisiAktif != null && admisiAktif.getTglMasuk() > 0) {
            lamaRawat = DateUtil.hitungSelisihHari(admisiAktif.getTglMasuk(), DateUtil.sekarang());
            if (lamaRawat < 1) lamaRawat = 1;
        } else {
            lamaRawat = 1;
        }

        updateBiaya();
    }

    /**
     * Update biaya based on current lamaRawat.
     */
    private void updateBiaya() {
        // Logic Approach 2: Class-Based Premium Rates
        boolean isVIP = false;
        if (ruangan != null && ruangan.getNamaRuangan() != null) {
            String rName = ruangan.getNamaRuangan().toUpperCase();
            if (rName.indexOf("VIP") >= 0 || rName.indexOf("VVIP") >= 0) {
                isVIP = true;
            }
        }

        // Fallback for Room Price if Rp 0
        if (hargaPerMalam <= 0 && ruangan != null) {
            String tipe = ruangan.getTipeKamar().toUpperCase();
            if (tipe.indexOf("VVIP") >= 0) hargaPerMalam = 2500000;
            else if (tipe.indexOf("VIP") >= 0) hargaPerMalam = 1500000;
            else if (tipe.indexOf("KELAS I") >= 0 || tipe.indexOf("KELAS 1") >= 0) hargaPerMalam = 800000;
            else if (tipe.indexOf("KELAS II") >= 0 || tipe.indexOf("KELAS 2") >= 0) hargaPerMalam = 500000;
            else if (tipe.indexOf("KELAS III") >= 0 || tipe.indexOf("KELAS 3") >= 0) hargaPerMalam = 200000;
            else hargaPerMalam = 150000; // General fallback
        }

        biayaRuangan = hargaPerMalam * lamaRawat;
        biayaMakanan = (isVIP ? 200000 : 125000) * lamaRawat;
        biayaObat = (isVIP ? 400000 : 250000) * lamaRawat;
        biayaAdmin = 50000;

        totalBiaya = biayaRuangan + biayaMakanan + biayaObat + biayaAdmin;
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        Font fBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        Font fSedangB = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fKecilB = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);

        // Background
        g.setColor(C_BG);
        g.fillRect(0, 0, w, h);

        // === TOP BAR ===
        int barH = 38;
        g.setColor(C_HEADER);
        g.fillRect(0, 0, w, barH);
        g.setColor(C_TEXT_WHITE);
        g.setFont(fSedangB);
        g.drawString("Pembayaran", w / 2, (barH - fSedangB.getHeight()) / 2,
                Graphics.TOP | Graphics.HCENTER);
        // Back
        g.setFont(fKecil);
        g.drawString("<", 10, (barH - fKecil.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);

        int margin = 10;
        int cardW = w - (margin * 2);
        int y = barH + 6 - scrollY;

        // ======================================================
        // SECTION 1: Ringkasan Pasien (compact header card)
        // ======================================================
        int sec1H = fSedangB.getHeight() + fKecil.getHeight() * 2 + 24;
        g.setColor(C_CARD);
        g.fillRoundRect(margin, y, cardW, sec1H, 10, 10);

        int cx = margin + 10;
        int ty = y + 8;

        g.setColor(C_TEXT);
        g.setFont(fSedangB);
        g.drawString(pasien.getNama() != null ? pasien.getNama() : "-", cx, ty,
                Graphics.TOP | Graphics.LEFT);
        ty += fSedangB.getHeight() + 2;

        g.setColor(C_LABEL);
        g.setFont(fKecil);
        StringBuffer infoBuf = new StringBuffer();
        infoBuf.append("No. RM: ").append(pasien.getNoRM() != null ? pasien.getNoRM() : "-");
        g.drawString(infoBuf.toString(), cx, ty, Graphics.TOP | Graphics.LEFT);
        ty += fKecil.getHeight() + 2;

        StringBuffer info2 = new StringBuffer();
        if (dokter != null) {
            info2.append("Dr. ").append(dokter.getNama());
        }
        info2.append(" | ");
        if (ruangan != null) {
            info2.append(ruangan.getNamaRuangan());
        }
        g.drawString(info2.toString(), cx, ty, Graphics.TOP | Graphics.LEFT);

        y += sec1H + 6;

        // ======================================================
        // SECTION 1.5: Input Lama Inap (New)
        // ======================================================
        int secInapH = fKecilB.getHeight() + 30;
        g.setColor(C_CARD);
        g.fillRoundRect(margin, y, cardW, secInapH, 10, 10);
        
        if (focusSection == 0) {
            g.setColor(C_FOCUS_BORDER);
            g.drawRoundRect(margin - 1, y - 1, cardW + 2, secInapH + 2, 10, 10);
        }

        int iy = y + 8;
        g.setColor(C_ACCENT);
        g.setFont(fKecilB);
        g.drawString("DURASI RAWAT INAP", cx, iy, Graphics.TOP | Graphics.LEFT);
        
        iy += fKecilB.getHeight() + 4;
        g.setColor(C_TEXT);
        g.setFont(fBesar);
        String txtInap = lamaRawat + " Hari";
        g.drawString(txtInap, margin + cardW / 2, iy, Graphics.TOP | Graphics.HCENTER);
        
        // Buttons indicators
        g.setFont(fKecil);
        g.setColor(C_LABEL);
        g.drawString("< -", margin + 20, iy + 4, Graphics.TOP | Graphics.LEFT);
        g.drawString("+ >", margin + cardW - 20, iy + 4, Graphics.TOP | Graphics.RIGHT);

        y += secInapH + 6;

        // ======================================================
        // SECTION 2: Detail Biaya (Itemized Invoice)
        // ======================================================
        int rowH = fKecil.getHeight() + 6;
        int sec2H = fKecilB.getHeight() + (rowH * 4) + fSedangB.getHeight() + 32;
        g.setColor(C_CARD);
        g.fillRoundRect(margin, y, cardW, sec2H, 10, 10);
        g.setColor(C_MONEY);
        g.fillRoundRect(margin, y, 4, sec2H, 4, 4);

        int by = y + 8;
        g.setColor(C_MONEY);
        g.setFont(fKecilB);
        g.drawString("RINCIAN BIAYA (INVOICE)", cx, by, Graphics.TOP | Graphics.LEFT);
        by += fKecilB.getHeight() + 8;

        g.setFont(fKecil);
        
        // 1. Kamar
        drawInvoiceRow(g, "Kamar (" + lamaRawat + " hari)", biayaRuangan, cx, by, cardW);
        by += rowH;
        
        // 2. Makanan
        drawInvoiceRow(g, "Makanan & Gizi", biayaMakanan, cx, by, cardW);
        by += rowH;
        
        // 3. Obat & Vitamin
        drawInvoiceRow(g, "Obat & Vitamin", biayaObat, cx, by, cardW);
        by += rowH;
        
        // 4. Administrasi
        drawInvoiceRow(g, "Administrasi", biayaAdmin, cx, by, cardW);
        by += rowH;

        // Divider
        g.setColor(C_DIVIDER);
        g.drawLine(cx, by, margin + cardW - 10, by);
        by += 8;

        // Total
        g.setColor(C_TEXT);
        g.setFont(fKecilB);
        g.drawString("TOTAL PEMBAYARAN", cx, by, Graphics.TOP | Graphics.LEFT);
        g.setColor(C_MONEY);
        g.setFont(fSedangB);
        g.drawString("Rp " + formatHarga(totalBiaya), margin + cardW - 10, by - 2, 
                    Graphics.TOP | Graphics.RIGHT);

        y += sec2H + 6;

        // ======================================================
        // SECTION 3: Tipe Pembayaran
        // ======================================================
        int chipH = fKecilB.getHeight() + 10;
        int sec3H = fKecilB.getHeight() + chipH + 20;
        g.setColor(C_CARD);
        g.fillRoundRect(margin, y, cardW, sec3H, 10, 10);

        int py2 = y + 8;
        g.setColor(C_ACCENT);
        g.setFont(fKecilB);
        g.drawString("TIPE PEMBAYARAN", cx, py2, Graphics.TOP | Graphics.LEFT);
        py2 += fKecilB.getHeight() + 6;

        // Focus indicator
        if (focusSection == 1) {
            g.setColor(C_FOCUS_BORDER);
            g.drawRoundRect(margin - 1, y - 1, cardW + 2, sec3H + 2, 10, 10);
        }

        // Draw chips
        int chipX = cx;
        for (int i = 0; i < TIPE_BAYAR.length; i++) {
            int cw = fKecilB.stringWidth(TIPE_BAYAR[i]) + 14;
            boolean sel = (i == selectedTipeBayar);

            g.setColor(sel ? C_SELECTED : C_UNSELECTED);
            g.fillRoundRect(chipX, py2, cw, chipH, chipH, chipH);

            g.setColor(sel ? C_TEXT_WHITE : C_UNSEL_TEXT);
            g.setFont(fKecilB);
            g.drawString(TIPE_BAYAR[i], chipX + cw / 2, py2 + 5,
                    Graphics.TOP | Graphics.HCENTER);

            chipX += cw + 5;
        }

        y += sec3H + 6;

        // ======================================================
        // SECTION 4: Pilih Bank (hanya untuk DEBIT/KREDIT)
        // ======================================================
        showBankSelection = (selectedTipeBayar == 2 || selectedTipeBayar == 3);

        if (showBankSelection) {
            int bankChipH = fKecil.getHeight() + 10;
            int bankRows = (DAFTAR_BANK.length + 1) / 2; // 2 columns
            int sec4H = fKecilB.getHeight() + bankRows * (bankChipH + 5) + 18;
            g.setColor(C_CARD);
            g.fillRoundRect(margin, y, cardW, sec4H, 10, 10);

            if (focusSection == 2) {
                g.setColor(C_FOCUS_BORDER);
                g.drawRoundRect(margin - 1, y - 1, cardW + 2, sec4H + 2, 10, 10);
            }

            int by2 = y + 8;
            g.setColor(C_ACCENT);
            g.setFont(fKecilB);
            g.drawString("PILIH BANK", cx, by2, Graphics.TOP | Graphics.LEFT);
            by2 += fKecilB.getHeight() + 6;

            // 2-column bank layout
            int halfW = (cardW - 30) / 2;
            for (int i = 0; i < DAFTAR_BANK.length; i++) {
                int col = i % 2;
                int row = i / 2;
                int bx = cx + col * (halfW + 5);
                int bby = by2 + row * (bankChipH + 5);
                boolean sel = (i == selectedBank);

                g.setColor(sel ? C_ACCENT : C_UNSELECTED);
                g.fillRoundRect(bx, bby, halfW, bankChipH, 6, 6);

                g.setColor(sel ? C_TEXT_WHITE : C_UNSEL_TEXT);
                g.setFont(fKecil);
                g.drawString(DAFTAR_BANK[i], bx + halfW / 2, bby + 5,
                        Graphics.TOP | Graphics.HCENTER);
            }

            y += sec4H + 6;
        }

        // ======================================================
        // TOMBOL BAYAR
        // ======================================================
        int btnH = 44;
        btnBayarX = margin;
        btnBayarY = y;
        btnBayarW = cardW;
        btnBayarH = btnH;

        if (focusSection == 3 || (!showBankSelection && focusSection == 2)) {
            g.setColor(C_FOCUS_BORDER);
            g.drawRoundRect(margin - 2, y - 2, cardW + 4, btnH + 4, 14, 14);
        }

        // Green gradient button
        g.setColor(C_BAYAR);
        g.fillRoundRect(margin, y, cardW, btnH, 12, 12);
        g.setColor(C_BAYAR_DARK);
        g.fillRoundRect(margin, y + btnH - 10, cardW, 10, 0, 0);
        g.fillRoundRect(margin, y + btnH - 14, cardW, 14, 12, 12);
        g.setColor(C_BAYAR);
        g.fillRoundRect(margin, y, cardW, btnH - 6, 12, 12);

        g.setColor(C_TEXT_WHITE);
        g.setFont(fSedangB);
        StringBuffer bayarText = new StringBuffer();
        bayarText.append("BAYAR  Rp ").append(formatHarga(totalBiaya));
        g.drawString(bayarText.toString(), margin + cardW / 2,
                y + (btnH - fSedangB.getHeight()) / 2 - 2,
                Graphics.TOP | Graphics.HCENTER);

        y += btnH + 20;

        this.contentHeight = y + scrollY;

        // === FOOTER ===
        g.setColor(C_CARD);
        g.fillRect(0, h - 20, w, 20);
        g.setColor(C_TEXT_MUTED);
        g.setFont(fKecil);
        g.drawString("< Kembali | OK Bayar", w / 2, h - 17,
                Graphics.TOP | Graphics.HCENTER);
    }

    // ==================== INPUT HANDLING ====================

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        int maxFocus = showBankSelection ? 3 : 2;

        if (action == Canvas.LEFT) {
            if (focusSection == 0) {
                if (lamaRawat > 1) {
                    lamaRawat--;
                    updateBiaya();
                }
            } else if (focusSection == 1 && selectedTipeBayar > 0) {
                selectedTipeBayar--;
            } else if (focusSection == 2 && showBankSelection && selectedBank > 0) {
                selectedBank--;
            }
        } else if (action == Canvas.RIGHT) {
            if (focusSection == 0) {
                lamaRawat++;
                updateBiaya();
            } else if (focusSection == 1 && selectedTipeBayar < TIPE_BAYAR.length - 1) {
                selectedTipeBayar++;
            } else if (focusSection == 2 && showBankSelection && selectedBank < DAFTAR_BANK.length - 1) {
                selectedBank++;
            }
        } else if (action == Canvas.DOWN) {
            if (focusSection < maxFocus) {
                focusSection++;
            } else {
                // Scroll down
                int maxScroll = contentHeight - getHeight();
                if (maxScroll > 0) {
                    scrollY = Math.min(maxScroll, scrollY + 30);
                }
            }
        } else if (action == Canvas.UP) {
            if (focusSection > 0) {
                focusSection--;
            } else {
                scrollY = Math.max(0, scrollY - 30);
            }
        } else if (action == Canvas.FIRE) {
            // Jika focus di tombol bayar → proses
            if (focusSection == maxFocus) {
                prosesBayar();
                return;
            }
        } else if (keyCode == Canvas.KEY_STAR) {
            ScreenManager.getInstance().kembali();
            return;
        }
        repaint();
    }

    protected void pointerPressed(int px, int py) {
        int w = getWidth();
        // Back
        if (py < 38 && px < 50) {
            ScreenManager.getInstance().kembali();
            return;
        }

        // Tipe pembayaran chips
        Font fKecilB = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int barH = 38;
        // Rough area detection — recalculate chip positions
        // We detect which chip was tapped based on x position
        int margin = 10;
        int cx = margin + 10;
        // Check if tap is in the payment type area (approximate)
        int chipX = cx;
        int chipH = fKecilB.getHeight() + 10;
        for (int i = 0; i < TIPE_BAYAR.length; i++) {
            int cw = fKecilB.stringWidth(TIPE_BAYAR[i]) + 14;
            // We can't know exact Y without recalculating, but check X range
            chipX += cw + 5;
        }

        // Bank selection
        if (showBankSelection) {
            Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            int halfW = (w - 20 - 30) / 2;
            // Approximate bank detection based on relative positions
        }

        // Tombol Bayar detection
        if (px >= btnBayarX && px <= btnBayarX + btnBayarW
                && py >= btnBayarY && py <= btnBayarY + btnBayarH) {
            prosesBayar();
            return;
        }
    }

    // ==================== PROSES PEMBAYARAN ====================

    /**
     * Proses pembayaran:
     * 1. Buat/Update Admisi → SELESAI
     * 2. Update Pasien → PULANG
     * 3. Release Ruangan → KOSONG
     * 4. Tampilkan animasi LUNAS
     */
    private void prosesBayar() {
        try {
            String tipeBayar = TIPE_BAYAR[selectedTipeBayar];
            String bank = showBankSelection ? DAFTAR_BANK[selectedBank] : "";

            // 1. Update atau buat Admisi baru
            IAdmisiRepository admisiRepo = ServiceFactory.getInstance().getAdmisiRepo();
            if (admisiAktif != null) {
                admisiAktif.setStatus(Admisi.STATUS_SELESAI);
                admisiAktif.setTglKeluar(DateUtil.sekarang());
                admisiAktif.setBiayaTotal(totalBiaya);
                admisiAktif.setBiayaRuangan(biayaRuangan);
                admisiAktif.setBiayaMakanan(biayaMakanan);
                admisiAktif.setBiayaObat(biayaObat);
                admisiAktif.setBiayaAdmin(biayaAdmin);
                admisiAktif.setTipePembayaran(tipeBayar);
                admisiAktif.setNamaBank(bank);
                admisiAktif.setDiagnosisAkhir(
                        admisiAktif.getDiagnosisAwal() != null ? admisiAktif.getDiagnosisAwal() : "Sembuh");
                admisiRepo.update(admisiAktif);
            } else {
                // Buat Admisi baru jika belum ada
                Admisi baru = new Admisi();
                baru.setIdAdmisi(IDGenerator.generateAdmisiId());
                baru.setNoRM(pasien.getNoRM());
                baru.setIdDokter(dokter != null ? dokter.getNama() : "");
                baru.setIdRuangan(ruangan != null ? ruangan.getNamaRuangan() : "");
                baru.setTglMasuk(DateUtil.sekarang() - (86400000L * lamaRawat));
                baru.setTglKeluar(DateUtil.sekarang());
                baru.setDiagnosisAwal("Rawat Inap");
                baru.setDiagnosisAkhir("Sembuh");
                baru.setStatus(Admisi.STATUS_SELESAI);
                baru.setBiayaTotal(totalBiaya);
                baru.setBiayaRuangan(biayaRuangan);
                baru.setBiayaMakanan(biayaMakanan);
                baru.setBiayaObat(biayaObat);
                baru.setBiayaAdmin(biayaAdmin);
                baru.setTipePembayaran(tipeBayar);
                baru.setNamaBank(bank);
                admisiRepo.save(baru);
            }

            // 2. Update status pasien → PULANG
            PasienService pasienService = ServiceFactory.getInstance().getPasienService();
            pasienService.keluarkanPasien(pasien);

            // 3. Release ruangan → KOSONG
            if (ruangan != null) {
                RuanganService ruanganService = ServiceFactory.getInstance().getRuanganService();
                ruanganService.lepaskanKamar(ruangan.getNamaRuangan());
            }

            // 4. Tampilkan animasi LUNAS
            String tipeBayarDisplay = tipeBayar;
            if (bank.length() > 0) {
                tipeBayarDisplay = new StringBuffer().append(tipeBayar)
                        .append(" - ").append(bank).toString();
            }
            ScreenManager.getInstance().tampilkanLayar(
                    new LunasAnimationScreen(totalBiaya, tipeBayarDisplay));

        } catch (Exception e) {
            // Tampilkan error
            Alert alert = new Alert("ERROR",
                    new StringBuffer().append("Gagal memproses: ").append(e.getMessage()).toString(),
                    null, AlertType.ERROR);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        }
    }

    // ==================== UTILITY ====================

    private void drawInvoiceRow(Graphics g, String label, int value, int x, int y, int cardW) {
        g.setColor(C_LABEL);
        g.drawString(label, x, y, Graphics.TOP | Graphics.LEFT);
        
        // Draw dotted line
        int labelW = g.getFont().stringWidth(label);
        int valueW = g.getFont().stringWidth("Rp " + formatHarga(value));
        int dotX = x + labelW + 5;
        int dotEnd = x + cardW - 25 - valueW;
        g.setColor(C_DIVIDER);
        for (int i = dotX; i < dotEnd; i += 4) {
            g.drawLine(i, y + 10, i + 1, y + 10);
        }
        
        g.setColor(C_TEXT);
        g.drawString("Rp " + formatHarga(value), x + cardW - 20, y, Graphics.TOP | Graphics.RIGHT);
    }

    private String formatHarga(int harga) {
        String s = String.valueOf(harga);
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
}
