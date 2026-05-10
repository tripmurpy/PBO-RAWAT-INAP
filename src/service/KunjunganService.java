package service;

import java.util.Vector;
import model.Admisi;
import model.Kunjungan;
import model.repository.IAdmisiRepository;
import util.DateUtil;

/**
 * KunjunganService — Mengelola riwayat kunjungan pasien.
 */
public class KunjunganService {
    private IAdmisiRepository admisiRepo;

    public KunjunganService(IAdmisiRepository admisiRepo) {
        this.admisiRepo = admisiRepo;
    }

    /**
     * Mendapatkan semua riwayat kunjungan (konversi dari Admisi).
     */
    public Vector getRiwayatKunjungan() throws Exception {
        Vector allAdmisi = admisiRepo.getAll();
        Vector listKunjungan = new Vector();
        
        for (int i = 0; i < allAdmisi.size(); i++) {
            Admisi a = (Admisi) allAdmisi.elementAt(i);
            
            int lamaRawat = 0;
            if (a.getTglKeluar() > 0) {
                lamaRawat = DateUtil.hitungSelisihHari(a.getTglMasuk(), a.getTglKeluar());
            } else {
                lamaRawat = DateUtil.hitungSelisihHari(a.getTglMasuk(), DateUtil.sekarang());
            }
            
            Kunjungan k = new Kunjungan(
                a.getIdAdmisi(),
                a.getNoRMPasien(),
                a.getTglMasuk(),
                a.getTglKeluar(),
                a.getStatus(),
                a.isAktif() ? a.getDiagnosisAwal() : a.getDiagnosisAkhir(),
                lamaRawat,
                a.getBiayaTotal(),
                a.getBiayaRuangan(),
                a.getBiayaMakanan(),
                a.getBiayaObat(),
                a.getBiayaAdmin(),
                a.getTipePembayaran(),
                a.getNamaBank()
            );
            listKunjungan.addElement(k);
        }
        
        return listKunjungan;
    }
}
