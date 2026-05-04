package service;

import java.util.Vector;
import model.Admisi;
import model.Pasien;
import model.Dokter;
import model.Ruangan;
import storage.AdmisiDB;
import util.DateUtil;
import util.IDGenerator;
import util.Validator;

/**
 * AdmisiService — Logika bisnis proses rawat inap.
 * Mengelola admisi masuk dan discharge (keluar).
 */
public class AdmisiService {

    private AdmisiDB admisiDB;
    private PasienService pasienService;
    private DokterService dokterService;
    private RuanganService ruanganService;

    public AdmisiService() {
        this.admisiDB = new AdmisiDB();
        this.pasienService = new PasienService();
        this.dokterService = new DokterService();
        this.ruanganService = new RuanganService();
    }

    /**
     * Membuat admisi rawat inap baru.
     * Validasi → Cek pasien → Cek dokter → Cek kamar → Simpan.
     */
    public Admisi buatAdmisi(String noRM, String idDokter, 
            String idRuangan, String diagnosisAwal, 
            String tglMasukStr) throws Exception {

        // Validasi input
        String error = Validator.validasiAdmisi(noRM, idDokter, 
                                                 idRuangan, diagnosisAwal, tglMasukStr);
        if (error != null) throw new Exception(error);

        // Cek pasien ada
        Pasien pasien = pasienService.cariByRM(noRM);

        // Cek dokter ada
        Dokter dokter = dokterService.cariById(idDokter);

        // Cek ruangan tersedia
        Ruangan ruangan = ruanganService.cariById(idRuangan);
        if (!ruangan.isKosong()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Kamar ").append(ruangan.getNamaRuangan()).append(" tidak tersedia");
            throw new Exception(sb.toString());
        }

        // Parse tanggal
        long tglMasuk = DateUtil.parseTanggal(tglMasukStr);

        // Generate ID Admisi
        String idAdmisi = IDGenerator.generateAdmisiId();

        // Buat admisi
        Admisi admisi = new Admisi(idAdmisi, noRM, idDokter, 
                                    idRuangan, tglMasuk, diagnosisAwal.trim());
        admisiDB.save(admisi);

        // Update status kamar menjadi TERISI
        ruanganService.isiKamar(idRuangan, pasien.getNama());

        return admisi;
    }

    /**
     * Proses discharge (keluar pasien).
     */
    public Admisi discharge(String idAdmisi, String diagnosisAkhir, 
            String kodeICD10, String tglKeluarStr, 
            String catatan) throws Exception {

        // Validasi
        String error = Validator.validasiDischarge(diagnosisAkhir, kodeICD10, tglKeluarStr);
        if (error != null) throw new Exception(error);

        // Cari admisi
        Admisi admisi = admisiDB.findById(idAdmisi);
        if (admisi == null) throw new Exception("Admisi tidak ditemukan");
        if (!admisi.isAktif()) throw new Exception("Admisi sudah selesai");

        // Parse tanggal keluar
        long tglKeluar = DateUtil.parseTanggal(tglKeluarStr);

        // Update admisi
        admisi.setTglKeluar(tglKeluar);
        admisi.setDiagnosisAkhir(diagnosisAkhir.trim());
        admisi.setKodeICD10(kodeICD10.trim());
        admisi.setCatatan(catatan != null ? catatan.trim() : "");
        admisi.setStatus(Admisi.STATUS_SELESAI);
        admisiDB.update(admisi);

        // Kosongkan kamar
        ruanganService.kosongkanKamar(admisi.getIdRuangan());

        return admisi;
    }

    /** Cari admisi aktif berdasarkan No. RM */
    public Admisi cariAdmisiAktifByRM(String noRM) throws Exception {
        Vector list = admisiDB.findByPasien(noRM);
        for (int i = 0; i < list.size(); i++) {
            Admisi a = (Admisi) list.elementAt(i);
            if (a.isAktif()) return a;
        }
        return null;
    }

    /** Mendapatkan semua admisi aktif */
    public Vector getAdmisiAktif() throws Exception {
        return admisiDB.findAktif();
    }

    /** Mendapatkan semua admisi */
    public Vector getSemuaAdmisi() throws Exception {
        return admisiDB.getAll();
    }

    /** Menghitung lama rawat */
    public int hitungLamaRawat(Admisi admisi) {
        if (admisi.getTglKeluar() <= 0) {
            return DateUtil.hitungSelisihHari(admisi.getTglMasuk(), DateUtil.sekarang());
        }
        return DateUtil.hitungSelisihHari(admisi.getTglMasuk(), admisi.getTglKeluar());
    }
}
