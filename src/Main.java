

public class Main {
	public static void main(String[] args) {
		try {
			if(Integer.parseInt(args[0]) == 0) { //start server
				Server server = new Server(4444);
        		server.startServer();
			} else if(Integer.parseInt(args[0]) == 1) { //serve client
				new Frame();				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}