package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import controller.DokterController;
import model.Dokter;

/**
 * DokterScreen — List & form manajemen dokter.
 * INHERITANCE: Extends Form (LCDUI).
 * POLYMORPHISM: CommandListener interface implementation.
 */
public class DokterScreen extends Form implements CommandListener {

    private DokterController controller;
    private TextField tfNama, tfSpesialisasi, tfJadwal, tfHapusId;
    private Command cmdTambah, cmdKembali, cmdHapus;
    private StringItem siInfo;

    public DokterScreen() {
        super("MANAJEMEN DOKTER");
        this.controller = new DokterController();

        siInfo = new StringItem("", "");
        muatDaftarDokter();

        tfNama = new TextField("Nama Dokter", "", 100, TextField.ANY);
        tfSpesialisasi = new TextField("Spesialisasi", "", 50, TextField.ANY);
        tfJadwal = new TextField("Jadwal Praktik", "", 50, TextField.ANY);
        
        append(siInfo);
        append(new Spacer(10, 10));
        append(new StringItem("", "--- Tambah Dokter Baru ---"));
        append(tfNama);
        append(tfSpesialisasi);
        append(tfJadwal);
        
        append(new Spacer(10, 10));
        append(new StringItem("", "--- Hapus Dokter ---"));
        tfHapusId = new TextField("ID Dokter (contoh: DKT-0001)", "", 20, TextField.ANY);
        append(tfHapusId);

        cmdTambah = new Command("Tambah", Command.OK, 1);
        cmdHapus = new Command("Hapus", Command.ITEM, 2);
        cmdKembali = new Command("Kembali", Command.BACK, 3);
        
        addCommand(cmdTambah);
        addCommand(cmdHapus);
        addCommand(cmdKembali);
        setCommandListener(this);
    }

    private void muatDaftarDokter() {
        try {
            Vector list = controller.getSemuaDokter();
            if (list.size() == 0) {
                siInfo.setText("Belum ada data dokter.");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("Total: ").append(list.size()).append(" dokter\n\n");
                for (int i = 0; i < list.size(); i++) {
                    Dokter d = (Dokter) list.elementAt(i);
                    sb.append(i + 1).append(". ").append(d.getNama()).append("\n");
                    sb.append("   ").append(d.getSpesialisasi()).append(" | ").append(d.getJadwal()).append("\n");
                    sb.append("   ID: ").append(d.getId()).append("\n\n");
                }
                siInfo.setText(sb.toString());
            }
        } catch (Exception e) {
            siInfo.setText(new StringBuffer().append("Error: ").append(e.getMessage()).toString());
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdTambah) {
            try {
                Dokter dokter = controller.tambahDokter(
                    tfNama.getString(), tfSpesialisasi.getString(), 
                    tfJadwal.getString());

                Alert alert = new Alert("BERHASIL",
                    new StringBuffer().append("Dokter berhasil ditambahkan!\nID: ").append(dokter.getId()).toString(),
                    null, AlertType.CONFIRMATION);
                alert.setTimeout(3000);
                
                // Refresh list
                muatDaftarDokter();
                tfNama.setString("");
                tfSpesialisasi.setString("");
                tfJadwal.setString("");
                
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        } else if (c == cmdHapus) {
            String idStr = tfHapusId.getString().trim().toUpperCase();
            if (idStr.length() == 0) {
                Alert alert = new Alert("ERROR", "Masukkan ID Dokter (contoh: DKT-0001)", null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
                return;
            }
            // Cari dokter berdasarkan String ID (DKT-XXXX)
            try {
                java.util.Vector list = controller.getSemuaDokter();
                model.Dokter target = null;
                for (int i = 0; i < list.size(); i++) {
                    model.Dokter dk = (model.Dokter) list.elementAt(i);
                    if (idStr.equals(dk.getId())) {
                        target = dk;
                        break;
                    }
                }
                if (target == null) {
                    throw new Exception(new StringBuffer().append("Dokter dengan ID ").append(idStr).append(" tidak ditemukan").toString());
                }
                controller.hapusDokter(target.getRecordId());
                
                Alert alert = new Alert("BERHASIL", new StringBuffer().append("Dokter ").append(target.getNama()).append(" berhasil dihapus").toString(), null, AlertType.CONFIRMATION);
                alert.setTimeout(3000);
                
                muatDaftarDokter();
                tfHapusId.setString("");
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", new StringBuffer().append("Gagal hapus: ").append(e.getMessage()).toString(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        }
    }
}
