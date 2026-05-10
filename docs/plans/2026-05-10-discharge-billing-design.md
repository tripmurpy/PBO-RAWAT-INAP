# Design Document: Patient Discharge Billing System (Approach 2)

**Date:** 2026-05-10
**Topic:** Hospital Inpatient Billing Breakdown
**Status:** Validated

## 1. Overview
The Patient Discharge Billing System is designed to provide a transparent and premium invoice breakdown when a patient is discharged. It implements a class-based pricing logic where VIP rooms receive higher-end services (food and medicine/vitamins).

## 2. Calculation Logic (Approach 2: Class-Based)
The system automatically calculates costs based on the room type and duration of stay.

| Category | VIP / VVIP Room | Regular Room (Class 1-3) | Frequency |
| :--- | :--- | :--- | :--- |
| **Ruangan** | Based on Room Price | Based on Room Price | Per Day |
| **Makanan** | Rp 200,000 | Rp 125,000 | Per Day |
| **Obat & Vitamin** | Rp 400,000 | Rp 250,000 | Per Day |
| **Administrasi** | Rp 50,000 | Rp 50,000 | Flat (One-time) |

### Formula:
`Total = (Harga Kamar * Hari) + (Rate Makanan * Hari) + (Rate Obat/Vit * Hari) + Admin`

## 3. Architecture & Data Model

### A. Model Changes (`Admisi.java` & `Kunjungan.java`)
We will add fields to store the specific breakdown:
- `biayaRuangan` (int)
- `biayaMakanan` (int)
- `biayaObat` (int)
- `biayaAdmin` (int)

### B. Storage Changes (`AdmisiDB.java`)
- **Serialization**: Append the 4 new integers to the byte stream.
- **Deserialization**: Use `dis.available()` to check for the presence of these fields to maintain compatibility with old data records.

### C. UI Changes (`PasienKeluarScreen.java`)
- **Invoice Layout**: Redesign the "Detail Biaya" section to show an itemized list with dotted lines (Receipt style).
- **History View**: Update `KunjunganScreen` to display these details when a past record is selected.

## 4. Verification Plan
1. **Build**: Ensure the project compiles successfully after model changes.
2. **Discharge Flow**: Verify that selecting "Pasien Keluar" calculates the correct values for both VIP and non-VIP rooms.
3. **Persistence**: Discharge a patient, then check the visit history to ensure the breakdown was saved correctly.
