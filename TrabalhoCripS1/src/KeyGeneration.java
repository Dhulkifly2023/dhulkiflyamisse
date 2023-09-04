
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGeneration {
    public static void main(String[] args) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Tamanho da chave em bits (128 bits)
            SecretKey secretKey = keyGen.generateKey();
            // Agora você tem uma chave AES de 128 bits em secretKey
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
