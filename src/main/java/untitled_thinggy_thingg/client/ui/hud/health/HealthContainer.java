package untitled_thinggy_thingg.client.ui.hud.health;

import java.util.ArrayList;

import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;

public class HealthContainer extends UIContainer {

	private ArrayList<HealthComponent> hearts = new ArrayList<>();
	
	public HealthContainer() {
		this(3);
	}
	
	public HealthContainer(int hearts) {
		for (int i = 0; i < hearts; i++) {
			HealthComponent heart = new HealthComponent(4);
			this.hearts.add(heart);
			this.addComponent(heart);
		}
		
		updateLocations();
	}
	
	public void updateLocations() {
		for (int i = 0; i < hearts.size(); i++) {
			HealthComponent heart = hearts.get(i);
			this.moveComponent(heart, (((TextureSprite) heart.getSprite()).getWidth()+3) * i, 0);
		}
	}
	
	public void addHeart() {
		HealthComponent heart = new HealthComponent(4);
		this.hearts.add(heart);
		this.addComponent(heart);
		setHealth(hearts.size()*4);
		updateLocations();
	}
	
	public void addHealth(int health) {
		int r;
		boolean negative = Math.abs(health) != health; 
		int x = negative ? hearts.size()-1 : 0;
		
		do {
			if (x < 0) {
				GameManager.getInstance().getMapState().getPlayer().die();
				break;
			} else if  (x > hearts.size()-1) {
				break;
			} else {r = hearts.get(x).addHealth(health);x += negative ? -1 : 1;}
		} while (r > 0);
	}
	
	public int getHealth() {
		int r = 0;
		for (HealthComponent heart : hearts) {
			r += heart.getHealth();
		}
		
		return r;
	}
	
	public void setHealth(int health) {
		int h = health;
		int x = 0;
		
		do {
			hearts.get(x++).setHealth(h > 4 ? 4 : h);
			h -= 4;
		} while (h > 0);
	}
}