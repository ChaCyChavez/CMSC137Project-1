import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String name;
    private final LinkedList<String> toSend;
    private boolean hasMessages = false;

    public ServerThread(Socket socket, String name){
        this.socket = socket;
        this.name = name;
        toSend = new LinkedList<String>();
    }

    public void addMessage(String message){
        synchronized (toSend){
            hasMessages = true;
            toSend.push(message);
        }
    }

    @Override
    public void run(){
        System.out.println("Welcome :" + name);
        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInputStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInputStream);

            while(!socket.isClosed()){
                if(serverInputStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String sendNext = "";
                    synchronized(toSend){
                        sendNext = toSend.pop();
                        hasMessages = !toSend.isEmpty();
                    }
                    serverOutput.println(name + " > " + sendNext);
                    serverOutput.flush();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}