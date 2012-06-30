package packets;

public class Packet14TestUsername extends Packet {
	private static final long serialVersionUID = 1L;
	private String name;
	
	public Packet14TestUsername(String n){
		super((byte)14);
		name = n;
	}
	
	public String getName(){
		return name;
	}
}
