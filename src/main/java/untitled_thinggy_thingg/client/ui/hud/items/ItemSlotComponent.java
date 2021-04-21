package untitled_thinggy_thingg.client.ui.hud.items;

import java.awt.Color;
import java.awt.Font;

import javax.swing.KeyStroke;

import untitled_thinggy_thingg.client.ui.SpriteComponentWrapper;
import untitled_thinggy_thingg.client.ui.StringComponent;
import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.StringDrawable.Alignment;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class ItemSlotComponent extends UIContainer {
	private ItemSlotSprite slotSprite;
	
	public ItemSlotComponent(int slotIndex) {
		slotSprite = new ItemSlotSprite();
		this.addComponent(slotSprite);
		KeyStroke stroke = GameManager.getInstance().getControls().getControl("use item in slot " + slotIndex);
		String control = "" + (char) stroke.getKeyCode();
		TextureSprite sprite = (TextureSprite) slotSprite.getSprite();
		StringComponent string = new StringComponent(control, new Font("Comic Sans MS", Font.BOLD, sprite.getWidth()/2), Alignment.LEFT, Color.WHITE);
		this.addComponent(string, -sprite.getHeight()/10, sprite.getHeight()-(sprite.getHeight()/3)-(sprite.getHeight()/14));
	}
	
	public ItemSlotSprite getSprite() {
		return this.slotSprite;
	}
	
	static class ItemSlotSprite extends SpriteComponentWrapper {
		public ItemSlotSprite() {
			super(new TextureSprite(new ResourcePath("ui/hud/items/ItemSlot.png")));
		}
	}
}
