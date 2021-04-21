package untitled_thinggy_thingg.dev.maps;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.world.MapState;
import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.entities.Entity;
import untitled_thinggy_thingg.world.tiles.Tile;

/**
 * A separate main class used for creating map files. Used for testing only.
 */
public class MapCreator {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Map<String,Tile> tileLegend = getTileLegend();
		Map<String,Block> blockLegend = getBlockLegend();
		
		writeMapFromCSV(new ResourcePath("OkatbridgeTileMap.csv"), new ResourcePath("EmptyFile.csv"), tileLegend, blockLegend, "TestMaps", "Okatbridge.ser");
		//writeMapFromCSV(new ResourcePath("EmptyFile.csv"), new ResourcePath("EmptyFile.csv"), tileLegend, blockLegend, "TestMaps", "Test4.ser");
	}	
	
	/**
	 * Reads a CSV (comma-separated-values) file from a {@link FilePath}, and uses a {@link Map}
	 * to translate it into a two-dimensional {@link List} of objects of a given type.
	 * 
	 * @param <V> The type of object to read
	 * 
	 * @param csv The {@code FilePath} to the CSV file
	 * @param legend A {@code Map} used to translate the CSV file
	 * 
	 * @return A two-dimensional list of objects read from the CSV file
	 * 
	 * @throws FileNotFoundException When {@code csv} points to a non-existent file
	 */
	public static <V> List<List<V>> listFromCSV(FilePath csv, Map<String,V> legend) throws FileNotFoundException {
		Scanner scan = new Scanner(csv.getInputStream());
		
		List<List<V>> list = new ArrayList<>();
		
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			List<V> row = new ArrayList<>();
			
			for (String key : line.split(",")) {
				row.add(legend.get(key));
				if (legend.get(key) == null) {System.out.println("\"" + key + "\" is missing from the legend" );}
			}
			
			list.add(row);
		}
		
		scan.close();
		
		return list;
	}
	
	/**
	 * Reads CSV (comma-separated-values) files from two {@link FilePath}s (relative to "maps/csv/{@code <mapDirectory>}"), and uses two {@link Map}s
	 * to translate them into a two-dimensional {@link List} of {@link Tile}s, and another {@code List} of {@link Block}s.
	 * It then writes them to a map file at "maps/{@code <mapDirectory>}/{@code <mapName>}" using {@link MapState#saveTemplateMap(String, String, List, List, Set)}.
	 * It returs true if it suceeded, and false otherwise. Cannot handle {@link Entity}s.
	 * 
	 * @param tiles A {@code FilePath} to a CSV file with {@code Tile} data
	 * @param blocks A {@code FilePath} to a CSV file with {@code Block} data
	 * @param tileLegend A {@code Map} for reading the {@code Tile} file
	 * @param blockLegend A {@code Map} for reading the {@code Block} file
	 * @param mapDirectory The target folder to put the map file in
	 * @param mapName The name of the map file
	 * 
	 * @return Whether the write suceeded
	 * 
	 * @throws FileNotFoundException When {@code tiles} or {@code blocks} points to a non-existent file
	 */
	public static boolean writeMapFromCSV(FilePath tiles, FilePath blocks, Map<String,Tile> tileLegend, Map<String,Block> blockLegend, String mapDirectory, String mapName) throws FileNotFoundException {
		List<List<Tile>> tileMap = listFromCSV(tiles.inDirectory("maps/csv/" + mapDirectory + "/"), tileLegend);
		List<List<Block>> blockMap = listFromCSV(blocks.inDirectory("maps/csv/" + mapDirectory + "/"), blockLegend);
		
		// Empty set
		Set<Entity> entities = new HashSet<>();
		
		// Convert to arrays
		Tile[][] tileArray = new Tile[tileMap.size()][];
		for (int i = 0; i < tileMap.size(); i++) {
		    List<Tile> row = tileMap.get(i);
		    tileArray[i] = row.toArray(new Tile[row.size()]);
		}
		Block[][] blockArray = new Block[blockMap.size()][];
		for (int i = 0; i < blockMap.size(); i++) {
		    List<Block> row = blockMap.get(i);
		    blockArray[i] = row.toArray(new Block[row.size()]);
		}
		
		
		return MapState.saveTemplateMap(mapDirectory, mapName, tileArray, blockArray, entities);
	}
	

	/**
	 * @return A {@link Map} translating {@link String} keys to {@link Tile}s
	 */
	public static Map<String,Tile> getTileLegend() {
		Map<String,Tile> tileLegend = new HashMap<>();
		
		tileLegend.put("", new Tile());
		
		int id = 0;
		/*tileLegend.put("BR", new Tile(id++,"BaseRock","BaseRock.png"));
		tileLegend.put("DBR", new Tile(id++,"DarkBaseRock","DarkBaseRock.png"));
		tileLegend.put("CR", new Tile(id++,"CobbleRock","CobbleRock.png"));
		tileLegend.put("DCR", new Tile(id++,"DarkCobbleRock","DarkCobbleRock.png"));
		
		tileLegend.put("DI", new Tile(id++,"Dirt","Dirt.png"));
		tileLegend.put("DD", new Tile(id++,"DarkDirt","DarkDirt.png"));
		tileLegend.put("GR", new Tile(id++,"Grass","Grass.png"));
		tileLegend.put("DG", new Tile(id++,"DarkGrass","DarkGrass.png"));
		tileLegend.put("SN", new Tile(id++,"Sand","Sand.png"));
		
		tileLegend.put("CLD", new Tile(id++,"CliffLeftDown","CliffLD.png"));
		tileLegend.put("CRD", new Tile(id++,"CliffRightDown","CliffRD.png"));
		tileLegend.put("CLU", new Tile(id++,"CliffLeftUp","CliffLU.png"));
		tileLegend.put("CRU", new Tile(id++,"CliffRightUp","CliffRU.png"));
		
		tileLegend.put("PU", new Tile(id++,"PathUp","PathU.png"));
		tileLegend.put("PD", new Tile(id++,"PathDown","PathD.png"));
		tileLegend.put("PL", new Tile(id++,"PathLeft","PathL.png"));
		tileLegend.put("PR", new Tile(id++,"PathRight","PathR.png"));
		
		tileLegend.put("TDL", new Tile(id++,"TreeDownLeft","TreeDL.png"));
		tileLegend.put("TDR", new Tile(id++,"TreeDownRight","TreeDR.png"));
		tileLegend.put("TL", new Tile(id++,"TreeLeft","TreeL.png"));
		tileLegend.put("TR", new Tile(id++,"TreeRight","TreeR.png"));
		tileLegend.put("TM", new Tile(id++,"TreeMiddle","TreeM.png"));
		tileLegend.put("TU", new Tile(id++,"TreeUp","TreeU.png"));
		tileLegend.put("TD", new Tile(id++,"TreeDown","TreeD.png"));

		tileLegend.put("TUL", new Tile(id++,"TreeUpLeft","TreeUL.png"));
		tileLegend.put("TUR", new Tile(id++,"TreeUpRight","TreeUR.png"));
		
		tileLegend.put("WM", new Tile(id++, "WaterMiddle","WaterM.png"));
		tileLegend.put("WD", new Tile(id++, "WaterDown","WaterD.png"));
		tileLegend.put("WU", new Tile(id++, "WaterUp","WaterU.png"));
		tileLegend.put("WL", new Tile(id++, "WaterLeft","WaterL.png"));
		tileLegend.put("WR", new Tile(id++, "WaterRight","WaterR.png"));
		
		tileLegend.put("SWDL", new Tile(id++, "WaterSandDownLeft","WaterSDL.png"));
		tileLegend.put("SWDR", new Tile(id++, "WaterSandDownRight","WaterSDR.png"));
		tileLegend.put("SWUL", new Tile(id++, "WaterSandUpLeft","WaterSUL.png"));
		tileLegend.put("SWUR", new Tile(id++, "WaterSandUpRight","WaterSUR.png"));
		
		tileLegend.put("WDL", new Tile(id++, "WaterDotDownLeft","WateCRDotL.png"));

		tileLegend.put("WOD", new Tile(id++,"Wood","Wood.png"));
		tileLegend.put("WOF", new Tile(id++,"WoodFloor","WoodFloor.png"));*/
		/*
		tileLegend.put("Darkness", new Tile(id++, "darkness", "Darkness.png", ""));
		tileLegend.put("WoodFloor", new Tile(id++, "woodfloor", "WoodFloor.png", ""));
		tileLegend.put("SandRocks", new Tile(id++, "sandrocks", "SandRocks.png", "sand"));
		tileLegend.put("Sand", new Tile(id++, "sand", "Sand.png", "sand"));
		tileLegend.put("SandOne", new Tile(id++, "sandone", "SandOne.png", "sand"));
		tileLegend.put("SandTwo", new Tile(id++, "sandtwo", "SandTwo.png", "sand"));
		tileLegend.put("SandThree", new Tile(id++, "sandthree", "SandThree.png", "sand"));*/
		
		tileLegend.put("beach", new Tile(id++, "beach", "Sand.png", ""));
		tileLegend.put("railing", new Tile(id++, "fence", "Fence.png", ""));
		tileLegend.put("field", new Tile(id++, "field", "Field.png", ""));
		tileLegend.put("grass", new Tile(id++, "grass", "Grass.png", ""));
		tileLegend.put("path", new Tile(id++, "path", "Path.png", ""));
		tileLegend.put("rock", new Tile(id++, "rock", "Rock.png", ""));
		tileLegend.put("wood", new Tile(id++, "stonepath", "StonePath.png", ""));
		tileLegend.put("water", new Tile(id++, "water", "Water.png", ""));
		tileLegend.put("building", new Tile(id++, "building", "Building.png", ""));
		
		
		return tileLegend;
	}
	
	/**
	 * @return A {@link Map} translating {@link String} keys to {@link Block}s
	 */
	public static Map<String,Block> getBlockLegend() {
		Map<String,Block> blockLegend = new HashMap<>();
		
		blockLegend.put("", new Block());
		
		//int id = 0;
		
		/*
		blockLegend.put("Bookshelf", (Block) new Block(id++, "bookshelf", Arrays.asList(new ResourcePath("Bookshelf.png")), "book", 0, ConvexCollider.rectFromCorners(12,0,52,26)).setTextureOffset(0, -32));
		blockLegend.put("OpenBook", new Block(id++, "openbook", Arrays.asList(new ResourcePath("OpenBook.png")), "book", 0, ConvexCollider.rectFromCorners(0, 0, 0, 0)));
		blockLegend.put("BigCactus", new Block(id++, "bigcactus", Arrays.asList(new ResourcePath("BigCactus.png")), "cactus", 0, ConvexCollider.rectFromCorners(9, 25, 15, 30)));
		blockLegend.put("SmallCactus", new Block(id++, "smallcactus", Arrays.asList(new ResourcePath("SmallCactus.png")), "cactus", 0, ConvexCollider.rectFromCorners(10 ,20, 20 ,26)));
		blockLegend.put("CaveEntrance", new Block(id++, "caveentrance", Arrays.asList(new ResourcePath("CaveEntrance.png")), "cave", 0, ConvexCollider.rectFromCorners(0, 0, 62, 42)));
		blockLegend.put("StoneDoor", new Block(id++, "stonedoor", Arrays.asList(new ResourcePath("StoneDoor.png")), "door/stone", 0, ConvexCollider.rectFromCorners(0, 0, 32, 32)));
		blockLegend.put("Rug", new Block(id++, "rug", Arrays.asList(new ResourcePath("Rug.png")), "rug", 0, ConvexCollider.rectFromCorners(0, 0, 0, 0)));
		blockLegend.put("LargeTable", new Block(id++, "largetable", Arrays.asList(new ResourcePath("LargeTable.png")), "table", 0, ConvexCollider.rectFromCorners(4, 2, 92, 52)));
		blockLegend.put("DesertTent", new Block(id++, "deserttent", Arrays.asList(new ResourcePath("DesertTent.png")), "tent/desert", 0, ConvexCollider.rectFromCorners(0, 0, 0, 0)));
		blockLegend.put("StoneWall", new Block(id++, "stonewall", Arrays.asList(new ResourcePath("StoneWall.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(0, 16, 32, 32)));
		blockLegend.put("StoneWallLeft", new Block(id++, "stonewallleft", Arrays.asList(new ResourcePath("StoneWallLeft.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(0, 0, 10, 32)));
		blockLegend.put("StoneWallRight", new Block(id++, "stonewallright", Arrays.asList(new ResourcePath("StoneWallRight.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(22, 0, 32, 32)));
		blockLegend.put("StoneWallLeftLong", new Block(id++, "stonewallleftlong", Arrays.asList(new ResourcePath("StoneWallLeft.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(0, 0, 10, 48)));
		blockLegend.put("StoneWallRightLong", new Block(id++, "stonewallrightlong", Arrays.asList(new ResourcePath("StoneWallRight.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(22, 0, 32, 48)));
		blockLegend.put("StoneWallTopLeft", new Block(id++, "stonewalltopleft", Arrays.asList(new ResourcePath("StoneWallTopLeft.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(0, 16, 32, 32)));
		blockLegend.put("StoneWallTopRight", new Block(id++, "stonewalltopright", Arrays.asList(new ResourcePath("StoneWallTopRight.png")), "wall/stone", 0, ConvexCollider.rectFromCorners(0, 16, 32, 32)));
		blockLegend.put("RugOffsetDown", (Block) new Block(id++, "rugoffsetdown", Arrays.asList(new ResourcePath("Rug.png")), "rug", 0, ConvexCollider.rectFromCorners(0, 0, 0, 0)).setTextureOffset(0, 48));*/
		
		return blockLegend;
	}
}










