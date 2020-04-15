package connect5;


import java.util.ArrayList;

// Class responsible for calculating the best move evaluating the board and deciding who is playing
public class Board {
	

    private Move last_Move_Played;
    
    // A variable to store the symbol of the player who played last,leading to the current board state.
   
    private int last_Symbol_Played;
    private int winner;
	private int [][] game_Board;	
	private boolean overflow_occured;		
	private boolean game_over;
	
	// constructor
	public Board() {
		this.last_Move_Played = new Move();
		this.last_Symbol_Played = Constants.Player2;
		
		
		this.winner = 0;
		this.game_Board = new int[7][8];
		this.overflow_occured = false;
		this.game_over = false;
		for(int i=0; i<7; i++) {
			for(int j=0; j<8; j++) {
				game_Board[i][j] = Constants.EMPTY;
			}
		}
	}
	
	// copy constructor
	public Board(Board board) {
		last_Move_Played = board.last_Move_Played;
		last_Symbol_Played = board.last_Symbol_Played;
		winner = board.winner;
		game_Board = new int[7][8];
		this.overflow_occured = false;
		this.game_over = false;
		for(int i=0; i<7; i++) {
			for(int j=0; j<8; j++) {
				game_Board[i][j] = board.game_Board[i][j];
			}
		}
	}
	
	public Move getLastMove() {
		return last_Move_Played;
	}
	
	public void setLastMove(Move lastMove) {
		this.last_Move_Played.setRow(lastMove.getRow());
		this.last_Move_Played.setCol(lastMove.getCol());
		this.last_Move_Played.setValue(lastMove.getValue());
	}
	
	public int getLastSymbolPlayed() {
		return last_Symbol_Played;
	}
	
	public void setLastSymbolPlayed(int lastLetterPlayed) {
		this.last_Symbol_Played = lastLetterPlayed;
	}
		
	public int[][] getGameBoard() {
		return game_Board;
	}
		
	public void setGameBoard(int[][] gameBoard) {
		for(int i=0; i<7; i++) {
			for(int j=0; j<8; j++) {
				this.game_Board[i][j] = gameBoard[i][j];
			}
		}
	}
		
	public int get_Winner() {
		return winner;
	}
		
	public void set_Winner(int winner) {
		this.winner = winner;
	}
		
	public boolean is_Game_Over() {
		return game_over;
	}

	public void set_Game_Over(boolean isGameOver) {
		this.game_over = isGameOver;
	}
		
	public boolean has_Overflow_Occured() {
		return overflow_occured;
	}

	public void set_Overflow_Occured(boolean overflowOccured) {
		this.overflow_occured = overflowOccured;
	}
	
