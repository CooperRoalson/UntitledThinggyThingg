package untitled_thinggy_thingg.core;

import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.entities.Entity;
import untitled_thinggy_thingg.world.tiles.Tile;

/**
 * Stores constants. Cannot be instantiated.
 */
public class Constants {
	
	// Can't instantiate
	private Constants() {}
	
	/**
	 * Stores constants related to the game window.
	 */
	public class Window {
		/** The window width */
		public static final int BASE_WIDTH = 512;
		/** The window height */
	    public static final int BASE_HEIGHT = 288;
	    /** The window base scale */
	    public static final double BASE_SCALE = 1.0;
	    
	    /** The target update speed */
	    public static final int TARGET_FPS = 60;
	    /** The number of milliseconds between frames */
	    public static final double MILLIS_BETWEEN_FRAMES = 1000.0/TARGET_FPS;
	}
	
	/**
	 * Stores constants related to in-game things.
	 */
	public class Game {
		/** The in-game size of {@link Tile}s and {@link Block}s */
		public static final int TILE_SIZE = 32;
		/** The in-game size of {@link Entity}s */
		public static final int ENTITY_SIZE = 32;
	}
	
	/**
	 * Stores constants related to textures.
	 */
	public class Textures {
		/** The texture size of {@link Tile}s and {@link Block}s */
		public static final int TILE_TEXTURE_SIZE = 16;
		/** The texture size of {@link Entity}s */
		public static final int ENTITY_TEXTURE_SIZE = 32;
		
		public static final int ITEM_TEXTURE_SIZE = 16;
		
		/** The scale for {@code Tile} and {@code Block} textures*/
		public static final double TILE_TEXTURE_SCALE = (double) Game.TILE_SIZE/Textures.TILE_TEXTURE_SIZE;
		/** The scale for {@code Entity} textures*/
		public static final double ENTITY_TEXTURE_SCALE = (double) Game.ENTITY_SIZE/Textures.ENTITY_TEXTURE_SIZE;
		
		public static final double ITEM_TEXTURE_SCALE = (double) GUI.ITEM_ICON_SIZE/Textures.ITEM_TEXTURE_SIZE;
	}
	
	public class GUI {
		public static final int ITEM_ICON_SIZE = 20;
	}
}