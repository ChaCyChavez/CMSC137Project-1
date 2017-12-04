import java.awt.image.BufferStrategy;

public class Main {
	public static void main(String[] args) {
		try {
			if(Integer.parseInt(args[0]) == 0) { //start server
				new UDPServer(args[1], Integer.parseInt(args[3]), Integer.parseInt(args[4])); 
				//<server> <udp port number> <player limit>
				Server server = new Server(Integer.parseInt(args[2])); 
				//<tcp port number>
        server.startServer();
			} else if(Integer.parseInt(args[0]) == 1) { //serve client
				new Frame(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));				
				//<server> <tcp portnumber> <udp portnumber>
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
