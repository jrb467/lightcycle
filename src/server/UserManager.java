package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserManager {
	private static final File userFile = new File("misc/user.dat");
	private static ArrayList<User> users = new ArrayList<User>();
	private static ArrayList<User> online = new ArrayList<User>();
	private static boolean running;
	
	public synchronized static void load(){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(userFile));
			Object o;
			while((o = in.readObject()) != null){
				if(o instanceof User){
					users.add((User)o);
				}
			}
			in.close();
			running = true;
			Thread backUpThread = new Thread(){
				public void run(){
					while(running){
						backUp();
						try{
							Thread.sleep(120000);
						}catch(Exception e){}
					}
				}
			};
			backUpThread.start();
		} catch (Exception e){
			running = false;
		}
	}
	
	public synchronized static void backUp(){
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFile));
			out.flush();
			for(User u: users){
				out.writeObject(u);
			}
			out.flush();
			out.close();
		}catch(Exception e){
			running = false;
		}
	}
	
	public synchronized static void save(){
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFile));
			out.flush();
			for(User u: users){
				out.writeObject(u);
			}
			out.flush();
			out.close();
			running = false;
		}catch(Exception e){}
	}
	
	public static synchronized void addUser(User u){
		users.add(u);
	}
	
	public static ArrayList<User> getUsers(){
		return users;
	}
	
	public static ArrayList<User> getOnlineUsers(){
		return online;
	}
	
	public static User getUser(String name, String pword){
		for(User u: users){
			if(u.getName().equals(name) && u.getPassword().equals(pword)) return u;
		}
		return null;
	}
	
	public static User getUser(String name){
		try{
			for(User u: users){
				if(u.getName().equals(name)) return u;
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
