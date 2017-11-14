import java.awt.Color;

import java.net.InetAddress;

public class GamePlayer {
    private InetAddress inetAddress;
    private int portNumber;
    private String playerName;
    private int xPosition, yPosition;
		private Color playerColor;

    public GamePlayer(String playerName, InetAddress inetAddress, int portNumber) {
        this.playerName = playerName;
        this.inetAddress = inetAddress;
        this.portNumber = portNumber;
				this.playerColor = new Color((int)(Math.random() * 0x1000000));
    }

    public InetAddress getInetAddress(){
		return inetAddress;
	}

	public int getPortNumber(){
		return portNumber;
	}

	public String getPlayerName(){
		return playerName;
	}

  public void setX(int x){ /*** Sets the X coordinate of the player */
		this.xPosition = x;
	}
	
	public int getX(){ /*** Returns the X coordinate of the player */
		return xPosition;
	}
	
	public int getY(){ /*** Returns the y coordinate of the player */
		return yPosition;
	}
	
	public void setY(int y){ /*** Sets the y coordinate of the player */
		this.yPosition = y;		
	}

	public String playerToString(){ /*** String representation. used for transfer over the network */
		return ("PLAYER " + playerName + " " + xPosition + " " + yPosition + " " + Integer.toString(playerColor.getRGB()));
	}	
}