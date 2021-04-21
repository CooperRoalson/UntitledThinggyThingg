package untitled_thinggy_thingg.world.entities;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.drawables.PolygonDrawable;
import untitled_thinggy_thingg.core.drawing.sprites.AnimationGroupSprite;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.world.MapState;
import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.collision.Collider;
import untitled_thinggy_thingg.world.collision.ConvexCollider;

public class Entity extends AnimationGroupSprite {
	private static final long serialVersionUID = 3L;
	
	private int x, y; //Correspond to the top left corner of the Entity
	private String name;
	private boolean interactable;
	
	private Collider collider; // Can be null
	
	private boolean movingUp;
	private boolean movingDown;
	private boolean movingLeft;
	private boolean movingRight;
	
	private int speed;
	
	public Entity(List<List<FilePath>> textures, String directoryName, int fps, int x, int y, int speed, boolean interactable, Collider collider) {
		super(texturesInDirectory(textures, "entities/" + directoryName), fps);
		this.setScale(Constants.Textures.ENTITY_TEXTURE_SCALE);
		this.x = x;
		this.y = y;
		this.interactable = interactable;
		this.speed = speed;
		this.collider = collider;
	}
	
	public static List<List<FilePath>> texturesInDirectory(List<List<FilePath>> textures, String directory) {
		List<List<FilePath>> texturesInDirectory = new ArrayList<>();
		
		for (List<FilePath> paths : textures) {
			
			List<FilePath> pathsInDirectory = new ArrayList<>();
			for (FilePath path : paths) {
				pathsInDirectory.add(path.inDirectory(directory));
			}
			
			texturesInDirectory.add(pathsInDirectory);
		}
		
		return texturesInDirectory;
	}

	public void interact() {}
	
	@Override
	public void update() {
		super.update();
		updateEntity();
	}

	public Collider getCollider() {
		return collider;
	}
	
	public boolean hasCollision() {
		return collider != null;
	}
	
	public int getColliderMaxX() {
		if (!hasCollision()) {return this.x;}
		Rectangle r = this.collider.getBoundingBox();
		return r.x + r.width + this.x;
	}
	
	public int getColliderMaxY() {
		if (!hasCollision()) {return this.y;}
		Rectangle r = this.collider.getBoundingBox();
		return r.y + r.height + this.y;
	}
	
	public void move(short horizontal, short vertical) {
		for (int verticalAmount = speed; verticalAmount > 0; verticalAmount --) {
			if (canMoveTo(getX(), getY()+verticalAmount*vertical)) {
				incrY(verticalAmount*vertical);
				break;
			}
		}
		for (int horizontalAmount = speed; horizontalAmount > 0; horizontalAmount --) {
			if (canMoveTo(getX()+horizontalAmount*horizontal, getY())) {
				incrX(horizontalAmount*horizontal);
				break;
			}
		}
	}
	
	public void incrX(int x) {
		setX(getX() + x);
	}
	
	public void incrY(int y) {
		setY(getY() + y);
	}
	
	public boolean canMoveTo(int x, int y) {
		if (!hasCollision()) {return true;}
		return !checkBlockCollision(x, y);
	}
			
	public boolean checkBlockCollision(int x, int y) {
		if (!hasCollision()) {return false;}
		
		MapState mapState = GameManager.getInstance().getMapState();
		Block[][] blockMap = mapState.getBlockMap();
		
		if (!checkCollisionMap(x, y)) {
			return false;
		}
		
		int worldX, worldY;
		Collider other;
		boolean collides;
		
		for (int blockY = 0; blockY < blockMap.length; blockY++) {
			worldY = blockY * Constants.Game.TILE_SIZE;
			for (int blockX = 0; blockX < blockMap[blockY].length; blockX++) {
				worldX = blockX * Constants.Game.TILE_SIZE;
				
				other = mapState.getBlock(blockX, blockY).getCollider();
				
				if (other == null) {continue;}

				collides = this.collider.getBoundingBox(x, y).intersects(other.getBoundingBox(worldX, worldY))
						&& this.collider.collidesWith(other, x, y, worldX, worldY);
				
				if (collides) {return true;}
			}
		}
		
		return false;
	}
	
	public boolean checkCollisionMap(int x, int y) {
		MapState mapState = GameManager.getInstance().getMapState();
		
		int tileSize = Constants.Game.TILE_SIZE;
		Rectangle r = collider.getBoundingBox(x, y);
		int x1 = Math.floorDiv(r.x, tileSize);
		int y1 = Math.floorDiv(r.y, tileSize);
		int x2 = Math.floorDiv(r.x + r.width, tileSize);
		int y2 = Math.floorDiv(r.y + r.height, tileSize);
		
		for (int blockY = y1; blockY <= y2; blockY++) {
			for (int blockX = x1; blockX <= x2; blockX++) {
				if (mapState.locationHasCollision(blockX, blockY)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean collidesWithEntity(Entity e) {
		if (!hasCollision() || !e.hasCollision()) {return false;}
		
		int ex = e.getX(), ey = e.getY();
		
		Collider other = e.getCollider();
		
		return this.collider.getBoundingBox(x, y).intersects(other.getBoundingBox(ex, ey))
				&& this.collider.collidesWith(other, x, y, ex, ey);
		
	}
		
	public void setSpeed(int speedIn) {
		speed = speedIn;
	}
	
	public void updateEntity() {
		if (getHorizontalMovement() != 0 || getVerticalMovement() != 0) {
			this.move(getHorizontalMovement(), getVerticalMovement());
		}
	}
	
	public boolean isInteractable() {return interactable;}

	public int getX() {return x;}
	
	public int getY() {return y;}
	
	public void setX(int x) {this.x = x;}
	
	public void setY(int y) {this.y = y;}
	
	public void setName(String name) {this.name = name;}
	
	public String getName() {return name;}
	
	public void setMoveUp(boolean pressed) {
		movingUp = pressed;
		updateDirection();
	}

	public void setMoveDown(boolean pressed) {
		movingDown = pressed;
		updateDirection();
	}

	public void setMoveLeft(boolean pressed) {
		movingLeft = pressed;
		updateDirection();
	}

	public void setMoveRight(boolean pressed) {
		movingRight = pressed;
		updateDirection();
	}
	
	public void updateDirection() {}
	
	public short getHorizontalMovement() {
		return movingRight ? (movingLeft ? 0 : (short) 1) : (movingLeft ? (short) -1 : 0);
	}
	
	public short getVerticalMovement() {
		return movingUp ? (movingDown ? 0 : (short) -1) : (movingDown ? (short) 1 : 0);
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
			collider.numOfPoints()), Color.BLUE));
			
		}
		return drawables;
	}
	
}