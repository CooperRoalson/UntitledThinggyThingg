package untitled_thinggy_thingg.client.ui;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;

public class UIContainer implements UIComponent {
	
	protected ConcurrentNavigableMap<Integer, ChildComponentWrapper> childComponents = new ConcurrentSkipListMap<>();

	public void addComponent(UIComponent component) {
		this.addComponent(component, 0, 0, childComponents.isEmpty() ? 0 : childComponents.lastKey() + 1);
	}
	
	public void addComponent(UIComponent component, int x, int y) {
		this.addComponent(component, x, y, childComponents.isEmpty() ? 0 : childComponents.lastKey() + 1);
	}
	
	public void addComponent(UIComponent component, int x, int y, int zIndex) {
		childComponents.put(zIndex, new ChildComponentWrapper(component, x, y));
		component.registerActionMap();
	}
	
	public boolean removeComponent(UIComponent c) {
		for (ChildComponentWrapper w : childComponents.descendingMap().values()) {
			if (w.inner == c) {
				childComponents.descendingMap().values().remove(w);
				return true;
			} else if (w.inner instanceof UIContainer) {
				if (((UIContainer) w.inner).removeComponent(c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean moveComponent(UIComponent c, int x, int y) {
		for (ChildComponentWrapper w : childComponents.descendingMap().values()) {
			if (w.inner == c) {
				w.x = x;
				w.y = y;
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmpty() {
		return childComponents.isEmpty();
	}

	@Override
	public List<Drawable> draw(int xOffset, int yOffset) {
		List<Drawable> drawables = new ArrayList<>();
		
		drawables.addAll(this.drawContainer(xOffset, yOffset));
		childComponents.values().forEach(component -> drawables.addAll(component.draw(component.getX() + xOffset, component.getY() + yOffset)));
		
		return drawables;
	}
	
	public void forEachChildComponent(Consumer<ChildComponentWrapper> action) {
		childComponents.descendingMap().values().forEach(action);
	}
	
	@Override
	public void handleAWTEvent(AWTEvent e, int xOffset, int yOffset) {
		this.forEachChildComponent(c -> c.handleAWTEvent(e, xOffset + c.x, yOffset + c.y));
	}
	
	@Override
	public void update() {
		this.forEachChildComponent(c -> c.update());
	}
	
	@Override
	public ComponentInputMap registerKeyBindings(JComponent component) {
		ComponentInputMap inputMap = new ComponentInputMap(component);
				
		this.forEachChildComponent(c -> inputMap.setParent(c.registerKeyBindings(component)));
		inputMap.setParent(this.registerContainerKeyBindings(component));
		
		return inputMap;
	}
	
	@Override
	public void registerActionMap() {
		this.registerContainerActionMap();
	}
	
	// Optional methods
	protected List<Drawable> drawContainer(int xOffset, int yOffset) {return new ArrayList<>();}
	public void containerHandleMouseEvent(MouseEvent e) {}
	public void updateContainer() {}
	public ComponentInputMap registerContainerKeyBindings(JComponent component) {return new ComponentInputMap(component);}
	public void registerContainerActionMap() {}
	
	
	private class ChildComponentWrapper implements UIComponent {
		
		private UIComponent inner;
		private int x, y;

		public ChildComponentWrapper(UIComponent inner, int x, int y) {
			this.inner = inner;
			this.x = x;
			this.y = y;
		}

		public int getX() {return x;}
		public int getY() {return y;}

		@Override
		public List<Drawable> draw(int xOffset, int yOffset) {return inner.draw(xOffset, yOffset);}
		@Override
		public void handleAWTEvent(AWTEvent e, int xOffset, int yOffset) {inner.handleAWTEvent(e, xOffset, yOffset);}
		@Override
		public void update() {inner.update();}
		@Override
		public ComponentInputMap registerKeyBindings(JComponent component) {return inner.registerKeyBindings(component);}		

	}

}
