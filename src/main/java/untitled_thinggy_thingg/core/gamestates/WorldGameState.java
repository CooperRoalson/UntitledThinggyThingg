package untitled_thinggy_thingg.core.gamestates;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;

import untitled_thinggy_thingg.client.Controls;
import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.world.MapState;
import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.entities.Entity;
import untitled_thinggy_thingg.world.entities.Player;
import untitled_thinggy_thingg.world.tiles.Tile;

/**
 * An implementation of {@link GameState} that runs the world and player.
 */
public class WorldGameState implements GameState {    
    // Screen's coordinates and dimensions in world-space
    private Rectangle screenRect;
    
    private static boolean actionsRegistered = false;
    
    /**
     * Creates a new {@code WorldGameState}.
     */
    public WorldGameState() {
        screenRect = getRectCenteredOnPlayer();
		this.screenRect.x = Math.max(screenRect.x, 0);
		this.screenRect.y = Math.max(screenRect.y, 0);
    }
    
    /**
     * Updates the world. Does this by calling {@link MapState#update()} and {@link WorldGameState#updateScreenCoordinates()}.
     */
	@Override
	public void update() {
		GameManager.getInstance().getMapState().update();
		
		updateScreenPos();
		
	}
    
	private void updateScreenPos() {
		this.screenRect.setSize(getScreenDimensions());
		
		Player p = GameManager.getInstance().getMapState().getPlayer();
		this.moveScreenX(p.getX() - (int) screenRect.getCenterX());
		this.moveScreenY(p.getY() - (int) screenRect.getCenterY());
	}

	/**
	 * {@inheritDoc} These {@code Drawable}s are from the {@link Player}, {@link Block}s, {@link Tile}s, and {@link Entity}s currently on-screen.
	 */
	@Override
	public List<Drawable> draw() {
        List<Drawable> drawables = new ArrayList<>();
        
        /* Testing
        drawables.add(new TextureDrawable(new ResourcePath("assets/yay.png"), 0, 0, scale));
        */
        
        MapState mapState = GameManager.getInstance().getMapState();
        if (!mapState.hasMap()) {return drawables;}
        
        // *----------Draw Tiles----------*
        int tileSize = Constants.Game.TILE_SIZE;
        
        Tile[][] tileMap = mapState.getTileMap();
        
        Rectangle tileWorldRect;
        Tile tile;
        
        for (int tileY = 0; tileY < tileMap.length; tileY++) {
        	for (int tileX = 0; tileX < tileMap[tileY].length; tileX++) {
        		tile = tileMap[tileY][tileX];
        		// The tile's rectangle in world-space
        		tileWorldRect = new Rectangle(tileSize * tileX, tileSize * tileY, tileSize, tileSize);
        		if (screenRect.intersects(tileWorldRect)) {
        			drawables.addAll(tile.draw(tileSize * tileX - screenRect.x, tileSize * tileY - screenRect.y));
        		}
        	}
        }
        // *------------------------------*
        
        // *----------Draw Blocks & Entities----------*
        
                
        Block[][] blockMap = mapState.getBlockMap();
        
        List<Object> sprites = new ArrayList<>(mapState.getEntities());
        sprites.add(mapState.getPlayer());
        
        for (int blockY = 0; blockY < blockMap.length; blockY++) {
        	for (int blockX = 0; blockX < blockMap[blockY].length; blockX++) {
        		sprites.add(new BlockWrapper(blockMap[blockY][blockX], blockX * tileSize, blockY * tileSize));
        	}
        }

        sprites.sort(new DrawingOrderComparator());
        
        
        Rectangle entityWorldRect;
        Rectangle blockWorldRect;
        BlockWrapper blockWrapper;
        Block block;
        Entity entity;

        for (Object o : sprites) {
        	if (o instanceof BlockWrapper) {
        		blockWrapper = (BlockWrapper) o;
        		block = blockWrapper.block;
        		 // The block's rectangle in world-space
        		 blockWorldRect = new Rectangle(blockWrapper.x + block.getTextureOffsetX(), blockWrapper.y + block.getTextureOffsetY(), block.getWidth(), block.getHeight());
        		 if (screenRect.intersects(blockWorldRect)) {
        			 drawables.addAll(block.draw(blockWrapper.x - screenRect.x, blockWrapper.y - screenRect.y));
        		 }
        	} else if (o instanceof Entity) {
        		entity = (Entity) o;
        		entityWorldRect = new Rectangle(entity.getX() + entity.getTextureOffsetX(), entity.getY() + entity.getTextureOffsetY(), entity.getWidth(), entity.getHeight());
        		 if (screenRect.intersects(entityWorldRect)) {
        		 	drawables.addAll(entity.draw(entity.getX() - screenRect.x,entity.getY() - screenRect.y));
        		 }
        	}
        }
        // *---------------------------------*
        
        return drawables;
    }
	
	/**
	 * A {@link Comparator} used to order {@code Entity}s and {@code Block}s when drawing.
	 * It sorts them primarily by y-value, and then by x-value.
	 */
	private class DrawingOrderComparator implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {
			if (!((o1 instanceof BlockWrapper || o1 instanceof Entity) && (o2 instanceof BlockWrapper || o2 instanceof Entity))) {
				throw new IllegalArgumentException("Arguments must be of type BlockWrapper or Entity!");
			}
			
