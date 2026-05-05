package service;

import java.util.Vector;
import model.Obat;
import model.Pasien;
import model.Ruangan;
import model.Admisi;
import model.RekamMedis;
import model.CatatanHarian;

/**
 * LaporanService — Agregasi data untuk modul laporan.
 * Read-only: tidak ada side effects ke storage.
 */
public class LaporanService {

    private ObatService obatService;
    private PasienService pasienService;
    private RuanganService ruanganService;
    private AdmisiService admisiService;
    private RekamMedisService rekamMedisService;

    public LaporanService(ObatService obatService,
                          PasienService pasienService,
                          RuanganService ruanganService,
                          AdmisiService admisiService,
                          RekamMedisService rekamMedisService) {
        this.obatService = obatService;
        this.pasienService = pasienService;
        this.ruanganService = ruanganService;
        this.admisiService = admisiService;
        this.rekamMedisService = rekamMedisService;
    }

    /** Laporan occupancy ruangan. */
    public Vector getLaporanRuangan() throws Exception {
        return ruanganService.getSemuaRuangan();
    }

    /** Laporan stok obat semua. */
    public Vector getLaporanStokObat() throws Exception {
        return obatService.getSemuaObat();
    }

    /** List obat dengan stok di bawah minimum. */
    public Vector getLowStockObat() throws Exception {
        return obatService.getLowStock();
    }

    /** Laporan pasien aktif (sedang dirawat). */
    public Vector getPasienAktif() throws Exception {
        Vector semua = admisiService.getAdmisiAktif();
        return semua;
    }

    /**
     * Statistik diagnosa terbanyak dari rekam medis.
     * Return: Vector of String[] {diagnosa, count}
     */
    public Vector getTopDiagnosa(int topN) throws Exception {
        Vector allRM = rekamMedisService.getSemuaRekamMedis();
        // Count diagnosa occurrences
        Vector diagnosaList = new Vector();
        Vector countList = new Vector();

        for (int i = 0; i < allRM.size(); i++) {
            RekamMedis rm = (RekamMedis) allRM.elementAt(i);
            Vector catatan = rm.getListCatatan();
            for (int j = 0; j < catatan.size(); j++) {
                CatatanHarian c = (CatatanHarian) catatan.elementAt(j);
                String diag = c.getDiagnosa();
                if (diag == null || diag.trim().length() == 0) continue;
                diag = diag.trim();

                int idx = diagnosaList.indexOf(diag);
                if (idx >= 0) {
                    int count = ((Integer) countList.elementAt(idx)).intValue() + 1;
                    countList.setElementAt(new Integer(count), idx);
                } else {
                    diagnosaList.addElement(diag);
                    countList.addElement(new Integer(1));
                }
            }
        }

        // Build result array, sort by count descending (bubble sort)
        int size = diagnosaList.size();
        String[] diagArr = new String[size];
        int[] cntArr = new int[size];
        for (int i = 0; i < size; i++) {
            diagArr[i] = (String) diagnosaList.elementAt(i);
            cntArr[i] = ((Integer) countList.elementAt(i)).intValue();
        }
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (cntArr[j] < cntArr[j + 1]) {
                    int tmpC = cntArr[j]; cntArr[j] = cntArr[j + 1]; cntArr[j + 1] = tmpC;
                    String tmpD = diagArr[j]; diagArr[j] = diagArr[j + 1]; diagArr[j + 1] = tmpD;
                }
            }
        }

        Vector result = new Vector();
        int limit = Math.min(topN, size);
        for (int i = 0; i < limit; i++) {
            result.addElement(new String[]{diagArr[i], String.valueOf(cntArr[i])});
        }
        return result;
    }
}
