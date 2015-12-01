package com.swampville.main;

import static org.junit.Assert.*;
import java.awt.Component;
import javax.swing.JFrame;
import org.junit.Test;

/**
 * @author Mark Betters
Jake Fiumara
Will Taylor
Foster Clark
Briana Lamet
 *
 */
public class InstructionScreenTests {
	
	JFrame testFrame = new JFrame();
	InstructionsScreen instrScreenTest = new InstructionsScreen(testFrame);
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void displayInstructionsTest() {
		assertEquals(true, instrScreenTest.content.getText().equals("Instructions Sample Text (actual instructions not yet written)"));
	}

	@Test
	public void transitionToStartScreenTest() {
		Component origComps[] = instrScreenTest.frame.getComponents();
		instrScreenTest.transitionToStartScreen();
		Component newComps[] = instrScreenTest.frame.getComponents();
		assertEquals(false, newComps.equals(origComps));
	}

}
