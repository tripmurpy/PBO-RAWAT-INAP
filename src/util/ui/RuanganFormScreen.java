package util.ui;
/*
 * Copyright (c) 2026 tripmurpy/PBO-RAWAT-INAP
 */

import javax.microedition.lcdui.*;
import java.util.Vector;
import service.RuanganService;
import model.Ruangan;
import util.ServiceFactory;

/**
 * RuanganFormScreen — Form penambahan ruangan baru.
 * INHERITANCE: Extends Form.
 * POLYMORPHISM: CommandListener.
 */
public class RuanganFormScreen extends Form implements CommandListener {

    private RuanganService service;
    private StringItem siStatus;
    private TextField tfNama;
    private ChoiceGroup cgTipe;
    private TextField tfKapasitas;
    private Command cmdTambah, cmdRefresh, cmdKembali;

    public RuanganFormScreen() {
        super("TAMBAH KAMAR BARU");
        this.service = ServiceFactory.getInstance().getRuanganService();

        siStatus = new StringItem("", "");
        muatStatusKamar();

        append(siStatus);
        append(new StringItem("", "\n--- Tambah Kamar Baru ---"));

        tfNama = new TextField("Nama/Nomor Kamar", "", 20, TextField.ANY);
        cgTipe = new ChoiceGroup("Tipe Kamar", ChoiceGroup.EXCLUSIVE);
        cgTipe.append("VIP", null);
        cgTipe.append("Kelas I", null);
        cgTipe.append("Kelas II", null);
        cgTipe.append("Kelas III", null);
        tfKapasitas = new TextField("Kapasitas", "1", 3, TextField.NUMERIC);

        append(tfNama);
        append(cgTipe);
        append(tfKapasitas);

        cmdTambah = new Command("Tambah", Command.OK, 1);
        cmdRefresh = new Command("Refresh", Command.SCREEN, 2);
        cmdKembali = new Command("Kembali", Command.BACK, 3);
        addCommand(cmdTambah);
        addCommand(cmdRefresh);
        addCommand(cmdKembali);
        setCommandListener(this);
    }

    private void muatStatusKamar() {
        try {
            Vector list = service.getSemuaRuangan();
            int[] stat = service.hitungStatistik();
            StringBuffer sb = new StringBuffer();
            sb.append("=== STATUS KAMAR ===\n\n");

            if (list.size() == 0) {
                sb.append("Belum ada data kamar.\n");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    Ruangan r = (Ruangan) list.elementAt(i);
                    String status = r.isKosong() ? "KOSONG  \u2713" : 
                                    new StringBuffer().append("TERISI (").append(r.getNamaPasien()).append(")").toString();
                    sb.append(" ").append(r.getNamaRuangan()).append(" : ").append(status).append("\n");
                }
                sb.append("\n Total Terisi : ").append(stat[1]).append(" / ").append(stat[0]);
                sb.append("\n Total Kosong : ").append(stat[2]).append(" / ").append(stat[0]);
            }
            siStatus.setText(sb.toString());
        } catch (Exception e) {
            siStatus.setText(new StringBuffer().append("Error: ").append(e.getMessage()).toString());
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdRefresh) {
            muatStatusKamar();
        } else if (c == cmdTambah) {
            try {
                String[] tipeArr = {"VIP", "Kelas I", "Kelas II", "Kelas III"};
                String tipe = tipeArr[cgTipe.getSelectedIndex()];
                int kap = Integer.parseInt(tfKapasitas.getString());

                Ruangan r = service.tambahRuangan(tfNama.getString(), tipe, kap);

                Alert alert = new Alert("BERHASIL",
                    new StringBuffer().append("Kamar berhasil ditambahkan!\n").append(r.getNamaRuangan())
                    .append(" (").append(r.getTipeKamar()).append(")").toString(),
                    null, AlertType.CONFIRMATION);
                alert.setTimeout(3000);

                muatStatusKamar();
                tfNama.setString("");
                tfKapasitas.setString("1");

                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        }
    }
}

