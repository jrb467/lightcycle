package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;

import util.ButtonAdapter;
import util.network.ConnectionData;
import util.network.Networked;
import packets.*;

public class TronFrame extends JFrame implements Networked{
	private static final long serialVersionUID = 1L;
	private JPanel main;
	private JPanel connect;
	private JPanel options;
	private JList<String> clientList;
	private JPanel stats;
	
	private JLabel name;
	private JLabel played;
	private JLabel wins;
	private JLabel ratio;
	private JLabel rating;
	
	private ArrayList<String> players = new ArrayList<String>();
	
	protected ConnectionData server;

	/**
	 * Create the frame.
	 */
	public TronFrame( ConnectionData server, LoginBox b) {
		super("Tron");
		
		this.server = server;
		server.setNetworkHandler(this);
		
		init(b);
	}
	
	private void init(LoginBox b){
		setResizable(false);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 640);
		
		setLocation(b.getX()+(b.getWidth()-getWidth())/2, b.getY()+(b.getHeight()-getHeight())/2);
		
		main = new JPanel();
		main.setBackground(Color.BLACK);
		main.setBorder(new EmptyBorder(0,0,0,0));
		main.setLayout(new MigLayout("", "[grow]", "[200px:200px:200px][90px:90px:90px][90px:90px:90px][90px:90px:90px][90px:90px:90px]"));
		setContentPane(main);
		
		connect = new JPanel();
		connect.setBackground(Color.BLACK);
		connect.setBorder(new EmptyBorder(0,0,0,0));
		connect.setLayout(new MigLayout("", "[grow]", "[200px:200px:200px][260px:260px:260px][40px:40px:40px][40px:40px:40px]"));
		
		options = new JPanel();
		options.setBackground(Color.BLACK);
		options.setBorder(new EmptyBorder(0,0,0,0));
		options.setLayout(new MigLayout("", "[grow]", "[200px:200px:200px][120px:120px:120px][120px:120px:120px][120px:120px:120px]"));
		
		stats = new JPanel();
		stats.setBackground(Color.BLACK);
		stats.setBorder(new EmptyBorder(0,0,0,0));
		stats.setLayout(new MigLayout("", "[300px:300px:300px,left][300px:300px:300px,right]", "[200px:200px:200px][100px:100px:100px][25px:25px:25px][75px:75px:75px][25px:25px:25px][75px:75px:75px][60px:60px:60px]"));
		
		//Set up logo for all three
		
		JLabel icon = null;
		try{
			icon = new JLabel(new ImageIcon(new URL("misc/tron.jpg")));
		}catch(Exception e){}

		main.add(icon, "cell 0 0,alignx center,aligny center");

		try{
			icon = new JLabel(new ImageIcon(new URL("misc/tron.jpg")));
		}catch(Exception e){}
		connect.add(icon, "cell 0 0,alignx center,aligny center");

		try{
			icon = new JLabel(new ImageIcon(new URL("misc/tron.jpg")));
		}catch(Exception e){}
		options.add(icon, "cell 0 0,alignx center, aligny center");

		try{
			icon = new JLabel(new ImageIcon(new URL("misc/tron.jpg")));
		}catch(Exception e){}
		stats.add(icon, "cell 0 0, alignx center, aligny center");
		
		name = new JLabel ("");
		name.setFont(new Font("SansSerif", Font.PLAIN, 50));
		name.setForeground(Color.WHITE);
		stats.add(name, "cell 0 1 2 1,alignx center,aligny center");
		
		JLabel label = new JLabel ("Games Played:");
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		stats.add(label, "cell 0 2,alignx center,aligny center");
		
		label = new JLabel ("Games Won:");
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		stats.add(label, "cell 1 2,alignx center,aligny center");
		
		played = new JLabel ("");
		played.setFont(new Font("SansSerif", Font.PLAIN, 30));
		played.setForeground(Color.WHITE);
		stats.add(played, "cell 0 3,alignx center,aligny center");
		
		wins = new JLabel ("");
		wins.setFont(new Font("SansSerif", Font.PLAIN, 30));
		wins.setForeground(Color.WHITE);
		stats.add(wins, "cell 1 3,alignx center,aligny center");
		
		label = new JLabel ("Win/Loss Ratio:");
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		stats.add(label, "cell 0 4,alignx center,aligny center");
		
		label = new JLabel ("Effective Rating:");
		label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		stats.add(label, "cell 1 4,alignx center,aligny center");
		
		ratio = new JLabel ("");
		ratio.setFont(new Font("SansSerif", Font.PLAIN, 30));
		ratio.setForeground(Color.WHITE);
		stats.add(ratio, "cell 0 5,alignx center,aligny center");
		
		rating = new JLabel ("");
		rating.setFont(new Font("SansSerif", Font.PLAIN, 30));
		rating.setForeground(Color.WHITE);
		stats.add(rating, "cell 1 5,alignx center,aligny center");
		
