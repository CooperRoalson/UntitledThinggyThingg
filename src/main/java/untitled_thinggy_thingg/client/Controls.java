package untitled_thinggy_thingg.client;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.util.files.SimpleFilePath;
import untitled_thinggy_thingg.util.files.UserDataPath;

public class Controls {
	
	public static final SimpleFilePath PATH = new UserDataPath("controls.ser");
	
	
	private Map<KeyStroke, String> inputMap;
	private JComponent component;
	
	private static ActionMap actionMap;
	
	
	public static ActionMap getActionMap() {
		return actionMap;
	}
	
	public ComponentInputMap getInputMap() {
		ComponentInputMap result = new ComponentInputMap(component);
		
		for (Entry<KeyStroke,String> e : inputMap.entrySet()) {
			result.put(e.getKey(), e.getValue());
		}
		
		return result;
	}
	
	public Controls(JComponent component) {
		setDefaultInputMap();
		this.component = component;
	}
	
	public KeyStroke getControl(String value) {
		return inputMap.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().get();
	}
	
	public void setDefaultInputMap() {
		this.inputMap = new HashMap<>();
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "start character move up");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "stop character move up");
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "start character move down");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "stop character move down");
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "start character move left");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stop character move left");
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "start character move right");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stop character move right");
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "interact entity");
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0, false), "use item in slot 0");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "use item in slot 1");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "use item in slot 2");
	}
	
	public static void setActionMap() {
		actionMap = new ActionMap();
		
		actionMap.put("start character move up", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveUp(true)
		));
		actionMap.put("stop character move up", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveUp(false)
		));
		actionMap.put("start character move down", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveDown(true)
		));
		actionMap.put("stop character move down", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveDown(false)
		));
		actionMap.put("start character move left", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveLeft(true)
		));
		actionMap.put("stop character move left", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveLeft(false)
		));
		actionMap.put("start character move right", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveRight(true)
		));
		actionMap.put("stop character move right", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().setMoveRight(false)
		));
		actionMap.put("interact entity", KeyAction.from(() ->
			GameManager.getInstance().getMapState().getPlayer().interactEntity()
		));
		
		for (int i = 0; i < 3; i++) {
			final int j = i; // Workaround to this error: "Local variable i defined in an enclosing scope must be final or effectively final"
			actionMap.put("use item in slot " + j, KeyAction.from(() ->
				GameManager.getInstance().getMapState().getPlayer().useItem(j)
			));
		}
	}
	
	
	public static Controls getControls(JComponent component) {
		Controls saved = loadControls();
		if (saved != null) {
			return saved;
		}
		return new Controls(component);
	}
	
	public static Controls loadControls() {
		setActionMap();
		
		try {
	    	System.out.println("Reading controls from " + PATH);
	    	
	    	File file = PATH.getFile();
	    	
	    	if (file.exists()) {
	    		FileInputStream fileStream = new FileInputStream(file);
	    		ObjectInputStream inputStream = new ObjectInputStream(fileStream);
	    		Controls result = (Controls) inputStream.readObject();
		    	System.out.println("Controls deserialized from " + PATH);
		    	
		    	fileStream.close();
		    	inputStream.close();
		    	return result;
	    	} else {
	    		return null;
	    	}
		} catch(Exception e) {
        	System.out.println("Error loading controls: " + e.getClass().getName());
        	return null;
        }
	}
	public boolean saveControls() {
		try {
	    	System.out.println("Saving controls to " + PATH);
	    	
	    	File file = PATH.getFile();
	    	file.getParentFile().mkdirs();
	    	
    		FileOutputStream fileStream = new FileOutputStream(file);
    		ObjectOutputStream outputStream = new ObjectOutputStream(fileStream);
    		outputStream.writeObject(this);
	    	System.out.println("Controls serialized to " + PATH);
	    	
	    	fileStream.close();
	    	outputStream.close();
	    	return true;
		} catch(Exception e) {
        	System.out.println("Error saving controls: " + e.getClass().getName());
        	return false;
        }
	}
}
