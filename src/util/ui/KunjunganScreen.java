package util.ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import service.KunjunganService;
import model.Kunjungan;
import util.DateUtil;
import util.ServiceFactory;

/**
 * KunjunganScreen — Riwayat kunjungan / admisi.
 * INHERITANCE: Extends Form.
 */
public class KunjunganScreen extends Form implements CommandListener {

    private KunjunganService kunjunganService;
    private Command cmdKembali, cmdRefresh;

    public KunjunganScreen() {
        super("RIWAYAT KUNJUNGAN");
        this.kunjunganService = ServiceFactory.getInstance().getKunjunganService();

        muatRiwayat();

        cmdKembali = new Command("Kembali", Command.BACK, 1);
        cmdRefresh = new Command("Refresh", Command.SCREEN, 2);
        addCommand(cmdKembali);
        addCommand(cmdRefresh);
        setCommandListener(this);
    }

    private void muatRiwayat() {
        deleteAll();
        try {
            Vector list = kunjunganService.getRiwayatKunjungan();
            if (list.size() == 0) {
                append(new StringItem("", "Belum ada riwayat kunjungan."));
            } else {
                StringItem headerTotal = new StringItem("", "Total: " + list.size() + " kunjungan");
                headerTotal.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER);
                append(headerTotal);

                for (int i = 0; i < list.size(); i++) {
                    Kunjungan k = (Kunjungan) list.elementAt(i);
                    
                    StringItem sepTop = new StringItem("", "=======================");
                    sepTop.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
                    append(sepTop);

                    append(new StringItem("Kunjungan", "#" + (i + 1)));
                    append(new StringItem("No. RM", k.getNoRMPasien()));
                    append(new StringItem("Admisi", k.getIdAdmisi()));
                    append(new StringItem("Masuk", DateUtil.formatTanggal(k.getTglMasuk())));
                    if (k.getTglKeluar() > 0) {
                        append(new StringItem("Keluar", DateUtil.formatTanggal(k.getTglKeluar())));
                        append(new StringItem("Rawat", k.getLamaRawat() + " hari"));
                    }
                    append(new StringItem("Status", k.getStatus()));
                    append(new StringItem("Diagnosa", k.getDiagnosis() != null ? k.getDiagnosis() : "-"));

                    if (k.getBiayaTotal() > 0) {
                        StringItem sepBiayaTop = new StringItem("", "--- RINCIAN BIAYA ---");
                        sepBiayaTop.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
                        append(sepBiayaTop);

                        if (k.getBiayaRuangan() > 0) {
                            append(new StringItem("Kamar", "Rp " + formatHarga(k.getBiayaRuangan())));
                            append(new StringItem("Makan", "Rp " + formatHarga(k.getBiayaMakanan())));
                            append(new StringItem("Obat/Vit", "Rp " + formatHarga(k.getBiayaObat())));
                            append(new StringItem("Admin", "Rp " + formatHarga(k.getBiayaAdmin())));
                        }

                        StringItem sepBiayaBot = new StringItem("", "-----------------------");
                        sepBiayaBot.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
                        append(sepBiayaBot);

                        append(new StringItem("TOTAL", "Rp " + formatHarga(k.getBiayaTotal())));
                        
                        String bayar = k.getTipePembayaran() != null && k.getTipePembayaran().length() > 0 ? k.getTipePembayaran() : "-";
                        if (k.getNamaBank() != null && k.getNamaBank().length() > 0) {
                            bayar += " (" + k.getNamaBank() + ")";
                        }
                        append(new StringItem("Bayar", bayar));
                        append(new StringItem("Status", "[LUNAS]"));
                    }
                    StringItem sepBot = new StringItem("", "=======================\n");
                    sepBot.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
                    append(sepBot);
                }
            }
        } catch (Exception e) {
            append(new StringItem("Error", e.getMessage()));
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdRefresh) {
            muatRiwayat();
        }
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
