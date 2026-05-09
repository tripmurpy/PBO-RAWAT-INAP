package util;

import model.Dokter;
import model.Ruangan;
import model.Obat;
import storage.DokterDB;
import storage.RuanganDB;
import storage.ObatDB;

/**
 * SeedData — Mengisi data awal pada first run aplikasi.
 * Dipanggil dari HospitalMIDlet jika RMS belum ada data.
 */
public class SeedData {

    public static void run() {
        seedDokter();
        seedRuangan();
        seedObat();
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
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A11", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A12", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A13", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A14", Ruangan.TIPE_VVIP, 1));
            // Lantai 2
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A21", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A22", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A23", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A24", Ruangan.TIPE_VVIP, 1));
            // Lantai 3
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A31", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A32", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A33", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A34", Ruangan.TIPE_VVIP, 1));
            // Lantai 4
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A41", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A42", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A43", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A44", Ruangan.TIPE_VVIP, 1));
            // Lantai 5
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A51", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A52", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A53", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A54", Ruangan.TIPE_VVIP, 1));
            // Lantai 6
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A61", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A62", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A63", Ruangan.TIPE_VVIP, 1));
            // Lantai 7
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A71", Ruangan.TIPE_VIP, 1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "A72", Ruangan.TIPE_VVIP, 1));

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

    private static void seedObat() {
        try {
            ObatDB db = new ObatDB();
            if (db.getAll().size() > 0) return;

            db.save(new Obat(IDGenerator.generateObatId(), "Paracetamol 500mg",  Obat.BENTUK_TABLET,   "tablet",  500, 50, 500));
            db.save(new Obat(IDGenerator.generateObatId(), "Amoxicillin 500mg",  Obat.BENTUK_TABLET,   "kapsul",  200, 20, 2000));
            db.save(new Obat(IDGenerator.generateObatId(), "Omeprazole 20mg",    Obat.BENTUK_TABLET,   "kapsul",  150, 20, 3000));
            db.save(new Obat(IDGenerator.generateObatId(), "Ondansetron 4mg",    Obat.BENTUK_INJEKSI,  "ampul",    50, 10, 15000));
            db.save(new Obat(IDGenerator.generateObatId(), "Ringer Laktat",      Obat.BENTUK_INJEKSI,  "kolf",    100, 20, 25000));
            db.save(new Obat(IDGenerator.generateObatId(), "Ambroxol Sirup",     Obat.BENTUK_SIRUP,    "botol",    60, 10, 12000));
            db.save(new Obat(IDGenerator.generateObatId(), "Ibuprofen 400mg",    Obat.BENTUK_TABLET,   "tablet",  300, 30, 1500));
            db.save(new Obat(IDGenerator.generateObatId(), "Cefixime 100mg",     Obat.BENTUK_TABLET,   "kapsul",  100, 15, 5000));
            db.save(new Obat(IDGenerator.generateObatId(), "Metronidazol 500mg", Obat.BENTUK_TABLET,   "tablet",  200, 20, 1000));
            db.save(new Obat(IDGenerator.generateObatId(), "Vit C 500mg",        Obat.BENTUK_TABLET,   "tablet", 1000, 100, 300));
        } catch (Exception e) {
            // Seed gagal — tidak kritis
        }
    }
}
