package untitled_thinggy_thingg.client.ui.hud.items;

import untitled_thinggy_thingg.client.ui.SpriteComponentWrapper;
import untitled_thinggy_thingg.world.items.Item;

public class ItemIconComponent extends SpriteComponentWrapper {	
	public ItemIconComponent(Item item) {
		super(item);
	}
	
	public Item getItem() {
		return (Item) inner;
	}
	
	public void setItem(Item item) {
		inner = item;
	}
}
