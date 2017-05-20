package es.ucm.fdi.tp.launcher;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Game launcher tests. Correct parameter syntax tests
 */
public class MainTest {

	/**
	 * Error when more than three arguments, less than three
	 * and incorrect game name argument
	 */
	@Test
	public void badArgsSyntax() {
		String[] tooManyArgs = { "was", "smart", "smart", "smart" };
		String[] fewArgs = { "was", "smart" };
		String[] badGameName = { "wwas" , "console", "smart" };
		
		MainPr4.main(tooManyArgs);
		fail("Correct syntax when too many arguments!");
		MainPr4.main(fewArgs);
		fail("Correct syntax when less than 3 arguments!");
		MainPr4.main(badGameName);
		fail("Runs when bad game name argument!");
	}
	
	/**
	 * Error when incorrect game name argument
	 */
	// Not tested because the method above calls System.exit(1) and the code below is never executed
	// @Test
	public void incorrectGameNameArg() {
		String[] badGameName = { "wwas" , "console", "smart" };
		
		MainPr4.main(badGameName);
		fail("Runs when bad game name argument!");
	}
}
