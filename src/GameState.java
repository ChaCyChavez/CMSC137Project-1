public class GameState {
    private HashMap gamePlayers = new HashMap();

    public GameState() {

    } 

    public void update(String playerName, GamePlayer player) {
        gamePlayers.put(playerName, player);
    }

    public HashMap getGamePlayers() {
        return gamePlayers;
    }

    public String gameToString() {
        String game = "";
        for(Iterator i = gamePlayers.keySet().iterator(); i.hasNext();){
			String playerName = (String) i.next();
			GamePlayer player = (GamePlayer) gamePlayers.get(playerName);
			game += player.playerToString() + ":";
            System.out.println(game);
		}

		return game;
    }
}