		JButton btn = new JButton("Find Opponent");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				setContentPane(connect);
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				validate();
				Thread h = new Thread(){
					public void run(){
						while(getContentPane().equals(connect)){
							for(String s: players){
								if(!((DefaultListModel<String>)clientList.getModel()).contains(s)){
									((DefaultListModel<String>)clientList.getModel()).addElement(s);
								}
							}
							for(int i = 0; i < ((DefaultListModel<String>)clientList.getModel()).size(); i++){
								if(!players.contains(((DefaultListModel<String>)clientList.getModel()).get(i))){
									((DefaultListModel<String>)clientList.getModel()).remove(i);
								}
							}
							server.write(new Packet5Request());
							try{
								Thread.sleep(300);
							}catch(Exception e){}
						}
						((DefaultListModel<String>)clientList.getModel()).clear();
						players.clear();
					}
				};
				h.start();
			}
		});
		main.add(btn, "cell 0 1,alignx center,aligny center");
		
		btn = new JButton("Back");
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				setContentPane(options);
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				validate();
			}
		});
		stats.add(btn, "cell 0 6 2 1,grow");
		
		btn = new JButton("Local Game");
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.setFocusable(false);
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				OfflineGame g = new OfflineGame(getInstance());
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				setContentPane(g);
				g.start();
				validate();
			}
		});
		main.add(btn, "cell 0 2,alignx center,aligny center");
		
		btn = new JButton("Options");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				setContentPane(options);
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				validate();
				repaint();
			}
		});
		main.add(btn, "cell 0 3,alignx center,aligny center");
		
		btn = new JButton("Quit");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				System.exit(0);
			}
		});
		main.add(btn, "cell 0 4,alignx center,aligny center");
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(false);
		scrollPane.setBackground(Color.BLACK);
		scrollPane.setBorder(null);
		scrollPane.setMinimumSize(new Dimension(350, 280));
		scrollPane.setMaximumSize(new Dimension(350, 240));
		connect.add(scrollPane, "cell 0 1,alignx center,aligny center");
		
		clientList = new JList<String>();
		clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientList.setFocusable(false);
		scrollPane.setViewportView(clientList);
		clientList.setModel(new DefaultListModel<String>());
		clientList.setMaximumSize(new Dimension(200, 250));
		clientList.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		clientList.setBackground(Color.LIGHT_GRAY);
		clientList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		clientList.setMinimumSize(new Dimension(200, 250));
		
		btn = new JButton("Connect");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(350, 40));
		btn.setMaximumSize(new Dimension(350, 40));
		btn.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				Object o = clientList.getSelectedValue();
				if(o != null){
					server.write(new Packet9RequestGame(o.toString()));
				}
			}
		});
		connect.add(btn, "cell 0 2,alignx center,aligny center");
		
		btn = new JButton("Back");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(350, 40));
		btn.setMaximumSize(new Dimension(350, 40));
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				setContentPane(main);
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				validate();
			}
		});
		connect.add(btn, "cell 0 3,alignx center,aligny center");
		
		btn = new JButton("View Stats");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				server.write(new Packet19StatRequest());
			}
		});
		options.add(btn, "cell 0 1,alignx center,aligny center");
		
		btn = new JButton("Switch Player");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				server.write(new Packet17Logout());
				new LoginBox(server);
				server = null;
				dispose();
			}
		});
		options.add(btn, "cell 0 2,alignx center,aligny center");
		
		btn = new JButton("Back");
		btn.setFocusable(false);
		btn.setMinimumSize(new Dimension(300, 75));
		btn.setMaximumSize(new Dimension(300, 75));
		btn.addMouseListener(new ButtonAdapter(btn){
			public void mousePressed(MouseEvent e){
				setContentPane(main);
				button.dispatchEvent(new MouseEvent(button, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, button.getX()+1, button.getY()+1, 1, true));
				validate();
			}
		});
		options.add(btn, "cell 0 3,alignx center,aligny center");
		this.requestFocusInWindow();
		setVisible(true);
	}
	
	public TronFrame getInstance(){
		return this;
	}
	
	public void goToMain(){
		setContentPane(main);
		validate();
	}

	public void connectionSevered(ConnectionData connection) {
		JOptionPane.showMessageDialog(this, "Disconnected from server", "Network Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	public void handleInput(Object o, ConnectionData connection) {
		if(o instanceof Packet){
			switch(((Packet)o).getID()){
			case(3):
				players = ((Packet3Clients)o).getClients();
				break;
			case(9):
				for(Component c: getContentPane().getComponents()){
					if(c instanceof RequestPanel){
						((RequestPanel)c).requests.add(new Request((RequestPanel)c, ((Packet9RequestGame)o).getPlayer()));
					}
				}
				//TODO go back to icon logo and simply do an overlay instead
				break;
			case(16):
				Game g = new Game(this, ((Packet16NewGame)o).isP1(), ((Packet16NewGame)o).getP1(), ((Packet16NewGame)o).getP2());
				server.setNetworkHandler(g);
				setContentPane(g);
				g.start();
				validate();
				break;
			case(20):
				name.setText(((Packet20Stats)o).getName());
				played.setText(((Packet20Stats)o).getGamesPlayed() + "");
				wins.setText(((Packet20Stats)o).getGamesWon() + "");
				ratio.setText(((Packet20Stats)o).getWinLossRatio() + "");
				rating.setText(((Packet20Stats)o).getEffectiveRating() + "");
				setContentPane(stats);
				validate();
				break;
			}
		}
	}
}