package untitled_thinggy_thingg.client.ui.hud.items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.world.items.Item;

public class ItemSlotContainer extends UIContainer {	
	private List<ItemSlot> slots;
	
	public ItemSlotContainer() {
		slots = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			ItemSlot slot = new ItemSlot(i);
			slots.add(slot);
			this.addComponent(slot, (((TextureSprite) slot.getItemSlot().getSprite().getSprite()).getWidth()+3)* (slots.size()-1), 0);
		}
	}
	
	public Item getItem(int index) {
		return slots.get(index).getItem();
	}
	
	public List<Item> getItems() {		
		return slots.stream().map(ItemSlot::getItem).collect(Collectors.toList());
	}
	
	public void setItem(int index, Item item) {
		slots.get(index).setItem(item);
	}
}
