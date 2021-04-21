package untitled_thinggy_thingg.client.ui;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.util.files.FilePath;
import untitled_thinggy_thingg.util.files.ResourcePath;

@SuppressWarnings("serial")
public class ButtonComponent extends TextureSprite implements UIComponent {

	private FilePath[] textures;
	private int state; // 0: up   1: highlighted   2: pressed
	
	private Rectangle collisionBox;
	
	public ButtonComponent(Rectangle collisionBox, FilePath[] textures) {
		super(textures[0]);
		this.collisionBox = collisionBox;
		this.textures = textures;
		this.scaleTo(collisionBox.width, collisionBox.height);
	}
	
	public ButtonComponent(int width, int height, FilePath[] textures) {
		this(new Rectangle(width, height), textures);
	}
	
	public ButtonComponent(int width, int height) {
		this(width, height, new FilePath[] {new ResourcePath(""), new ResourcePath(""), new ResourcePath("")});
	}
	
	// Should be overriden
	public void onPress() {}
	public void onRelease() {}
	public void onHighlight() {}
	
	public void setState(int state) {
		if (state == this.state) {return;}
		
		if (state == 2) {
			this.onPress();
		}
		else if (this.state == 2) {
			this.onRelease();
		}
		else if (state == 1) {
			this.onHighlight();
		}
		
		this.state = state;

		this.setTexture(textures[state]);
	}
	
	public int getState() {return state;}
	
	private boolean isInComponent(MouseEvent e, int xOffset, int yOffset) {
		Rectangle collision = new Rectangle(collisionBox);
		collision.translate(xOffset, yOffset);
		return collision.contains(e.getPoint());
	}
	
	@Override
	public void handleAWTEvent(AWTEvent e, int xOffset, int yOffset) {
		if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) e;
			
			boolean inComponent = isInComponent(me, xOffset, yOffset);
			
			if (this.state != 2 && e.getID() == MouseEvent.MOUSE_MOVED) {
				this.setState(inComponent ? 1 : 0);
				return;
			}
			
			
			if (me.getButton() != MouseEvent.BUTTON1) {return;}
			
			if (me.getID() == MouseEvent.MOUSE_RELEASED && this.state == 2) {
				this.setState(inComponent ? 1 : 0);
			}
			
			else if (me.getID() == MouseEvent.MOUSE_PRESSED && inComponent) {
				this.setState(2);
			}
			
			else {
				return;
			}
		}
	}

	public int getWidth() {
		return collisionBox.width;
	}

	public int getHeight() {
		return collisionBox.height;
	}
	
}
