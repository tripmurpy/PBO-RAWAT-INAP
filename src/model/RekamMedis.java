package model;

import java.util.Vector;

/**
 * RekamMedis — Rekam medis lengkap selama rawat inap.
 * COMPOSITION: composes Vector<CatatanHarian> dan Vector<Resep>.
 */
public class RekamMedis extends Entity {

    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_CLOSED = "CLOSED";

    private int recordId;
    private String admisiId;
    private String status;
    private long tanggalBuka;
    private long tanggalTutup;
    private Vector listCatatan; // Vector<CatatanHarian>
    private Vector listResep; // Vector<Resep>

    public RekamMedis() {
        this.status = STATUS_OPEN;
        this.listCatatan = new Vector();
        this.listResep = new Vector();
    }

    public RekamMedis(String id, String admisiId, long tanggalBuka) {
        super(id);
        this.admisiId = admisiId;
        this.status = STATUS_OPEN;
        this.tanggalBuka = tanggalBuka;
        this.tanggalTutup = 0L;
        this.listCatatan = new Vector();
        this.listResep = new Vector();
    }

    public int getRecordId() {
        return recordId;
    }

    public String getAdmisiId() {
        return admisiId;
    }

    public String getStatus() {
        return status;
    }

    public long getTanggalBuka() {
        return tanggalBuka;
    }

    public long getTanggalTutup() {
        return tanggalTutup;
    }

    public Vector getListCatatan() {
        return listCatatan;
    }

    public Vector getListResep() {
        return listResep;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setAdmisiId(String admisiId) {
        this.admisiId = admisiId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTanggalBuka(long tanggalBuka) {
        this.tanggalBuka = tanggalBuka;
    }

    public void setTanggalTutup(long tanggalTutup) {
        this.tanggalTutup = tanggalTutup;
    }

    public void setListCatatan(Vector listCatatan) {
        this.listCatatan = listCatatan;
    }

    public void setListResep(Vector listResep) {
        this.listResep = listResep;
    }

    public boolean isOpen() {
        return STATUS_OPEN.equals(status);
    }

    public void tambahCatatan(CatatanHarian c) {
        listCatatan.addElement(c);
    }

    public void tambahResep(Resep r) {
        listResep.addElement(r);
    }

    public void tutup(long tanggalTutup) {
        this.status = STATUS_CLOSED;
        this.tanggalTutup = tanggalTutup;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("RM ").append(id).append(" [").append(status).append("]");
        return sb.toString();
    }
}
