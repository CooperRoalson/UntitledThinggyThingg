package untitled_thinggy_thingg.util.files;

public class UserDataPath extends SimpleFilePath {	
	private static final long serialVersionUID = 1L;

	protected static String userDataPath = "user_data/";
	
	public UserDataPath(String pathName) {
		super(userDataPath, pathName);
	}
}
