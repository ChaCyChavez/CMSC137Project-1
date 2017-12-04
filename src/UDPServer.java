import java.net.DatagramSocket;
import java.net.DatagramPacket;

import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

public class UDPServer implements Runnable {
    private String playerData;
    private DatagramSocket serverDatagramSocket = null;
    private GameState gameState;
    private Thread thread = new Thread(this);
    private int connectedPlayers = 0;
    private int stage = 3; //public final int WAITING_FOR_PLAYERS=3;
    private int playerLimit;
    private Random rand;
    private String players = "PLAYER_LIST ";
    private Boolean running = true;
    private String server;
    private long start;
    private long end;

    ArrayList<Food> foods = new ArrayList<Food>();
    ArrayList<Bomb> bombs = new ArrayList<Bomb>();

    public UDPServer (String server, int portNumber, int playerLimit) {
        this.playerLimit = playerLimit;

        try {
            serverDatagramSocket = new DatagramSocket(portNumber);
            serverDatagramSocket.setSoTimeout(7000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameState = new GameState();

        generateFoodBomb();
        thread.start();
    }

    public void generateFoodBomb() {
        rand = new Random();

        for(int i = 0; i < 10; i++) {
            int fx = Math.abs(rand.nextInt() % 830) + 80;
            int fy = Math.abs(rand.nextInt() % 430) + 80;

            int bx = Math.abs(rand.nextInt() % 830) + 80;
            int by = Math.abs(rand.nextInt() % 430) + 80;

            foods.add(new Food(fx, fy));
            bombs.add(new Bomb(bx, by));
        }


    }

    public void broadcast(String message) { //broadcast data to all players
        //iterate through players from getGamePlayers method of GameState
        for(Iterator i = gameState.getGamePlayers().keySet().iterator(); i.hasNext();){
			String playerName = (String) i.next();
			GameObject player = (GameObject) gameState.getGamePlayers().get(playerName);			
			sendMessage(player, message);	
		}
    }

    public void sendMessage(GameObject gamePlayer, String message) { //send message to a player
        byte buffer[] = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, gamePlayer.getInetAddress(), gamePlayer.getPortNumber());
        try {
            serverDatagramSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long start = System.currentTimeMillis();
        long end = start + 60*1000; // 60 seconds * 1000 ms/sec

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        long power_timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while(running) {
            frames++;

            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // packet for receiving packets with lengthf buffer
            try {
                serverDatagramSocket.receive(packet);
            } catch (Exception e) {
                // e.printStackTrace();
            }

            playerData = new String(buffer);
            playerData = playerData.trim();

            switch(stage) {
                case 3: //if waiting for players
                    if(playerData.startsWith("CONNECT")) {
                        String playerDataTokens[] = playerData.split(" "); //split playerData by space
                        System.out.println("playerDataToken[1] = " + playerDataTokens[1]);                        
                        if(!players.contains(playerDataTokens[1])) {    
                            Circle player = new Circle(100, 100, playerDataTokens[1], packet.getAddress(), packet.getPort(), server); //instantiate new player
                            gameState.update(playerDataTokens[1].trim(), player); //add to player hashmap
                            System.out.println("Player connected: " + playerDataTokens[1]);                        
                            broadcast("CONNECTED " + playerDataTokens[1]);
                            players += playerDataTokens[1] + ":" + "100:100 ";
                            connectedPlayers++;
                            if (connectedPlayers == playerLimit){
                                stage = 0; //public static final int GAME_START=0;
                            }
                        }
                    }
                    break;
                case 0: //GAME_START
                    System.out.println("Game State: START");
                    for(int i = 0; i < foods.size(); i++) {
                        players += foods.get(i).getName() +
                                    ":" + foods.get(i).getX() + ":" +
                                    foods.get(i).getY() + " ";

                        players += bombs.get(i).getName() +
                                    ":" + bombs.get(i).getX() + ":" +
                                    bombs.get(i).getY() + " ";
                    }

                    int x = Math.abs(rand.nextInt() % 830) + 80;
                    int y = Math.abs(rand.nextInt() % 430) + 80;

                    players += "powerup:" + x + ":" + y;

                    broadcast(players);
                    stage = 1; //IN_PROGRESS
                    start = System.currentTimeMillis();
                    end = start + 600*1000; // 60 seconds * 1000 ms/sec
                    break;
                case 1: //IN_PROGRESS
                    start = System.currentTimeMillis();
                    broadcast("TIME " + Long.toString(((end-start)/1000)/60) + ":" + Long.toString(((end-start)/1000)%60));
                    if (playerData.startsWith("PLAYER")){
                        String[] playerPosition = playerData.split(" ");	 //Tokenize:				  
                        String playerName = playerPosition[1]; //The format: PLAYER <player name> <x> <y>
                        float xPosition = Float.parseFloat(playerPosition[2].trim());
                        float yPosition = Float.parseFloat(playerPosition[3].trim());
                        int score = Integer.parseInt(playerPosition[4].trim());
                        Boolean alive = Boolean.parseBoolean(playerPosition[5].trim());
                        GameObject player = (GameObject) gameState.getGamePlayers().get(playerName);	//Get the player from the game state				  
                        player.setX(xPosition);
                        player.setY(yPosition);
                        player.setScore(score);
                        if(!alive) player.isDead(); 
                        gameState.update(playerName, player); //Update current player in hashmap of players
                        broadcast(gameState.gameToString()); //Send to all the updated game state
					} else if(playerData.startsWith("FOOD")) {
                        if(System.currentTimeMillis() - timer > 1000) {
                          timer += 1000;
                            int fx = Math.abs(rand.nextInt() % 830) + 80;
                            int fy = Math.abs(rand.nextInt() % 430) + 80;

                            Food food = new Food(fx, fy);
                            broadcast("FOOD " + fx + ":" + fy);

                          frames = 0;
                          updates = 0;
                          break;
                        }

                        if(System.currentTimeMillis() - power_timer > 30000) {
                          power_timer += 30000;
                            int px = Math.abs(rand.nextInt() % 830) + 80;
                            int py = Math.abs(rand.nextInt() % 430) + 80;

                            PowerUp food = new PowerUp(px, py);
                            broadcast("POWERUP " + px + ":" + py);
                          break;
                        }
                    }
                    if(!(System.currentTimeMillis() < end)) {
                        running = false;
                    }
					break;
            }

            int remaining = 0;
            int highScore = 0;
            String currWinner = "";
            for(Iterator i = gameState.getGamePlayers().keySet().iterator(); i.hasNext();){
                String playerName = (String) i.next();
                GameObject player = (GameObject) gameState.getGamePlayers().get(playerName);			
                if(player.getType().equals("circle") && player.isAlive()) {
                    remaining += 1;
                    if(player.getScore() > highScore) {
                        highScore = player.getScore();
                        currWinner = player.getName();
                    }
                }
            }

            if(remaining == 1 && stage == 1 || !running) {
                running = false;
                broadcast("END " + currWinner);
            }
        }
    }
}