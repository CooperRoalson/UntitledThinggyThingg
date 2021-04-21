package untitled_thinggy_thingg.world.entities;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.client.ui.Dialog;
import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.world.collision.Collider;
import untitled_thinggy_thingg.world.collision.ConvexCollider;

public class SignEntity extends InteractableEntity {
	private static final long serialVersionUID = 1L;
	
	private static final List<List<FilePath>> TEXTURES = Arrays.asList(Arrays.asList(new ResourcePath("Sign.png")));
	private static final FilePath ICON = new ResourcePath("Sign.png");
	private static final Collider COLLIDER = ConvexCollider.rectFromCorners(2, 11, 14, 16);
	
	private Font font;
	private Color color;
	
	public SignEntity(String[] text, Font font, Color color, int x, int y, boolean blockCoords) {
		super(TEXTURES, "sign", 0, blockCoords ? x*Constants.Game.TILE_SIZE : x, blockCoords ? y*Constants.Game.TILE_SIZE : y, 0, COLLIDER, ICON);
		super.setDialogs(new Dialog[] {new Dialog(this, text)});
		this.font = font;
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void interact() {
		super.initiateDialog();
	}
}
