package ui;
/*
 * Copyright (c) 2026 tripmurpy/PBO-RAWAT-INAP
 */

import javax.microedition.lcdui.*;
import service.AdmisiService;
import model.Admisi;
import util.DateUtil;
import util.ServiceFactory;

/**
 * DischargeScreen — Form keluar pasien (discharge).
 * INHERITANCE: Extends Form.
 */
public class DischargeScreen extends Form implements CommandListener {

    private AdmisiService admisiService;
    private TextField tfCari, tfDiagnosisAkhir, tfKodeICD10, tfTglKeluar, tfCatatan;
    private StringItem siInfo;
    private Command cmdCari, cmdSelesai, cmdBatal;
    private Admisi admisiAktif;

    public DischargeScreen() {
        super("KELUAR PASIEN");
        this.admisiService = ServiceFactory.getAdmisiService();

        tfCari = new TextField("No. RM / ID Admisi", "", 20, TextField.ANY);
        siInfo = new StringItem("Info Admisi", "Belum dicari");

        append(tfCari);
        append(siInfo);

        cmdCari = new Command("Cari", Command.OK, 1);
        cmdSelesai = new Command("Selesaikan", Command.OK, 2);
        cmdBatal = new Command("Batal", Command.BACK, 3);
        addCommand(cmdCari);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdCari) {
            cariAdmisi();
        } else if (c == cmdSelesai) {
            prosesDischarge();
        }
    }

    private void cariAdmisi() {
        try {
            String keyword = tfCari.getString().trim();
            admisiAktif = admisiService.cariAdmisiAktifByRM(keyword);
            if (admisiAktif == null) {
                siInfo.setText(
                        new StringBuffer().append("Admisi aktif tidak ditemukan untuk: ").append(keyword).toString());
                return;
            }

            siInfo.setText(new StringBuffer()
                    .append("ID Admisi : ").append(admisiAktif.getIdAdmisi()).append("\n")
                    .append("No. RM    : ").append(admisiAktif.getNoRMPasien()).append("\n")
                    .append("Masuk     : ").append(DateUtil.formatTanggal(admisiAktif.getTglMasuk())).append("\n")
                    .append("Diagnosis : ").append(admisiAktif.getDiagnosisAwal()).toString());

            // Tampilkan form discharge
            tfDiagnosisAkhir = new TextField("Diagnosis Akhir", "", 200, TextField.ANY);
            tfKodeICD10 = new TextField("Kode ICD-10", "", 10, TextField.ANY);
            tfTglKeluar = new TextField("Tgl Keluar (DD/MM/YYYY)",
                    DateUtil.tanggalHariIni(), 10, TextField.ANY);
            tfCatatan = new TextField("Catatan", "", 200, TextField.ANY);

            append(tfDiagnosisAkhir);
            append(tfKodeICD10);
            append(tfTglKeluar);
            append(tfCatatan);

            removeCommand(cmdCari);
            addCommand(cmdSelesai);
        } catch (Exception e) {
            siInfo.setText(new StringBuffer().append("Error: ").append(e.getMessage()).toString());
        }
    }

    private void prosesDischarge() {
        try {
            Admisi hasil = admisiService.discharge(
                    admisiAktif.getIdAdmisi(),
                    tfDiagnosisAkhir.getString(),
                    tfKodeICD10.getString(),
                    tfTglKeluar.getString(),
                    tfCatatan.getString());

            int lamaRawat = admisiService.hitungLamaRawat(hasil);

            Alert alert = new Alert("PASIEN BERHASIL KELUAR",
                    new StringBuffer().append("Kamar kembali KOSONG\n")
                            .append("Lama rawat: ").append(lamaRawat).append(" hari").toString(),
                    null, AlertType.CONFIRMATION);
            alert.setTimeout(Alert.FOREVER);
            Command cmdOK = new Command("OK", Command.OK, 1);
            alert.addCommand(cmdOK);
            alert.setCommandListener(new CommandListener() {
                public void commandAction(Command cmd, Displayable disp) {
                    ScreenManager.getInstance().kembali();
                }
            });
            ScreenManager.getInstance().getDisplay().setCurrent(alert);
        } catch (Exception e) {
            Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(3000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        }
    }
}
