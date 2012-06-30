package packets;


public class Packet13CreateAccountSucess extends Packet {
	private static final long serialVersionUID = 1L;
	private boolean sucess;
	
	public Packet13CreateAccountSucess(boolean b){
		super((byte)13);
		sucess = b;
	}
	
	public boolean sucessful(){
		return sucess;
	}
}
