package service;

import java.util.Vector;
import model.Obat;
import model.repository.IObatRepository;
import util.IDGenerator;
import exception.StokHabisException;
import exception.NotFoundException;
import exception.ValidationException;

/**
 * ObatService — Logika bisnis pengelolaan obat dan stok.
 */
public class ObatService {

    private IObatRepository obatRepo;

    public ObatService(IObatRepository obatRepo) {
        this.obatRepo = obatRepo;
    }

    public Obat tambahObat(String nama, String bentuk, String satuan,
                           int stok, int stokMinimum, int hargaPerUnit) throws Exception {
        if (nama == null || nama.trim().length() == 0)
            throw new ValidationException("Nama obat tidak boleh kosong");
        if (stok < 0)
            throw new ValidationException("Stok tidak boleh negatif");
        if (hargaPerUnit < 0)
            throw new ValidationException("Harga tidak boleh negatif");

        String id = IDGenerator.generateObatId();
        Obat obat = new Obat(id, nama.trim(), bentuk, satuan, stok, stokMinimum, hargaPerUnit);
        obatRepo.save(obat);
        return obat;
    }

    public void updateObat(Obat obat) throws Exception {
        obatRepo.update(obat);
    }

    public void hapusObat(int recordId) throws Exception {
        obatRepo.delete(recordId);
    }

    public Obat cariById(String id) throws Exception {
        Obat o = obatRepo.findById(id);
        if (o == null) throw new NotFoundException(new StringBuffer().append("Obat tidak ditemukan: ").append(id).toString());
        return o;
    }

    public Vector getSemuaObat() throws Exception {
        return obatRepo.getAll();
    }

    public Vector cariByNama(String keyword) throws Exception {
        return obatRepo.cariByNama(keyword);
    }

    public Vector getLowStock() throws Exception {
        return obatRepo.getLowStock();
    }

    /**
     * Atomic check-and-decrement stok.
     * Throws StokHabisException jika stok < quantity.
     */
    public void kurangiStok(String obatId, int quantity) throws Exception {
        Obat obat = cariById(obatId);
        if (obat.getStok() < quantity)
            throw new StokHabisException(obat.getNama(), obat.getStok());
        obat.setStok(obat.getStok() - quantity);
        obatRepo.update(obat);
    }

    public void tambahStok(String obatId, int quantity) throws Exception {
        if (quantity <= 0) throw new ValidationException("Quantity harus positif");
        Obat obat = cariById(obatId);
        obat.setStok(obat.getStok() + quantity);
        obatRepo.update(obat);
    }
}
