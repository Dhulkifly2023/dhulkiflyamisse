import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Server {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Aguardando conexão...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Conexão estabelecida.");

            // Gere uma chave AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();

            // Envie a chave AES para o cliente
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(secretKey);
            out.flush();

            // Inicialize o cifrador AES
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Receba a mensagem do cliente e decifre
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            byte[] encryptedMessage = (byte[]) in.readObject();
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
            String message = new String(decryptedMessage);

            System.out.println("Mensagem recebida: " + message);

            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
