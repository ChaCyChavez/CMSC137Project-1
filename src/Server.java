import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int portNumber = 4444;

    private int port;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"

    // public static void main(String[] args){
    //     Server server = new Server(portNumber);
    //     server.startServer();
    // }

    public Server(int portNumber){
        this.port = portNumber;
    }

    public List<ClientThread> getClients(){
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