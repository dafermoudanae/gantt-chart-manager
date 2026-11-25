package load;

import util.FileTypes;

public class FileLoaderFactory {
	public IFileLoader createLoader( FileTypes filetype) {
		IFileLoader fileloader=null;
		if(filetype== FileTypes.XLS|| filetype == FileTypes.XLSX){
		fileloader = new FileLoaderExcel();
		}
		else {
			fileloader=new FileLoaderText();
		}
		return fileloader;
	}
}
