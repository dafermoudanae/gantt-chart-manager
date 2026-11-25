package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T2_V1_03_NoTasksLoaded {

	@Test
	public void testGetTasksInRange() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/invalid.xlsx", FileTypes.XLSX);
		
		assertTrue("To arxeio den yparxei, to arrayList prepei na einai adeio", mainController.getTasksInRange(102,300).isEmpty());	
	}

}
