package util.ui;

import javax.microedition.lcdui.*;
import service.PasienService;
import model.Pasien;
import java.util.Date;
import util.DateUtil;
import util.ServiceFactory;
import java.util.Vector;

/**
 * PasienFormScreen — Form pendaftaran pasien baru.
 * INHERITANCE: Extends Form (J2ME LCDUI Form).
 * Menggunakan Form API bawaan untuk input field.
 */
public class PasienFormScreen extends Form implements CommandListener {

    private PasienService service;
    private TextField tfNama, tfAlamat, tfNoTelp, tfTglLahir;
    private ChoiceGroup cgJenisKelamin, cgAsuransi, cgDokter;
    private StringItem siKamar;       // label tampil nama kamar terpilih
    private StringItem btnPilihKamar; // tombol langsung bisa diklik
    private Command cmdPilihKamar, cmdDaftar, cmdBatal;
    
    private String kamarTerpilihId = "";
    private String kamarTerpilihNama = "";
    private Vector listDokter;

    public PasienFormScreen() {
        super("PENDAFTARAN PASIEN BARU");
        this.service = ServiceFactory.getInstance().getPasienService();

        // Input fields
        tfNama = new TextField("Nama Lengkap", "", 100, TextField.ANY);
        tfTglLahir = new TextField("Tgl Lahir (DD/MM/YYYY)", DateUtil.tanggalHariIni(), 20, TextField.ANY);
        cgJenisKelamin = new ChoiceGroup("Jenis Kelamin", ChoiceGroup.POPUP);
        cgJenisKelamin.append("Laki-laki (L)", null);
        cgJenisKelamin.append("Perempuan (P)", null);
        tfAlamat = new TextField("Alamat", "", 200, TextField.ANY);
        tfNoTelp = new TextField("No. Telepon", "", 15, TextField.PHONENUMBER);
        cgAsuransi = new ChoiceGroup("Asuransi", ChoiceGroup.POPUP);
        cgAsuransi.append("BPJS", null);
        cgAsuransi.append("Asuransi Kesehatan Eka Hospital", null);
        cgAsuransi.append("Asuransi Swasta", null);

        // Dropdown Dokter
        cgDokter = new ChoiceGroup("Dokter Penanggung Jawab", ChoiceGroup.POPUP);
        try {
            listDokter = ServiceFactory.getInstance().getDokterService().getSemuaDokter();
            for(int i=0; i<listDokter.size(); i++) {
                model.Dokter d = (model.Dokter)listDokter.elementAt(i);
                cgDokter.append(d.getNama() + " (" + d.getSpesialisasi() + ")", null);
            }
        } catch(Exception e) {}

        // Label kamar terpilih (display only)
        siKamar = new StringItem("Kamar Rawat", "Belum dipilih");

        // Tombol PILIH KAMAR — Item.BUTTON agar langsung bisa diklik / ditap
        cmdPilihKamar = new Command("Pilih", Command.ITEM, 1);
        btnPilihKamar = new StringItem("", "[ Pilih Kamar ]", Item.BUTTON);
        btnPilihKamar.addCommand(cmdPilihKamar);
        btnPilihKamar.setItemCommandListener(new ItemCommandListener() {
            public void commandAction(Command c, Item item) {
                ScreenManager.getInstance().tampilkanLayar(new KamarSelectionScreen(PasienFormScreen.this));
            }
        });
        btnPilihKamar.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_NEWLINE_AFTER);

        append(tfNama);
        append(tfTglLahir);
        append(cgJenisKelamin);
        append(tfAlamat);
        append(tfNoTelp);
        append(cgAsuransi);
        append(cgDokter);
        append(siKamar);
        append(btnPilihKamar);

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

    public void setKamarTerpilih(String id, String nama) {
        this.kamarTerpilihId = id;
        this.kamarTerpilihNama = nama;
        this.siKamar.setText(nama);           // update label nama kamar
        this.btnPilihKamar.setText("[ Ganti Kamar ]"); // ubah label tombol setelah dipilih
    }

    private void prosesDaftar() {
        try {
            String jk = (cgJenisKelamin.getSelectedIndex() == 0) ? "L" : "P";
            String[] asuransiArr = {"BPJS", "Asuransi Kesehatan Eka Hospital", "Asuransi Swasta"};
            String asuransi = asuransiArr[cgAsuransi.getSelectedIndex()];
            String tglLahirStr = tfTglLahir.getString();
            
            String dokterNama = "";
            if (cgDokter.size() > 0 && listDokter != null && listDokter.size() > 0) {
                model.Dokter d = (model.Dokter)listDokter.elementAt(cgDokter.getSelectedIndex());
                dokterNama = d.getNama();
            }

            Pasien pasien = service.daftarPasienBaru(
                tfNama.getString(),
                tglLahirStr,
                jk,
                tfAlamat.getString(),
                tfNoTelp.getString(),
                asuransi,
                dokterNama,
                kamarTerpilihNama
            );

            // Update status kamar menjadi TERPAKAI
            if (!kamarTerpilihId.equals("")) {
                ServiceFactory.getInstance().getRuanganService().isiKamar(kamarTerpilihId, pasien.getNama(), "");
            }

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

