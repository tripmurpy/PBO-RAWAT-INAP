package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordEnumeration;

/**
 * RMSUtil — Utilitas untuk operasi RMS (Record Management System).
 * 
 * Menyediakan metode helper untuk serialisasi/deserialisasi data
 * menggunakan DataOutputStream dan DataInputStream, serta
 * operasi dasar CRUD pada RecordStore.
 * 
 * ENCAPSULATION: Semua detail implementasi RMS disembunyikan,
 * layer lain hanya memanggil metode publik tanpa tahu mekanisme internal.
 */
public class RMSUtil {

    // ========== SERIALISASI HELPER ==========

    /**
     * Membuat DataOutputStream baru yang terbungkus ByteArrayOutputStream.
     * Gunakan untuk menulis data secara berurutan.
     */
    public static Object[] buatOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        return new Object[]{baos, dos};
    }

    /**
     * Mengambil byte[] hasil dari ByteArrayOutputStream.
     */
    public static byte[] ambilBytes(Object[] streamPair) throws IOException {
        DataOutputStream dos = (DataOutputStream) streamPair[1];
        dos.flush();
        ByteArrayOutputStream baos = (ByteArrayOutputStream) streamPair[0];
        return baos.toByteArray();
    }

    /**
     * Membuat DataInputStream dari byte[] record.
     */
    public static DataInputStream buatInputStream(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        return new DataInputStream(bais);
    }

    // ========== OPERASI RECORD STORE ==========

    /**
     * Membuka atau membuat RecordStore baru.
     * @param namaStore nama RecordStore
     * @return RecordStore yang sudah terbuka
     */
    public static RecordStore bukaStore(String namaStore) throws RecordStoreException {
        return RecordStore.openRecordStore(namaStore, true);
    }

    /**
     * Menutup RecordStore dengan aman.
     */
    public static void tutupStore(RecordStore rs) {
        if (rs != null) {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException e) {
                // Abaikan error saat menutup
            }
        }
    }

    /**
     * Menyimpan record baru ke RecordStore.
     * @return recordId dari record yang baru ditambahkan
     */
    public static int simpanRecord(RecordStore rs, byte[] data) throws RecordStoreException {
        return rs.addRecord(data, 0, data.length);
    }

    /**
     * Memperbarui record yang sudah ada.
     */
    public static void updateRecord(RecordStore rs, int recordId, byte[] data) throws RecordStoreException {
        rs.setRecord(recordId, data, 0, data.length);
    }

    /**
     * Menghapus record dari RecordStore.
     */
    public static void hapusRecord(RecordStore rs, int recordId) throws RecordStoreException {
        rs.deleteRecord(recordId);
    }

    /**
     * Mengambil data record berdasarkan ID.
     */
    public static byte[] ambilRecord(RecordStore rs, int recordId) throws RecordStoreException {
        return rs.getRecord(recordId);
    }

    /**
     * Menghapus seluruh isi RecordStore (untuk reset data).
     */
    public static void hapusSemuaRecord(String namaStore) {
        try {
            RecordStore.deleteRecordStore(namaStore);
        } catch (RecordStoreNotFoundException e) {
            // Store belum ada, abaikan
        } catch (RecordStoreException e) {
            // Error lain saat menghapus
        }
    }

    /**
     * Mendapatkan jumlah record dalam store.
     */
    public static int hitungRecord(RecordStore rs) throws RecordStoreException {
        return rs.getNumRecords();
    }

    /**
     * Mendapatkan enumerasi seluruh record.
     */
    public static RecordEnumeration ambilSemuaRecord(RecordStore rs) throws RecordStoreException {
        return rs.enumerateRecords(null, null, false);
    }
}
