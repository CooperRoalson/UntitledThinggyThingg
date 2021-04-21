package untitled_thinggy_thingg.core.gamestates;

import java.awt.AWTEvent;
import java.util.List;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.sprites.AnimationSprite;
import untitled_thinggy_thingg.util.files.FilePath;

/**
 * An implementation of {@link GameState} that extends {@link AnimationSprite}, and simply displays an animation.
 * Designed to be used as a background for a {@link UIGameState}.
 */
public class BackgroundAnimationGameState extends AnimationSprite implements GameState {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a {@code BackgroundAnimationGameState} with a list of frames
	 * and a constant FPS.
	 * 
	 * @param textures List of frames
	 * @param fps Animation speed
	 */
	public BackgroundAnimationGameState(List<FilePath> textures, int fps) {
		super(textures, fps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Drawable> draw() {
		return this.draw(0,0);
	}
	
	/**
	 * Does nothing
	 */
	@Override
	public void handleAWTEvent(AWTEvent e) {}

}
