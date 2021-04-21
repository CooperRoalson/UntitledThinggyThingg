package untitled_thinggy_thingg.core;

import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;

import untitled_thinggy_thingg.client.Controls;
import untitled_thinggy_thingg.client.KeyAction;
import untitled_thinggy_thingg.client.ui.UIComponent;
import untitled_thinggy_thingg.client.ui.hud.HUD;
import untitled_thinggy_thingg.core.drawing.window.WindowData;
import untitled_thinggy_thingg.core.drawing.window.WindowManager;
import untitled_thinggy_thingg.core.gamestates.GameState;
import untitled_thinggy_thingg.core.gamestates.UIGameState;
import untitled_thinggy_thingg.core.gamestates.UIGameState.UIPauseMode;
import untitled_thinggy_thingg.core.gamestates.WorldGameState;
import untitled_thinggy_thingg.world.MapState;

/**
 * The class that manages the entire game. It stores instances of important
 * classes like {@link WindowManager} and {@link MapState}, and keeps
 * track of the current {@link GameState}. It basically holds everything
 * together. There is one static instace of it, which is accessible
 * to everything through the {@link GameManager#getInstance()} method.
 * It implements {@link Runnable}, so it can be started in a new
 * {@link Thread} using {@link Thread#Thread(Runnable)} and {@link Thread#start()}.
 */
public class GameManager implements Runnable {
	
	private static final GameManager instance = new GameManager();
	
    private WindowManager windowManager;
    private EventHandler eventHandler;
    private MapState mapState;
    private GameState gameState;
    private Controls controls;
    
    /**
     * Creates a new {@code GameManager}, along with a {@link WindowManager},
     * an {@link EventHandler}, and a {@link MapState}.
     */
    public GameManager() {
    	WindowData windowData = new WindowData(Constants.Window.BASE_WIDTH, Constants.Window.BASE_HEIGHT, Constants.Window.BASE_SCALE, 0.7, 2.0);
        windowManager = new WindowManager(windowData);
        
        this.eventHandler = new EventHandler();
        
    	mapState = new MapState();
    }
    
    /**
     * @return The static instance of {@code GameManager} accessible to everything.
     */
    public static GameManager getInstance() {
    	return instance;
    }
    
    /**
     * Starts the gameloop. This is typically activated via a
     * {@link Thread}, using {@link Thread#Thread(Runnable)} and
     * {@link Thread#start()}. It first runs {@link GameManager#init()}.
     */
    @Override
    public void run() {
    	init();
    	
        boolean running = true;
        
        long lastCycleTime;
		while (running) {
			update();
        	windowManager.render();
        	lastCycleTime = System.currentTimeMillis();
        	
        	while (System.currentTimeMillis() - lastCycleTime < Constants.Window.MILLIS_BETWEEN_FRAMES) {
        		try {Thread.sleep(1);} catch (InterruptedException e) {}
        		Thread.yield();
        	}
        }
    }
    
    /**
     * This is run at the start of the {@link GameManager#run()} method,
     * and sets up the game. It starts the {@link WindowManager},
     * creates a {@link Controls}, and creates the initial {@link GameState}.
     */
	private void init() {
		
    	controls = Controls.getControls(windowManager.getGamePanel());
    	
        // *---------- Testing ----------*
        if (!mapState.loadPlayer("Null")) {System.exit(1);}
        if (!mapState.loadMap("MyExampleSave", "TestMaps", "Okatbridge.ser")) {System.exit(1);}
        
    	setGameState(new WorldGameState());
    	
    	this.createOrAddUI(new HUD(), 0, 0, UIPauseMode.CONTINUE);    
	/*
    	mapState.addEntity(new BedEntity(26, 20, true));
    	mapState.addEntity(new NPCEntity(Player.createMoveTextures(4), "blank", 5, 5, true,
    			new Polygon(new int[] {3, 3, 15, 15}, new int[] {8, 3, 3, 8}, 4),
    			new ConvexCollider(new Point(5, 5), new Point(5, 0), new Point(0, 0), new Point(0, 5)),
    			new ResourcePath("idleDown/IdleDown.png"),
    			new String[] {"Hello, I am named Blank!", "I am named that because my texture is so sad and undetailed :(", "Bye!"}
    	));*/
    	
    	// *------------------------------
    	
    	windowManager.start();
	}
	
