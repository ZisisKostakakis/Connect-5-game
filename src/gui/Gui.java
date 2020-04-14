package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.imageio.IIOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import connect5.Board;
import connect5.Constants;
import connect5.GameParameters;
import connect5.MiniMaxAi;
import connect5.Move;

public class Gui {
	
	public static int i=0;
	public static int ii=0;
	static Board board;
	static JFrame frame_Main_Window;
	static JFrame frame_Game_Over;
	
	static JPanel panel_Main;
	static JPanel panel_Board_Numbers;
	static JPanel panel_AIvsAI;
	static JLayeredPane layered_Game_Board;
	static JPanel panel_Turns;
	
	static final int DEFAULT_WIDTH = 650;
	static final int DEFAULT_HEIGHT = 600;
	
    static final Stack<JLabel> label = new Stack<JLabel>();
    static final Stack<JLabel> labelAI = new Stack<JLabel>();
	static boolean firstGame = true;

	static JButton column1_button = new JButton("1");
	static JButton column2_button = new JButton("2");
	static JButton column3_button = new JButton("3");
	static JButton column4_button = new JButton("4");
	static JButton column5_button = new JButton("5");
	static JButton column6_button = new JButton("6");
	static JButton column7_button = new JButton("7");
	static JButton column8_button = new JButton("8");
	// buttons for turns
	private static JTextField player1_turn;
	private static JTextField player2_turn;
	// pause and start buttons
	static  JButton pause = new JButton("Pause");
	static JButton start = new JButton("Start");
	static JButton Full_Speed = new JButton("Full Speed");

	static MiniMaxAi ai;

	//	Player 1 symbol: X. Player 1 plays first.
	//	Player 2 symbol: O. Player 2 plays second.
	
	public static JLabel checker_Label = null;
	
	// for Undo operation
	private static int human_player_undo_row;
	private static int human_player_undo_col;
	private static int human_player_undo_letter;
	
	
	private static JLabel human_Player_Undo_Checker_Label;

	// Menu bars and items
	static JMenuBar menu_Bar;
	static JMenu file_Menu;
	static JMenuItem new_Game_Item;
	static JMenuItem undo_Item;
	static JMenuItem settings_Item;
	static JMenuItem exit_Item;
	static JMenu help_Menu;
	static JMenuItem how_To_Play_Item;
	static JMenuItem about_Item;
	
	public static int undo_Array [][] = new int[100][3];
	public static int undo_Array2 [][] = new int[100][3];

	public Gui() {
		
		try {
			// Option 1
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	// Adds the menu bars and items to the window.
	private static void AddMenus() {
		
		// Adding the menu bar
		menu_Bar = new JMenuBar();
		
		file_Menu = new JMenu("File");
		new_Game_Item = new JMenuItem("New Game");
		undo_Item = new JMenuItem("Undo or Ctrl+Z");
		settings_Item = new JMenuItem("Settings");
		exit_Item = new JMenuItem("Quit");
		
		undo_Item.setEnabled(false);

		file_Menu.add(new_Game_Item);
		file_Menu.add(undo_Item);
		file_Menu.add(settings_Item);
		file_Menu.add(exit_Item);
		
		help_Menu = new JMenu("Help");
		how_To_Play_Item = new JMenuItem("How to Play");
		about_Item = new JMenuItem("Licence");
		help_Menu.add(how_To_Play_Item);
		help_Menu.add(about_Item);

		new_Game_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				create_New_Game();
			}
		});
		
