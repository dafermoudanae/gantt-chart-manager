package load;
import  java.util.Scanner;

import util.FileTypes;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class FileLoaderText implements IFileLoader {
	
	public List<String> load(String sourcePath, FileTypes filetype){
		List<String> tasks = new ArrayList<String>();
	
		try {
			File file=new File(sourcePath);
			Scanner fileReader = new Scanner(file);
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				if(filetype==FileTypes.CSV) {
					line.replace(",", "\t");
				}
				tasks.add(line);
			}
			fileReader.close();
		}
		catch (FileNotFoundException e) {
			return null;
		}
		
		return tasks;
	}
}
