package untitled_thinggy_thingg.util.files;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class ResourcePath implements FilePath, Serializable {
	private static final long serialVersionUID = 1L;

	private String path;
	
	public ResourcePath(String pathName) {
		this.path = pathName;
	}

	@Override
	public FilePath inDirectory(String directoryName) {
		if (!(directoryName.endsWith("/") || path.startsWith("/"))) {directoryName = directoryName + "/";}
		return new ResourcePath(directoryName + path);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		if (!path.startsWith("/")) {path = "/" + path;}
		return getClass().getResourceAsStream(path);
	}

	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return getPath();
	}
}
