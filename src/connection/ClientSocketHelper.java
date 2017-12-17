package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.Socket;


/**
 * Created by Empyreans on 11.12.2017.
 */
public class ClientSocketHelper implements Runnable {

    private Socket clientSocket;
    private Server server;

    public ClientSocketHelper(Socket clientSocket, Server server){
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        establishClientCommunication();
    }

    public void establishClientCommunication() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            Server.log.info("[IP: " + clientSocket.getInetAddress().toString() + "]" + " [PORT: " + clientSocket.getPort() + "]" + " [ThreadID: " + Thread.currentThread().getId() + "]" + " [ProzessID: " + ManagementFactory.getRuntimeMXBean().getName() + "]");
            System.out.println("Der connection.Server hat eine Verbindung mit dem connection.Client hergestellt");
            out.println("Bitte geben sie ein Datum in folgendem Format ein: tt.mm.jjjj");

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                out.println(server.serverHandle(clientMessage));
            }
        } catch (IOException e) {
            // hier vielleicht aus Array nehmen falls benötigt
        }
    }


}
