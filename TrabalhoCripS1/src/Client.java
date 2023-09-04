import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.Security;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            Socket socket = new Socket("localhost", 12345);

            // Receba a chave AES do servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            SecretKey secretKey = (SecretKey) in.readObject();

            // Inicialize o cifrador AES
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            System.out.print("Digite a mensagem a ser enviada: ");
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            // Envie a mensagem cifrada para o servidor
            //String message = "Mensagem secreta";
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(encryptedMessage);
            out.flush();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
