package packets;

public class Packet9RequestGame extends Packet {
	private static final long serialVersionUID = 1L;
	private String player;
	
	public Packet9RequestGame(String player){
		super((byte)9);
		this.player = player;
	}
	
	public String getPlayer(){
		return player;
	}
}
