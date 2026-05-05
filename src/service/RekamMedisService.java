package service;

import java.util.Vector;
import model.RekamMedis;
import model.CatatanHarian;
import model.repository.IRekamMedisRepository;
import util.IDGenerator;
import util.DateUtil;
import exception.NotFoundException;
import exception.ValidationException;

/**
 * RekamMedisService — Logika bisnis pengelolaan rekam medis.
 */
public class RekamMedisService {

    private IRekamMedisRepository rekamMedisRepo;

    public RekamMedisService(IRekamMedisRepository rekamMedisRepo) {
        this.rekamMedisRepo = rekamMedisRepo;
    }

    public RekamMedis buatRekamMedis(String admisiId) throws Exception {
        String id = IDGenerator.generateRekamMedisId();
        RekamMedis rm = new RekamMedis(id, admisiId, DateUtil.sekarang());
        rekamMedisRepo.save(rm);
        return rm;
    }

    public RekamMedis cariByAdmisiId(String admisiId) throws Exception {
        RekamMedis rm = rekamMedisRepo.findByAdmisiId(admisiId);
        if (rm == null) throw new NotFoundException(new StringBuffer().append("Rekam medis tidak ditemukan untuk admisi: ").append(admisiId).toString());
        return rm;
    }

    public RekamMedis cariById(String id) throws Exception {
        RekamMedis rm = rekamMedisRepo.findById(id);
        if (rm == null) throw new NotFoundException(new StringBuffer().append("Rekam medis tidak ditemukan: ").append(id).toString());
        return rm;
    }

    public void tambahCatatan(String rekamMedisId, String vitalSigns,
                               String keluhan, String diagnosa, String tindakan) throws Exception {
        RekamMedis rm = cariById(rekamMedisId);
        if (!rm.isOpen())
            throw new ValidationException("Rekam medis sudah ditutup, tidak dapat menambah catatan");

        String catatanId = IDGenerator.generateCatatanId();
        CatatanHarian catatan = new CatatanHarian(catatanId, DateUtil.sekarang(),
                vitalSigns, keluhan, diagnosa, tindakan);
        rm.tambahCatatan(catatan);
        rekamMedisRepo.update(rm);
    }

    public void tutupRekamMedis(String rekamMedisId) throws Exception {
        RekamMedis rm = cariById(rekamMedisId);
        rm.tutup(DateUtil.sekarang());
        rekamMedisRepo.update(rm);
    }

    public Vector getSemuaRekamMedis() throws Exception {
        return rekamMedisRepo.getAll();
    }
}
