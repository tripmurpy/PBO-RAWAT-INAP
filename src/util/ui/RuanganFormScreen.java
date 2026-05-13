package util.ui;

import javax.microedition.lcdui.*;
import util.ServiceFactory;

/**
 * RuanganFormScreen — Form tambah ruangan baru.
 * INHERITANCE: Extends Form.
 */
public class RuanganFormScreen extends Form implements CommandListener {

    private TextField tfNama, tfLantai, tfHarga, tfFasilitas;
    private ChoiceGroup cgTipe;
    private Command cmdSimpan, cmdBatal;

    public RuanganFormScreen() {
        super("TAMBAH RUANGAN");

        tfNama = new TextField("Nama Ruangan", "", 10, TextField.ANY);
        cgTipe = new ChoiceGroup("Tipe Kamar", ChoiceGroup.POPUP);
        cgTipe.append("VIP", null);
        cgTipe.append("VVIP", null);
        cgTipe.append("Kelas I", null);
        cgTipe.append("Kelas II", null);
        cgTipe.append("Kelas III", null);
        
        tfLantai = new TextField("Lantai", "1", 2, TextField.NUMERIC);
        tfHarga = new TextField("Harga Per Malam (Rp)", "0", 15, TextField.NUMERIC);
        tfFasilitas = new TextField("Fasilitas", "", 100, TextField.ANY);

        append(tfNama);
        append(cgTipe);
        append(tfLantai);
        append(tfHarga);
        append(tfFasilitas);

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
            prosesSimpan();
        }
    }

    private void prosesSimpan() {
        try {
            String nama = tfNama.getString();
            String tipe = cgTipe.getString(cgTipe.getSelectedIndex());
            int lantai = Integer.parseInt(tfLantai.getString());
            double harga = Double.parseDouble(tfHarga.getString());
            String fasilitas = tfFasilitas.getString();

            ServiceFactory.getInstance().getRuanganService().tambahRuangan(nama, tipe, lantai, harga, fasilitas);

            Alert alert = new Alert("BERHASIL", "Ruangan " + nama + " berhasil ditambahkan.", null, AlertType.CONFIRMATION);
            alert.setTimeout(2000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert);
            ScreenManager.getInstance().kembali();

        } catch (Exception e) {
            Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(3000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        }
    }
}
