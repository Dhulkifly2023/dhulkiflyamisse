import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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

            // Solicite ao usuário que digite a mensagem

            System.out.print("Digite a mensagem a ser enviada: ");
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            // Cifre a mensagem
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Grave o texto cifrado em um arquivo TXT
            FileOutputStream fos = new FileOutputStream("DhulkiflyAmisse.txt");
            fos.write(encryptedMessage);
            fos.close();

            // Envie o arquivo ao servidor
            FileInputStream fis = new FileInputStream("DhulkiflyAmisse.txt");
            byte[] buffer = new byte[1024];
            int bytesRead;
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
