package packets;

public class Packet16NewGame extends Packet {
	private static final long serialVersionUID = 1L;
	private boolean p1;
	private String p1Name;
	private String p2Name;
	
	public Packet16NewGame(String p1Name, String p2Name, boolean p1){
		super((byte)16);
		this.p1 = p1;
		this.p1Name = p1Name;
		this.p2Name = p2Name;
	}
	
	public String getP1(){
		return p1Name;
	}
	
	public String getP2(){
		return p2Name;
	}
	
	public boolean isP1(){
		return p1;
	}

}
