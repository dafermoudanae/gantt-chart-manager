package service;

import static org.junit.Assert.*;

import org.junit.Test;

import util.FileTypes;

public class T3_V1_02_Xwris_Dimiourgia_Arxeiou_eksodou {


	@Test
	public void testCreateNewSheet() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		// assertTrue me to !
		assertTrue("Se epityxia theloume true", !mainController.createNewSheet("test", mainController.getAllTasks(), "testStyle", "testStyle","testStyle","testStyle","testStyle","testStyle"));

	}

}
