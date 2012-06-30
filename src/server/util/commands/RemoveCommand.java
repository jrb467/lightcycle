package server.util.commands;

import server.User;
import server.UserManager;
import server.util.InputHandler;
import server.util.StringChecker;
import server.util.StringList;

public class RemoveCommand extends Command {
	public static String name = "remove";
	public static String help = "Name: remove \n>remove {str} \n>remove (username) \n>Removes a user from the userlist.";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "remove")){
			if(words.size() > 1){
				User u = UserManager.getUser(words.get(1));
				if(u == null){
					handler.field.write("Not a valid user");
					return true;
				}else{
					UserManager.getUsers().remove(u);
					return true;
				}
			}
		}
		return false;
	}
}
