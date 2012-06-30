package packets;

public class Packet1Win extends Packet {
	private static final long serialVersionUID = 1L;
	public String winner;
	public int p1Wins;
	public int p2Wins;
	
	public Packet1Win(String winner, int p1, int p2){
		super((byte)1);
		this.winner = winner;
		p1Wins = p1;
		p2Wins = p2;
	}
}
