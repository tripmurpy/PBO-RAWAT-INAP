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
                append(new StringItem("",
                        new StringBuffer().append("Total: ").append(list.size()).append(" kunjungan\n").toString()));
                for (int i = 0; i < list.size(); i++) {
                    Kunjungan k = (Kunjungan) list.elementAt(i);
                    StringBuffer sb = new StringBuffer();
                    sb.append("No. RM  : ").append(k.getNoRMPasien()).append("\n");
                    sb.append("Admisi  : ").append(k.getIdAdmisi()).append("\n");
                    sb.append("Masuk   : ").append(DateUtil.formatTanggal(k.getTglMasuk())).append("\n");
                    if (k.getTglKeluar() > 0) {
                        sb.append("Keluar  : ").append(DateUtil.formatTanggal(k.getTglKeluar())).append("\n");
                        sb.append("Rawat   : ").append(k.getLamaRawat()).append(" hari\n");
                    }
                    sb.append("Status  : ").append(k.getStatus()).append("\n");
                    sb.append("Diagnosa: ").append(k.getDiagnosis());

                    // Informasi pembayaran (jika sudah selesai)
                    if (k.getBiayaTotal() > 0) {
                        sb.append("\n\n--- RINCIAN BIAYA ---\n");
                        if (k.getBiayaRuangan() > 0) {
                            sb.append("- Kamar   : Rp ").append(formatHarga(k.getBiayaRuangan())).append("\n");
                            sb.append("- Makan   : Rp ").append(formatHarga(k.getBiayaMakanan())).append("\n");
                            sb.append("- Obat/Vit: Rp ").append(formatHarga(k.getBiayaObat())).append("\n");
                            sb.append("- Admin   : Rp ").append(formatHarga(k.getBiayaAdmin())).append("\n");
                        }
                        sb.append("TOTAL     : Rp ").append(formatHarga(k.getBiayaTotal())).append("\n");
                        sb.append("Bayar     : ").append(
                                k.getTipePembayaran() != null && k.getTipePembayaran().length() > 0
                                        ? k.getTipePembayaran() : "-");
                        if (k.getNamaBank() != null && k.getNamaBank().length() > 0) {
                            sb.append(" (").append(k.getNamaBank()).append(")");
                        }
                        sb.append("\n[LUNAS]");
                    }

                    StringItem si = new StringItem(
                            new StringBuffer().append("--- #").append(i + 1).append(" ---").toString(), sb.toString());
                    append(si);
                    append(new Spacer(getWidth(), 5));
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
