package untitled_thinggy_thingg.world.items;

import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.util.files.FilePath;

public abstract class SwordItem extends Item {
	private static final long serialVersionUID = 1L;

	private static final int FPS = 10;
	
	private int damage;
	
	public SwordItem(String name, int count, int damage, FilePath idle, List<FilePath> swing, String directoryName) {
		super(ItemType.WEAPON, name, count, FPS, Arrays.asList(Arrays.asList(idle), swing), "sword/" + directoryName);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	@Override
	public void use() {
		if (getState() == 0) {
			setState(1);
		}
	}
	
	@Override
	public void onAnimationComplete() {
		setState(0);
	}
}
