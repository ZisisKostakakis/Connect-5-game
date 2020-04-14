//package connect4;
//
//import static org.junit.Assert.assertTrue;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class BoardTest {
//
//	@BeforeEach
//	public void setUp() throws Exception {
//	}
//
////	@Test
////	public void testBoard() {
////		fail("Not yet implemented");
////	}
////
////
////	@Test
////	public void testGetLastMove() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testGetLastLetterPlayed() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testGetGameBoard() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testGetWinner() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testSetLastMove() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testSetLastLetterPlayed() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testSetGameBoard() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testSetWinner() {
////		fail("Not yet implemented");
////	}
//
//	@Test
//	public void testMakeMoveInt() {
//		Board b = new Board();
//		
//		b.makeMove(0, Constants.X);
//		
//		assertTrue("Board was not updated correctly.", b.getGameBoard()[5][0] == Constants.X);
//		assertTrue("Last player played is not correct.", b.getLastSymbolPlayed() == Constants.X);
//	}
//
////	@Test
////	public void testIsValidMoveIntInt() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testIsValidMoveInt() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testCanMove() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testCheckFullColumn() {
////		fail("Not yet implemented");
////	}
////
//	@Test
//	public void testGetRowPosition() {
//		Board b = new Board();
//		
//		int gameBoard[][] = new int[6][7];
//		for(int col = 0; col <7; col++) {
//			gameBoard[5][col] = Constants.X;
//		}
//		b.setGameBoard(gameBoard);
//		
//		for(int col =0; col < 7; col++) {
//			int row = b.getRowPosition(col);
//			assertTrue("row result is not the expected one.", row == 4);
//		}
//		
//	}
////
////	@Test
////	public void testGetChildren() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testEvaluate() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testCheckWinState() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testCheck3InARow() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testCheck2InARow() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testIsTerminal() {
////		fail("Not yet implemented");
////	}
////
////	@Test
////	public void testPrint() {
////		fail("Not yet implemented");
////	}
//
//}
