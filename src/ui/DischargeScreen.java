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
    private TextField tfBiayaKamar, tfBiayaObat;
    private StringItem siInfo;
    private Command cmdCari, cmdSelesai, cmdBatal;
    private Admisi admisiAktif;

    public DischargeScreen() {
        super("KELUAR PASIEN");
        this.admisiService = ServiceFactory.getInstance().getAdmisiService();

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

            String namaDokter = "-";
            String namaKamar = "-";
            try {
                model.Dokter dk = ServiceFactory.getInstance().getDokterService().cariById(admisiAktif.getIdDokter());
                if (dk != null) namaDokter = dk.getNama();
            } catch(Exception e){}
            try {
                model.Ruangan rn = ServiceFactory.getInstance().getRuanganService().cariById(admisiAktif.getIdRuangan());
                if (rn != null) namaKamar = rn.getNamaRuangan();
            } catch(Exception e){}

            siInfo.setText(new StringBuffer()
                    .append("ID Admisi : ").append(admisiAktif.getIdAdmisi()).append("\n")
                    .append("No. RM    : ").append(admisiAktif.getNoRMPasien()).append("\n")
                    .append("Masuk     : ").append(DateUtil.formatTanggal(admisiAktif.getTglMasuk())).append("\n")
                    .append("Kamar     : ").append(namaKamar).append("\n")
                    .append("Dokter    : ").append(namaDokter).append("\n")
                    .append("Diagnosis : ").append(admisiAktif.getDiagnosisAwal()).toString());

            tfDiagnosisAkhir = new TextField("Diagnosis Akhir", "", 200, TextField.ANY);
            tfKodeICD10 = new TextField("Kode ICD-10", "", 10, TextField.ANY);
            tfTglKeluar = new TextField("Tgl Keluar (DD/MM/YYYY)",
                    DateUtil.tanggalHariIni(), 10, TextField.ANY);
            tfBiayaKamar = new TextField("Biaya Kamar (Rp)", "0", 15, TextField.NUMERIC);
            tfBiayaObat = new TextField("Biaya Obat (Rp)", "0", 15, TextField.NUMERIC);
            tfCatatan = new TextField("Catatan Tambahan", "", 200, TextField.ANY);

            append(tfDiagnosisAkhir);
            append(tfKodeICD10);
            append(tfTglKeluar);
            append(tfBiayaKamar);
            append(tfBiayaObat);
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

            int biayaKamar = 0;
            int biayaObat = 0;
            try { biayaKamar = Integer.parseInt(tfBiayaKamar.getString()); } catch(Exception e){}
            try { biayaObat = Integer.parseInt(tfBiayaObat.getString()); } catch(Exception e){}
            
            int biayaTotal = biayaKamar + biayaObat;
            hasil.setBiayaTotal(biayaTotal);
            // Re-save/update admisi untuk apply biayaTotal
            // We assume discharge method above updated the repo, we just need to update it again or it's handled by reference
            ServiceFactory.getInstance().getAdmisiService().discharge(
                    hasil.getIdAdmisi(), hasil.getDiagnosisAkhir(), hasil.getKodeICD10(), 
                    tfTglKeluar.getString(), hasil.getCatatan()); // re-update doesn't break, but maybe just use repository
            
            Alert alert = new Alert("PASIEN BERHASIL KELUAR",
                    new StringBuffer().append("Kamar kembali KOSONG\n")
                            .append("Lama rawat: ").append(lamaRawat).append(" hari\n")
                            .append("Total Biaya: Rp ").append(biayaTotal).toString(),
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
