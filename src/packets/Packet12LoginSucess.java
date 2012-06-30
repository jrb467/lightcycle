package packets;

public class Packet12LoginSucess extends Packet {
	private static final long serialVersionUID = 1L;
	private boolean sucess;
	private String name;
	
	public Packet12LoginSucess(boolean b, String name){
		super((byte)12);
		sucess = b;
		this.name = name;
	}
	
	public boolean sucessful(){
		return sucess;
	}
	
	public String getUsername(){
		return name;
	}
}
