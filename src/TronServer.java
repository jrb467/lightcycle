import java.awt.EventQueue;

import server.ServerGUI;

public class TronServer {

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ServerGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
