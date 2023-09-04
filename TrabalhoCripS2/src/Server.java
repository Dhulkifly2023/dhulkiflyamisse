import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();

            // Envie a chave AES para o cliente
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(secretKey);
            out.flush();

            // Receba o arquivo com o texto cifrado do cliente
            ObjectInputStream fileIn = new ObjectInputStream(clientSocket.getInputStream());
            FileOutputStream fos = new FileOutputStream("texto_cifrado_recebido.txt");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();

            // Inicialize o cifrador AES
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Leia o arquivo cifrado e decifre
            FileInputStream fis = new FileInputStream("texto_cifrado_recebido.txt");
            ByteArrayOutputStream decryptedMessage = new ByteArrayOutputStream();
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (decryptedBytes != null) {
                    decryptedMessage.write(decryptedBytes);
                }
            }
            byte[] finalDecryptedBytes = cipher.doFinal();
            if (finalDecryptedBytes != null) {
                decryptedMessage.write(finalDecryptedBytes);
            }

            String message = new String(decryptedMessage.toByteArray(), "UTF-8");
            System.out.println("Texto decifrado: " + message);

            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
