package untitled_thinggy_thingg.util.files;

import java.io.FileNotFoundException;
import java.io.InputStream;

import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;

/**
 * An interface for storing the locations of files. It is immutable.
 * It is designed to be used by creating one with only the file name,
 * then building up the file heirarchy depending on what context you are using it in.
 * <br><br>
 * For example, in a {@link TextureSprite}, you merely pass in a file name,
 * and it is taken to refer to a file within the folder
 * "src/main/resources/assets/textures/".
 */
public interface FilePath {
	/**
	 * Returns a new {@code FilePath} where it is prepended to be
	 * within the target directory.
	 * 
	 * @param directoryName The target directory
	 * @return A new {@code FilePath} within the target directory
	 */
	public FilePath inDirectory(String directoryName);
	
	/**
	 * Generates an {@link InputStream} pointing to the file currently
	 * referenced by the {@code FilePath}.
	 * 
	 * @return An {@code InputStream} of the current file
	 * @throws FileNotFoundException The file isn't found at the current path
	 */
	public InputStream getInputStream() throws FileNotFoundException;
	
	/**
	 * @return The current path
	 */
	public String getPath();
}
