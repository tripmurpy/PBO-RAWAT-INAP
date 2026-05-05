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
import service.AdmisiService;
import service.AuthService;
import service.DokterService;
import service.KunjunganService;
import service.PasienService;
import service.RuanganService;

/**
 * ServiceFactory — Composition Root + Singleton.
 *
 * Bertanggung jawab membuat repository konkret dan men-inject ke service
 * layer (Constructor Injection). UI layer hanya berinteraksi via getter
 * service, sehingga implementasi storage dapat ditukar (misalnya untuk
 * testing) tanpa mengubah UI.
 *
 * Inisialisasi default user dipindahkan ke method terpisah agar konstruktor
 * tidak melempar checked Exception (Java tidak mengizinkan ini tanpa throws).
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
    private final AdmisiService admisiService;
    private final KunjunganService kunjunganService;

    private ServiceFactory() {
        userRepo    = new UserDB();
        pasienRepo  = new PasienDB();
        dokterRepo  = new DokterDB();
        ruanganRepo = new RuanganDB();
        admisiRepo  = new AdmisiDB();

        authService    = new AuthService(userRepo);
        pasienService  = new PasienService(pasienRepo);
        dokterService  = new DokterService(dokterRepo);
        ruanganService = new RuanganService(ruanganRepo);
        admisiService  = new AdmisiService(admisiRepo, pasienService,
                                           dokterService, ruanganService);
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
     * Setup default user. Dipanggil sekali setelah konstruksi. Error
     * di-log namun tidak menggagalkan startup karena fallback dapat
     * dilakukan di login flow.
     */
    private void bootstrap() {
        try {
            userRepo.inisialisasiDefault();
        } catch (Exception e) {
            Logger.error(TAG, "gagal inisialisasi user default", e);
        }
    }

    public AuthService       getAuthService()       { return authService; }
    public PasienService     getPasienService()     { return pasienService; }
    public DokterService     getDokterService()     { return dokterService; }
    public RuanganService    getRuanganService()    { return ruanganService; }
    public AdmisiService     getAdmisiService()     { return admisiService; }
    public KunjunganService  getKunjunganService()  { return kunjunganService; }

    public IUserRepository    getUserRepo()    { return userRepo; }
    public IPasienRepository  getPasienRepo()  { return pasienRepo; }
    public IDokterRepository  getDokterRepo()  { return dokterRepo; }
    public IRuanganRepository getRuanganRepo() { return ruanganRepo; }
    public IAdmisiRepository  getAdmisiRepo()  { return admisiRepo; }
}
