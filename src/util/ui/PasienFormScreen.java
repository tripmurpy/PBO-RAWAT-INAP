package util.ui;

import javax.microedition.lcdui.*;
import service.PasienService;
import model.Pasien;
import model.Dokter;
import util.DateUtil;
import util.ServiceFactory;
import java.util.Vector;

/**
 * PasienFormScreen — Form pendaftaran pasien baru.
 * Update: Menggunakan ChoiceGroup untuk Gender, Asuransi, dan Dokter.
 */
public class PasienFormScreen extends Form implements CommandListener {

    private PasienService service;
    private TextField tfNama, tfTglLahir, tfAlamat, tfNoTelp, tfKeluhan;
    private TextField tfNamaWali, tfNoTelpWali;
    private ChoiceGroup cgGender, cgAsuransi, cgDokter;
    
    private StringItem siKamar;
    private StringItem btnPilihKamar;
    private Command cmdPilihKamar, cmdDaftar, cmdBatal;
    
    private String kamarTerpilihId = "";
    private String kamarTerpilihNama = "";

    public PasienFormScreen() {
        super("PENDAFTARAN PASIEN BARU");
        this.service = ServiceFactory.getInstance().getPasienService();

        // 1. Nama Pasien & Wali
        tfNama = new TextField("Nama Pasien", "", 30, TextField.ANY);
        tfNamaWali = new TextField("Nama Wali Pasien", "", 30, TextField.ANY);
        
        // 2. Tgl Lahir & Gender
        tfTglLahir = new TextField("Tgl Lahir (YYYY-MM-DD)", "", 10, TextField.ANY);
        cgGender = new ChoiceGroup("Jenis Kelamin", ChoiceGroup.POPUP);
        cgGender.append("Laki-laki", null);
        cgGender.append("Perempuan", null);
        
        // 3. Kontak & Alamat
        tfAlamat = new TextField("Alamat", "", 50, TextField.ANY);
        tfNoTelp = new TextField("Nomor Telepon Pasien", "", 15, TextField.PHONENUMBER);
        tfNoTelpWali = new TextField("Nomor Telepon Wali", "", 15, TextField.PHONENUMBER);
        
        // 4. Asuransi (Choice)
        cgAsuransi = new ChoiceGroup("Asuransi", ChoiceGroup.POPUP);
        cgAsuransi.append("Pribadi / Cash", null);
        cgAsuransi.append("BPJS Kesehatan", null);
        cgAsuransi.append("Mandiri Inhealth", null);
        cgAsuransi.append("Prudential", null);
        cgAsuransi.append("Allianz", null);
        
        // 5. Dokter (Choice dari Database)
        cgDokter = new ChoiceGroup("Dokter Penanggung Jawab", ChoiceGroup.POPUP);
        muatDaftarDokter();

        // 6. Keluhan
        tfKeluhan = new TextField("Keluhan Utama", "", 100, TextField.ANY);

        // 7. Kamar
        siKamar = new StringItem("Kamar Rawat", "Belum dipilih");
        cmdPilihKamar = new Command("Pilih", Command.ITEM, 1);
        btnPilihKamar = new StringItem("", "[ Pilih Kamar ]", Item.BUTTON);
        btnPilihKamar.addCommand(cmdPilihKamar);
        btnPilihKamar.setItemCommandListener(new ItemCommandListener() {
            public void commandAction(Command c, Item item) {
                ScreenManager.getInstance().tampilkanLayar(new KamarSelectionScreen(PasienFormScreen.this));
            }
        });
        btnPilihKamar.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_BEFORE);

        // Append items in order
        append(tfNama);
        append(tfNamaWali);
        append(tfTglLahir);
        append(cgGender);
        append(tfAlamat);
        append(tfNoTelp);
        append(tfNoTelpWali);
        append(cgAsuransi);
        append(tfKeluhan);
        append(cgDokter);
        append(siKamar);
        append(btnPilihKamar);

        cmdDaftar = new Command("Daftar", Command.OK, 1);
        cmdBatal = new Command("Batal", Command.BACK, 2);
        addCommand(cmdDaftar);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    private void muatDaftarDokter() {
        try {
            Vector v = ServiceFactory.getInstance().getDokterService().getSemuaDokter();
            if (v.size() == 0) {
                cgDokter.append("Belum ada dokter", null);
            } else {
                for (int i = 0; i < v.size(); i++) {
                    Dokter d = (Dokter) v.elementAt(i);
                    cgDokter.append(d.getNama() + " (" + d.getSpesialisasi() + ")", null);
                }
            }
        } catch (Exception e) {
            cgDokter.append("Gagal muat dokter", null);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) ScreenManager.getInstance().kembali();
        else if (c == cmdDaftar) prosesDaftar();
    }

    public void setKamarTerpilih(String id, String nama) {
        this.kamarTerpilihId = id;
        int idx = nama.indexOf(" (");
        this.kamarTerpilihNama = (idx != -1) ? nama.substring(0, idx) : nama;
        this.siKamar.setText(nama);
        this.btnPilihKamar.setText("[ Ganti Kamar ]");
    }

    private void prosesDaftar() {
        try {
            // Get selected values from choices
            String selectedGender = cgGender.getString(cgGender.getSelectedIndex());
            String gender = selectedGender.equals("Laki-laki") ? "L" : "P";
            String asuransi = cgAsuransi.getString(cgAsuransi.getSelectedIndex());
            
            String dokterFull = cgDokter.getString(cgDokter.getSelectedIndex());
            String dokterName = dokterFull;
            int idx = dokterFull.indexOf(" (");
            if (idx != -1) dokterName = dokterFull.substring(0, idx);

            Pasien pasien = service.daftarPasienBaru(
                tfNama.getString(),
                tfTglLahir.getString(),
                gender,
                tfAlamat.getString(),
                tfNoTelp.getString(),
                asuransi,
                dokterName,
                kamarTerpilihNama,
                tfKeluhan.getString(),
                tfNamaWali.getString(),
                tfNoTelpWali.getString()
            );

            if (!kamarTerpilihId.equals("")) {
                ServiceFactory.getInstance().getRuanganService().isiKamar(kamarTerpilihId, pasien.getNama(), "");
            }

            Alert alert = new Alert("BERHASIL", "Pasien " + pasien.getNama() + " berhasil didaftar!", null, AlertType.CONFIRMATION);
            alert.setTimeout(Alert.FOREVER);
            Command ok = new Command("OK", Command.OK, 1);
            alert.addCommand(ok);
            alert.setCommandListener(new CommandListener() {
                public void commandAction(Command cmd, Displayable disp) { ScreenManager.getInstance().kembali(); }
            });
            ScreenManager.getInstance().getDisplay().setCurrent(alert);

        } catch (Exception e) {
            Alert a = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
            a.setTimeout(3000);
            ScreenManager.getInstance().getDisplay().setCurrent(a, this);
        }
    }
}
