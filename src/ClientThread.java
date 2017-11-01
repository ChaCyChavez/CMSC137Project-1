import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOutput;
    private Server server;

    public ClientThread(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOutput;
    }

    @Override
    public void run() {
        try{
            this.clientOutput = new PrintWriter(socket.getOutputStream(), false);
            InputStream socketInputStream = socket.getInputStream();
            Scanner sc = new Scanner(socketInputStream);

            while(!socket.isClosed()){
                if(sc.hasNextLine()){
                    String input = sc.nextLine();
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOutput = thatClient.getWriter();
                        if(thatClientOutput != null){
                            thatClientOutput.write(input + "\r\n");
                            thatClientOutput.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}