import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        try {
            this.clientOutput = new PrintWriter(socket.getOutputStream(), false); //output stream to send data
            BufferedReader socketInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input stream to receive responses

            while(!socket.isClosed()){
                if(socketInputStream.ready()){
                    String input;                
                    if((input = socketInputStream.readLine()) != null){
                        for(ClientThread thatClient : server.getClients()){ 
                            PrintWriter thatClientOutput = thatClient.getWriter(); //writes to every PrintWriter of clients
                            if(thatClientOutput != null){
                                thatClientOutput.write(input + "\r\n");
                                thatClientOutput.flush();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}