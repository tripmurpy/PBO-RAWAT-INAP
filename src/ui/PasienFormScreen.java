package ui;

import javax.microedition.lcdui.*;
import service.PasienService;
import model.Pasien;
import java.util.Date;
import util.DateUtil;
import util.ServiceFactory;

/**
 * PasienFormScreen — Form pendaftaran pasien baru.
 * INHERITANCE: Extends Form (J2ME LCDUI Form).
 * Menggunakan Form API bawaan untuk input field.
 */
public class PasienFormScreen extends Form implements CommandListener {

    private PasienService service;
    private TextField tfNama, tfAlamat, tfNoTelp;
    private DateField dfTglLahir;
    private ChoiceGroup cgJenisKelamin, cgAsuransi;
    private Command cmdDaftar, cmdBatal;

    public PasienFormScreen() {
        super("PENDAFTARAN PASIEN BARU");
        this.service = ServiceFactory.getInstance().getPasienService();

        // Input fields
        tfNama = new TextField("Nama Lengkap", "", 100, TextField.ANY);
        dfTglLahir = new DateField("Tgl Lahir", DateField.DATE);
        dfTglLahir.setDate(new Date());
        cgJenisKelamin = new ChoiceGroup("Jenis Kelamin", ChoiceGroup.EXCLUSIVE);
        cgJenisKelamin.append("Laki-laki (L)", null);
        cgJenisKelamin.append("Perempuan (P)", null);
        tfAlamat = new TextField("Alamat", "", 200, TextField.ANY);
        tfNoTelp = new TextField("No. Telepon", "", 15, TextField.PHONENUMBER);
        cgAsuransi = new ChoiceGroup("Asuransi", ChoiceGroup.EXCLUSIVE);
        cgAsuransi.append("BPJS", null);
        cgAsuransi.append("Asuransi Kesehatan Eka Hospital", null);
        cgAsuransi.append("Asuransi Swasta", null);

        append(tfNama);
        append(dfTglLahir);
        append(cgJenisKelamin);
        append(tfAlamat);
        append(tfNoTelp);
        append(cgAsuransi);

        // Commands
        cmdDaftar = new Command("Daftar", Command.OK, 1);
        cmdBatal = new Command("Batal", Command.BACK, 2);
        addCommand(cmdDaftar);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdDaftar) {
            prosesDaftar();
        }
    }

    private void prosesDaftar() {
        try {
            String jk = (cgJenisKelamin.getSelectedIndex() == 0) ? "L" : "P";
            String[] asuransiArr = {"BPJS", "Asuransi Kesehatan Eka Hospital", "Asuransi Swasta"};
            String asuransi = asuransiArr[cgAsuransi.getSelectedIndex()];
            Date d = dfTglLahir.getDate();
            String tglLahirStr = (d != null) ? DateUtil.formatTanggal(d.getTime()) : "";

            Pasien pasien = service.daftarPasienBaru(
                tfNama.getString(),
                tglLahirStr,
                jk,
                tfAlamat.getString(),
                tfNoTelp.getString(),
                asuransi
            );

            // Tampilkan alert sukses dengan tombol OK
            final Alert alert = new Alert("BERHASIL",
                new StringBuffer().append("Pasien berhasil didaftar!\n")
                .append("No. RM: ").append(pasien.getNoRM()).append("\n")
                .append("Nama: ").append(pasien.getNama()).toString(),
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

