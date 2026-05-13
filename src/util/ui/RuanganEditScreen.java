package util.ui;

import javax.microedition.lcdui.*;
import model.Ruangan;
import util.ServiceFactory;

/**
 * RuanganEditScreen — Form edit data ruangan.
 */
public class RuanganEditScreen extends Form implements CommandListener {

    private Ruangan ruangan;
    private TextField tfNama, tfLantai, tfHarga, tfFasilitas;
    private ChoiceGroup cgTipe;
    private Command cmdSimpan, cmdBatal;

    public RuanganEditScreen(Ruangan r) {
        super("EDIT RUANGAN: " + r.getNamaRuangan());
        this.ruangan = r;

        tfNama = new TextField("Nama Ruangan", r.getNamaRuangan(), 10, TextField.ANY);
        cgTipe = new ChoiceGroup("Tipe Kamar", ChoiceGroup.POPUP);
        cgTipe.append("VIP", null);
        cgTipe.append("VVIP", null);
        cgTipe.append("Kelas I", null);
        cgTipe.append("Kelas II", null);
        cgTipe.append("Kelas III", null);
        
        // Select current type
        for (int i = 0; i < cgTipe.size(); i++) {
            if (cgTipe.getString(i).equals(r.getTipeKamar())) {
                cgTipe.setSelectedIndex(i, true);
                break;
            }
        }

        tfLantai = new TextField("Lantai", "1", 2, TextField.NUMERIC);
        tfHarga = new TextField("Harga Per Malam (Rp)", String.valueOf((long)r.getHarga()), 15, TextField.NUMERIC);
        tfFasilitas = new TextField("Fasilitas", r.getFasilitas(), 100, TextField.ANY);

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
            ruangan.setNamaRuangan(tfNama.getString());
            ruangan.setTipeKamar(cgTipe.getString(cgTipe.getSelectedIndex()));
            ruangan.setHarga(Double.parseDouble(tfHarga.getString()));
            ruangan.setFasilitas(tfFasilitas.getString());

            ServiceFactory.getInstance().getRuanganService().updateRuangan(ruangan);

            Alert alert = new Alert("BERHASIL", "Data ruangan berhasil diperbarui.", null, AlertType.CONFIRMATION);
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
