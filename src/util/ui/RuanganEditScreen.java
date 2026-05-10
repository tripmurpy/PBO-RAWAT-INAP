package util.ui;

import javax.microedition.lcdui.*;
import model.Ruangan;
import service.RuanganService;
import util.ServiceFactory;

/**
 * RuanganEditScreen — Form untuk mengedit ruangan.
 */
public class RuanganEditScreen extends Form implements CommandListener {

    private RuanganService service;
    private Ruangan ruangan;
    private TextField tfNama, tfKapasitas, tfNamaPasien;
    private ChoiceGroup cgStatus;
    private Command cmdSimpan, cmdBatal;

    public RuanganEditScreen(Ruangan r) {
        super("EDIT KAMAR: " + r.getNamaRuangan());
        this.service = ServiceFactory.getInstance().getRuanganService();
        this.ruangan = r;

        tfNama = new TextField("Nama/Nomor Kamar", r.getNamaRuangan(), 20, TextField.ANY);
        tfKapasitas = new TextField("Kapasitas", String.valueOf(r.getKapasitas()), 3, TextField.NUMERIC);
        
        cgStatus = new ChoiceGroup("Status", ChoiceGroup.EXCLUSIVE);
        cgStatus.append(Ruangan.STATUS_KOSONG, null);
        cgStatus.append(Ruangan.STATUS_TERISI, null);
        cgStatus.append(Ruangan.STATUS_MAINTENANCE, null);
        
        if (r.getStatusKamar().equals(Ruangan.STATUS_TERISI)) {
            cgStatus.setSelectedIndex(1, true);
        } else if (r.getStatusKamar().equals(Ruangan.STATUS_MAINTENANCE)) {
            cgStatus.setSelectedIndex(2, true);
        } else {
            cgStatus.setSelectedIndex(0, true);
        }

        tfNamaPasien = new TextField("Nama Pasien (Jika Terisi)", r.getNamaPasien(), 50, TextField.ANY);

        append(tfNama);
        append(tfKapasitas);
        append(cgStatus);
        append(tfNamaPasien);

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
                ruangan.setNamaRuangan(tfNama.getString());
                ruangan.setKapasitas(Integer.parseInt(tfKapasitas.getString()));
                String status = cgStatus.getString(cgStatus.getSelectedIndex());
                ruangan.setStatusKamar(status);
                
                if (status.equals(Ruangan.STATUS_KOSONG)) {
                    ruangan.setKosong(true);
                } else if (status.equals(Ruangan.STATUS_TERISI)) {
                    ruangan.setNamaPasien(tfNamaPasien.getString());
                } else {
                    ruangan.setNamaPasien("");
                }
                
                service.updateRuangan(ruangan);
                
                Alert alert = new Alert("BERHASIL", "Kamar berhasil diupdate!", null, AlertType.CONFIRMATION);
                alert.setTimeout(2000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, new RuanganScreen());
            } catch (Exception e) {
                Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(3000);
                ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
            }
        }
    }
}
