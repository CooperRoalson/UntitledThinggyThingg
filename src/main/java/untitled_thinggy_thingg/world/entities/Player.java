package untitled_thinggy_thingg.world.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import untitled_thinggy_thingg.client.ui.hud.health.HealthContainer;
import untitled_thinggy_thingg.client.ui.hud.items.ItemSlotContainer;
import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.world.collision.Collider;
import untitled_thinggy_thingg.world.collision.ConvexCollider;
import untitled_thinggy_thingg.world.items.Item;
import untitled_thinggy_thingg.world.items.MetalSwordItem;

public class Player extends Entity {
	private static final long serialVersionUID = 2L;
	
	private int maxHearts;
	private int health;
	private HealthContainer heartContainer;
	private ItemSlotContainer itemSlotContainer;
	
	private ArrayList<Item> inventory;
	private int maxInvSize;
		
	// -------super() variables--------
	private static final int BASE_SPEED = 2;
	
	private static final Collider COLLIDER = new ConvexCollider(new Point(10, 29), new Point(21, 29), new Point(16, 22));
	
	static final int textureFps = 5;
	private static final List<List<FilePath>> textures = new ArrayList<>();
	
	/* ---------STATES---------
	 * MOVEMENT
	 * 0: down
	 * 1: left
	 * 2: up
	 * 3: right
	 * 
	 * IDLE
	 * 4: idleDown
	 * 5: idleLeft
	 * 6: idleUp
	 * 7: idleRight
	 * 
	 * MISC
	 * 8: sleeping
	 * ------------------------ */
	static { // instantiate list of textures
		textures.addAll(createMoveTextures(2));
		
		List<FilePath> sleeping = new ArrayList<>();
		
		sleeping.add(new ResourcePath("sleep/Sleep.png"));
		
		textures.add(sleeping);
	}
	// --------------------------------
	
	public static List<List<FilePath>> createMoveTextures(int movementFrames) {
		List<List<FilePath>> textures = new ArrayList<>();
		
		List<FilePath> down, left, up, right, idleDown, idleLeft, idleUp, idleRight;
		down = new ArrayList<>();
		left = new ArrayList<>();
		up = new ArrayList<>();
		right = new ArrayList<>();
		idleDown = new ArrayList<>();
		idleLeft = new ArrayList<>();
		idleUp = new ArrayList<>();
		idleRight = new ArrayList<>();
		
		for (int d = 0; d < movementFrames; d++) {
			down.add(new ResourcePath("down/Down"+d+".png"));
		}
		idleDown.add(new ResourcePath("idleDown/IdleDown.png"));
		
		for (int l = 0; l < movementFrames; l++) {
			left.add(new ResourcePath("left/Left"+l+".png"));
		}
		idleLeft.add(new ResourcePath("idleLeft/IdleLeft.png"));
		
		for (int u = 0; u < movementFrames; u++) {
			up.add(new ResourcePath("up/Up"+u+".png"));
		}
		idleUp.add(new ResourcePath("idleUp/IdleUp.png"));
		
		for (int r = 0; r < movementFrames; r++) {
			right.add(new ResourcePath("right/Right"+r+".png"));
		}
		idleRight.add(new ResourcePath("idleRight/IdleRight.png"));
				
		textures.add(down);
		textures.add(left);
		textures.add(up);
		textures.add(right);
		textures.add(idleDown);
		textures.add(idleLeft);
		textures.add(idleUp);
		textures.add(idleRight);
		
		return textures;
	}
	
	public Player(int x, int y, boolean blockCoords) {
		super(textures, "player", textureFps, blockCoords ? x*Constants.Game.TILE_SIZE : x, blockCoords ? y*Constants.Game.TILE_SIZE : y, BASE_SPEED, false, COLLIDER);
		maxHearts = 3;
		maxInvSize = 10;
		heartContainer = new HealthContainer(maxHearts);
		health = heartContainer.getHealth();
		itemSlotContainer = new ItemSlotContainer();
		setState(4);
		itemSlotContainer.setItem(0, new MetalSwordItem());
	}
	
	public int getHealth() {
		return health; 
	}
	
	public int getMaxHealth() {
		return maxHearts;
	}
	
	public HealthContainer getHeartContainer() {
		return heartContainer;
	}
	
	public ItemSlotContainer getItemSlotContainer() {
		return itemSlotContainer;
	}
	
	public void setMaxHealth(int maxHealthIn) {
		maxHearts = maxHealthIn;
	}
	
	public void setHealth(int healthIn) {
		heartContainer.setHealth(health);
	}
	
	public void addHealth(int health) {
		heartContainer.addHealth(health);
	}
	
	public boolean giveItem(Item item) {
		return giveItem(item, inventory.size());
	}
	
	public boolean giveItem(Item item, int index) {
		if (inventory.size() < maxInvSize) {
			inventory.add(item);
			return true;
		}
		return false;
	}
	
	public Item takeItem(int index) {
		return inventory.remove(index);
	}
	
	public int clearInventory() {
		int l = inventory.size();
		inventory.clear();
		return l;
	}
	
	public void interactEntity() {
		for (Entity entity : GameManager.getInstance().getMapState().getEntities()) {
			if (entity.isInteractable() && this.collidesWithEntity(entity)) {
				setMoveUp(false);
				setMoveDown(false);
				setMoveLeft(false);
				setMoveRight(false);
				
				entity.interact();
			}
		}
	}
	
	@Override
	public void updateDirection() {
		if (getVerticalMovement() == 1) {
			setState(0);
		} else if (getVerticalMovement() == -1) {
			setState(2);
		} else if (getHorizontalMovement() == 1) {
			setState(3);
		} else if (getHorizontalMovement() == -1) {
			setState(1);
		} else {
			if (getState() == 0) {setState(4);}
			else if (getState() == 1) {setState(5);}
			else if (getState() == 2) {setState(6);}
			else if (getState() == 3) {setState(7);}
		}
	}

	public void die() {
	}

	public void sleep(BedEntity bedEntity) {
		setState(8);
		setX(bedEntity.getX());
		setY(bedEntity.getY());
	}

	public void useItem(int index) {
		itemSlotContainer.getItem(index).use();
	}
}
