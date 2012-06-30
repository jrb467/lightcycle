package server;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String pword;
	private int totalGamesWon;
	private int totalGamesPlayed;
	private float effectiveRating;
	//TODO add in more stats maybe?
	
	public User(String name, String pword){
		this.name = name;
		this.pword = pword;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public void resetGamesPlayed(){
		totalGamesPlayed = 0;
	}
	
	public void resetGamesWon(){
		totalGamesWon = 0;
	}
	
	public String getPassword(){
		return pword;
	}
	
	public int getWins(){
		return totalGamesWon;
	}
	
	public int getGamesPlayed(){
		return totalGamesPlayed;
	}
	
	public float getWLRatio(){
		return (float)totalGamesWon/totalGamesPlayed;
	}
	
	public void gameWon(){
		totalGamesWon++;
	}
	
	public void gamePlayed(){
		totalGamesPlayed++;
	}
	
	public void setPassword(String newPword){
		pword = newPword;
	}
	
	public float getEffectiveRating(){
		return effectiveRating;
	}
	
	public void setEffectiveRating(float newRating){
		effectiveRating = newRating;
	}
}
