package untitled_thinggy_thingg.world.items;

public class Inventory {
	private Item[] items;
	
	public Inventory() {
		this.items = new Item[] {};
	}
	
	public Inventory(Item[] items) {
		this.items = items;
	}
	
	public Item[] getAllItems() {
		return items;
	}
	
	public Item getItem(int index) {
		return items[index];
	}
	//Is an item in items
	public boolean hasItem(Item item) {
		for(Item i : items) {
			if(i.equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	public int itemIndex(Item item) {
		int c = 0;
		for(Item i : items) {
			if(i.equals(item)) {
				return c;
			}
			c++;
		}
		return -1;
	}
}
