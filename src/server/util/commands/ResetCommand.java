package server.util.commands;

import server.User;
import server.UserManager;
import server.util.InputHandler;
import server.util.StringChecker;
import server.util.StringList;

public class ResetCommand extends Command {
	public static String name = "reset";
	public static String help = "Name: reset \n>reset {str} \n>reset (username) \n>Resets the stats of one player.";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "reset")){
			if(words.size() > 1){
				User u = UserManager.getUser(words.get(1));
				if(u == null){
					handler.field.write("Not a valid user");
					return true;
				}else{
					u.resetGamesPlayed();
					u.resetGamesWon();
					handler.field.write("User reset");
					return true;
				}
			}
		}
		return false;
	}
}
