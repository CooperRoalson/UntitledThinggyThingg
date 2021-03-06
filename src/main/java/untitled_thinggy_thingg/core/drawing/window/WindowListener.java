package untitled_thinggy_thingg.core.drawing.window;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import untitled_thinggy_thingg.core.EventHandler;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.window.WindowManager.GamePanel;

/**
 * A class used to catalogue the {@link MouseEvent}s generated by the {@link GamePanel}. The events are passed to the {@link EventHandler} to be dispatched.
 */
public class WindowListener implements ComponentListener, MouseListener, MouseMotionListener {

	@Override
	public void mouseClicked(MouseEvent e) {handle(e);}

	@Override
	public void mousePressed(MouseEvent e) {handle(e);}

	@Override
	public void mouseReleased(MouseEvent e) {handle(e);}

	@Override
	public void mouseEntered(MouseEvent e) {handle(e);}

	@Override
	public void mouseExited(MouseEvent e) {handle(e);}

	
	@Override
	public void mouseMoved(MouseEvent e) {handle(e);}
	
	@Override
	public void mouseDragged(MouseEvent e) {handle(e);}

	@Override
	public void componentResized(ComponentEvent e) {handle(e);}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}
	
	/**
	 * Passes {@code MouseEvent}s to the {@link GameManager}'s {@code EventHandler} to be processed. First it normalizes the coordinates using the window scale
	 * 
	 * @param e The {@code MouseEvent} to handle
	 */
	private void handle(AWTEvent e) {
		if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent) e;
			
			double scale = GameManager.getInstance().getWindowManager().getScale();
			
			int newX = (int) (me.getX() / scale);
			int newY = (int) (me.getY() / scale);
			
			e = new MouseEvent((Component) me.getSource(), me.getID(), me.getWhen(), me.getModifiers(), newX, newY, me.getXOnScreen(), me.getYOnScreen(), me.getClickCount(), me.isPopupTrigger(), me.getButton());
		}
		GameManager.getInstance().getEventHandler().add(e);
	}

}
