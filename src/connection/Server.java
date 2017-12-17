package connection;

import parser.CSVParser;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by Empyreans on 31.10.2017.
 */
public class Server {

    private CSVParser csvParser;
    private ServerSocket serverSocket;
    public static final Logger log = Logger.getLogger(ClientSocketHelper.class.getName());


    public Server(String fileName, int port){
        this.csvParser = new CSVParser(fileName);
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Serversocket konnte nicht erstellt werden");
        }
        configureLogger();
        establishClientConnection();
    }

    private void configureLogger(){
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
            SimpleFormatter stringFormater = new SimpleFormatter();
            FileHandler fh = new FileHandler("Uebung4.log", true);
            fh.setFormatter(stringFormater);
            log.addHandler(fh);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void establishClientConnection() {
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientSocketHelper(clientSocket, this)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String serverHandle(String clientMessage) {
        String parserResponse;
        if (csvParser.dayAvailabe(clientMessage) != null){
            if (csvParser.countAvailableWeatherDataForDay(clientMessage) != 24) {
                parserResponse = "keine 24 Wetterdaten für den Tag vorhanden";
            } else {
                parserResponse = csvParser.requestDayWeatherData(clientMessage);
            }
        } else {
            parserResponse = "Tag nicht vorhanden";
        }
        return parserResponse;
    }

}
