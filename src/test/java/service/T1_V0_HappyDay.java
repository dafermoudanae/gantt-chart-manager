package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import util.FileTypes;

public class T1_V0_HappyDay {

	@Test
	public void testLoadWithExcel() {
		MainController mainController = new MainController();
		List <String> tasks = mainController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		assertEquals("To arxeio exei 8 tasks, theloume 8 stoixeia sto arraylist", 8, tasks.size());
	}
	
	//dokimi kai me arxeio keimenou
	
	@Test
	public void testLoadWithTSV() {
		MainController mainController = new MainController();
		List <String> tasks = mainController.load("src/test/resources/input/Shop.tsv", FileTypes.TSV);
		assertEquals("To arxeio exei 8 tasks, theloume 8 stoixeia sto arraylist", 8, tasks.size());
	}

}
