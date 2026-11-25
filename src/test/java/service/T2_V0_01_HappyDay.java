package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import util.FileTypes;

public class T2_V0_01_HappyDay {

	@Test
	public void testGetAllTasks() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		
		assertEquals("To arxeio exei 8 tasks, tosa prepei na exei to arraylist", 8, mainController.getAllTasks().size());	
	}

}
