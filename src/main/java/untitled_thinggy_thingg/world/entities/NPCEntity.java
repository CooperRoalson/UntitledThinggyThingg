package untitled_thinggy_thingg.world.entities;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import untitled_thinggy_thingg.client.ui.Dialog;
import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.world.collision.Collider;

public class NPCEntity extends InteractableEntity {
	private static final long serialVersionUID = 1L;

	private static final int FPS = 5;
	private static final int moveChance = 100000;
	
	private int moveCount;
	private Random random;
	private Polygon moveArea;
	private int moveX;
	private int moveY;

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
	 * ------------------------ */
	
	public NPCEntity(List<List<FilePath>> textures, String directoryName, int x, int y, boolean blockCoords, int speed, Polygon moveArea, Collider collider, FilePath iconLocation, String[] dialog) {
		super(textures, "NPCs/" + directoryName, FPS, blockCoords ? x*Constants.Game.TILE_SIZE : x, blockCoords ? y*Constants.Game.TILE_SIZE : y, speed, collider, iconLocation);
		
		super.setDialogs(new Dialog[] {new Dialog(this, dialog)});
		this.random = new Random();
		
		int[] xPoints = new int[moveArea.npoints];
		for (int xp = 0; xp < moveArea.npoints; xp++) {
			xPoints[xp] = moveArea.xpoints[xp] * Constants.Game.TILE_SIZE;
		}
		
		int[] yPoints = new int[moveArea.npoints];
		for (int yp = 0; yp < moveArea.npoints; yp++) {
			yPoints[yp] = moveArea.ypoints[yp] * Constants.Game.TILE_SIZE;
		}
		this.moveArea = new Polygon(xPoints, yPoints, moveArea.npoints);
		
		this.moveCount = moveChance;
		this.moveX = blockCoords ? x*Constants.Game.TILE_SIZE : x;
		this.moveY = blockCoords ? y*Constants.Game.TILE_SIZE : y;
	}
	
	public NPCEntity(List<List<FilePath>> textures, String directoryName, int x, int y, boolean blockCoords, Polygon moveArea, Collider collider, FilePath iconLocation, String[] dialog) {
		this(textures, directoryName, x, y, blockCoords, 2, moveArea, collider, iconLocation, dialog);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (random.nextInt(moveChance) >= moveCount) {
			this.moveRandom();
			moveCount = moveChance;
		} else {moveCount--;}
		
		this.setMoveDown(getY() < moveY-5);
		this.setMoveUp(getY() > moveY+5);
		this.setMoveLeft(getX() > moveX-5);
		this.setMoveRight(getX() < moveX+5);
	}
	
	public void moveRandom() {
		Rectangle bounds = moveArea.getBounds();
		
		moveX = (int) (random.nextInt((int) (bounds.getMaxX()-bounds.getMinX()))+bounds.getMinX());
		moveY = (int) (random.nextInt((int) (bounds.getMaxY()-bounds.getMinY()))+bounds.getMinY());
	}
	
	@Override
	public void interact() {
		super.initiateDialog();
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
}
