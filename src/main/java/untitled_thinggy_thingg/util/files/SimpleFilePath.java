package untitled_thinggy_thingg.util.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class SimpleFilePath implements FilePath,Serializable {
	private static final long serialVersionUID = 1L;
	
	String path;
	String baseDirectory;

	public SimpleFilePath(String baseDirectory, String path) {
		this.baseDirectory = baseDirectory;
		this.path = path;
	}
	
	@Override
	public FilePath inDirectory(String directoryName) {
		if (!(directoryName.endsWith("/") || path.startsWith("/"))) {directoryName = directoryName + "/";}
		return new SimpleFilePath(baseDirectory, directoryName + path);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(getPath());
	}
	
	public File getFile() {
		return new File(getPath());
	}

	@Override
	public String getPath() {
		return baseDirectory + path;
	}
	
	protected void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	@Override
	public String toString() {
		return this.getPath();
	}
}
