package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Empyreans on 31.10.2017.
 */
public class Client {

    public Client(int port){
        connectClientToServer(port);
    }

    private void connectClientToServer(int port) {
        Scanner scanner = new Scanner(System.in);

        try (
                Socket socket = new Socket("localhost", port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            char[] serverMessage = new char[3000];
            StringBuilder stringBuilder = new StringBuilder();

            while ((in.read(serverMessage)) > 0) {

                String serverMessageString = new String(serverMessage);
                String serverMessageStringSplit = serverMessageString.split("\u0000")[0];

                stringBuilder.append(serverMessageStringSplit);
                System.out.println(stringBuilder.toString());

                String clientMessage = scanner.next();
                out.println(clientMessage);

                serverMessage = new char[3000];
                stringBuilder.setLength(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
