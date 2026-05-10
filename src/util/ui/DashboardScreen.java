package util.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import model.User;
import controller.DashboardController;

/**
 * DashboardScreen — Menu utama (Dashboard PRO Frontend).
 *
 * INHERITANCE: Extends Canvas.
 * ENCAPSULATION: Komponen UI (grid, scroll) di-manage secara privat.
 */
public class DashboardScreen extends Canvas {

    private User currentUser;
    private DashboardController controller;
    
    // UI State
    private int selectedIndex = 0;
    private int scrollY = 0;
    private int targetScrollY = 0;
    
    // Pro Colors (Berdasarkan Design System J2ME Modern)
    private static final int COLOR_BG = 0xF3F4F6;
    private static final int COLOR_HEADER = 0x0058BC;
    private static final int COLOR_HEADER_TEXT = 0xFFFFFF;
    private static final int COLOR_CARD = 0xFFFFFF;
    private static final int COLOR_CARD_BORDER = 0xE2E8F0;
    private static final int COLOR_TEXT = 0x1E293B;
    private static final int COLOR_TEXT_MUTED = 0x64748B;
    private static final int COLOR_SELECTED_BG = 0xDBEAFE;
    private static final int COLOR_SELECTED_BORDER = 0x3B82F6;
    private static final int COLOR_SELECTED_TEXT = 0x1D4ED8;

    // Grid config
    private int cols = 2;
    private int cardW, cardH;
    
    private static final class MenuItem {
        String title;
        int actionId;
        MenuItem(String title, int actionId) {
            this.title = title;
            this.actionId = actionId;
        }
    }

    private MenuItem[] menus = {
        new MenuItem("Daftar pasien", 0),
        new MenuItem("Pasien", 1),
        new MenuItem("Dokter", 4),
        new MenuItem("Kamar", 5),
        new MenuItem("Riwayat", 8),
        new MenuItem("Logout", 9)
    };

    public DashboardScreen(User user) {
        this.currentUser = user;
        this.controller = new DashboardController();
        setFullScreenMode(true);
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        
        // Calculate Grid
        cols = w > 300 ? 3 : 2;
        int margin = 12;
        int spacing = 8;
        cardW = (w - (margin * 2) - (spacing * (cols - 1))) / cols;
        cardH = 80;

        // Smooth scroll step
        if (scrollY != targetScrollY) {
            scrollY += (targetScrollY - scrollY) / 3;
            if (Math.abs(targetScrollY - scrollY) < 2) scrollY = targetScrollY;
            repaint();
        }

        // 1. Background
        g.setColor(COLOR_BG);
        g.fillRect(0, 0, w, h);

        int headerH = 50;
        
        // --- DRAW SCROLLABLE CONTENT ---
        // Set clip for content area
        g.setClip(0, headerH, w, h - headerH);
        
        int startY = headerH + margin - scrollY;
        
        // Draw Welcome Text
        Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        Font fIcon = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        
        g.setFont(fKecil);
        g.setColor(COLOR_TEXT_MUTED);
        String welcome = "Selamat datang,";
        g.drawString(welcome, margin, startY, Graphics.TOP | Graphics.LEFT);
        
        g.setFont(fSedang);
        g.setColor(COLOR_TEXT);
        String nama = (currentUser != null) ? currentUser.getNamaLengkap() : "Admin";
        g.drawString(nama, margin, startY + fKecil.getHeight() + 2, Graphics.TOP | Graphics.LEFT);
        
        int gridY = startY + fKecil.getHeight() + fSedang.getHeight() + 16;
        
        // Draw Grid
        for (int i = 0; i < menus.length; i++) {
            int row = i / cols;
            int col = i % cols;
            
            int cx = margin + col * (cardW + spacing);
            int cy = gridY + row * (cardH + spacing);
            
            // Culling optimization (don't draw if outside screen)
            if (cy + cardH < headerH || cy > h) continue;

            boolean isSelected = (i == selectedIndex);
            
            // Card BG
            g.setColor(isSelected ? COLOR_SELECTED_BG : COLOR_CARD);
            g.fillRoundRect(cx, cy, cardW, cardH, 8, 8);
            
            // Card Border
            g.setColor(isSelected ? COLOR_SELECTED_BORDER : COLOR_CARD_BORDER);
            g.drawRoundRect(cx, cy, cardW, cardH, 8, 8);
            
            // Text (Centered in card)
            g.setFont(fKecil);
            g.setColor(isSelected ? COLOR_SELECTED_TEXT : COLOR_TEXT);
            
            int textY = cy + (cardH - fKecil.getHeight()) / 2;
            g.drawString(menus[i].title, cx + cardW/2, textY, Graphics.TOP | Graphics.HCENTER);
        }
        
        // --- END CONTENT ---
        g.setClip(0, 0, w, h);

        // 2. Header (Fixed at top)
        g.setColor(COLOR_HEADER);
        g.fillRect(0, 0, w, headerH);
        
        g.setColor(COLOR_HEADER_TEXT);
        Font fBesar = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        g.setFont(fBesar);
        g.drawString("DASHBOARD ADMIN", margin, headerH / 2 - fBesar.getHeight() / 2, Graphics.TOP | Graphics.LEFT);
        
        // 3. Scrollbar indicator
        int totalRows = (menus.length + cols - 1) / cols;
        int totalContentH = gridY - startY + scrollY + totalRows * (cardH + spacing) + margin - headerH;
        int viewH = h - headerH;
        
        if (totalContentH > viewH) {
            int scrollBarH = Math.max(20, (viewH * viewH) / totalContentH);
            int scrollBarY = headerH + (scrollY * viewH) / totalContentH;
            g.setColor(COLOR_CARD_BORDER);
            g.fillRect(w - 4, headerH, 4, viewH);
            g.setColor(COLOR_HEADER);
            g.fillRect(w - 4, scrollBarY, 4, scrollBarH);
        }
    }

