package untitled_thinggy_thingg.core.gamestates;

import java.awt.AWTEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;

import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.window.WindowManager;

/**
 * An interface which represents the current state of the game.
 * This interface handles the drawing of the screen, event handling,
 * and game updates. The {@link GameManager} stores the current {@code GameState}
 */
public interface GameState {
	
	/**
	 * Compiles a list of {@link Drawable}s, which will be drawn to the screen by the {@link WindowManager}.
	 * 
	 * @return A {@code List} of {@code Drawable}s 
	 */
	public List<Drawable> draw();
	
	/**
	 * Registers/activates the current keybindings by adding them to the {@link JPanel}'s {@link InputMap}.
	 * 
	 * @param component The {@code GamePanel}
	 * @return An input map with all the key mappings
	 */
	public default ComponentInputMap registerKeyBindings(JComponent component) {return new ComponentInputMap(component);}
	/**
	 * Registers the {@link Action}s associated with this {@code GameState}.
	 * This should only occur once per class (you should probably use a
	 * {@code static boolean} field to keep track of this).
	 */
	public default void registerActionMap() {}
	
	/**
	 * Updates the game.
	 */
	public void update();
	
	/**
	 * Dispatches {@code AWTEvent}s
	 * 
	 * @param e The {@code AWTEvent} to handle
	 */
	public void handleAWTEvent(AWTEvent e);
}
