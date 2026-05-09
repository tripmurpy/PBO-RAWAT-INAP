package controller;

import ui.ScreenManager;
import ui.PasienFormScreen;
import ui.PasienListScreen;
import ui.DokterScreen;
import ui.RuanganScreen;
import ui.KunjunganScreen;
import ui.LoginScreen;

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
