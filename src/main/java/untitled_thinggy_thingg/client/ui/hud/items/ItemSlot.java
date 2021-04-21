package untitled_thinggy_thingg.client.ui.hud.items;

import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.world.items.Item;

public class ItemSlot extends UIContainer {
	private ItemIconComponent itemIcon;
	private ItemSlotComponent itemSlot;
	
	public ItemSlot(int slotIndex) {
		this(new Item(), slotIndex);
	}
	
	public ItemSlot(Item item, int slotIndex) {
		this.itemIcon = new ItemIconComponent(item);
		this.itemSlot = new ItemSlotComponent(slotIndex);
		this.addComponent(itemSlot);
		int offset = (((TextureSprite) itemSlot.getSprite().getSprite()).getWidth()-Constants.GUI.ITEM_ICON_SIZE)/2;
		this.addComponent(itemIcon, offset, offset);
	}
	
	public Item getItem() {
		return this.itemIcon.getItem();
	}
	
	public ItemSlotComponent getItemSlot() {
		return this.itemSlot;
	}
	
	public void setItem(Item item) {
		this.itemIcon.setItem(item);
	}
}
