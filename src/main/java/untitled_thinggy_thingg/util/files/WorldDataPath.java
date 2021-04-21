package untitled_thinggy_thingg.util.files;

public class WorldDataPath extends UserDataPath {
	private static final long serialVersionUID = 1L;

	public WorldDataPath(String pathName, String saveName) {
		super(pathName);
		this.setBaseDirectory(userDataPath + "saves/" + saveName + "/");
	}

}
