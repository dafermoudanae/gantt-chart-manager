package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T3_V0_01_HappyDay {

	@Test
	public void testRawWriteToExcelFile() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		mainController.prepareTargetWorkbook(FileTypes.XLS, "src/test/resources/output/Shop_test.xlsx");
		assertTrue("Se epityxia theloume true", mainController.rawWriteToExcelFile(mainController.getAllTasks()));
	}

}
