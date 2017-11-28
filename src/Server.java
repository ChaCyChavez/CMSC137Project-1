import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

public class Server {
    private int port;
    private ArrayList<ClientThread> clients;

    public Server(int portNumber){
        this.port = portNumber;
    }

    public ArrayList<ClientThread> getClients(){
        return clients;
    }

    public void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            acceptClients(serverSocket);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void acceptClients(ServerSocket serverSocket){
        while(true){
            try{
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
                System.out.println("Accepted " + socket.getRemoteSocketAddress());                
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}