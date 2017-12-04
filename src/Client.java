import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class Client {
    private String name;
    private String serverHost;
    private int serverPort;
    public String message;
    public Boolean sendMessage = false;
    public GamePanel gamePanel;

    public Client(String name, String host, int port, GamePanel gamePanel){
        this.name = name;
        this.serverHost = host;
        this.serverPort = port;
        this.gamePanel = gamePanel;

        gamePanel.chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                message = gamePanel.input.getText();
                sendMessage = true;
                gamePanel.input.setText("");
            }
        });

        gamePanel.input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                message = gamePanel.input.getText();
                sendMessage = true;
                gamePanel.input.setText("");
            }
        });
    }

    public void startClient(){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating

            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), false); //output stream to send data
            BufferedReader serverInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream())); //input stream to read data

            while(!socket.isClosed()){
                if(serverInputStream.ready()){ //wait for data to become available
                    String input;                
                    if((input = serverInputStream.readLine()) != null){ //read from inputstream
                        gamePanel.appendConversationPane(input + "\n"); //must print to JTextPane
                    }
                }
                if(sendMessage){
                    serverOutput.println(name + ": " + message); //write to output stream
                    serverOutput.flush();
                    message = null;
                    sendMessage = false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}