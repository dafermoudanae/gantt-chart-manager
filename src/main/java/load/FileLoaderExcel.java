package load;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import util.FileTypes;

public class FileLoaderExcel implements IFileLoader {
	
	public List<String> load(String sourcePath, FileTypes filetype){
		List<String> tasks= new ArrayList<String>();
		try(FileInputStream file=new FileInputStream(sourcePath)){
			Workbook inputWorkbook = null;
			if(filetype==FileTypes.XLS) {
				inputWorkbook = new HSSFWorkbook(file);
			}
			else {
				inputWorkbook = new XSSFWorkbook(file);
			}
			Sheet sheet = inputWorkbook.getSheetAt(0);
			for (Row row : sheet) {
				String line="";
				for (Cell cell : row) {
					switch (cell.getCellType()) {
						case STRING:
							line += (cell.getStringCellValue() + "\t");
							break;
						case NUMERIC:
							line += (cell.getNumericCellValue() + "\t");
					
					}
				}
				tasks.add(line);
			}
		}
		catch (IOException e) {
			return null;
		}
		return tasks;
		
	 }
		
	   
		
}
	



