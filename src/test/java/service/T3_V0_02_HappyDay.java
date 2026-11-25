package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T3_V0_02_HappyDay {

	@Test
	public void testCreateNewSheet() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		mainController.prepareTargetWorkbook(FileTypes.XLS, "src/test/resources/output/Shop_test.xlsx");
		assertTrue("Se epityxia theloume true", mainController.createNewSheet("test", mainController.getAllTasks(), "testStyle", "testStyle","testStyle","testStyle","testStyle","testStyle"));

	}

}
