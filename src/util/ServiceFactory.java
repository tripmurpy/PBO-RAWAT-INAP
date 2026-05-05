package util;

import storage.AdmisiDB;
import storage.DokterDB;
import storage.PasienDB;
import storage.RuanganDB;
import storage.UserDB;
import storage.ObatDB;
import storage.RekamMedisDB;
import storage.ResepDB;
import model.repository.IAdmisiRepository;
import model.repository.IDokterRepository;
import model.repository.IPasienRepository;
import model.repository.IRuanganRepository;
import model.repository.IUserRepository;
import model.repository.IObatRepository;
import model.repository.IRekamMedisRepository;
import model.repository.IResepRepository;
import service.AdmisiService;
import service.AuthService;
import service.DokterService;
import service.KunjunganService;
import service.PasienService;
import service.RuanganService;
import service.ObatService;
import service.RekamMedisService;
import service.ResepService;
import service.LaporanService;

/**
 * ServiceFactory — Composition Root + Singleton.
 *
 * Bertanggung jawab membuat repository konkret dan men-inject ke service
 * layer (Constructor Injection). UI layer hanya berinteraksi via getter
 * service, sehingga implementasi storage dapat ditukar (misalnya untuk
 * testing) tanpa mengubah UI.
 */
public class ServiceFactory {

    private static final String TAG = "ServiceFactory";
    private static ServiceFactory instance;

    // Repositories
    private final IUserRepository userRepo;
    private final IPasienRepository pasienRepo;
    private final IDokterRepository dokterRepo;
    private final IRuanganRepository ruanganRepo;
    private final IAdmisiRepository admisiRepo;
    private final IObatRepository obatRepo;
    private final IRekamMedisRepository rekamMedisRepo;
    private final IResepRepository resepRepo;

    // Services
    private final AuthService authService;
    private final PasienService pasienService;
    private final DokterService dokterService;
    private final RuanganService ruanganService;
    private final AdmisiService admisiService;
    private final KunjunganService kunjunganService;
    private final ObatService obatService;
    private final RekamMedisService rekamMedisService;
    private final ResepService resepService;
    private final LaporanService laporanService;

    private ServiceFactory() {
        // 1. Initialize Repositories (Storage Layer)
        userRepo       = new UserDB();
        pasienRepo     = new PasienDB();
        dokterRepo     = new DokterDB();
        ruanganRepo    = new RuanganDB();
        admisiRepo     = new AdmisiDB();
        obatRepo       = new ObatDB();
        rekamMedisRepo = new RekamMedisDB();
        resepRepo      = new ResepDB();

        // 2. Initialize Services (Business Layer) with Constructor Injection
        authService    = new AuthService(userRepo);
        pasienService  = new PasienService(pasienRepo);
        dokterService  = new DokterService(dokterRepo);
        ruanganService = new RuanganService(ruanganRepo);
        obatService    = new ObatService(obatRepo);
        rekamMedisService = new RekamMedisService(rekamMedisRepo);
        
        // Services with dependencies
        resepService   = new ResepService(resepRepo, obatService, rekamMedisService);
        admisiService  = new AdmisiService(admisiRepo, pasienService, dokterService, ruanganService);
        kunjunganService = new KunjunganService(admisiRepo);
        laporanService = new LaporanService(obatService, pasienService, ruanganService, admisiService, rekamMedisService);
    }

    /** Lazy singleton. J2ME UI single-thread, jadi cukup tanpa lock. */
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
            instance.bootstrap();
        }
        return instance;
    }

    /**
     * Setup default data. Dipanggil sekali setelah konstruksi.
     */
    private void bootstrap() {
        try {
            userRepo.inisialisasiDefault();
        } catch (Exception e) {
            // Lanjut jika gagal — tidak kritis
        }
    }

    public AuthService       getAuthService()       { return authService; }
    public PasienService     getPasienService()     { return pasienService; }
    public DokterService     getDokterService()     { return dokterService; }
    public RuanganService    getRuanganService()    { return ruanganService; }
    public AdmisiService     getAdmisiService()     { return admisiService; }
    public KunjunganService  getKunjunganService()  { return kunjunganService; }
    public ObatService       getObatService()       { return obatService; }
    public RekamMedisService getRekamMedisService() { return rekamMedisService; }
    public ResepService      getResepService()      { return resepService; }
    public LaporanService    getLaporanService()    { return laporanService; }

    public IUserRepository    getUserRepo()    { return userRepo; }
    public IPasienRepository  getPasienRepo()  { return pasienRepo; }
    public IDokterRepository  getDokterRepo()  { return dokterRepo; }
    public IRuanganRepository getRuanganRepo() { return ruanganRepo; }
    public IAdmisiRepository  getAdmisiRepo()  { return admisiRepo; }
}