	/**
	 * Adds a {@link UIComponent} to the current gamestate, creating
	 * a new {@link UIGameState} if necessary.
	 * 
	 * @param c The component to add
	 * @param x The x coordinate of the component's top-left corner
	 * @param y The y coordinate of the component's top-left corner
	 * @param pauseMode The {@code UIPauseMode} to use in the {@code UIGameState}
	 */
	public void createOrAddUI(UIComponent c, int x, int y, UIPauseMode pauseMode) {
		if (!(gameState instanceof UIGameState && ((UIGameState) gameState).getPauseMode() == pauseMode)) {
			this.setGameState(new UIGameState(this.gameState, pauseMode));
		}
		
		((UIGameState) gameState).addComponent(c, x, y);
		this.addInputMap(c.registerKeyBindings(windowManager.getGamePanel()));
	}
	
	/**
	 * Removes a {@link UIComponent} from the current gamestate,
	 * deleting the {@link UIGameState} if it becomes empty. Returns
	 * true if the component was found and deleted, false otherwise.
	 * 
	 * @param c The component to remove
	 * @return Whether the removal suceeded
	 */
	public boolean removeUI(UIComponent c) {
		if (gameState instanceof UIGameState) {
			UIGameState ui = (UIGameState) gameState;
			boolean result = ui.removeComponent(c);
			if (ui.isEmpty()) {
				setGameState(ui.getBackgroundGameState());
			}
			return result;
		}
		return false;
	}

	/**
	 * Updates the game. It does this by running {@link EventHandler#processEvents()}
	 * and {@link GameState#update()}
	 */
    private void update() {
		this.eventHandler.processEvents();
		gameState.update();
	}
	
	/**
	 * @return The {@code WindowManager}
	 */
	public WindowManager getWindowManager() {
		return windowManager;
	}
	
	/**
	 * @return The {@code MapState}
	 */
	public MapState getMapState() {
		return mapState;
	}
	
	/**
	 * Sets the current {@link JPanel}'s {@link InputMap}. This
	 * changes the current active keybindings.
	 * 
	 * @param map The new input map
	 * 
	 * @see JPanel#setInputMap(int, InputMap)
	 * @see GameManager#setGameState(GameState)
	 * @see GameState#registerKeyBindings(JComponent)
	 */
	public void setInputMap(ComponentInputMap map) {
		JComponent component = windowManager.getGamePanel();
		component.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, map);
	}
	
	public void addInputMap(ComponentInputMap map) {
		JComponent component = windowManager.getGamePanel();
		InputMap target = map;
		while (target.getParent() != null) {
			target = target.getParent();
		}
		target.setParent(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW));
		component.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, map);
	}
	
	public InputMap getInputMap() {
		JComponent component = windowManager.getGamePanel();
		return component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	/**
	 * Appends to the current {@link JPanel}'s {@link ActionMap}. This
	 * registers new {@link KeyAction}s.
	 * 
	 * @param map The action map to append
	 * 
	 * @see JComponent#setActionMap(ActionMap)
	 * @see ActionMap#setParent(ActionMap)
	 * @see GameManager#setGameState(GameState)
	 * @see GameState#registerActionMap()
	 */
	public void addActionMap(ActionMap map) {
		JComponent component = windowManager.getGamePanel();
		ActionMap target = map;
		while (target.getParent() != null) {
			target = target.getParent();
		}
		target.setParent(component.getActionMap());
		component.setActionMap(map);
	}
	
	/**
	 * Changes to a new {@code GameState}. This calls the {@link GameState#registerKeyBindings(JComponent)}
	 * and {@link GameState#registerActionMap()} methods of the new {@code GameState}.
	 * 
	 * @param gs The new {@link GameState}
	 * 
	 * @see GameManager#setInputMap(ComponentInputMap)
	 * @see GameManager#addActionMap(ActionMap)
	 */
	public void setGameState(GameState gs) {
		this.gameState = gs;
		this.setInputMap(this.gameState.registerKeyBindings(windowManager.getGamePanel()));
		this.gameState.registerActionMap();
	}
	
	/**
	 * @return The current {@code GameState}
	 */
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * @return The current {@code Controls} object
	 */
	public Controls getControls() {
		return controls;
	}
	
	/**
	 * @return The current {@code EventHandler}
	 */
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}
	
}