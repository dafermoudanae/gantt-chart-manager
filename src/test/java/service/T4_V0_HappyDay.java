package service;

import static org.junit.Assert.*;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;

import util.FileTypes;

public class T4_V0_HappyDay {

	@Test
	public void testAddFontedStyle() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		//thelei k i prepareTargetWorkbook klisi giati i addFonted xreiazetai to workbook pou tha etoimastei
		mainController.prepareTargetWorkbook(FileTypes.XLS, "src/test/resources/output/Shop_test.xlsx");

		
		String testValue = mainController.addFontedStyle("test", 
				IndexedColors.TEAL.getIndex(), (short)10, "Times New Roman", 
				false, false, false, 
				IndexedColors.WHITE.getIndex(), "Solid", "Left", false);	
			
		
		assertEquals("To testValue prepei na exei timi \"test\"", "test", testValue);	
	}

}
