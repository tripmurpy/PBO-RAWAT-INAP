package controller;

import ui.ScreenManager;
import ui.PasienFormScreen;
import ui.PasienListScreen;
import ui.AdmisiScreen;
import ui.DischargeScreen;
import ui.DokterScreen;
import ui.RuanganScreen;
import ui.KunjunganScreen;
import ui.ObatScreen;
import ui.LaporanScreen;
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

    public void navigateAdmisi() {
        ScreenManager.getInstance().tampilkanLayar(new AdmisiScreen());
    }

    public void navigateDischarge() {
        ScreenManager.getInstance().tampilkanLayar(new DischargeScreen());
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

    public void navigateObat() {
        ScreenManager.getInstance().tampilkanLayar(new ObatScreen());
    }

    public void navigateLaporan() {
        ScreenManager.getInstance().tampilkanLayar(new LaporanScreen());
    }

    public void logout() {
        new LoginController().logout();
        ScreenManager.getInstance().logout();
    }
}
