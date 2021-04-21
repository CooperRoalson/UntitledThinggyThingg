package untitled_thinggy_thingg.core.drawing.sprites;

import java.util.List;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.window.WindowManager;

/**
 * An interface for objects that are drawn to the screen. It generates a {@link List} of {@link Drawable}s It has no concept of position.
 */
public interface Sprite {
	/**
	 * Returns a list of {@code Drawable}s, which will be subsequently drawn to the screen by the {@link WindowManager}
	 * 
	 * @param x The x position to draw at
	 * @param y The y position to draw at
	 * @return A {@code List} of {@code Drawable}s
	 */
    public List<Drawable> draw(int x, int y);
}