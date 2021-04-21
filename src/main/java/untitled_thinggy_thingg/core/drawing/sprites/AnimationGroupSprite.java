package untitled_thinggy_thingg.core.drawing.sprites;

import java.util.List;

import untitled_thinggy_thingg.util.files.FilePath;

public class AnimationGroupSprite extends AnimationSprite {
	private static final long serialVersionUID = 1L;
	
	private List<List<FilePath>> animations;
	private int state;

	public AnimationGroupSprite(List<List<FilePath>> textures, int fps) {
		super(textures.get(0), fps);
		
		this.animations = textures;
		this.state = 0;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		if (getState() != state) {
			this.state = state;
			
			this.setTextures(animations.get(state));
		}
	}
}
