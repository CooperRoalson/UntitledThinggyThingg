package untitled_thinggy_thingg.core.drawing.drawables;


import java.awt.Graphics;

import untitled_thinggy_thingg.core.drawing.window.WindowManager;

/**
 * An interface for an object which can be directly draw onto the screen using the {@link Drawable#render(Graphics)} method
 *
 */
public interface Drawable {
	
	/**
	 * Draws the Drawable to the screen. Called by the {@link WindowManager}.
	 * 
	 * @param g The current {@link Graphics} object supplied by the {@code WindowManager}
	 * 
	 */
    public void render(Graphics g);
}