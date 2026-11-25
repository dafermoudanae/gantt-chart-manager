package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T2_V0_02_HappyDay {

	@Test
	public void testGetTopLevelTasksOnly() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		
		assertEquals("To arxeio exei 3 top tasks, tosa prepei na exei to arraylist", 3, mainController.getTopLevelTasksOnly().size());	
	}

}
