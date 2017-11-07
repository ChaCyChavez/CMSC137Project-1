import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String name;
    private String serverHost;
    private int serverPort;
    public Socket socket;
    private ServerThread serverThread;

    // public static void main(String[] args){
    //     System.out.println("Enter your name:");
    //     Scanner sc = new Scanner(System.in);
    //     String readName = null;
    //     while(readName == null || readName.trim().equals("")){ // null, empty, whitespace(s) not allowed.
    //         readName = sc.nextLine();
    //         if(readName.trim().equals("")){
    //             System.out.println("Invalid name. Enter a new one:");
    //         }
    //     }

    //     Client client = new Client(readName, host, port);
    //     client.startClient(sc);
    // }

    public Client(String name, String host, int port){
        this.name = name;
        this.serverHost = host;
        this.serverPort = port;
    }

    public void startClient(){
        try{
            socket = new Socket(serverHost, serverPort);
            Scanner sc = new Scanner(System.in);
            Thread.sleep(1000); // waiting for network communicating.

            serverThread = new ServerThread(socket, name);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(sc.hasNextLine()){
                    serverThread.addMessage(sc.nextLine());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}