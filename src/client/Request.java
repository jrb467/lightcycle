package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.File;

import javax.imageio.ImageIO;

public class Request {
	private Color transparent = new Color(70, 70,70,220);
	private String user;
	private Image red = getImage("misc/red.png");
	private Image green = getImage("misc/green.png");
	private TextLayout orientation;
	private FontRenderContext context;
	private Font font = getFont("misc/sf.ttf");
	private Font playerFont = font.deriveFont(20f);
	private int hoverCount = 0;
	private RequestPanel panel;
	
	public Request(RequestPanel panel, String user){
		this.panel = panel;
		this.user = user;
	}

	public void paint(Graphics g){
		g.setFont(font);
		g.setColor(transparent);
		g.fillRoundRect(0, 0, 200, 100, 20, 20);
		g.setColor(Color.white);
		g.drawRoundRect(0, 0, 200, 100, 20, 20);
		context = ((Graphics2D)g).getFontRenderContext();
		orientation = new TextLayout("Gamer request from:", font, context);
		g.drawString("Game request from:", (int)(200-orientation.getBounds().getWidth())/2, 20);
		g.setFont(playerFont);
		orientation = new TextLayout(user, playerFont, context);
		g.drawString(user,(int)(200-orientation.getBounds().getWidth())/2,45);
		g.drawImage(green, 10, 60, 80, 30, null);
		g.drawImage(red, 110, 60, 80, 30, null);
		g.drawRect(10, 60, 80, 30);
		g.drawRect(110, 60, 80, 30);
		g.setColor(Color.black);
		orientation = new TextLayout("Accept", playerFont, context);
		g.drawString("Accept", 50-(int)(orientation.getBounds().getWidth()/2), 75+(int)(orientation.getBounds().getHeight()/2));
		orientation = new TextLayout("Decline", playerFont, context);
		g.drawString("Decline", 150-(int)(orientation.getBounds().getWidth()/2), 75+(int)(orientation.getBounds().getHeight()/2));
		if(hoverCount != 0){
			g.setColor(new Color(0,0,0,15*hoverCount));
			g.fillRoundRect(-1, -1, 202, 102, 20, 20);
		}
	}
	
	public void update(boolean hovering){
		if(hovering){
			hoverCount--;
			if(hoverCount < 0) hoverCount = 0;
		}else{
			hoverCount++;
			if(hoverCount > 10) hoverCount = 10;
		}
	}
	
	private Image getImage(String file){
		try{
			return ImageIO.read(new File(file));
		}catch(Exception e){
			return null;
		}
	}
	
	private Font getFont(String file){
		try{
			Font temp = Font.createFont(Font.TRUETYPE_FONT, new File(file));
			temp = temp.deriveFont(16f);
			return temp;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void mousePress(int x, int y){
		if(new Rectangle(10, 60, 80, 30).contains(x, y)){
			panel.accept();
		}else if(new Rectangle(110, 60, 80, 30).contains(x, y)){
			panel.decline();
		}
	}
	
	public String getUser(){
		return user;
	}
}
