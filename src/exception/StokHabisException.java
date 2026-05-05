package exception;

public class StokHabisException extends AppException {
    public StokHabisException(String namaObat, int stokTersedia) {
        super(new StringBuffer().append("Stok ").append(namaObat).append(" tidak cukup (tersedia: ").append(stokTersedia).append(")").toString());
    }
}
