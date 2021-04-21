package untitled_thinggy_thingg.world.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.core.drawing.sprites.AnimationGroupSprite;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class Item extends AnimationGroupSprite {	
	private static final long serialVersionUID = 1L;
	
	private final ItemType type;
	private String name;
	private int count;
	
	public Item(ItemType type, String name, int count, int fps, List<List<FilePath>> textureLocations, String directoryName) {
		super(texturesInDirectory(textureLocations, "items/" + directoryName), fps);
		this.type = type;
		this.name = name;
		this.count = count;
		
		this.setScale(Constants.Textures.ITEM_TEXTURE_SCALE);
	}
	
	public Item(ItemType type, String name, int count, int fps, FilePath texture, String directoryName) {
		this(type, name, count, fps, Arrays.asList(Arrays.asList(texture)), directoryName);
	}
	
	public Item() {
		this(ItemType.MISC, "", 1, 0, new ResourcePath(""), "");
	}
	
	public static List<List<FilePath>> texturesInDirectory(List<List<FilePath>> textures, String directory) {
		List<List<FilePath>> texturesInDirectory = new ArrayList<>();
		
		for (List<FilePath> paths : textures) {
			
			List<FilePath> pathsInDirectory = new ArrayList<>();
			for (FilePath path : paths) {
				pathsInDirectory.add(path.inDirectory(directory));
			}
			
			texturesInDirectory.add(pathsInDirectory);
		}
		
		return texturesInDirectory;
	}

	public ItemType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void addCount(int add) {
		this.count += add;
	}
	
	public void addCount() {
		addCount(1);
	}

	public void use() {}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Item)) {
			return false;
		}
		Item i = (Item) o;
		
		return i.getType() == this.getType() && i.getName() == this.getName();
	}
}
