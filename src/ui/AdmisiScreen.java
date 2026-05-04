package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import service.AdmisiService;
import service.PasienService;
import service.DokterService;
import service.RuanganService;
import model.Pasien;
import model.Dokter;
import model.Ruangan;
import model.Admisi;
import util.DateUtil;
import util.ServiceFactory;

/**
 * AdmisiScreen — Form rawat inap baru.
 * INHERITANCE: Extends Form.
 */
public class AdmisiScreen extends Form implements CommandListener {

    private AdmisiService admisiService;
    private PasienService pasienService;
    private DokterService dokterService;
    private RuanganService ruanganService;

    private TextField tfNoRM, tfDiagnosis, tfTglMasuk;
    private ChoiceGroup cgDokter, cgRuangan;
    private StringItem siPasienInfo;
    private Command cmdCari, cmdProses, cmdBatal;

    private Pasien pasienDitemukan;
    private Vector listDokter, listRuangan;

    public AdmisiScreen() {
        super("RAWAT INAP BARU");
        ServiceFactory sf = ServiceFactory.getInstance();
        this.admisiService = sf.getAdmisiService();
        this.pasienService = sf.getPasienService();
        this.dokterService = sf.getDokterService();
        this.ruanganService = sf.getRuanganService();

        tfNoRM = new TextField("No. RM Pasien", "", 20, TextField.ANY);
        siPasienInfo = new StringItem("Info Pasien", "Belum dicari");

        append(tfNoRM);
        append(siPasienInfo);

        cmdCari = new Command("Cari Pasien", Command.OK, 1);
        cmdProses = new Command("Proses Admisi", Command.OK, 2);
        cmdBatal = new Command("Batal", Command.BACK, 3);
        addCommand(cmdCari);
        addCommand(cmdBatal);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBatal) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdCari) {
            cariPasien();
        } else if (c == cmdProses) {
            prosesAdmisi();
        }
    }

    private void cariPasien() {
        try {
            pasienDitemukan = pasienService.cariByRM(tfNoRM.getString().trim());
            StringBuffer sb = new StringBuffer();
            sb.append("Nama    : ").append(pasienDitemukan.getNama()).append("\n");
            sb.append("Tgl Lhr : ").append(DateUtil.formatTanggal(pasienDitemukan.getTglLahir())).append("\n");
            sb.append("Asuransi: ").append(pasienDitemukan.getAsuransi());
            siPasienInfo.setText(sb.toString());

            // Tampilkan form lengkap admisi
            tampilkanFormAdmisi();
        } catch (Exception e) {
            siPasienInfo.setText(new StringBuffer().append("Error: ").append(e.getMessage()).toString());
        }
    }

    private void tampilkanFormAdmisi() {
        try {
            // Dokter
            cgDokter = new ChoiceGroup("Dokter PJ", ChoiceGroup.EXCLUSIVE);
            listDokter = dokterService.getSemuaDokter();
            for (int i = 0; i < listDokter.size(); i++) {
                Dokter dk = (Dokter) listDokter.elementAt(i);
                StringBuffer sb = new StringBuffer();
                sb.append(dk.getNama()).append(" (").append(dk.getSpesialisasi()).append(")");
                cgDokter.append(sb.toString(), null);
            }

            // Ruangan tersedia
            cgRuangan = new ChoiceGroup("Kamar Tersedia", ChoiceGroup.EXCLUSIVE);
            listRuangan = ruanganService.cariKamarTersedia(null);
            for (int i = 0; i < listRuangan.size(); i++) {
                Ruangan rn = (Ruangan) listRuangan.elementAt(i);
                StringBuffer sb = new StringBuffer();
                sb.append(rn.getNamaRuangan()).append(" (").append(rn.getTipeKamar()).append(")");
                cgRuangan.append(sb.toString(), null);
            }

            tfDiagnosis = new TextField("Diagnosis Awal", "", 200, TextField.ANY);
            tfTglMasuk = new TextField("Tgl Masuk (DD/MM/YYYY)", 
                                       DateUtil.tanggalHariIni(), 10, TextField.ANY);

            append(cgDokter);
            append(cgRuangan);
            append(tfDiagnosis);
            append(tfTglMasuk);

            removeCommand(cmdCari);
            addCommand(cmdProses);
        } catch (Exception e) {
            Alert alert = new Alert("ERROR", e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(3000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        }
    }

    private void prosesAdmisi() {
        try {
            if (listDokter == null || listDokter.size() == 0) {
                throw new Exception("Belum ada data dokter");
            }
            if (listRuangan == null || listRuangan.size() == 0) {
                throw new Exception("Tidak ada kamar tersedia");
            }

            Dokter dkPilih = (Dokter) listDokter.elementAt(cgDokter.getSelectedIndex());
            Ruangan rnPilih = (Ruangan) listRuangan.elementAt(cgRuangan.getSelectedIndex());

            Admisi admisi = admisiService.buatAdmisi(
                pasienDitemukan.getNoRM(),
                dkPilih.getId(),
                rnPilih.getId(),
                tfDiagnosis.getString(),
                tfTglMasuk.getString()
            );

            StringBuffer sb = new StringBuffer();
            sb.append("ID Admisi : ").append(admisi.getIdAdmisi()).append("\n");
            sb.append("Pasien    : ").append(pasienDitemukan.getNama()).append("\n");
            sb.append("Kamar     : ").append(rnPilih.getNamaRuangan()).append("\n");
            sb.append("Dokter    : ").append(dkPilih.getNama());

            Alert alert = new Alert("ADMISI BERHASIL",
                sb.toString(),
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
