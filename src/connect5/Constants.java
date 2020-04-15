package connect5;

// Class for final public variables used in the game

public class Constants {
	//Pause
	public static final int Pause = 1;
	public static final int Running = 2;
	// Turns
	public static boolean first_time_undo = true;
	public static boolean firstPlayer = true;
	public static int COUNT1 = 0;
	public static int COUNT2= 0;
	// Board values
	public static final int Player1 = 1;  // Player 1
	public static final int Player2 = -1;  // Player 2
	public static final int EMPTY = 0;
	public static String line = null;
	public static  boolean is_gameover = false;	
	// GUI styles
	public static final int SystemStyle = 1;
	public static final int CrossPlatformStyle = 2;
	public static final int NimbusStyle = 3;
	
	// Colors
	public static final int RED = 1;
	public static final int YELLOW = 2;
	public static final int BLACK = 3;
	public static final int GREEN = 4;
	public static final int ORANGE = 5;
	public static final int PURPLE = 6;
	
	// Undo
	public static boolean undo1 = false;
	public static  boolean undo2 = false;
	
	// Game modes
	public static final int HumanVsAi = 1;
	public static final int HumanVsHuman = 2;
	public static final int AiVsAi = 3;
	public static final int AiVsHuman = 4;
	
	//Gui configurations
	public static final int DEFAULT_WIDTH = 650;
	public static final int DEFAULT_HEIGHT = 600;
	
}
