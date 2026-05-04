package service;

import java.util.Vector;
import model.Admisi;
import model.Kunjungan;
import storage.AdmisiDB;
import util.DateUtil;
import util.IDGenerator;

/**
 * KunjunganService — Logika bisnis riwayat kunjungan.
 * Membuat dan menutup catatan kunjungan perawatan.
 */
public class KunjunganService {

    private AdmisiDB admisiDB;

    public KunjunganService() {
        this.admisiDB = new AdmisiDB();
    }

    /**
     * Membuat Kunjungan dari data Admisi.
     */
    public Kunjungan buatKunjungan(Admisi admisi, String namaDokter, 
                                    String namaRuangan) {
        String id = IDGenerator.generateKunjunganId();
        Kunjungan knj = new Kunjungan(id, admisi.getIdAdmisi(),
                admisi.getNoRMPasien(), admisi.getTglMasuk(),
                admisi.getDiagnosisAwal(), namaDokter, namaRuangan);
        return knj;
    }

    /**
     * Menutup kunjungan saat discharge.
     */
    public void tutupKunjungan(Kunjungan knj, long tglKeluar, 
                                String diagnosisAkhir, String kodeICD10) {
        knj.setTglKeluar(tglKeluar);
        knj.setDiagnosis(diagnosisAkhir);
        knj.setKodeICD10(kodeICD10);
        knj.setStatus(Kunjungan.STATUS_SELESAI);
        knj.setLamaRawat(DateUtil.hitungSelisihHari(knj.getTglMasuk(), tglKeluar));
    }

    /**
     * Mengambil riwayat kunjungan dari semua admisi.
     * Mengkonversi Admisi menjadi Kunjungan untuk ditampilkan.
     */
    public Vector getRiwayatKunjungan() throws Exception {
        Vector kunjunganList = new Vector();
        Vector admisiList = admisiDB.getAll();

        for (int i = 0; i < admisiList.size(); i++) {
            Admisi a = (Admisi) admisiList.elementAt(i);
            Kunjungan knj = new Kunjungan();
            knj.setId("KNJ-" + (i + 1));
            knj.setIdAdmisi(a.getIdAdmisi());
            knj.setNoRMPasien(a.getNoRMPasien());
            knj.setTglMasuk(a.getTglMasuk());
            knj.setTglKeluar(a.getTglKeluar());
            knj.setDiagnosis(a.getDiagnosisAwal());
            knj.setKodeICD10(a.getKodeICD10() != null ? a.getKodeICD10() : "");
            knj.setStatus(a.isAktif() ? Kunjungan.STATUS_BERLANGSUNG 
                                       : Kunjungan.STATUS_SELESAI);
            if (a.getTglKeluar() > 0) {
                knj.setLamaRawat(DateUtil.hitungSelisihHari(
                    a.getTglMasuk(), a.getTglKeluar()));
            }
            kunjunganList.addElement(knj);
        }
        return kunjunganList;
    }
}