	// Makes a move based on the given column.
	// It finds automatically in which row the checker should be inserted.
	public void make_Move(int col, int letter) {
		try {
			// The variable "lastMove" must be changed before the variable
			// "gameBoard[][]" because of the function "getRowPosition(col)".
			this.last_Move_Played = new Move(get_the_Row_Position(col), col);
			this.last_Symbol_Played = letter;
			this.game_Board[get_the_Row_Position(col)][col] = letter;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Column " + (col+1) + " is full!");
			set_Overflow_Occured(true);
		}
	}
	
	
	// Makes the specified cell in the border empty.
	public void undo_Move_Option(int row, int col, int letter) {
		this.game_Board[row][col] = 0;
		if(GameParameters.gameMode == Constants.HumanVsHuman){
			if (letter == Constants.Player2 ) {
				this.last_Symbol_Played = Constants.Player1;
			}	
			else if (letter == Constants.Player1) {
				this.last_Symbol_Played = Constants.Player2;
			}
		}
		
		 if (GameParameters.gameMode == Constants.HumanVsAi || GameParameters.gameMode == Constants.AiVsHuman){
				if (letter == Constants.Player2 ) {
					this.last_Symbol_Played = Constants.Player2;
				}
				else if (letter == Constants.Player1) {
					this.last_Symbol_Played = Constants.Player1;
				}
		}		
	}
	
	
	// This function is used when we want to search the whole board,
	// without getting out of borders.
	public boolean can_make_a_Move(int row, int col) {
		if ((row <= -1) || (col <= -1) || (row > 6) || (col > 7)) {
			return false;
		}
		return true;
	}
	
	
	public boolean check_Full_Column(int col) {
		if (game_Board[0][col] == Constants.EMPTY)
			return false;
		return true;
	}
	
	
	// It returns the position of the last empty row in a column.
	public int get_the_Row_Position(int col) {
		int row_Position = -1;
		for (int row=0; row<7; row++) {
			if (game_Board[row][col] == Constants.EMPTY) {
				row_Position = row;
			}
		}
		return row_Position;
	}
	
	
	/* Generates the children of the state.
     * The max number of the children is 8,
     * because we have 8 columns
     */
	public ArrayList<Board> get_Children(int letter) {
		ArrayList<Board> children = new ArrayList<Board>();
		for(int col=0; col<8; col++) {
			if(!check_Full_Column(col)) {
				Board child = new Board(this);
				child.make_Move(col, letter);
				children.add(child);
			}
		}
		return children;
	}
	
	
	public int evaluation() {
		// +1000 'Constants.X' wins, -1000 'Constants.O' wins,
		// +100 for each four 'Constants.X' in a row, -100 for each three 'Constants.O' in a row,
		// +10 for each three 'Constants.X' in a row, -10 for each three 'Constants.O' in a row,
		// +1 for each two 'Constants.X' in a row, -1 for each two 'Constants.O' in a row
		int linesX = 0;
		int linesO = 0;

        if (check_The_Win_State()) {
			if(get_Winner() == Constants.Player1) {
				linesX = linesX + 1000;
			} else if (get_Winner() == Constants.Player2) {
				linesO = linesO + 1000;
			}
		}
		
        linesX  = linesX + count_3checkers_In_A_Row(Constants.Player1) * 10 + count_2checkers_In_A_Row(Constants.Player1) + count_4checkers_In_A_Row(Constants.Player1)*100;
        linesO  = linesO + count_3checkers_In_A_Row(Constants.Player2) * 10 + count_2checkers_In_A_Row(Constants.Player2) + count_4checkers_In_A_Row(Constants.Player2)*100;
		
        // if the result is 0, then it'a a draw 
		return linesX - linesO;
	}
	
	
	/*
	 * Terminal win check.
	 * It checks whether somebody has won the game.
	 */
	public boolean check_The_Win_State() {
		

		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i, j+4)) {
					if (game_Board[i][j] == game_Board[i][j+1]
							&& game_Board[i][j] == game_Board[i][j+2]
							&& game_Board[i][j] == game_Board[i][j+3]
							&& game_Board[i][j] == game_Board[i][j+4]
							&& game_Board[i][j] != Constants.EMPTY) {
						set_Winner(game_Board[i][j]);
						return true;
					}
				}
			}
		}

		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-4, j)) {
					if (game_Board[i][j] == game_Board[i-1][j]
							&& game_Board[i][j] == game_Board[i-2][j]
							&& game_Board[i][j] == game_Board[i-3][j]
							&& game_Board[i][j] == game_Board[i-4][j]
							&& game_Board[i][j] != Constants.EMPTY) {
						set_Winner(game_Board[i][j]);
						return true;
					}
				}
			}
		}
		
	
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i+4, j+4)) {
					if (game_Board[i][j] == game_Board[i+1][j+1]
							&& game_Board[i][j] == game_Board[i+2][j+2]
							&& game_Board[i][j] == game_Board[i+3][j+3] 
							&& game_Board[i][j] == game_Board[i+4][j+4]
							&& game_Board[i][j] != Constants.EMPTY) {
						set_Winner(game_Board[i][j]);
						return true;
					}
				}
			}
		}
		
		
		// Check for 4 consecutive checkers in a row, in ascending diagonals.
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-4, j+4)) {
					if (game_Board[i][j] == game_Board[i-1][j+1]
							&& game_Board[i][j] == game_Board[i-2][j+2]
							&& game_Board[i][j] == game_Board[i-3][j+3] 
							&& game_Board[i][j] == game_Board[i-4][j+4]
							&& game_Board[i][j] != Constants.EMPTY) {
						set_Winner(game_Board[i][j]);
						return true;
					}
				}
			}
		}
		
		set_Winner(Constants.EMPTY);  // set nobody as the winner
		return false;
	}
	
	
    public boolean check_Game_Over() {
    	// Check if there is a winner.
    	if (check_The_Win_State()) {
    		return true;
    	}
    	
    	// Check for an empty cell, i.e. check to find if it is a draw.
    	// The game is in draw state, if all cells are full
    	// and nobody has won the game.
    	for(int row=0; row<7; row++) {
			for(int col=0; col<8; col++) {
				if(game_Board[row][col] == Constants.EMPTY) {
                    return false;
                }
            }
        }
    	
    	return true;
    }
	
    // It returns the frequency of 4 checkers in a row,
    // for the given player.
    public int count_4checkers_In_A_Row(int playerSymbol) {
		int times = 0;
		
			for (int i=0; i<7; i++) {
				for (int j=0; j<8; j++) {
					if (can_make_a_Move(i, j+3)) {
						if (game_Board[i][j] == game_Board[i][j+1]
							&& game_Board[i][j] == game_Board[i][j+2]
							&& game_Board[i][j] == game_Board[i][j+3]
							&& game_Board[i][j] != Constants.EMPTY) {
							times++;
					}
				}
			}
		}
		
	
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-3, j)) {
					if (game_Board[i][j] == game_Board[i-1][j]
							&& game_Board[i][j] == game_Board[i-2][j]
							&& game_Board[i][j] == game_Board[i-3][j]
							&& game_Board[i][j] != Constants.EMPTY) {
							times++;
						
					}
				}
			}
		}
	
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i+3, j+3)) {
					if (game_Board[i][j] == game_Board[i+1][j+1]
							&& game_Board[i][j] == game_Board[i+2][j+2]
							&& game_Board[i][j] == game_Board[i+3][j+3] 
							&& game_Board[i][j] != Constants.EMPTY) {
							times++;
					}
				}
			}
		}
		
		
		// Check for 4 consecutive checkers in a row, in ascending diagonals.
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-3, j+3)) {
					if (game_Board[i][j] == game_Board[i-1][j+1]
							&& game_Board[i][j] == game_Board[i-2][j+2]
							&& game_Board[i][j] == game_Board[i-3][j+3] 
							&& game_Board[i][j] != Constants.EMPTY) {
							times++;
					}
				}
			}
		}
		return times;
	}
    // It returns the frequency of 3 checkers in a row,
    // for the given player.
	public int count_3checkers_In_A_Row(int playerSymbol) {
		
		int times = 0;
		
		
		// Check for 3 consecutive checkers in a row, horizontally.

		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i, j+2)) {
					if (game_Board[i][j] == game_Board[i][j+1]
							&& game_Board[i][j] == game_Board[i][j+2]
							&& game_Board[i][j] == playerSymbol) {
						times++;
					}
				}
			}
		}
		

		// Check for 3 consecutive checkers in a row, vertically.

		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-2, j)) {
					if (game_Board[i][j] == game_Board[i-1][j]
							&& game_Board[i][j] == game_Board[i-2][j]
							&& game_Board[i][j] == playerSymbol) {
						times++;
					}
				}
			}
		}

		
		// Check for 3 consecutive checkers in a row, in descending diagonal.
		

		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i+2, j+2)) {
					if (game_Board[i][j] == game_Board[i+1][j+1]
							&& game_Board[i][j] == game_Board[i+2][j+2]
							&& game_Board[i][j] == playerSymbol) {
						times++;
					}
				}
			}
		}

		
		// Check for 3 consecutive checkers in a row, in ascending diagonal.
		
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-2, j+2)) {
					if (game_Board[i][j] == game_Board[i-1][j+1]
							&& game_Board[i][j] == game_Board[i-2][j+2]
							&& game_Board[i][j] == playerSymbol) {
						times++;
					}
				}
			}
		}

		return times;
				
	}
	
	
    // It returns the frequency of 2 checkers in a row,
    // for the given player.
	public int count_2checkers_In_A_Row(int player) {
		
		int times = 0;
	
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i, j+1)) {
					if (game_Board[i][j] == game_Board[i][j+1]
							&& game_Board[i][j] == player) {
						times++;
					}
				}
			}
		}
		
	
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-1, j)) {
					if (game_Board[i][j] == game_Board[i-1][j]
							&& game_Board[i][j] == player) {
						times++;
					}
				}
			}
		}

		
		// Check for 3 consecutive checkers in a row, in descending diagonal.
		
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i+1, j+1)) {
					if (game_Board[i][j] == game_Board[i+1][j+1]
							&& game_Board[i][j] == player) {
						times++;
					}
				}
			}
		}

		
		// Check for 3 consecutive checkers in a row, in ascending diagonal.
		
		for (int i=0; i<7; i++) {
			for (int j=0; j<8; j++) {
				if (can_make_a_Move(i-1, j + 1)) {
					if (game_Board[i][j] == game_Board[i-1][j+1]
							&& game_Board[i][j] == player) {
						times++;
					}
				}
			}
		}

		return times;		
	}
}
