package controller;

import java.util.Vector;
import model.RekamMedis;
import model.Resep;
import service.RekamMedisService;
import service.ResepService;
import util.ServiceFactory;

/**
 * RekamMedisController — Validasi ringan + routing ke RekamMedisService & ResepService.
 */
public class RekamMedisController {

    private RekamMedisService rekamMedisService;
    private ResepService resepService;

    public RekamMedisController() {
        this.rekamMedisService = ServiceFactory.getInstance().getRekamMedisService();
        this.resepService = ServiceFactory.getInstance().getResepService();
    }

    public RekamMedis buatRekamMedis(String admisiId) throws Exception {
        return rekamMedisService.buatRekamMedis(admisiId);
    }

    public RekamMedis cariByAdmisiId(String admisiId) throws Exception {
        return rekamMedisService.cariByAdmisiId(admisiId);
    }

    public RekamMedis cariById(String id) throws Exception {
        return rekamMedisService.cariById(id);
    }

    public void tambahCatatan(String rekamMedisId, String vitalSigns,
                               String keluhan, String diagnosa, String tindakan) throws Exception {
        rekamMedisService.tambahCatatan(rekamMedisId, vitalSigns, keluhan, diagnosa, tindakan);
    }

    public Resep tambahResep(String rekamMedisId, String obatId,
                              int quantity, String dosis) throws Exception {
        return resepService.tambahResep(rekamMedisId, obatId, quantity, dosis);
    }

    public Vector getResepByRekamMedis(String rekamMedisId) throws Exception {
        return resepService.getResepByRekamMedis(rekamMedisId);
    }

    public void tutupRekamMedis(String rekamMedisId) throws Exception {
        rekamMedisService.tutupRekamMedis(rekamMedisId);
    }
}
