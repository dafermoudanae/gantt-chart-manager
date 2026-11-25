package service;

import static org.junit.Assert.*;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;

import util.FileTypes;

public class T4_V1_Invalid_Style {

	@Test // xwris klisi tis prepareTargetWorkbook
	public void testAddFontedStyleWithoutWorkbook() {
		MainController mainController = new MainController();
		mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		
		String testValue = mainController.addFontedStyle("test", 
				IndexedColors.TEAL.getIndex(), (short)10, "Times New Roman", 
				false, false, false, 
				IndexedColors.WHITE.getIndex(), "Solid", "Left", false);	
			
		
		assertEquals("To testValue prepei na exei timi \"Normal\"", "Normal", testValue);		
	}

}



