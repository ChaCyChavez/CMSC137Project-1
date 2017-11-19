import java.awt.image.BufferStrategy;

public class Main {
	public static void main(String[] args) {
		try {
			if(Integer.parseInt(args[0]) == 0) { //start server
				new UDPServer(Integer.parseInt(args[1]), Integer.parseInt(args[2])); //argument is player limit
				Server server = new Server(Integer.parseInt(args[1])); //argument is port number
        server.startServer();
			} else if(Integer.parseInt(args[0]) == 1) { //serve client
				new Frame(args[1], Integer.parseInt(args[2]));				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}