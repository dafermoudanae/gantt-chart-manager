package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T2_V1_01_NoTasksLoaded {

	@Test
	public void testGetAllTasks() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/invalid.xlsx", FileTypes.XLSX);
		
		assertTrue("To arxeio den yparxei, to arrayList prepei na einai adeio", mainController.getAllTasks().isEmpty());	
	}

}
