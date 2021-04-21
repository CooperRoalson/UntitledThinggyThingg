package untitled_thinggy_thingg.world.entities;

import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.world.collision.Collider;
import untitled_thinggy_thingg.world.collision.ConvexCollider;

public class BedEntity extends InteractableEntity {
	private static final long serialVersionUID = 1L;
	
	private static final List<List<FilePath>> TEXTURES = Arrays.asList(Arrays.asList(new ResourcePath("Bed.png")));
	private static final FilePath ICON = new ResourcePath("Bed.png");
	private static final Collider COLLIDER = ConvexCollider.rectFromCorners(2, 11, 14, 16);

	public BedEntity(int x, int y, boolean blockCoords) {
		super(TEXTURES, "bed", 0, blockCoords ? x*Constants.Game.TILE_SIZE : x, blockCoords ? y*Constants.Game.TILE_SIZE : y, 0, COLLIDER, ICON);
		this.scaleTo(Constants.Game.TILE_SIZE, Constants.Game.TILE_SIZE*2);
	}
	
	@Override
	public void interact() {
		GameManager.getInstance().getMapState().getPlayer().sleep(this);
	}
}