			return (o1 instanceof BlockWrapper) ? ((o2 instanceof BlockWrapper) ? compareBlocks((BlockWrapper) o1, (BlockWrapper) o2) : compareBlockToEntity((BlockWrapper) o1, (Entity) o2)) : ((o2 instanceof BlockWrapper) ? compareEntityToBlock((Entity) o1, (BlockWrapper) o2) : compareEntities((Entity) o1, (Entity) o2));
		}
		
		public int compareBlocks(BlockWrapper b1, BlockWrapper b2) {
			return (b1.y > b2.y) ? 1 : ((b1.y < b2.y) ? -1 : ((b1.x > b2.x) ? 1 : ((b1.x < b2.x) ? -1 : 0)));
		}
		
		public int compareEntities(Entity e1, Entity e2) {
			int e1x = e1.getColliderMaxX();
			int e1y = e1.getColliderMaxY();
			int e2x = e2.getColliderMaxX();
			int e2y = e1.getColliderMaxY();
			
			return (e1y > e2y) ? 1 : ((e1y < e2y) ? -1 : ((e1x > e2x) ? 1 : ((e1x < e2x) ? -1 : 0)));
		}
		
		public int compareEntityToBlock(Entity e, BlockWrapper b) {
			int ex = e.getColliderMaxX();
			int ey = e.getColliderMaxY();
			int bx = b.getColliderMaxX();
			int by = b.getColliderMaxY();
			
			return (ey > by) ? 1 : ((ey < by) ? -1 : ((ex > bx) ? 1 : ((ex < bx) ? -1 : 0)));
		}
		
		public int compareBlockToEntity(BlockWrapper b, Entity e) {
			return -compareEntityToBlock(e, b);
		}
		
    }
		
	private class BlockWrapper {
		private int x, y;
		private Block block;
		
		public BlockWrapper(Block block, int x, int y) {
			this.block = block;
			this.x = x;
			this.y = y;
		}
		
		public int getColliderMaxX() {
			if (block.getCollider() == null) {return this.x;}
			Rectangle r = block.getCollider().getBoundingBox();
			return r.x + r.width + this.x;
		}
		
		public int getColliderMaxY() {
			if (block.getCollider() == null) {return this.y;}
			Rectangle r = block.getCollider().getBoundingBox();
			return r.y + r.height + this.y;
		}
		
	}
    
    private static Dimension getScreenDimensions() {
    	// Size in world-space
    	return GameManager.getInstance().getWindowManager().getDimensionsNormalized();
    }
    
    private static Rectangle getRectCenteredOnPlayer() {
    	Player player = GameManager.getInstance().getMapState().getPlayer();
    	
    	Rectangle rectangle = new Rectangle(getScreenDimensions());
    	rectangle.setLocation(player.getX() - rectangle.width/2, player.getY() - rectangle.height/2);
    	
    	return rectangle;
    }
    
    /**
     * {@inheritDoc} It does this using {@link Controls#getInputMap()}.
     */
	@Override
	public ComponentInputMap registerKeyBindings(JComponent component) {
		return GameManager.getInstance().getControls().getInputMap();
	}
	
	/**
     * {@inheritDoc} It does this using {@link Controls#getActionMap()}.
     */
	@Override
	public void registerActionMap() {
		if (!actionsRegistered) {
			GameManager.getInstance().addActionMap(Controls.getActionMap());
			actionsRegistered = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleAWTEvent(AWTEvent e) {
		// TODO Add this
	}

	private void moveScreenX(int x) {
		if (x == 0) {return;}
		
		int val = Math.abs(x);
		short dir = (short) Math.signum(x);
		
		for (int amount = val; amount > 0; amount--) {
			int testX = (dir < 0) ? screenRect.x : screenRect.x + screenRect.width;
			if (!screenCollidesAtX(testX + amount * dir)) {
				screenRect.x += amount * dir;
				break;
			}
		}
	}

	private void moveScreenY(int y) {
		if (y == 0) {return;}
		
		int val = Math.abs(y);
		short dir = (short) Math.signum(y);
		
		for (int amount = val; amount > 0; amount--) {
			int testY = (dir < 0) ? screenRect.y : screenRect.y + screenRect.height;
			if (!screenCollidesAtY(testY + amount * dir)) {
				screenRect.y += amount * dir;
				break;
			}
		}
	}
	
	private boolean screenCollidesAtX(int testX) {
		MapState mapState = GameManager.getInstance().getMapState();
		
		int tileSize = Constants.Game.TILE_SIZE;
		int blockX = Math.floorDiv(testX, tileSize);
		int startY = screenRect.y/tileSize;
		int height = (int) Math.ceil((double) screenRect.height/tileSize);
		for (int blockY = startY; blockY < startY + height; blockY++) {
			if (mapState.getBlock(blockX, blockY).collidesWithScreen()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean screenCollidesAtY(int testY) {
		MapState mapState = GameManager.getInstance().getMapState();
		
		int tileSize = Constants.Game.TILE_SIZE;
		int blockY = Math.floorDiv(testY, tileSize);
		int startX = screenRect.x/tileSize;
		int width = (int) Math.ceil((double) screenRect.width/tileSize);
				
		for (int blockX = startX; blockX < startX + width; blockX++) {
			if (mapState.getBlock(blockX, blockY).collidesWithScreen()) {
				return true;
			}
		}
		
		return false;
	}

}
