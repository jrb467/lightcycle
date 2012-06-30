package server.util.commands;

import java.util.List;
import java.util.Arrays;
import server.util.*;

public class Command {
	public static String name = "";
	public static String help = "The basic command. Cannot be executed";
	
	@SuppressWarnings("unchecked")
	public static final List<Class<? extends Command>> commands = Arrays.asList(ClearCommand.class, CloseCommand.class, ListCommand.class, HelpCommand.class, 
																				FingerCommand.class, RemoveCommand.class, ResetCommand.class);
	
	//Override in other commands
	public static boolean test(InputHandler handler, StringList words){
		return true;
	}

}
