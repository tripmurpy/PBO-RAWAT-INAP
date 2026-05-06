package ui;

import javax.microedition.lcdui.*;
import model.Obat;
import model.ItemObat;
import service.RekamMedisService;
import util.ServiceFactory;

/**
 * ObatPemberianScreen — Form untuk memberikan obat kepada pasien.
 */
public class ObatPemberianScreen extends Form implements CommandListener {

    private Obat obat;
    private TextField tfNoRM, tfAturanPakai, tfJumlah;
    private Command cmdSelesai, cmdBatal;

    public ObatPemberianScreen(Obat obat) {
        super("BERIKAN OBAT: " + obat.getNama());
        this.obat = obat;

        tfNoRM = new TextField("No. RM Pasien", "", 20, TextField.ANY);
        tfAturanPakai = new TextField("Aturan Pakai", "3 x 1 sesudah makan", 50, TextField.ANY);
        tfJumlah = new TextField("Jumlah Obat", "10", 5, TextField.NUMERIC);

        append(new StringItem("Obat", obat.getNama() + " (" + obat.getSatuan() + ")"));
        append(new StringItem("Stok Tersedia", String.valueOf(obat.getStok())));
        append(tfNoRM);
        append(tfAturanPakai);
        append(tfJumlah);

        cmdSelesai = new Command("Selesai", Command.OK, 1);
        cmdBatal = new Command("Batal", Command.BACK, 2);

        addCommand(cmdSelesai);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().getDisplay().setCurrent(new ObatScreen());
        } else if (c == cmdSelesai) {
            try {
                String noRM = tfNoRM.getString().trim();
                String aturan = tfAturanPakai.getString().trim();
                int jumlah = Integer.parseInt(tfJumlah.getString());

                if (jumlah > obat.getStok()) {
                    throw new Exception("Stok tidak mencukupi");
                }

                // Create ItemObat and ideally save to Resep/RekamMedis
                ItemObat io = new ItemObat(obat.getId(), obat.getNama(), jumlah, aturan);
                
                // For now, we simulate saving and decreasing stock
                obat.setStok(obat.getStok() - jumlah);
                ServiceFactory.getInstance().getObatService().updateObat(obat);

                Alert alert = new Alert("BERHASIL", "Obat " + obat.getNama() + " diberikan kepada " + noRM, null, AlertType.CONFIRMATION);
                alert.setTimeout(2000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, new ObatScreen());
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        }
    }
}
