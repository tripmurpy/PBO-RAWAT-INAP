package util;

import model.Dokter;
import model.Pasien;
import model.Ruangan;
import storage.DokterDB;
import storage.PasienDB;
import storage.RuanganDB;

/**
 * SeedData — Mengisi data awal pada first run aplikasi.
 * Dipanggil dari HospitalMIDlet jika RMS belum ada data.
 */
public class SeedData {

    public static void run() {
        seedDokter();
        seedRuangan();
        seedPasien();
    }

    private static void seedDokter() {
        try {
            DokterDB db = new DokterDB();
            java.util.Vector all = db.findAll();
            for(int i=0; i<all.size(); i++) {
                db.delete(((Dokter)all.elementAt(i)).getRecordId());
            }

            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Bayushi Eka P, Sp.JP(K)", "Jantung", "Senin-Kamis 08:00-14:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Sidhi Laksono, Sp.JP", "Jantung", "Senin-Rabu 14:00-18:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Radhiyatam M, Sp.PD", "Penyakit Dalam", "Selasa-Jumat 08:00-15:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Anggun Mekar K, Sp.PD", "Penyakit Dalam", "Senin-Kamis 09:00-16:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Johannes R, Sp.A", "Anak", "Senin-Rabu 10:00-15:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Christine Natalita, Sp.A", "Anak", "Kamis-Sabtu 08:00-13:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Agus Heriyanto, Sp.OG", "Kandungan", "Senin-Jumat 16:00-20:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Merwin Tjahjadi, Sp.OG", "Kandungan", "Selasa-Sabtu 09:00-14:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Lia Natalia, Sp.THT-KL", "THT", "Senin-Jumat 10:00-16:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "dr. Hilda Sasdyanita, Sp.OT", "Orthopedi", "Rabu-Sabtu 08:00-14:00"));
        } catch (Exception e) {
        }
    }

    private static void seedRuangan() {
        try {
            RuanganDB db = new RuanganDB();
            java.util.Vector all = db.findAll();
            for(int i=0; i<all.size(); i++) {
                db.delete(((Ruangan)all.elementAt(i)).getRecordId());
            }

            // Lantai 1
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A11", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa, Fridge"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A12", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa, Fridge"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A13", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa, Fridge"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A14", Ruangan.TIPE_VVIP, 1, 2500000, "Living Room, Kitchenette, 2 TVs"));
            // Lantai 2
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A21", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A22", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A23", Ruangan.TIPE_VIP, 1, 1500000, "AC, TV, Sofa"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A24", Ruangan.TIPE_VVIP, 1, 2500000, "Deluxe Facilities"));
            // Lantai 3
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A31", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A32", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A33", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A34", Ruangan.TIPE_VVIP, 1, 2500000, "Premium VVIP"));
            // Lantai 4
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A41", Ruangan.TIPE_VIP, 1, 1500000, "VIP Room"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A42", Ruangan.TIPE_VIP, 1, 1500000, "VIP Room"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A43", Ruangan.TIPE_VIP, 1, 1500000, "VIP Room"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A44", Ruangan.TIPE_VVIP, 1, 2500000, "Elite VVIP Suite"));
            // Lantai 5
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A51", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A52", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A53", Ruangan.TIPE_VIP, 1, 1500000, "Standard VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A54", Ruangan.TIPE_VVIP, 1, 2500000, "Superior VVIP"));
            // Lantai 6
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A61", Ruangan.TIPE_VIP, 1, 1500000, "VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A62", Ruangan.TIPE_VIP, 1, 1500000, "VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A63", Ruangan.TIPE_VVIP, 1, 2500000, "VVIP"));
            // Lantai 7
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A71", Ruangan.TIPE_VIP, 1, 1500000, "VIP"));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A72", Ruangan.TIPE_VVIP, 1, 2500000, "VVIP"));

            // Set some to occupied based on the report
            Ruangan rA11 = db.findById(getRoomIdByNama(db, "A11")); if(rA11 != null) { rA11.setKosong(false); rA11.setNamaPasien("Budi Santoso"); db.update(rA11); }
            Ruangan rA14 = db.findById(getRoomIdByNama(db, "A14")); if(rA14 != null) { rA14.setKosong(false); rA14.setNamaPasien("Siti Aminah"); db.update(rA14); }
            Ruangan rA23 = db.findById(getRoomIdByNama(db, "A23")); if(rA23 != null) { rA23.setKosong(false); rA23.setNamaPasien("Antonius Wijaya"); db.update(rA23); }
            Ruangan rA33 = db.findById(getRoomIdByNama(db, "A33")); if(rA33 != null) { rA33.setKosong(false); rA33.setNamaPasien("Rina Melati"); db.update(rA33); }
            Ruangan rA42 = db.findById(getRoomIdByNama(db, "A42")); if(rA42 != null) { rA42.setKosong(false); rA42.setNamaPasien("Hendra Gunawan"); db.update(rA42); }
            Ruangan rA53 = db.findById(getRoomIdByNama(db, "A53")); if(rA53 != null) { rA53.setKosong(false); rA53.setNamaPasien("Wahyu Saputra"); db.update(rA53); }
            Ruangan rA63 = db.findById(getRoomIdByNama(db, "A63")); if(rA63 != null) { rA63.setKosong(false); rA63.setNamaPasien("Eka Pertiwi"); db.update(rA63); }

        } catch (Exception e) {
        }
    }

    private static String getRoomIdByNama(RuanganDB db, String nama) {
        try {
            java.util.Vector all = db.findAll();
            for(int i=0; i<all.size(); i++) {
                Ruangan r = (Ruangan) all.elementAt(i);
                if(r.getNamaRuangan().equals(nama)) return r.getId();
            }
        } catch(Exception e) {}
        return "";
    }

    // ========== SEED PASIEN ==========


    private static void seedPasien() {
        try {
            PasienDB db = new PasienDB();
            if (db.getAll().size() > 0) return;

            RuanganDB rdb = new RuanganDB();

            // 1. Yosia Siahaan — Lahir 14 Mar 1995 | L | BPJS | A21-VIP | Jantung
            String noRM1 = IDGenerator.generateNoRM();
            Pasien p1 = new Pasien(noRM1, "Yosia Siahaan",
                    mkDate(1995, 3, 14), "Laki-laki",
                    "Jl. Sisingamangaraja No. 12, Medan",
                    "0812-3456-7890", "BPJS");
            p1.setDokterPenanggungJawab("dr. Bayushi Eka P, Sp.JP(K)");
            p1.setKamarRawat("A21");
            p1.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p1);
            isiKamar(rdb, "A21", "Yosia Siahaan", "dr. Bayushi Eka P, Sp.JP(K)");

            // 2. Joachim Simson Ririhena — Lahir 7 Nov 1988 | L | Asuransi Swasta | A14-VVIP | Penyakit Dalam
            String noRM2 = IDGenerator.generateNoRM();
            Pasien p2 = new Pasien(noRM2, "Joachim Simson Ririhena",
                    mkDate(1988, 11, 7), "Laki-laki",
                    "Jl. Patimura No. 45, Ambon",
                    "0813-9988-7766", "Asuransi Swasta");
            p2.setDokterPenanggungJawab("dr. Radhiyatam M, Sp.PD");
            p2.setKamarRawat("A14");
            p2.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p2);
            isiKamar(rdb, "A14", "Joachim Simson Ririhena", "dr. Radhiyatam M, Sp.PD");

            // 3. Margaretha Susanti — Lahir 22 Jun 2001 | P | BPJS | A31-VIP | Penyakit Dalam
            String noRM3 = IDGenerator.generateNoRM();
            Pasien p3 = new Pasien(noRM3, "Margaretha Susanti",
                    mkDate(2001, 6, 22), "Perempuan",
                    "Jl. Gatot Subroto Blok C No. 3, Jakarta",
                    "0819-1122-3344", "BPJS");
            p3.setDokterPenanggungJawab("dr. Anggun Mekar K, Sp.PD");
            p3.setKamarRawat("A31");
            p3.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p3);
            isiKamar(rdb, "A31", "Margaretha Susanti", "dr. Anggun Mekar K, Sp.PD");

            // 4. Bambang Prasetyo — Lahir 10 Jan 1979 | L | Umum | A22-VIP | Anak
            String noRM4 = IDGenerator.generateNoRM();
            Pasien p4 = new Pasien(noRM4, "Bambang Prasetyo",
                    mkDate(1979, 1, 10), "Laki-laki",
                    "Jl. Raya Bogor KM 32, Depok",
                    "0856-7788-9900", "Umum");
            p4.setDokterPenanggungJawab("dr. Johannes R, Sp.A");
            p4.setKamarRawat("A22");
            p4.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p4);
            isiKamar(rdb, "A22", "Bambang Prasetyo", "dr. Johannes R, Sp.A");

            // 5. Felicia Tanujaya — Lahir 3 Ags 1992 | P | Asuransi Swasta | A44-VVIP | Anak
            String noRM5 = IDGenerator.generateNoRM();
            Pasien p5 = new Pasien(noRM5, "Felicia Tanujaya",
                    mkDate(1992, 8, 3), "Perempuan",
                    "Jl. Sudirman Blok M No. 7, Surabaya",
                    "0822-6655-4433", "Asuransi Swasta");
            p5.setDokterPenanggungJawab("dr. Christine Natalita, Sp.A");
            p5.setKamarRawat("A44");
            p5.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p5);
            isiKamar(rdb, "A44", "Felicia Tanujaya", "dr. Christine Natalita, Sp.A");

            // 6. Rudi Hartono — Lahir 28 Feb 1985 | L | BPJS | A32-VIP | Orthopedi
            String noRM6 = IDGenerator.generateNoRM();
            Pasien p6 = new Pasien(noRM6, "Rudi Hartono",
                    mkDate(1985, 2, 28), "Laki-laki",
                    "Jl. Ahmad Yani No. 88, Bandung",
                    "0878-2233-4455", "BPJS");
            p6.setDokterPenanggungJawab("dr. Hilda Sasdyanita, Sp.OT");
            p6.setKamarRawat("A32");
            p6.setStatus(Pasien.STATUS_DIRAWAT);
            db.save(p6);
            isiKamar(rdb, "A32", "Rudi Hartono", "dr. Hilda Sasdyanita, Sp.OT");

        } catch (Exception e) {
            // Seed pasien gagal — tidak kritis
        }
    }

    /**
     * Membuat timestamp milidetik dari komponen tanggal (perkiraan untuk seed).
     */
    private static long mkDate(int tahun, int bulan, int hari) {
        long y = tahun - 1970;
        long ms = y * 365L * 24L * 3600L * 1000L;
        ms += (bulan - 1) * 30L * 24L * 3600L * 1000L;
        ms += (hari - 1) * 24L * 3600L * 1000L;
        return ms;
    }

    /**
     * Menandai kamar sebagai TERISI oleh pasien.
     */
    private static void isiKamar(RuanganDB rdb, String namaKamar,
            String namaPasien, String dokter) {
        try {
            java.util.Vector all = rdb.findAll();
            for (int i = 0; i < all.size(); i++) {
                Ruangan r = (Ruangan) all.elementAt(i);
                if (r.getNamaRuangan().equals(namaKamar)) {
                    r.setKosong(false);
                    r.setNamaPasien(namaPasien);
                    r.setNamaPenanggungJawab(dokter);
                    rdb.update(r);
                    return;
                }
            }
        } catch (Exception e) {}
    }
}
