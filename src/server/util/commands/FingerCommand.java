package server.util.commands;

import server.User;
import server.UserManager;
import server.util.InputHandler;
import server.util.StringChecker;
import server.util.StringList;

public class FingerCommand extends Command {
	public static String name = "finger";
	public static String help = "Name: finger \n>finger {str} \n>finger (username) \n>Returns all available info on the given user";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "finger")){
			if(words.size() > 1){
				User u = UserManager.getUser(words.get(1));
				if(u == null){
					handler.field.write("Not a valid user");
					return true;
				}else{
					handler.field.write("Name: "+u.getName());
					handler.field.write("Password: "+u.getPassword());
					handler.field.write("Games Played: "+u.getGamesPlayed());
					handler.field.write("Games Won: "+u.getWins());
					if(UserManager.getOnlineUsers().contains(u)){
						handler.field.write("User is online");
					}else{
						handler.field.write("User is offline");
					}
					return true;
				}
			}
		}
		return false;
	}

}
