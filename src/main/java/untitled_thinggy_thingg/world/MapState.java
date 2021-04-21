package untitled_thinggy_thingg.world;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.util.files.ResourcePath;
import untitled_thinggy_thingg.util.files.SimpleFilePath;
import untitled_thinggy_thingg.util.files.WorldDataPath;
import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.entities.Entity;
import untitled_thinggy_thingg.world.entities.Player;
import untitled_thinggy_thingg.world.tiles.Tile;

/*
Order of save data:
List<List<Tile>> tileMap
List<List<Block>> blockMap
Set<Entity> entities
*/

public class MapState {
	private Optional<Tile[][]> tileMap;
	private Optional<Block[][]> blockMap;
	private Optional<boolean[][]> collisionMap;
	private Optional<Set<Entity>> entities;
	private Optional<Player> player;
	
	private int mapWidth, mapHeight;

	
	private String saveName;
	private String mapDirectory;
	private String mapName;
	
	public static SimpleFilePath getUserMapPath(String saveName) {
		return new WorldDataPath("maps/", saveName);
	}
	
	public static ResourcePath getResourcesMapPath() {
		return new ResourcePath("maps/");
	}
	
	public MapState() {		
		tileMap = Optional.empty();
		blockMap = Optional.empty();
		entities = Optional.empty();
		
		saveName = "";
		mapDirectory = "";
		mapName = "";
	}
	
	private void calculateMapSize() {
		Tile[][] tileMap = getTileMap();
		
		mapHeight = tileMap.length;
		
		for (int y = 0; y < mapHeight; y++) {
			mapWidth = Math.max(mapWidth, tileMap[y].length);
		}
	}
	
	private void generateCollisionMap() {
		boolean[][] map = new boolean[getMapHeight()][getMapWidth()];
		
		Block[][] blockMap = getBlockMap();
		int tileSize = Constants.Game.TILE_SIZE;
		
		Rectangle r;
		for (int blockY = 0; blockY < blockMap.length; blockY++) {
			for (int blockX = 0; blockX < blockMap[blockY].length; blockX++) {
				if (!blockMap[blockY][blockX].hasCollision()) {continue;}
				r = blockMap[blockY][blockX].getCollider().getBoundingBox();
				int x1 = Math.floorDiv(r.x, tileSize) + blockX;
				int y1 = Math.floorDiv(r.y, tileSize) + blockY;
				int x2 = Math.floorDiv(r.x + r.width, tileSize) + blockX;
				int y2 = Math.floorDiv(r.y + r.height, tileSize) + blockY;
				
				for (int y = y1; y <= y2; y++) {
					for (int x = x1; x <= x2; x++) {
						map[y][x] = true;
					}
				}
				
			}
		}
		
		this.collisionMap = Optional.of(map);
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}
	
	public Tile[][] getTileMap() {return tileMap.get();}
	
	public Block getBlock(int blockX, int blockY) {
		try {
			return getBlockMap()[blockY][blockX];	
		}
		catch (IndexOutOfBoundsException e) {
			if (blockX < 0 || blockX >= mapWidth || blockY < 0 || blockY >= mapHeight) {
				return (Block) new Block().setCollidesWithScreen(true);
			} else {
				return new Block();
			}
		}
	}
	
