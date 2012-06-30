package packets;

public class Packet4Disconnect extends Packet {
	private static final long serialVersionUID = 1L;
	
	public Packet4Disconnect(){
		super((byte)4);
	}
}
