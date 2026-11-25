package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T2_V0_03_HappyDay {


	@Test
	public void testGetTasksInRange() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		
		assertEquals("To arxeio exei 4 tasks entos eurous, tosa prepei na exei to arraylist", 4, mainController.getTasksInRange(102,300).size());	
	}

}