    private void ensureVisible() {
        int w = getWidth();
        int h = getHeight();
        int margin = 12;
        int spacing = 8;
        
        Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        
        int welcomeAreaH = fKecil.getHeight() + fSedang.getHeight() + 16;
        int row = selectedIndex / cols;
        
        int itemTop = margin + welcomeAreaH + row * (cardH + spacing);
        int itemBottom = itemTop + cardH;
        
        int viewH = h - 50; // screen height - header height
        
        if (itemTop < targetScrollY) {
            targetScrollY = itemTop - margin;
        } else if (itemBottom > targetScrollY + viewH) {
            targetScrollY = itemBottom - viewH + margin;
        }
        
        if (targetScrollY < 0) targetScrollY = 0;
        repaint();
    }

    protected void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);

        if (action == Canvas.UP) {
            if (selectedIndex >= cols) selectedIndex -= cols;
            else selectedIndex = 0;
            ensureVisible();
        } else if (action == Canvas.DOWN) {
            if (selectedIndex + cols < menus.length) selectedIndex += cols;
            else selectedIndex = menus.length - 1;
            ensureVisible();
        } else if (action == Canvas.LEFT) {
            if (selectedIndex > 0) selectedIndex--;
            ensureVisible();
        } else if (action == Canvas.RIGHT) {
            if (selectedIndex < menus.length - 1) selectedIndex++;
            ensureVisible();
        } else if (action == Canvas.FIRE) {
            eksekusiMenu(menus[selectedIndex].actionId);
        } else if (keyCode == Canvas.KEY_NUM0) {
            eksekusiMenu(9); // Logout
        } else {
            int num = keyCode - Canvas.KEY_NUM1;
            if (num >= 0 && num < menus.length - 1) { // 1-9
                selectedIndex = num;
                ensureVisible();
                eksekusiMenu(menus[selectedIndex].actionId);
            }
        }
        repaint();
    }

    protected void pointerPressed(int x, int y) {
        int headerH = 50;
        if (y < headerH) return; // Header click
        
        int w = getWidth();
        int margin = 12;
        int spacing = 8;
        
        Font fKecil = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font fSedang = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int welcomeAreaH = fKecil.getHeight() + fSedang.getHeight() + 16;
        
        int startY = headerH + margin - scrollY + welcomeAreaH;
        
        for (int i = 0; i < menus.length; i++) {
            int row = i / cols;
            int col = i % cols;
            
            int cx = margin + col * (cardW + spacing);
            int cy = startY + row * (cardH + spacing);
            
            if (x >= cx && x <= cx + cardW && y >= cy && y <= cy + cardH) {
                selectedIndex = i;
                ensureVisible();
                eksekusiMenu(menus[i].actionId);
                return;
            }
        }
    }

    private void eksekusiMenu(int actionId) {
        switch (actionId) {
            case 0: controller.navigatePasienBaru();  break;
            case 1: controller.navigateCariPasien();  break;
            case 4: controller.navigateDokter();      break;
            case 5: controller.navigateRuangan();     break;
            case 8: controller.navigateRiwayat();     break;
            case 9: controller.logout();              break;
        }
    }
}
