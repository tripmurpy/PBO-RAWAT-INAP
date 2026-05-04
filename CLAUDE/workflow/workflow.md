# WORKFLOW & ARCHITECTURE: 5-Layer OOP

## 1. Arsitektur Sistem
Sistem ini menggunakan pemisahan tanggung jawab yang ketat antar lapisan:

1. **Presentation Layer (UI):** `LoginScreen`, `DashboardScreen`, `ScreenManager` (Singleton).
2. **Controller Layer:** `AuthController`, `PasienController`, `AdmisiController`, dll.
3. **Service Layer (Business Logic):** `AuthService`, `Validator`, `NoRMGenerator`.
4. **Domain Model Layer:** `Pasien`, `Dokter`, `Ruangan`, `Admisi` (POJO + Interface Repository).
5. **Infrastructure Layer (RMS):** `PasienDB`, `UserDB`, `RMSUtil` (Implementasi konkret).

## 2. Struktur Folder
```
src/
├── ui/          # UI Components & Navigation
├── controller/  # Action Handlers
├── service/     # Business Logic & Rules
├── model/       # Entities & Repository Interfaces
├── storage/     # RMS Persistence Implementation
└── util/        # Helpers (Date, Validation, ID)
```

## 3. Urutan Implementasi (Backend-First)
1. **Domain Model:** Buat POJO dan Repository Interfaces.
2. **Infrastructure:** Implementasikan penyimpanan RMS (`storage/`).
3. **Service:** Buat logika bisnis dan validasi.
4. **Controller:** Buat jembatan antara UI dan Service.
5. **UI:** Buat layar tampilan menggunakan LCDUI/Canvas.

## 4. Standar Coding
- **Singleton:** Gunakan untuk `ScreenManager`.
- **Error Handling:** Tampilkan pesan yang jelas kepada user jika RMS gagal atau input tidak valid.
- **Data Persistence:** Setiap entitas memiliki `RecordStore` sendiri.
- **Naming:** Gunakan CamelCase (Bahasa Indonesia).
