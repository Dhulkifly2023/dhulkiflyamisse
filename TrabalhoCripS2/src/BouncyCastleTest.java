// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class BouncyCastleTest {
    public static void main(String[] args) {
        // Registra o provedor Bouncy Castle
        Security.addProvider(new BouncyCastleProvider());

        // Exibe a lista de provedores de segurança, incluindo o Bouncy Castle
        for (java.security.Provider provider : Security.getProviders()) {
            System.out.println(provider.getName());
        }
    }
}
