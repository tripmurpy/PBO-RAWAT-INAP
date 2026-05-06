package ui;

import javax.microedition.lcdui.*;
import service.DokterService;
import util.ServiceFactory;

/**
 * DokterFormScreen — Form manajemen dokter (tambah).
 * INHERITANCE: Extends Form.
 */
public class DokterFormScreen extends Form implements CommandListener {

    private DokterService dokterService;
    private TextField tfNama, tfJadwal;
    private ChoiceGroup cgKeahlian;
    private Command cmdSimpan, cmdBatal;

    public DokterFormScreen() {
        super("TAMBAH DOKTER BARU");
        this.dokterService = ServiceFactory.getInstance().getDokterService();

        tfNama = new TextField("Nama Dokter", "", 100, TextField.ANY);
        
        cgKeahlian = new ChoiceGroup("Keahlian", ChoiceGroup.EXCLUSIVE);
        cgKeahlian.append("Umum", null);
        cgKeahlian.append("Penyakit Dalam", null);
        cgKeahlian.append("Bedah", null);
        cgKeahlian.append("Anak", null);
        cgKeahlian.append("Kandungan (Obgyn)", null);
        cgKeahlian.append("Saraf", null);

        tfJadwal = new TextField("Jadwal Praktik", "Senin-Jumat, 08:00-16:00", 100, TextField.ANY);

        append(tfNama);
        append(cgKeahlian);
        append(tfJadwal);

        cmdSimpan = new Command("Simpan", Command.OK, 1);
        cmdBatal = new Command("Batal", Command.BACK, 2);

        addCommand(cmdSimpan);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdSimpan) {
            try {
                String keahlian = cgKeahlian.getString(cgKeahlian.getSelectedIndex());
                model.Dokter dokter = dokterService.tambahDokter(
                        tfNama.getString(), keahlian,
                        tfJadwal.getString());

                Alert alert = new Alert("BERHASIL",
                        new StringBuffer().append("Dokter berhasil ditambahkan!\nID: ").append(dokter.getId())
                                .toString(),
                        null, AlertType.CONFIRMATION);
                alert.setTimeout(3000);

                tfNama.setString("");
                tfJadwal.setString("");

                ScreenManager.getInstance().getDisplay().setCurrent(alert, new DokterScreen());
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        }
    }
}
