package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import util.FileTypes;

public class T1_V1_RainyDay_Invalid_File {

	@Test
	public void testLoadInvalidFile() {
		MainController mainController = new MainController();
		List <String> tasks = mainController.load("src/test/resources/input/arxeioGiaTestLathos.tsv", FileTypes.TSV);
		assertTrue("To arxeio exei skoupidia, theloume adeio to arraylist", tasks.isEmpty());
	}

}