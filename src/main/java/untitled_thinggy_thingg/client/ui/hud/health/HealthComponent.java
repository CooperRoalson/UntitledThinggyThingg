package untitled_thinggy_thingg.client.ui.hud.health;

import untitled_thinggy_thingg.client.ui.SpriteComponentWrapper;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class HealthComponent extends SpriteComponentWrapper {	
	private int health;
	
	public HealthComponent() {
		this(4);
	}
	
	public HealthComponent(int health) {
		super(new TextureSprite(new ResourcePath("")));
		scaleTo(24, 24);
		
		updateTexture();
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;		
		
		updateTexture();
	}
	
	public void updateTexture() {
		((TextureSprite) inner).setTexture(new ResourcePath("ui/hud/heart/Heart" + this.health+".png"));
	}
	
	public void scaleTo(int width, int height) {
		((TextureSprite) inner).scaleTo(width, height);
	}
	
	public int addHealth(int health) {
		if (this.health+health < 0) {
			int r = Math.abs(this.health+health);
			setHealth(0);
			return r;
		} else if (this.health+health > 4) {
			int r = this.health+health-4;
			setHealth(4);
			return r;
		} else {setHealth(this.health+health); return 0;}
	}
}
