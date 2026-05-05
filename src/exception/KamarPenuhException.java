package exception;

public class KamarPenuhException extends AppException {
    public KamarPenuhException(String namaKamar) {
        super(new StringBuffer().append("Kamar ").append(namaKamar).append(" sudah penuh").toString());
    }
}
