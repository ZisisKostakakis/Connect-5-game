package connect5;


public class GameParameters {
	
	/* Default values */
	
	public static int gameMode = Constants.AiVsAi; // value is 1
	public static int maxDepth1 = 1;
	public static int maxDepth2 = 4;
	public static int player1Color = Constants.RED;
	public static int player2Color = Constants.YELLOW;
	
	
	public static final String getColorNameByNumber(int number) {
		switch (number) {
			case 1:
				return "RED";
			case 2:
				return "YELLOW";
			case 3:
				return "BLACK";
			case 4:
				return "GREEN";
			case 5:
				return "ORANGE";
			case 6:
				return "PURPLE";
			default:
				return "RED";
		}
	}
	
	
}
