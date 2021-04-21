package untitled_thinggy_thingg.client.ui;

import java.util.List;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.sprites.AnimationSprite;
import untitled_thinggy_thingg.core.drawing.sprites.Sprite;

public class SpriteComponentWrapper implements UIComponent {
	
	protected Sprite inner;
	
	public SpriteComponentWrapper(Sprite inner) {
		this.inner = inner;
	}

	@Override
	public List<Drawable> draw(int xOffset, int yOffset) {
		return inner.draw(xOffset, yOffset);
	}
	
	public Sprite getSprite() {
		return inner;
	}
	
	@Override
	public void update() {
		if (inner instanceof AnimationSprite) {
			((AnimationSprite) inner).update();
		}
	}
}
