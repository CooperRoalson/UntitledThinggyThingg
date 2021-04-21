package untitled_thinggy_thingg.core.drawing.sprites;

import java.util.List;

import untitled_thinggy_thingg.core.Constants;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.world.blocks.Block;
import untitled_thinggy_thingg.world.entities.Entity;

/**
 * A subclass of {@link TextureSprite} which supports multiple frames looping at a given speed.
 *
 */
public class AnimationSprite extends TextureSprite {	
	private static final long serialVersionUID = 1L;
	
	private transient double frameCounter = 0;
	private List<FilePath> textures;
	private int fps;
	private transient int animationFrame = 0;
	
	public AnimationSprite(List<FilePath> textures, int fps) {
		super(textures.get(0));
		this.textures = textures;
		this.fps = fps;
	}
	
	/**
	 * Returns the number of frames of animation that pass per game update (using {@link AnimationSprite#getCurrentFPS()}). Cannot be more than 1.
	 * 
	 * @return How many frames occur per game update 
	 */
	public double getCurrentFramesPerUpdate() {
		return Math.min((double) fps/Constants.Window.TARGET_FPS, 1.0);
	}
	
	/**
	 * Resets the animation to its first frame.
	 */
	public void resetFrameCounter() {
		frameCounter = 0;
	}
	
	/**
	 * Same as {@link AnimationSprite#updateAnimation()}. Used for compatibility with {@link Block}s and {@link Entity}s.
	 */
	public void update() {
		this.updateAnimation();
	}
	
	public void resetAnimationFrame() {
		this.animationFrame = 0;
	}
	
	public void setTextures(List<FilePath> textures) {
		this.textures = textures;
		
		this.resetAnimationFrame();
		this.resetFrameCounter();
		this.setTexture(textures.get(0));
	}
	
	public int getFps() {
		return fps;
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public void onAnimationComplete() {}
	
	private void nextFrame() {
		animationFrame ++;
		animationFrame %= textures.size();
		
		this.setTexture(textures.get(animationFrame));
		
		if (animationFrame == 0) {onAnimationComplete();}
	}
	
	/**
	 * Updates the animation.
	 */
	public void updateAnimation() {
		frameCounter += getCurrentFramesPerUpdate();
		if (frameCounter >= 1) {
			nextFrame();
			frameCounter --;
		}
	}
}