		undo_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		
		settings_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow settings = new SettingsWindow();
				settings.setVisible(true);
			}
		});
		
		exit_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		how_To_Play_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"You can either click the buttons or press 1-8 on your keyboard to insert a new checker."
						+ "\nTo win you must place 5 checkers in an row, vertically, diagonally or horizontally.",
						"How to Play", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		about_Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Created By Zisis Kostakakis",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		menu_Bar.add(file_Menu);
		menu_Bar.add(help_Menu);
		
		frame_Main_Window.setJMenuBar(menu_Bar);
		// Makes the board visible after adding menus.
		frame_Main_Window.setVisible(true);
		
	}
	
	
	// This is the main Connect-5 board.
	public static JLayeredPane create_Layered_Board() {
		layered_Game_Board = new JLayeredPane();
		layered_Game_Board.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		layered_Game_Board.setBorder(BorderFactory.createTitledBorder("Connect-5"));

		ImageIcon imageBoard = new ImageIcon(ResourceLoader.load("images/Board5.gif"));
		JLabel imageBoardLabel = new JLabel(imageBoard);

		imageBoardLabel.setBounds(20, 20, imageBoard.getIconWidth(), imageBoard.getIconHeight());
		layered_Game_Board.add(imageBoardLabel, 0, 1);

		return layered_Game_Board;
	}
	
	
	public static KeyListener gameKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			String button = KeyEvent.getKeyText(e.getKeyCode()); // variable responsible for the key events from keyboard
			
			if (button.equals("1")) {
				make_Move(0);

			} else if (button.equals("2")) {
				make_Move(1);

			} else if (button.equals("3")) {
				make_Move(2);

			} else if (button.equals("4")) {
				make_Move(3);

			} else if (button.equals("5")) {
				make_Move(4);

			} else if (button.equals("6")) {
				make_Move(5);

			} else if (button.equals("7")) {
				make_Move(6);

			}
			else if (button.equals("8")){
				make_Move(7);

			}
			
			 if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                undo();
                }
                 if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) !=0)){
                	create_New_Game();
                }
            
			
			if (button.equals("1") || button.equals("2") || button.equals("3") || button.equals("4")
					|| button.equals("5") || button.equals("6") || button.equals("7") || button.equals("8")) {
				if (!board.has_Overflow_Occured()) {
					if(GameParameters.gameMode == Constants.HumanVsHuman)
					{
						if(Constants.firstPlayer == true){
							Constants.COUNT1++;
							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							Constants.firstPlayer = false;
						}
						else if ( Constants.firstPlayer == false){
							Constants.COUNT2 ++;
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
							Constants.firstPlayer = true;
						}
					}

					Constants.is_gameover =	game();
					save_Undo_Move();
					
					if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
						Constants.COUNT1++;
						Constants.COUNT2 ++;
						ai_Move(ai);
						save_Undo_Move_AI();
						player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
						player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
					}
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	};
	
	
	
	private static void undo() {
		// This is the undo implementation for Human VS Human mode.
		if (GameParameters.gameMode == Constants.HumanVsHuman) {
			if(Constants.is_gameover)
				undo_Item.setEnabled(false);
			else{
			try {
				board.set_Game_Over(false);
				enable_Buttons();
				if (frame_Main_Window.getKeyListeners().length == 0) {
					frame_Main_Window.addKeyListener(gameKeyListener);
				}
				
				board.undo_Move_Option(undo_Array[i-1][0], undo_Array[i-1][1], undo_Array[i-1][2]);
				layered_Game_Board.remove(label.pop());
				frame_Main_Window.paint(frame_Main_Window.getGraphics());
				
				
				if(i>=0 && Constants.firstPlayer == true){
					i--;
					Constants.COUNT2 --;
					player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
					Constants.firstPlayer = false;
					System.out.println(i);
					}
				else if(i >=0 && Constants.firstPlayer == false){
					i--;
				Constants.COUNT1--;
				player1_turn.setText("Player 1 turn : " + Constants.COUNT2);
				Constants.firstPlayer = true;
				System.out.println(i);
				}
				
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.err.println("No move has been made yet!");
				System.err.flush();
			}
		}
	}
		// This is the undo implementation for Human VS AI and AI vs Human mode.
		 if (GameParameters.gameMode == Constants.HumanVsAi ||  GameParameters.gameMode == Constants.AiVsHuman) {
			if(Constants.is_gameover)
				undo_Item.setEnabled(false);
			else{
			try {
				
				enable_Buttons();
				if (frame_Main_Window.getKeyListeners().length == 0) {
					frame_Main_Window.addKeyListener(gameKeyListener);
				}
				board.undo_Move_Option(undo_Array[i-1][0], undo_Array[i-1][1],undo_Array[i-1][2]);
				board.undo_Move_Option(undo_Array2[ii-1][0],undo_Array2[ii-1][1], undo_Array2[ii-1][2]);
				layered_Game_Board.remove(labelAI.pop());
				layered_Game_Board.remove(label.pop()); 
				frame_Main_Window.paint(frame_Main_Window.getGraphics()); 	
				
			 
				if(i>=0){
				i--;
				Constants.COUNT1 --;
				player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
				System.out.println(i);
				}
				if(ii>=0){
					ii--;
					Constants.COUNT2--;
					player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
					System.out.println(ii);
				}
								
			} catch (EmptyStackException | ArrayIndexOutOfBoundsException ex) {
				System.err.println("No move has been made yet!");
				System.err.flush();
			}			
		}
	}		
}
	
	
	// To be called when the game starts for the first time
	// or a new game starts.
	public static void create_New_Game() {
		i=0;
		ii=0;
		board = new Board();		
		board = new Board();
		Constants.COUNT1 =0;
		Constants.COUNT2 =0;
		set_Color();
		buttons_Visible();
		Constants.firstPlayer = true;
		Constants.is_gameover = false;
		
		if (frame_Main_Window != null) frame_Main_Window.dispose();
		frame_Main_Window = new JFrame("Connect-5");
		// make the main window appear on the center
		center_Window(frame_Main_Window, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Component compMainWindowContents = create_Content_Components();
		frame_Main_Window.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);
		
		frame_Main_Window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		frame_Main_Window.addKeyListener(gameKeyListener);
		frame_Main_Window.setFocusable(true);
		
		// show window
		frame_Main_Window.pack();
		
		AddMenus();
		
		
		
		if (GameParameters.gameMode == Constants.HumanVsAi) {
			Constants.COUNT1 =0;
			Constants.COUNT2 =0;
			player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
			player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
			Full_Speed.setVisible(false);
			ai = new MiniMaxAi(GameParameters.maxDepth1, Constants.Player2);
			}
		
							
				
		else if ( GameParameters.gameMode == Constants.AiVsHuman)	{
			Constants.COUNT1 =0;
			Constants.COUNT2 =0;
			if(!board.is_Game_Over()){
				ai = new MiniMaxAi(GameParameters.maxDepth1, Constants.Player2);
				Full_Speed.setVisible(false);
				ai_Move(ai);
				Constants.COUNT2 ++;
				player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
			}
				}
		else if ( GameParameters.gameMode == Constants.HumanVsHuman){
				Full_Speed.setVisible(false);
		}
			
		
		 else if (GameParameters.gameMode == Constants.AiVsAi) {
			
			buttons_Invinsible();
			disable_Buttons();
			Full_Speed.setEnabled(true);
			Full_Speed.setVisible(true);
			
			// AI Vs AI implementation HERE
			// Initial maxDepth = 4. We can change this value for difficulty adjustment.
			MiniMaxAi ai1 = new MiniMaxAi(GameParameters.maxDepth1, Constants.Player1);
			MiniMaxAi ai2 = new MiniMaxAi(GameParameters.maxDepth2, Constants.Player2);
			frame_Main_Window.paint(frame_Main_Window.getGraphics());
		 
			
			
					Full_Speed.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							
							while(!Constants.is_gameover){
								 try{
									 //Thread.sleep(500);
									 ai_Move(ai1);
									 Constants.COUNT1++;
									 player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
									 frame_Main_Window.paint(frame_Main_Window.getGraphics());		 
								 }
								 catch(Exception e1){
									 
								 }
								 if(!Constants.is_gameover){
									 try{
										 //Thread.sleep(500);
											ai_Move(ai2);
											Constants.COUNT2++;
											player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
											frame_Main_Window.paint(frame_Main_Window.getGraphics());
											
										 }
									 catch(Exception e2){									 
								 }
							 }
						 }	
					}
				});
					frame_Main_Window.paint(frame_Main_Window.getGraphics());	
				}
		
	 }	
	
	// It centers the window on screen.
	public static void center_Window(Window frame, int width, int height) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (dimension.getWidth() - frame.getWidth() - width) / 2;
	    int y = (int) (dimension.getHeight() - frame.getHeight() - height) / 2;
	    frame.setLocation(x, y);
	}
	
	
	// It finds which player plays next and makes a move on the board.
	public static void make_Move(int col) {
		board.set_Overflow_Occured(false);
		
		int previousRow = board.getLastMove().getRow();
		int previousCol = board.getLastMove().getCol();
		int previousLetter = board.getLastSymbolPlayed();
		if(!board.is_Game_Over()){
		if (board.getLastSymbolPlayed() == Constants.Player2) {
			board.make_Move(col, Constants.Player1);
		} else  {
			board.make_Move(col, Constants.Player2);
		}
		
		if (board.has_Overflow_Occured()) {
			board.getLastMove().setRow(previousRow);
			board.getLastMove().setCol(previousCol);
			board.setLastSymbolPlayed(previousLetter);
		}
		}
	}
	
	
	// It places a checker on the board.
	public static void place_Checker(int color, int row, int col) {
		String colorString = GameParameters.getColorNameByNumber(color);
		int xOffset = 75 * col;
		int yOffset = 75 * row;
		ImageIcon checkerIcon = new ImageIcon(ResourceLoader.load("images/" + colorString + ".gif"));
		checker_Label = new JLabel(checkerIcon);
		checker_Label.setBounds(27 + xOffset, 27 + yOffset, checkerIcon.getIconWidth(),checkerIcon.getIconHeight());
		layered_Game_Board.add(checker_Label, 0, 0);
	
	}
	
	
	public static void save_Undo_Move() {
		
		human_player_undo_row = board.getLastMove().getRow();
		human_player_undo_col = board.getLastMove().getCol();
		human_player_undo_letter = board.getLastSymbolPlayed();
		human_Player_Undo_Checker_Label = checker_Label;
			undo_Array[i][0] = human_player_undo_row;
			undo_Array[i][1] = human_player_undo_col;
			undo_Array[i][2] = human_player_undo_letter;
			label.push(human_Player_Undo_Checker_Label);
			i++;
		
		
		
	}
	public static void save_Undo_Move_AI(){
		human_player_undo_row = board.getLastMove().getRow();
		human_player_undo_row = board.getLastMove().getRow();
		human_player_undo_col = board.getLastMove().getCol();
		human_player_undo_letter = board.getLastSymbolPlayed();
		human_Player_Undo_Checker_Label = checker_Label;
		
		undo_Array2[ii][0] = human_player_undo_row;
		undo_Array2[ii][1] = human_player_undo_col;
		undo_Array2[ii][2] = human_player_undo_letter;
		labelAI.push(human_Player_Undo_Checker_Label);
		ii++;
		
		
	}
	
	// Gets called after makeMove(int, col) is called.
	public static boolean game() {
		Constants.is_gameover = board.check_Game_Over();
		int row = board.getLastMove().getRow();
		int col = board.getLastMove().getCol();
		int currentPlayer = board.getLastSymbolPlayed();
		
		if (currentPlayer == Constants.Player1) {
			// It places a checker in the corresponding [row][col] of the GUI.
			place_Checker(GameParameters.player1Color, row, col);
		}
		
		else if (currentPlayer == Constants.Player2) {
			// It places a checker in the corresponding [row][col] of the GUI.
			place_Checker(GameParameters.player2Color, row, col);
		}
		
		if (Constants.is_gameover) {
			game_Over();
		}
		
		
		
		undo_Item.setEnabled(true);
		return Constants.is_gameover;
	}
	
	
	// Gets called after the human player makes a move. It makes a Minimax AI move.
	public static void ai_Move(MiniMaxAi ai){
		Move aiMove = ai.miniMax(board);
		board.make_Move(aiMove.getCol(), ai.getAiLetter());
		Constants.is_gameover=game();
		
	}
	public static void set_Color(){
		column1_button.setBackground(Color.white);
		column2_button.setBackground(Color.white);
		column3_button.setBackground(Color.white);
		column4_button.setBackground(Color.white);
		column5_button.setBackground(Color.white);
		column6_button.setBackground(Color.white);
		column7_button.setBackground(Color.white);
		column8_button.setBackground(Color.white);
		pause.setBackground(Color.white);
		start.setBackground(Color.white);
		column1_button.setForeground(Color.black);
		column2_button.setForeground(Color.black);
		column3_button.setForeground(Color.black);
		column4_button.setForeground(Color.black);
		column5_button.setForeground(Color.black);
		column6_button.setForeground(Color.black);
		column7_button.setForeground(Color.black);
		column8_button.setForeground(Color.black);
		pause.setForeground(Color.black);
		start.setForeground(Color.black);
	}
	
	public static void buttons_Invinsible(){
		column1_button.setVisible(false);
		column2_button.setVisible(false);
		column3_button.setVisible(false);
		column4_button.setVisible(false);
		column5_button.setVisible(false);
		column6_button.setVisible(false);
		column7_button.setVisible(false);
		column8_button.setVisible(false);
		pause.setVisible(false);
		start.setVisible(false);
		Full_Speed.setVisible(false);
	}
	
	public static void buttons_Visible(){
		column1_button.setVisible(true);
		column2_button.setVisible(true);
		column3_button.setVisible(true);
		column4_button.setVisible(true);
		column5_button.setVisible(true);
		column6_button.setVisible(true);
		column7_button.setVisible(true);
		column8_button.setVisible(true);
		pause.setVisible(true);
		start.setVisible(true);
		Full_Speed.setVisible(true);
	}
	public static void enable_Buttons() {
		column1_button.setEnabled(true);
		column2_button.setEnabled(true);
		column3_button.setEnabled(true);
		column4_button.setEnabled(true);
		column5_button.setEnabled(true);
		column6_button.setEnabled(true);
		column7_button.setEnabled(true);
		column8_button.setEnabled(true);
		pause.setEnabled(true);
		start.setEnabled(true);
		
	}
	
	
	public static void disable_Buttons() {
		column1_button.setEnabled(false);
		column2_button.setEnabled(false);
		column3_button.setEnabled(false);
		column4_button.setEnabled(false);
		column5_button.setEnabled(false);
		column6_button.setEnabled(false);
		column7_button.setEnabled(false);
		column8_button.setEnabled(false);
		start.setEnabled(false);
		pause.setEnabled(false);
		undo_Item.setEnabled(false);
	}
	
	
	/**
	 * Returns a component to be drawn by main window.
	 * This function creates the main window components.
	 * It calls the "actionListener" function, when a click on a button is made.
	 */
	public static Component create_Content_Components() {
		//create a panel to set up the turn buttons
		panel_Turns = new JPanel();
		panel_Turns.setLayout(new GridLayout(1,7,3,3));

		panel_Turns.setLayout(new FlowLayout());
		panel_Turns.add(player1_turn = new JTextField(20));
		player1_turn.setHorizontalAlignment(JTextField.LEFT);
		player1_turn.setEditable(false);
		player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
		
		panel_Turns.setLayout(new FlowLayout());
		panel_Turns.add(player2_turn = new JTextField(20));
		player2_turn.setHorizontalAlignment(JTextField.LEFT);
		player2_turn.setEditable(false);
		player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
		
		// Create a panel to set up the board buttons.
		panel_Board_Numbers = new JPanel();
		panel_Board_Numbers.setLayout(new GridLayout(1, 7, 6, 4));
		panel_Board_Numbers.setBorder(BorderFactory.createEmptyBorder(2,22, 2,22));
		
		panel_AIvsAI = new JPanel();
		panel_AIvsAI.setLayout(new GridLayout(3,1,10,10));
		panel_AIvsAI.setBorder(BorderFactory.createEmptyBorder(125, 20, 1, 20));
		
		if (GameParameters.gameMode != Constants.AiVsAi)
			enable_Buttons();
		
		
		if (firstGame) {
			Constants.COUNT1 = 0;
			Constants.COUNT2 = 0;
		
			column1_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					make_Move(0);
					
					if (!board.has_Overflow_Occured()) {
					
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
							
						}
						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();
							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						
					}
						
				}
					frame_Main_Window.requestFocusInWindow();
					
				}
			});
			
			column2_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(1);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}
						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {		
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}
			});
			
			column3_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(2);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}

						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}
			});
			
			column4_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(3);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}

						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}
			});
			
			column5_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(4);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}
						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}
			});
			
			column6_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(5);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}

						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}
			});
			
			column7_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(6);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}

						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}

			});
			column8_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					make_Move(7);
					if (!board.has_Overflow_Occured()) {
						if(GameParameters.gameMode == Constants.HumanVsHuman)
						{
							if(Constants.firstPlayer == true){
								Constants.COUNT1++;
								player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
								Constants.firstPlayer = false;
							}
							else if ( Constants.firstPlayer == false){
								Constants.COUNT2 ++;
								player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
								Constants.firstPlayer = true;
							}
						}

						Constants.is_gameover =	game();
						save_Undo_Move();
						
						if ((GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman )&& !Constants.is_gameover) {	
							Constants.COUNT1++;
							Constants.COUNT2 ++;
							ai_Move(ai);
							save_Undo_Move_AI();

							player1_turn.setText("Player 1 turn : " + Constants.COUNT1);
							player2_turn.setText("Player 2 turn : " + Constants.COUNT2);
						}
					}
					frame_Main_Window.requestFocusInWindow();
				}

			});
			pause.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					JOptionPane.showMessageDialog(null,
							"Game Paused the buttons will dissapear",
							"Pause", JOptionPane.PLAIN_MESSAGE);
					disable_Buttons();
					start.setEnabled(true);

					
				}});
				start.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						JOptionPane.showMessageDialog(null,
								"Game resumed the buttons will appear",
								"Resume",JOptionPane.PLAIN_MESSAGE);
						enable_Buttons();
					}
				});
				

			firstGame = false;
		}
		
	
		panel_Board_Numbers.add(column1_button);
		panel_Board_Numbers.add(column2_button);
		panel_Board_Numbers.add(column3_button);
		panel_Board_Numbers.add(column4_button);
		panel_Board_Numbers.add(column5_button);
		panel_Board_Numbers.add(column6_button);
		panel_Board_Numbers.add(column7_button);
		panel_Board_Numbers.add(column8_button);
		panel_Board_Numbers.add(pause);
		panel_Board_Numbers.add(start);
		panel_AIvsAI.add(Full_Speed);
		
		

		// main Connect-4 board creation
		layered_Game_Board = create_Layered_Board();

		// panel creation to store all the elements of the board
		panel_Main = new JPanel();
		panel_Main.setLayout(new BorderLayout());
		panel_Main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// add button and main board components to panelMain
		panel_Main.add(panel_Board_Numbers, BorderLayout.NORTH);
		panel_Main.add(layered_Game_Board, BorderLayout.CENTER);
		panel_Main.add(panel_Turns,BorderLayout.SOUTH);
		panel_Main.add(panel_AIvsAI,BorderLayout.EAST);
		panel_Main.setBackground(Color.gray);
		panel_AIvsAI.setBackground(Color.gray);
		panel_Board_Numbers.setBackground(Color.gray);
		frame_Main_Window.setResizable(false);
		return panel_Main;
	}
	
	
	// It gets called only of the game is over.
	// We can check if the game is over by calling the method "checkGameOver()"
	// of the class "Board".
	public static void game_Over() {
		board.set_Game_Over(true);
		int choice =0;
		if (board.get_Winner() == Constants.Player1) {
			if (GameParameters.gameMode == Constants.HumanVsAi)
				choice = JOptionPane.showConfirmDialog(null,
						"You win! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if(GameParameters.gameMode == Constants.AiVsHuman)
				choice = JOptionPane.showConfirmDialog(null,
						"Player 1 wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if (GameParameters.gameMode == Constants.HumanVsHuman)
				choice = JOptionPane.showConfirmDialog(null,
						"Player 1 wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if (GameParameters.gameMode == Constants.AiVsAi)
				choice = JOptionPane.showConfirmDialog(null,
						"Minimax AI 1 wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
		} else if (board.get_Winner() == Constants.Player2) {
			if (GameParameters.gameMode == Constants.HumanVsAi)
				choice = JOptionPane.showConfirmDialog(null,
						"Computer AI wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if (GameParameters.gameMode == Constants.HumanVsHuman)
				choice = JOptionPane.showConfirmDialog(null,
						"Player 2 wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if (GameParameters.gameMode == Constants.AiVsAi)
				choice = JOptionPane.showConfirmDialog(null,
						" AI 2 wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
			else if (GameParameters.gameMode == Constants.AiVsHuman)
				choice = JOptionPane.showConfirmDialog(null,
						" AI  wins! Start a new game?",
						"GAME OVER", JOptionPane.YES_NO_OPTION);
		} else {
			choice = JOptionPane.showConfirmDialog(null,
					"It's a draw! Start a new game?",
					"GAME OVER", JOptionPane.YES_NO_OPTION);
		}
		if(choice == JOptionPane.YES_OPTION)
			create_New_Game();
		
		else if (choice == JOptionPane.NO_OPTION)
		{
			Constants.COUNT1 =0;
			Constants.COUNT2 =0;
 			// Disable buttons
			
			disable_Buttons();
			buttons_Invinsible();
			JOptionPane.showMessageDialog(null,
					"Game is Over, you can either close the program or change the game mode by clikcing File/New Game",
					"Game is Over",JOptionPane.PLAIN_MESSAGE);
			// Remove key listener
			
			frame_Main_Window.removeKeyListener(frame_Main_Window.getKeyListeners()[0]);
			Full_Speed.removeActionListener(null);

		}
	}
	

	@SuppressWarnings("static-access")

	public static void main(String[] args){
		Gui connect5 = new Gui();
		connect5.create_New_Game();
	}
	

	
}
