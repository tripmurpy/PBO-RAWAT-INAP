package model;

/**
 * CatatanHarian — Catatan medis harian pasien dalam satu rekam medis.
 * Disimpan sebagai bagian komposit dari RekamMedis.
 */
public class CatatanHarian {

    private String id;
    private long tanggal;
    private String vitalSigns;  // max 200 char
    private String keluhan;     // max 200 char
    private String diagnosa;    // max 200 char
    private String tindakan;    // max 200 char

    public CatatanHarian() {}

    public CatatanHarian(String id, long tanggal, String vitalSigns,
                         String keluhan, String diagnosa, String tindakan) {
        this.id = id;
        this.tanggal = tanggal;
        this.vitalSigns = truncate(vitalSigns, 200);
        this.keluhan = truncate(keluhan, 200);
        this.diagnosa = truncate(diagnosa, 200);
        this.tindakan = truncate(tindakan, 200);
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) : s;
    }

    public String getId() { return id; }
    public long getTanggal() { return tanggal; }
    public String getVitalSigns() { return vitalSigns; }
    public String getKeluhan() { return keluhan; }
    public String getDiagnosa() { return diagnosa; }
    public String getTindakan() { return tindakan; }

    public void setId(String id) { this.id = id; }
    public void setTanggal(long tanggal) { this.tanggal = tanggal; }
    public void setVitalSigns(String vitalSigns) { this.vitalSigns = truncate(vitalSigns, 200); }
    public void setKeluhan(String keluhan) { this.keluhan = truncate(keluhan, 200); }
    public void setDiagnosa(String diagnosa) { this.diagnosa = truncate(diagnosa, 200); }
    public void setTindakan(String tindakan) { this.tindakan = truncate(tindakan, 200); }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Catatan ").append(id).append(": ").append(diagnosa);
        return sb.toString();
    }
}
