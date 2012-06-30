package server.util.commands;

import java.net.InetAddress;

import server.User;
import server.UserManager;
import server.util.*;

public class ListCommand extends Command {
	public static String name = "list";
	public static String help = "Name: list \n>list {str:opt('commands','network','info','users')} {IF(users)[str:opt('all','online')]}"
								+"\n>list (group to list) \n>Lists either all commands, all connected clients, basic local network info, or information on users.";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "list")){
			String[] options = {"commands", "network", "info", "users"};
			int choice = StringChecker.testWord(words, 1, options);
			switch(choice){
			case 0:
				for(Class<? extends Command> c: Command.commands){
					try {
						handler.field.write(c.getField("name").get(null).toString());
					} catch (Exception e){
						e.printStackTrace();
						handler.field.write("Error finding name encountered");
					}
				}
				break;
			case 1:
				//TODO
				break;
			case 2:
				try{
					handler.field.write(InetAddress.getLocalHost().getHostAddress());
				}catch(Exception e){}
				break;
			case 3:
				String[] o2 = {"all", "online"};
				int c2 = StringChecker.testWord(words, 2, o2);
				switch(c2){
				case 0:
					for(User u: UserManager.getUsers()){
						handler.field.write(u.getName());
					}
					break;
				case 1:
					for(User u: UserManager.getOnlineUsers()){
						handler.field.write(u.getName());
					}
					break;
				default:
					handler.field.write("Not a valid option (Must be either 'all' or 'online')");
					break;
				}
				break;
			default:
				handler.field.write("Not a valid list option");
				break;
			}
			return true;
		}
		return false;
	}

}
