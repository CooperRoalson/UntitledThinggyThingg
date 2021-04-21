package untitled_thinggy_thingg.world.entities;

import java.util.List;

import untitled_thinggy_thingg.client.ui.Dialog;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.core.gamestates.UIGameState.UIPauseMode;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.world.collision.Collider;

public class InteractableEntity extends Entity{
	private static final long serialVersionUID = 1L;
	private Dialog[] dialogs;
	private int dialogState = 0;
	private TextureSprite icon;
	
	public InteractableEntity(List<List<FilePath>> textures, String directoryName, int fps, int x, int y, int speed, Collider collider, FilePath iconLocation) {
		super(textures, directoryName, fps, x, y, speed, true, collider);
		icon = new TextureSprite(iconLocation.inDirectory("entities/" + directoryName));
	}
	
	public void setDialogs(Dialog[] d) {dialogs = d;}
	
	public TextureSprite getIcon() {return icon;}
	
	@Override //Restated so that interact method can call on methods in this class
	public void interact() {}
	
	
	
	private static final int[] DIALOGUE_COORDINATES = {50,200};
	
	public void initiateDialog() {
		GameManager.getInstance().createOrAddUI(dialogs[dialogState], DIALOGUE_COORDINATES[0], DIALOGUE_COORDINATES[1], UIPauseMode.BLOCK_ANIMATIONS_ONLY);
	}
	
}
