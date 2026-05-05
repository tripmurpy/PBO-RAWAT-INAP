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
            if (db.findAll().size() > 0) return;

            db.save(new Dokter(IDGenerator.generateDokterId(), "Dr. Ahmad Santoso", "PD", "Senin-Jumat 08:00-12:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "Dr. Siti Rahayu", "A",  "Senin-Rabu 13:00-17:00"));
            db.save(new Dokter(IDGenerator.generateDokterId(), "Dr. Budi Hartono",  "S",  "Selasa-Kamis 08:00-12:00"));
        } catch (Exception e) {
            // Seed gagal — tidak kritis, lanjut
        }
    }

    private static void seedRuangan() {
        try {
            RuanganDB db = new RuanganDB();
            if (db.findAll().size() > 0) return;

            db.save(new Ruangan(IDGenerator.generateRuanganId(), "VIP-01",   Ruangan.TIPE_VIP,     1));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "Kls1-01",  Ruangan.TIPE_KELAS_1, 2));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "Kls1-02",  Ruangan.TIPE_KELAS_1, 2));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "Kls2-01",  Ruangan.TIPE_KELAS_2, 4));
            db.save(new Ruangan(IDGenerator.generateRuanganId(), "Kls2-02",  Ruangan.TIPE_KELAS_2, 4));
        } catch (Exception e) {
            // Seed gagal — tidak kritis
        }
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
