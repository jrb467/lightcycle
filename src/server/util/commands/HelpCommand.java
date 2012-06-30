package server.util.commands;

import server.util.*;

public class HelpCommand extends Command {
	public static String name = "help";
	public static String help = "Name: help \n>help {str} \n>help (command name) \n>Command to get info on the other commands.";
	
	public static boolean test(InputHandler handler, StringList words){
		if(StringChecker.testWord(words, 0, "help")){
			if(words.size() > 1){
				String[] commands = new String[Command.commands.size()];
				for(int i = 0; i < Command.commands.size(); i++){
					try {
						commands[i] = Command.commands.get(i).getField("name").get(null).toString();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
				int choice = StringChecker.testWord(words, 1, commands);
				if(choice != -1)
					try{
						handler.field.write(Command.commands.get(choice).getField("help").get(null).toString());
					}catch(Exception e){
						e.printStackTrace();
					}
				else
					handler.field.write("Not a valid choice");
			}else{
				handler.field.write("No paramaters provided");
			}
			return true;
		}
		return false;
	}

}