//Test2 and Test3 key


/*
tileLegend.put("1", new Tile(1,"Grass Tile 1", new ResourcePath("grass0.png")));
tileLegend.put("2", new Tile(1,"Grass Tile 2", new ResourcePath("grass1.png")));

		//Block(int id, String name, List<FilePath> textures, String directoryName, int fps, boolean hasCollision)
		/*for(int i = 1; i <= 23; i++) {
			blockLegend.put(String.valueOf(i), new Block(i, "Grass Tile " + i, new ResourcePath("Grass Tiles-" + i + ".png"), "", false, null));
		}*/

/* ------------- KEY -------------
 * 0: Red Flower
 * 1: Yellow Flower
 * 2: White Flower
 * 3: Grass Left
 * 4: Grass Right
 * 5: Thin Fence
 * 6: Thick Fence Right
 * 7: Thick Fence Left
 * 8: Little Tree
 * 9: Big Tree
 * ------------------------------- */
/*
int id = 0;
// FLOWERS
for (int i=0; i < 3; i++) {
	blockLegend.put(String.valueOf(id), new Block(id++, "Flower " + i, new ResourcePath("flower" + i + ".png"), "", false, null));
}
// GRASS
for (int i=0; i < 2; i++) {
	blockLegend.put(String.valueOf(id), new Block(id++, "Grass " + i, new ResourcePath("grass" + i + ".png"), "", false, null));
}
// FENCES
for (int i=0; i < 3; i++) {
	blockLegend.put(String.valueOf(id), new Block(id++, "Fence " + i, new ResourcePath("fence" + i + ".png"), "", true, new TriangleCollider(0, 31, 31, 31, 16, 4)));
}
// TREE
blockLegend.put(String.valueOf(id), new Block(id++, "Little Tree", Arrays.asList(new ResourcePath("tree0.png")), "", 0, Constants.Game.TILE_SIZE*2, 0, true, new TriangleCollider(0 * hitboxMultiplier, (743-Constants.Textures.TILE_TEXTURE_SIZE*2) * hitboxMultiplier, 247 * hitboxMultiplier, (743-Constants.Textures.TILE_TEXTURE_SIZE*2) * hitboxMultiplier, 125 * hitboxMultiplier, (645-Constants.Textures.TILE_TEXTURE_SIZE*2) * hitboxMultiplier)));
blockLegend.put(String.valueOf(id), new Block(id++, "Big Tree", Arrays.asList(new ResourcePath("tree1.png")), "", Constants.Game.TILE_SIZE, Constants.Game.TILE_SIZE*4, 0, true, new RectangleCollider(0, 0, Constants.Game.TILE_SIZE, Constants.Game.TILE_SIZE)));
// MAP_LOADERS
//ID 10
blockLegend.put(String.valueOf(id), new Block(id++, "test2 Loader", new ResourcePath("fence0.png"), "", true, new TriangleLoadCollider(0, 31, 31, 31, 16, 4, "test2.ser")));
//ID 11
blockLegend.put(String.valueOf(id), new Block(id++, "test3 Loader", new ResourcePath("fence0.png"), "", true, new TriangleLoadCollider(0, 31, 31, 31, 16, 4, "test3.ser")));

blockLegend.put("", new Block());
*/