	public boolean locationHasCollision(int blockX, int blockY) {
		try {
			return getCollisionMap()[blockY][blockX];
	
		}
		catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
		
	public Block[][] getBlockMap() {return blockMap.get();}
	
	public boolean[][] getCollisionMap() {return collisionMap.get();}
	

	@SuppressWarnings("unchecked")
	public boolean loadMap(String saveName, String mapDirectory, String mapName) {
        try {
            // Reading the object from a file 
        	System.out.println("Reading map data from " + getUserMapPath(saveName).getFile()+ mapDirectory + "\\" + mapName);
        	//Checks if file exists in user save data
        	File testingFile = new File(getUserMapPath(saveName).getFile(), mapDirectory + "\\" + mapName);
        	boolean fileExists = testingFile.exists();
        	InputStream file;
        	//If file exists in user_data, use that file
        	if (fileExists) {file = new FileInputStream(getUserMapPath(saveName) + "/" + mapDirectory + "/" + mapName);}
        	//Otherwise use the default file in resources/maps
        	else {file = new ResourcePath(mapDirectory + "/" + mapName).inDirectory(getResourcesMapPath().getPath()).getInputStream();}
 
        	ObjectInputStream in = new ObjectInputStream(file); 

        	// Method for deserialization of objects
            tileMap = Optional.of((Tile[][]) in.readObject()); 
            System.out.println("tileMap has been deserialized");
            
            blockMap = Optional.of((Block[][]) in.readObject());
            System.out.println("blockMap has been deserialized");
            
            entities = Optional.of((Set<Entity>) in.readObject());
            System.out.println(entities.get().size() + " entities have been deserialized");
            
            calculateMapSize();
            generateCollisionMap();
            
            System.out.println("All data has been deserialized from " + mapDirectory + "/" + mapName);
            in.close(); 
            file.close();  
            
            this.saveName = saveName;
        	this.mapDirectory = mapDirectory;
        	this.mapName = mapName;
        	
        	return true;
        } catch(Exception e) {
        	System.out.println("Error loading map " + mapDirectory + "/" + mapName  + ": " + e.getClass().getName());
        	this.clearMapState();
        	return false;
        }
	}

	public boolean saveMap() {
		savePlayer(saveName);
	    try
	    {    
	        //Saving of object in a file 
	    	System.out.println("Writing map data to " + mapDirectory + "/" + mapName);
	    	
	    	File directoryFile = new File(getUserMapPath(saveName).getFile(), mapDirectory);
	    	directoryFile.mkdirs();
	    	File file = new File(directoryFile, mapName);
	    	
	        FileOutputStream fileStream = new FileOutputStream(file,false); 
	        ObjectOutputStream out = new ObjectOutputStream(fileStream); 
	          
	        // Method for serialization of object 
	        out.writeObject(tileMap.get()); 
	        System.out.println("tileMap has been serialized");
            

            out.writeObject(blockMap.get());
            System.out.println("blockMap has been serialized");

            out.writeObject(entities.get());
            System.out.println(entities.get().size() + " entities have been serialized");
	        
	        out.close(); 
	        fileStream.close(); 
	          
	        System.out.println("All data has been serialized to " + mapDirectory + "/" + mapName);
	        
	        return true;
	    } catch(Exception e) {
        	System.out.println("Error saving map " + mapDirectory + "/" + mapName  + ": " + e.getClass().getName());
        	return false;
	    }
	}
	
	public static boolean saveTemplateMap(String directory, String name, Tile[][] tiles, Block[][] blocks, Set<Entity> en) {
		final String mapsFolderLocation = "src/main/resources/maps/";
	    try
	    {    
	        //Saving of object in a file 
	    	System.out.println("Writing map data to " + directory + "/" + name);
	    	
	    	File directoryFile = new File(mapsFolderLocation + directory);
	    	directoryFile.mkdirs();
	    	File file = new File(directoryFile, name);
	    	
	    	FileOutputStream fileStream = new FileOutputStream(file,false); 
	        ObjectOutputStream out = new ObjectOutputStream(fileStream); 
	        
	        // Method for serialization of object 
	        out.writeObject(tiles); 
	        System.out.println("tileMap has been serialized");
            
            out.writeObject(blocks);
            System.out.println("blockMap has been serialized");
	        
            out.writeObject(en);
            System.out.println(en.size() + " entities have been serialized");
	        
	        out.close(); 
	        fileStream.close(); 
	          
	        System.out.println("All data has been serialized to " + directory + "/" + name);
	        return true;
	    } catch(Exception e) {
        	System.out.println("Error saving template map " + directory + "/" + name  + ": " + e.getClass().getName());
        	return false;
	    }
	}
	
	public void clearMapState() {
		tileMap = Optional.empty();
		blockMap = Optional.empty();
		entities = Optional.empty();
		
		saveName = "";
		mapDirectory = "";
		mapName = "";
	}
	
	public boolean hasMap() {
		return tileMap.isPresent();
	}

	public Set<Entity> getEntities() {
		return entities.get();
	}
	
	public void addEntity(Entity e) {
		entities.get().add(e);
	}
	
	public boolean loadPlayer(String saveName) {
		try {
			SimpleFilePath path = new WorldDataPath("player.ser", saveName);
			
	    	System.out.println("Reading player data from " + path);
	    	
	    	File file = path.getFile();
	    	
	    	if (file.exists()) {
	    		ObjectInputStream inputStream = new ObjectInputStream(path.getInputStream());
	    		player = Optional.of((Player) inputStream.readObject());
	    	} else {
	    		player = Optional.of(new Player(2290, 2855, false));
	    	}
	    	
	    	System.out.println("Player data deserialized from " + path);
	    	
	    	return true;
		} catch(Exception e) {
        	System.out.println("Error loading player: " + e.getClass().getName());
        	player = Optional.empty();
        	return false;
        }
	}
	
	public boolean savePlayer(String saveName) {
		try {
			SimpleFilePath path = new WorldDataPath("player.ser", saveName);
			
	    	System.out.println("Writing player data to " + path);
	    		    
	    	FileOutputStream fileStream = new FileOutputStream(path.getFile());
	    	ObjectOutputStream out = new ObjectOutputStream(fileStream);
	    	
	    	out.writeObject(player.get());
	    	
	    	out.close();
	    	fileStream.close();
	    	
	    	System.out.println("Player data serialized to " + path);
	    	
	    	return true;
		} catch(Exception e) {
        	System.out.println("Error loading player: " + e.getClass().getName());
        	player = Optional.empty();
        	return false;
        }
	}
	
	public Player getPlayer() {
		return player.get();
	}

	public void update() {
		updateBlocks();
		updateEntities();
		updatePlayer();
	}
	
	public void updateEntities() {
		getEntities().forEach(e -> e.update());
	}
	public void updateBlocks() {
		Arrays.stream(getBlockMap()).forEach(row -> Arrays.stream(row).forEach(block -> block.update()));
	}
	public void updatePlayer() {
		getPlayer().update();
	}
}
