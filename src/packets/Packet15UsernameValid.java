package packets;

public class Packet15UsernameValid extends Packet {
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean valid;
	
	public Packet15UsernameValid(String n, boolean tf){
		super((byte)15);
		name = n;
		valid = tf;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean valid(){
		return valid;
	}
}
