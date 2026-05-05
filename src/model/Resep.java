package model;

import java.util.Vector;

/**
 * Resep — Entitas resep obat untuk satu sesi kunjungan.
 * COMPOSITION: composes Vector<ItemObat>.
 */
public class Resep extends Entity {

    private int recordId;
    private String rekamMedisId;
    private long tanggal;
    private Vector listItem; // Vector<ItemObat>

    public Resep() {
        this.listItem = new Vector();
    }

    public Resep(String id, String rekamMedisId, long tanggal) {
        super(id);
        this.rekamMedisId = rekamMedisId;
        this.tanggal = tanggal;
        this.listItem = new Vector();
    }

    public int getRecordId() {
        return recordId;
    }

    public String getRekamMedisId() {
        return rekamMedisId;
    }

    public long getTanggal() {
        return tanggal;
    }

    public Vector getListItem() {
        return listItem;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setRekamMedisId(String rekamMedisId) {
        this.rekamMedisId = rekamMedisId;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public void setListItem(Vector listItem) {
        this.listItem = listItem;
    }

    public void tambahItem(ItemObat item) {
        listItem.addElement(item);
    }

    public int hitungTotalHarga(java.util.Hashtable obatMap) {
        int total = 0;
        for (int i = 0; i < listItem.size(); i++) {
            ItemObat item = (ItemObat) listItem.elementAt(i);
            Obat obat = (Obat) obatMap.get(item.getObatId());
            if (obat != null) {
                total += obat.getHargaPerUnit() * item.getQuantity();
            }
        }
        return total;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Resep ").append(id).append(" (").append(listItem.size()).append(" obat)");
        return sb.toString();
    }
}
