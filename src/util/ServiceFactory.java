package util;

import storage.AdmisiDB;
import storage.DokterDB;
import storage.PasienDB;
import storage.RuanganDB;
import storage.UserDB;
import model.repository.IAdmisiRepository;
import model.repository.IDokterRepository;
import model.repository.IPasienRepository;
import model.repository.IRuanganRepository;
import model.repository.IUserRepository;
import service.AuthService;
import service.DokterService;
import service.KunjunganService;
import service.PasienService;
import service.RuanganService;

/**
 * ServiceFactory — Composition Root + Singleton.
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

    // Services
    private final AuthService authService;
    private final PasienService pasienService;
    private final DokterService dokterService;
    private final RuanganService ruanganService;
    private final KunjunganService kunjunganService;

    private ServiceFactory() {
        // 1. Initialize Repositories (Storage Layer)
        userRepo       = new UserDB();
        pasienRepo     = new PasienDB();
        dokterRepo     = new DokterDB();
        ruanganRepo    = new RuanganDB();
        admisiRepo     = new AdmisiDB();

        // 2. Initialize Services (Business Layer) with Constructor Injection
        authService    = new AuthService(userRepo);
        pasienService  = new PasienService(pasienRepo);
        dokterService  = new DokterService(dokterRepo);
        ruanganService = new RuanganService(ruanganRepo);
        
        // Services with dependencies
        kunjunganService = new KunjunganService(admisiRepo);
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
    public KunjunganService  getKunjunganService()  { return kunjunganService; }


    public IUserRepository    getUserRepo()    { return userRepo; }
    public IPasienRepository  getPasienRepo()  { return pasienRepo; }
    public IDokterRepository  getDokterRepo()  { return dokterRepo; }
    public IRuanganRepository getRuanganRepo() { return ruanganRepo; }
    public IAdmisiRepository  getAdmisiRepo()  { return admisiRepo; }
}
