package load;

import java.util.List;

import util.FileTypes;

public interface IFileLoader {
	public List<String> load(String sourcePath, FileTypes filetype);
	
}
