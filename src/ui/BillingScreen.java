package ui;

import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Hashtable;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import model.Admisi;
import model.RekamMedis;
import model.Resep;
import model.Obat;
import util.ServiceFactory;

/**
 * BillingScreen — Mengkalkulasi dan mencetak biaya rawat inap pasien.
 */
public class BillingScreen extends Form implements CommandListener {

    private TextField tfIdAdmisi;
    private StringItem siHasil;
    private Command cmdKalkulasi, cmdSimpanTXT, cmdKembali;
    private String billingResultText = "";

    public BillingScreen() {
        super("BILLING & LAPORAN");

        tfIdAdmisi = new TextField("ID Admisi / No. RM", "", 20, TextField.ANY);
        siHasil = new StringItem("Hasil Kalkulasi", "Belum dikalkulasi");

        append(tfIdAdmisi);
        append(siHasil);

        cmdKalkulasi = new Command("Kalkulasi", Command.OK, 1);
        cmdSimpanTXT = new Command("Simpan TXT", Command.ITEM, 2);
        cmdKembali = new Command("Kembali", Command.BACK, 3);

        addCommand(cmdKalkulasi);
        addCommand(cmdKembali);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali) {
            ScreenManager.getInstance().kembali();
        } else if (c == cmdKalkulasi) {
            kalkulasiBilling();
        } else if (c == cmdSimpanTXT) {
            simpanLaporanTxt();
        }
    }

    private void kalkulasiBilling() {
        try {
            String id = tfIdAdmisi.getString().trim();
            if (id.length() == 0) throw new Exception("Masukkan ID Admisi");

            // Cari Admisi
            Admisi admisi = null;
            try {
                admisi = ServiceFactory.getInstance().getAdmisiService().cariAdmisiAktifByRM(id);
            } catch (Exception e){}

            if (admisi == null) {
                // Coba cari semua
                Vector semua = ServiceFactory.getInstance().getAdmisiService().getSemuaAdmisi();
                for (int i=0; i<semua.size(); i++) {
                    Admisi a = (Admisi) semua.elementAt(i);
                    if (a.getIdAdmisi().equals(id) || a.getNoRMPasien().equals(id)) {
                        admisi = a; break;
                    }
                }
            }

            if (admisi == null) throw new Exception("Admisi tidak ditemukan");

            int lamaRawat = ServiceFactory.getInstance().getAdmisiService().hitungLamaRawat(admisi);
            if (lamaRawat <= 0) lamaRawat = 1;

            // Biaya Kamar (Dummy Proxy)
            int hargaKamarPerHari = 200000; // default Kelas II
            try {
                model.Ruangan ruangan = ServiceFactory.getInstance().getRuanganService().cariById(admisi.getIdRuangan());
                if (ruangan.getTipeKamar().equals(model.Ruangan.TIPE_VIP)) hargaKamarPerHari = 500000;
                else if (ruangan.getTipeKamar().equals(model.Ruangan.TIPE_KELAS_1)) hargaKamarPerHari = 300000;
                else if (ruangan.getTipeKamar().equals(model.Ruangan.TIPE_KELAS_3)) hargaKamarPerHari = 100000;
            } catch (Exception e) {}

            int totalBiayaKamar = lamaRawat * hargaKamarPerHari;

            // Biaya Obat dari Rekam Medis
            int totalBiayaObat = 0;
            try {
                RekamMedis rm = ServiceFactory.getInstance().getRekamMedisService().cariByAdmisiId(admisi.getIdAdmisi());
                
                // Load all obat to map
                Vector listObat = ServiceFactory.getInstance().getObatService().getSemuaObat();
                Hashtable obatMap = new Hashtable();
                for (int i=0; i<listObat.size(); i++) {
                    Obat o = (Obat) listObat.elementAt(i);
                    obatMap.put(o.getId(), o);
                }

                Vector listResep = rm.getListResep();
                for (int i=0; i<listResep.size(); i++) {
                    Resep resep = (Resep) listResep.elementAt(i);
                    totalBiayaObat += resep.hitungTotalHarga(obatMap);
                }
            } catch (Exception e) {}

            int grandTotal = totalBiayaKamar + totalBiayaObat;
            admisi.setBiayaTotal(grandTotal); // Save to obj

            StringBuffer sb = new StringBuffer();
            sb.append("--- RINCIAN BILLING ---\n");
            sb.append("No. RM    : ").append(admisi.getNoRMPasien()).append("\n");
            sb.append("ID Admisi : ").append(admisi.getIdAdmisi()).append("\n");
            sb.append("Lama Rawat: ").append(lamaRawat).append(" hari\n");
            sb.append("Tarif/Hari: Rp ").append(hargaKamarPerHari).append("\n");
            sb.append("Biaya Kmr : Rp ").append(totalBiayaKamar).append("\n");
            sb.append("Biaya Obat: Rp ").append(totalBiayaObat).append("\n");
            sb.append("-----------------------\n");
            sb.append("TOTAL     : Rp ").append(grandTotal);

            billingResultText = sb.toString();
            siHasil.setText(billingResultText);

            addCommand(cmdSimpanTXT);

        } catch (Exception e) {
            siHasil.setText("Error: " + e.getMessage());
            removeCommand(cmdSimpanTXT);
        }
    }

    private void simpanLaporanTxt() {
        try {
            // FileConnection is JSR-75, will only work if API is supported by WTK
            // Fallback: Print to console and simulate saving
            System.out.println("=== EXPORT TXT ===");
            System.out.println(billingResultText);
            System.out.println("==================");

            Alert alert = new Alert("BERHASIL", "Laporan berhasil dicetak / diexport (Simulasi TXT)", null, AlertType.CONFIRMATION);
            alert.setTimeout(3000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        } catch (Exception e) {
            Alert alert = new Alert("ERROR", "Gagal menyimpan file: " + e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(4000);
            ScreenManager.getInstance().getDisplay().setCurrent(alert, this);
        }
    }
}
