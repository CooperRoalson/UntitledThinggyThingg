package untitled_thinggy_thingg.client.ui;

import java.awt.AWTEvent;
import java.util.List;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.sprites.Sprite;

public interface UIComponent extends Sprite {
	
	// Rename parameters
	@Override
	public List<Drawable> draw(int xOffset, int yOffset);
	
	// Optional
	public default void handleAWTEvent(AWTEvent e, int xOffset, int yOffset) {}
	
	public default void update() {}
	
	public default ComponentInputMap registerKeyBindings(JComponent component) {return new ComponentInputMap(component);}
	public default void registerActionMap() {}
	
}