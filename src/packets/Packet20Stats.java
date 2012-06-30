package packets;

public class Packet20Stats extends Packet {
	private static final long serialVersionUID = 1L;
	private String name;
	private int won;
	private int played;
	private float ratio;
	private float rating;
	
	public Packet20Stats(String name, int played, int won, float ratio, float rating){
		super((byte)20);
		this.name = name;
		this.won = won;
		this.played = played;
		this.ratio = ratio;
		this.rating = rating;
	}
	
	public String getName(){
		return name;
	}
	
	public int getGamesWon(){
		return won;
	}
	
	public int getGamesPlayed(){
		return played;
	}
	
	public float getWinLossRatio(){
		return ratio;
	}
	
	public float getEffectiveRating(){
		return rating;
	}
}
