package untitled_thinggy_thingg.world.tiles;

import java.io.Serializable;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class Tile extends TextureSprite implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	
	// Texture path relative to resources/assets/textures/tiles
	public Tile(int id, String name, FilePath texture, String directoryName) {
		super(texture.inDirectory("tiles/" + directoryName));
		this.id = id;
		this.name = name;
		this.setScale(Constants.Textures.TILE_TEXTURE_SCALE);
	}
	
	// Default constructor
	public Tile() {
		super(new ResourcePath(""));
		this.id = -1;
		this.name = "";
	}
	
	public Tile(int id, String name, String texture, String directoryName) {
		this(id, name, new ResourcePath(texture), directoryName);
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
}
