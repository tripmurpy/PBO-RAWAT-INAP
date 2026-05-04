package util;

import storage.*;
import model.repository.*;
import service.*;

/**
 * ServiceFactory — Singleton untuk mengelola lifecycle service dan repository.
 * Menerapkan pola Dependency Injection sederhana untuk J2ME.
 */
public class ServiceFactory {

    private static ServiceFactory instance;

    // Repositories
    private IUserRepository userRepo;
    private IPasienRepository pasienRepo;
    private IDokterRepository dokterRepo;
    private IRuanganRepository ruanganRepo;
    private IAdmisiRepository admisiRepo;

    // Services
    private AuthService authService;
    private PasienService pasienService;
    private DokterService dokterService;
    private RuanganService ruanganService;
    private AdmisiService admisiService;
    private KunjunganService kunjunganService;

    private ServiceFactory() {
        // 1. Initialize Repositories (Storage Layer)
        userRepo = new UserDB();
        pasienRepo = new PasienDB();
        dokterRepo = new DokterDB();
        ruanganRepo = new RuanganDB();
        admisiRepo = new AdmisiDB();

        // 2. Initialize Services (Business Layer) with Constructor Injection
        authService = new AuthService(userRepo);
        pasienService = new PasienService(pasienRepo);
        dokterService = new DokterService(dokterRepo);
        ruanganService = new RuanganService(ruanganRepo);

        // AdmisiService depends on other services
        admisiService = new AdmisiService(admisiRepo, pasienService, dokterService, ruanganService);
        kunjunganService = new KunjunganService(admisiRepo);

        // 3. Initial setup
        userRepo.inisialisasiDefault();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public PasienService getPasienService() {
        return pasienService;
    }

    public DokterService getDokterService() {
        return dokterService;
    }

    public RuanganService getRuanganService() {
        return ruanganService;
    }

    public AdmisiService getAdmisiService() {
        return admisiService;
    }

    public KunjunganService getKunjunganService() {
        return kunjunganService;
    }

    // Repository access if needed by some specialized components
    public IUserRepository getUserRepo() {
        return userRepo;
    }
}
