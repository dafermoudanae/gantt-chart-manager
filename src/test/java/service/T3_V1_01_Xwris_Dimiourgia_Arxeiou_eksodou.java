package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T3_V1_01_Xwris_Dimiourgia_Arxeiou_eksodou {

	@Test
	public void testRawWriteToExcelFile() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		// assertTrue me to !
		assertTrue("Se epityxia theloume true", !mainController.rawWriteToExcelFile(mainController.getAllTasks()));
	}

}
