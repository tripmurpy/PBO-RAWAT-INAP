package controller;

import util.ui.DokterScreen;
import util.ui.KunjunganScreen;
import util.ui.LoginScreen;
import util.ui.PasienFormScreen;
import util.ui.PasienListScreen;
import util.ui.RuanganScreen;
import util.ui.ScreenManager;

/**
 * DashboardController — Routing navigasi dari menu utama.
 */
public class DashboardController {

    public void navigatePasienBaru() {
        ScreenManager.getInstance().tampilkanLayar(new PasienFormScreen());
    }

    public void navigateCariPasien() {
        ScreenManager.getInstance().tampilkanLayar(new PasienListScreen());
    }

    public void navigateDokter() {
        ScreenManager.getInstance().tampilkanLayar(new DokterScreen());
    }

    public void navigateRuangan() {
        ScreenManager.getInstance().tampilkanLayar(new RuanganScreen());
    }

    public void navigateRiwayat() {
        ScreenManager.getInstance().tampilkanLayar(new KunjunganScreen());
    }

    public void logout() {
        new LoginController().logout();
        ScreenManager.getInstance().logout();
    }
}
