import java.awt.EventQueue;

import client.LoginBox;

public class Tron {
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try{
					new LoginBox();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
