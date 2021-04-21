package untitled_thinggy_thingg.world.blocks;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.drawables.PolygonDrawable;
import untitled_thinggy_thingg.core.drawing.sprites.AnimationSprite;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.world.collision.Collider;
import untitled_thinggy_thingg.world.collision.ConvexCollider;

public class Block extends AnimationSprite {
	private static final long serialVersionUID = 3L;	
	
	private int id;
	private String name;
	
	private boolean collidesWithScreen = false;
	
	private Collider collider; // Can be null
	
	public Block(int id, String name, List<FilePath> textures, String directoryName, int fps, Collider collider) {
		super(texturesInDirectory(textures, "blocks/" + directoryName), fps);
		this.id = id;
		this.name = name;
		this.setScale(Constants.Textures.TILE_TEXTURE_SCALE);
		this.collider = collider;
	}
	
	// Constructor with a single basic texture
	public Block(int id, String name, FilePath texture, String directoryName, Collider collider) {
		this(id, name, Arrays.asList(texture), directoryName, 0, collider);
	}
	
	// Default constructor
	public Block() {
		this(0, "", Arrays.asList(new ResourcePath("")), "", 0, null);
	}
	
	public static List<FilePath> texturesInDirectory(List<FilePath> textures, String directory) {
		List<FilePath> texturesInDirectory = new ArrayList<>();
		
			for (FilePath path : textures) {
				texturesInDirectory.add(path.inDirectory(directory));
			}
		
		return texturesInDirectory;
	}
	
	public Block setCollidesWithScreen(boolean collidesWithScreen) {
		this.collidesWithScreen = collidesWithScreen;
		return this;
	}
	
	public boolean collidesWithScreen() {
		return collidesWithScreen;
	}
	
	public Collider getCollider() {
		return collider;
	}
	
	public boolean hasCollision() {
		return collider != null;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
	// -------TESTING-------
	@Override
	public List<Drawable> draw(int x, int y) {
		List<Drawable> drawables = super.draw(x, y);
		
		if (this.collider instanceof ConvexCollider) {
			ConvexCollider collider = (ConvexCollider) this.collider;
			
			drawables.add(new PolygonDrawable(new Polygon(
				Arrays.stream(collider.getPoints()).mapToInt(p -> x + (int) p.getX()).toArray(),
				Arrays.stream(collider.getPoints()).mapToInt(p -> y + (int) p.getY()).toArray(),
			collider.numOfPoints()), Color.RED));
			
		}
		return drawables;
	}
	
}
