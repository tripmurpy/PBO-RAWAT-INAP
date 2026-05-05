package model;

/**
 * Person — Abstract class untuk entitas yang merupakan "orang".
 * Extends Entity, menambahkan nama, tglLahir, jenisKelamin.
 *
 * INHERITANCE: Pasien, Dokter, Perawat meng-extends class ini.
 * POLYMORPHISM: tampilkan() dapat di-override per subclass.
 */
public abstract class Person extends Entity {

    protected String nama;
    protected long tglLahir;
    protected String jenisKelamin;

    public Person() {
    }

    public Person(String id, String nama, long tglLahir, String jenisKelamin) {
        super(id);
        this.nama = nama;
        this.tglLahir = tglLahir;
        this.jenisKelamin = jenisKelamin;
    }

    public String getNama() {
        return nama;
    }

    public long getTglLahir() {
        return tglLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTglLahir(long tglLahir) {
        this.tglLahir = tglLahir;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    /** Representasi ringkas — dapat di-override subclass (polymorphism). */
    public String tampilkan() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" (").append(id).append(")");
        return sb.toString();
    }

    public String toString() {
        return tampilkan();
    }
}
