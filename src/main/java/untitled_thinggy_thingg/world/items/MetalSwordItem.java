package untitled_thinggy_thingg.world.items;

import java.util.ArrayList;
import java.util.List;

import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class MetalSwordItem extends SwordItem {
	private static final long serialVersionUID = 1L;
	
	private static final List<FilePath> SWING = new ArrayList<>();
	
	static {
		for (int i = 0; i < 5; i++) {
			SWING.add(new ResourcePath("Swing" + i + ".png"));
		}
	}
	
	public MetalSwordItem(int count) {
		super("Metal Sword", count, 5, new ResourcePath("Icon.png"), SWING, "metal");
	}
	
	public MetalSwordItem() {
		this(1);
	}
}
