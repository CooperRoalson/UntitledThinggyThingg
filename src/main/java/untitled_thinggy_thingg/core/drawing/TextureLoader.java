package untitled_thinggy_thingg.core.drawing;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import untitled_thinggy_thingg.util.files.FilePath;

public class TextureLoader {
	private static final Map<FilePath, Image> TEXTURES = new HashMap<>();
	
	private TextureLoader() {}
	
	public static Image get(FilePath path) {
		if (!(TEXTURES.containsKey(path))) {
			loadTexture(path);
		}
		
		//System.out.println("path: " + path);
		//System.out.println("image: " + TEXTURES.get(path));
		
		return TEXTURES.get(path);
	}
	
	public static void loadTexture(FilePath path) {
    	FilePath assetPath = path.inDirectory("assets/textures/");
    	try {
			TEXTURES.put(path, ImageIO.read(assetPath.getInputStream()));
		} catch (Exception e) {
			System.out.println("Error loading texture " + path + ": " + e.getClass().getName());
		}
    }
}
