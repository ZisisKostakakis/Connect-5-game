package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import connect5.Constants;
import connect5.GameParameters;


public class SettingsWindow extends JFrame {
	
	/**
	 * 
	 */
	
	private JLabel gameModeLabel;
	private JLabel maxDepth1Label;
	private JLabel maxDepth2Label;
	private JLabel player1ColorLabel;
	private JLabel player2ColorLabel;
	
	private JComboBox<String> game_mode_drop_down;
	private JComboBox<Integer> max_depth1_drop_down;
	private JComboBox<Integer> max_depth2_drop_down;
	private JComboBox<String> player1_color_drop_down;
	private JComboBox<String> player2_color_drop_down;
	
	private JButton apply;
	private JButton cancel;
	
	private EventHandler handler;
		
	public static int width = 400;
	public static int height = 320;
	
	
	public SettingsWindow() {
		super("Settings");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
		
		handler = new EventHandler();
		int selectedMode = GameParameters.gameMode;
		int maxDepth1 = GameParameters.maxDepth1 - 1;
		int maxDepth2 = GameParameters.maxDepth2 - 1;
		int selectedPlayer1Color = GameParameters.player1Color;
		int selectedPlayer2Color = GameParameters.player2Color;

		gameModeLabel = new JLabel("Game mode: ");
		maxDepth1Label = new JLabel("Minimax AI1 search depth: ");
		maxDepth2Label = new JLabel("AI2 search depth (AiVsAi mode): ");
		player1ColorLabel = new JLabel("Player 1 checker color: ");
		player2ColorLabel = new JLabel("Player 2 checker color: ");
		
		add(gameModeLabel);
		add(maxDepth1Label);
		add(maxDepth2Label);
		add(player1ColorLabel);
		add(player2ColorLabel);		
		
		
		
		game_mode_drop_down = new JComboBox<String>();
		game_mode_drop_down.addItem("Human vs AI");
		game_mode_drop_down.addItem("Human Vs Human");
		game_mode_drop_down.addItem("AI Vs AI");
		game_mode_drop_down.addItem("AI vs Human");
		
		if (selectedMode == Constants.HumanVsAi)
			game_mode_drop_down.setSelectedIndex(Constants.HumanVsAi - 1);
		else if (selectedMode == Constants.HumanVsHuman)
			game_mode_drop_down.setSelectedIndex(Constants.HumanVsHuman - 1);
		else if (selectedMode == Constants.AiVsAi)
			game_mode_drop_down.setSelectedIndex(Constants.AiVsAi - 1);
		else if (selectedMode == Constants.AiVsHuman)
			game_mode_drop_down.setSelectedIndex(Constants.AiVsHuman-1);
		
	
		max_depth1_drop_down = new JComboBox<Integer>();
		max_depth1_drop_down.addItem(1);
		max_depth1_drop_down.addItem(2);
		max_depth1_drop_down.addItem(3);
		max_depth1_drop_down.addItem(4);
		max_depth1_drop_down.addItem(5);
		max_depth1_drop_down.addItem(6);
		
		max_depth2_drop_down = new JComboBox<Integer>();
		max_depth2_drop_down.addItem(1);
		max_depth2_drop_down.addItem(2);
		max_depth2_drop_down.addItem(3);
		max_depth2_drop_down.addItem(4);
		max_depth2_drop_down.addItem(5);
		max_depth2_drop_down.addItem(6);
		
		max_depth1_drop_down.setSelectedIndex(maxDepth1);
		max_depth2_drop_down.setSelectedIndex(maxDepth2);
		
		player1_color_drop_down = new JComboBox<String>();
		player1_color_drop_down.addItem("RED");
		player1_color_drop_down.addItem("YELLOW");
		player1_color_drop_down.addItem("BLACK");
		player1_color_drop_down.addItem("GREEN");
		player1_color_drop_down.addItem("ORANGE");
		player1_color_drop_down.addItem("PURPLE");
		
		if (selectedPlayer1Color == Constants.RED)
			player1_color_drop_down.setSelectedIndex(Constants.RED - 1);
		else if (selectedPlayer1Color == Constants.YELLOW)
			player1_color_drop_down.setSelectedIndex(Constants.YELLOW - 1);
		else if (selectedPlayer1Color == Constants.BLACK)
			player1_color_drop_down.setSelectedIndex(Constants.BLACK - 1);
		else if (selectedPlayer1Color == Constants.GREEN)
			player1_color_drop_down.setSelectedIndex(Constants.GREEN - 1);
		else if (selectedPlayer1Color == Constants.ORANGE)
			player1_color_drop_down.setSelectedIndex(Constants.ORANGE - 1);
		else if (selectedPlayer1Color == Constants.PURPLE)
			player1_color_drop_down.setSelectedIndex(Constants.PURPLE - 1);
		
		player2_color_drop_down = new JComboBox<String>();
		player2_color_drop_down.addItem("RED");
		player2_color_drop_down.addItem("YELLOW");
		player2_color_drop_down.addItem("BLACK");
		player2_color_drop_down.addItem("GREEN");
		player2_color_drop_down.addItem("ORANGE");
		player2_color_drop_down.addItem("PURPLE");
		
		if (selectedPlayer2Color == Constants.RED)
			player2_color_drop_down.setSelectedIndex(Constants.RED - 1);
		else if (selectedPlayer2Color == Constants.YELLOW)
			player2_color_drop_down.setSelectedIndex(Constants.YELLOW - 1);
		else if (selectedPlayer2Color == Constants.BLACK)
			player2_color_drop_down.setSelectedIndex(Constants.BLACK - 1);
		else if (selectedPlayer2Color == Constants.GREEN)
			player2_color_drop_down.setSelectedIndex(Constants.GREEN - 1);
		else if (selectedPlayer2Color == Constants.ORANGE)
			player2_color_drop_down.setSelectedIndex(Constants.ORANGE - 1);
		else if (selectedPlayer2Color == Constants.PURPLE)
			player2_color_drop_down.setSelectedIndex(Constants.PURPLE - 1);
		
		
		add(game_mode_drop_down);
		add(max_depth1_drop_down);
		add(max_depth2_drop_down);
		add(player1_color_drop_down);
		add(player2_color_drop_down);


		gameModeLabel.setBounds(25, 25, 175, 20);
		maxDepth1Label.setBounds(25, 55, 175, 20);
		maxDepth2Label.setBounds(25, 85, 175, 20);
		player1ColorLabel.setBounds(25, 115, 175, 20);
		player2ColorLabel.setBounds(25, 145, 175, 20);
		
		game_mode_drop_down.setBounds(195, 25, 160, 20);
		max_depth1_drop_down.setBounds(195, 55, 160, 20);
		max_depth2_drop_down.setBounds(195, 85, 160, 20);
		player1_color_drop_down.setBounds(195, 115, 160, 20);
		player2_color_drop_down.setBounds(195, 145, 160, 20);
		
		apply = new JButton("Apply");
		cancel = new JButton("Cancel");
		add(apply);
		add(cancel);
		
		int distance = 10;
		apply.setBounds((int) (width / 2) - 110 - (int) (distance / 2), 230, 100, 30);
		apply.addActionListener(handler);
		cancel.setBounds((int) (width / 2) - 10 + (int) (distance / 2), 230, 100, 30);
		cancel.addActionListener(handler);
	}


	private class EventHandler implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			if (ev.getSource() == cancel) {
				dispose();
			}
			
			else if (ev.getSource() == apply) {
				try {
					
				
					int gameMode = game_mode_drop_down.getSelectedIndex() + 1;
					int maxDepth = (int) max_depth1_drop_down.getSelectedItem();
					int player1Color = player1_color_drop_down.getSelectedIndex() + 1;
					int player2Color = player2_color_drop_down.getSelectedIndex() + 1;
										
					if(player1Color == player2Color) {
						JOptionPane.showMessageDialog(null,
								"Player 1 and Player 2 cannot have the same color of checkers!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// Change game parameters based on settings.
				
					GameParameters.gameMode = gameMode;
					GameParameters.maxDepth1 = maxDepth;
					GameParameters.player1Color = player1Color;
					GameParameters.player2Color = player2Color;
					
					JOptionPane.showMessageDialog(null,
							"Game settings have been changed.\nThe changes will be applied in the next game.",
							"", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				
				catch(Exception e) {
					System.err.println("ERROR : " + e.getMessage());
				}
				
			} // else if.
			
		} // action performed.
		
	} // inner class.
	
	
} // class end.
