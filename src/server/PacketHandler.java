package server;

import java.util.ArrayList;

import packets.*;
import util.network.ConnectionData;

public class PacketHandler {
	private ServerGUI gui;
	
	public PacketHandler(ServerGUI gui){
		this.gui = gui;
	}
	
	public void handlePacket(Packet p, ConnectionData c){
		switch(p.getID()){
		case(4):
			gui.connectionSevered(c);
			gui.writeInterupt(c.name+" disconnected");
			break;
		case(5):
			ArrayList<String> temp = new ArrayList<String>();
			for(User u: UserManager.getOnlineUsers()){
				temp.add(u.getName());
			}
			temp.remove(c.name);
			c.write(new Packet3Clients(temp));
			break;
		case(9):
			if(!gui.isInGame(gui.getFromName(((Packet9RequestGame)p).getPlayer()))){
				gui.getFromName(((Packet9RequestGame)p).getPlayer()).write(new Packet9RequestGame(c.name));
			}
		//TODO add in client confirmation
			break;
		case(10):
			User u = UserManager.getUser(((Packet10Login)p).getUser(), ((Packet10Login)p).getPassword());
			if(u == null){
				c.write(new Packet12LoginSucess(false, ""));
			}else{
				if(!UserManager.getOnlineUsers().contains(u)){
					gui.writeInterupt(((Packet10Login)p).getUser() + " ("+c.name+") logged in");
					c.name = ((Packet10Login)p).getUser();
					c.write(new Packet12LoginSucess(true, u.getName()));
					UserManager.getOnlineUsers().add(UserManager.getUser(c.name));
					gui.updateClients();
				}else{
					c.write(new Packet12LoginSucess(false, ""));
				}
			}
			break;
		case(11):
			if(UserManager.getUser(((Packet11CreateAccount)p).getUser()) == null){
				UserManager.addUser(new User(((Packet11CreateAccount)p).getUser(), ((Packet11CreateAccount)p).getPassword()));
				c.write(new Packet13CreateAccountSucess(true));
				c.write(new Packet13CreateAccountSucess(true));
				gui.writeInterupt("Account created:");
				gui.writeInterupt("     Username: "+((Packet11CreateAccount)p).getUser());
				gui.writeInterupt("     Password: "+((Packet11CreateAccount)p).getPassword());
			}else{
				c.write(new Packet13CreateAccountSucess(false));
			}
			break;
		case(14):
			//Used for creating accounts
			if(UserManager.getUser(((Packet14TestUsername)p).getName()) == null)
				c.write(new Packet15UsernameValid(((Packet14TestUsername)p).getName(), true));
			else
				c.write(new Packet15UsernameValid("", false));
			break;
		case(17):
			gui.getClients().remove(c);
			UserManager.getOnlineUsers().remove(UserManager.getUser(c.name));
			gui.writeInterupt(c.name + " logged out");
			c.setName("temp_"+(gui.getClients().size()+1)+"");
			gui.getClients().add(c);
			gui.updateClients();
			break;
		case(18):
			if(!gui.isInGame(gui.getFromName(((Packet18ConfirmRequest)p).getUser()))){
				Game g = gui.createGame(c, gui.getFromName(((Packet18ConfirmRequest)p).getUser()));
				c.setNetworkHandler(g);
				gui.getFromName(((Packet18ConfirmRequest)p).getUser()).setNetworkHandler(g);
				gui.writeInterupt("Game created between "+c.name+" and "+((Packet18ConfirmRequest)p).getUser());
				c.write(new Packet16NewGame(c.name, ((Packet18ConfirmRequest)p).getUser(), true));
				gui.getFromName(((Packet18ConfirmRequest)p).getUser()).write(new Packet16NewGame(c.name, ((Packet18ConfirmRequest)p).getUser(), false));
			}
			break;
		case(19):
			User user = UserManager.getUser(c.name);
			c.write(new Packet20Stats(user.getName(), user.getGamesPlayed(), user.getWins(), user.getWLRatio(), user.getEffectiveRating()));
			break;
		}
	}
}
