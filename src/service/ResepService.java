package service;

import java.util.Vector;
import model.Resep;
import model.RekamMedis;
import model.ItemObat;
import model.Obat;
import model.repository.IResepRepository;
import util.IDGenerator;
import util.DateUtil;
import exception.NotFoundException;
import exception.ValidationException;

/**
 * ResepService — Logika bisnis pengelolaan resep obat.
 * Berinteraksi dengan ObatService untuk pengurangan stok.
 */
public class ResepService {

    private IResepRepository resepRepo;
    private ObatService obatService;
    private RekamMedisService rekamMedisService;

    public ResepService(IResepRepository resepRepo,
                        ObatService obatService,
                        RekamMedisService rekamMedisService) {
        this.resepRepo = resepRepo;
        this.obatService = obatService;
        this.rekamMedisService = rekamMedisService;
    }

    /**
     * Tambah item obat ke resep.
     * Cek stok → Kurangi stok → Simpan resep.
     */
    public Resep tambahResep(String rekamMedisId, String obatId,
                              int quantity, String dosis) throws Exception {
        if (quantity <= 0)
            throw new ValidationException("Quantity harus lebih dari 0");
        if (quantity > 1000)
            throw new ValidationException("Quantity terlalu besar (max 1000)");

        RekamMedis rm = rekamMedisService.cariById(rekamMedisId);
        if (!rm.isOpen())
            throw new ValidationException("Rekam medis sudah ditutup");

        Obat obat = obatService.cariById(obatId);

        // Atomic check-and-decrement
        obatService.kurangiStok(obatId, quantity);

        String resepId = IDGenerator.generateResepId();
        Resep resep = new Resep(resepId, rekamMedisId, DateUtil.sekarang());
        resep.tambahItem(new ItemObat(obatId, obat.getNama(), quantity, dosis));

        resepRepo.save(resep);
        return resep;
    }

    public Vector getResepByRekamMedis(String rekamMedisId) throws Exception {
        return resepRepo.findByRekamMedisId(rekamMedisId);
    }

    public void hapusResep(int recordId) throws Exception {
        resepRepo.delete(recordId);
    }
}
