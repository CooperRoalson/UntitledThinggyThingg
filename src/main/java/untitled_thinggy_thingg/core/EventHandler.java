package untitled_thinggy_thingg.core;

import java.awt.AWTEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

import untitled_thinggy_thingg.client.KeyAction;

/**
 * A class which dispatches {@link AWTEvent}s and excecutes {@link KeyAction}s.
 * It does this by storing them in a {@link ConcurrentLinkedQueue}, then
 * processing them later. An instance is stored in the
 * {@link GameManager}.
 */
public class EventHandler {
	
	private ConcurrentLinkedQueue<KeyAction> keyEventQueue;
	private ConcurrentLinkedQueue<AWTEvent> awtEventQueue;
	
	/**
	 * Create a new {@code EventHandler}
	 */
	public EventHandler() {
		this.keyEventQueue = new ConcurrentLinkedQueue<>();
		this.awtEventQueue = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Adds a {@code KeyAction} to the event queue.
	 * 
	 * @param keyAction The {@code KeyAction} to add
	 */
	public void add(KeyAction keyAction) {
		this.keyEventQueue.add(keyAction);
	}
	
	/**
	 * Adds a {@code AWTEvent} to the event queue.
	 * 
	 * @param e The {@code AWTEvent} to add
	 */
	public void add(AWTEvent e) {
		this.awtEventQueue.add(e);
	}
	
	/**
	 * Processes all events, either by dispatching {@code AWTEvent}s
	 * or executing {@code KeyAction}s.
	 */
	public void processEvents() {
		while (!keyEventQueue.isEmpty()) {
			keyEventQueue.poll().execute();
		}
		while (!awtEventQueue.isEmpty()) {
			GameManager.getInstance().getGameState().handleAWTEvent(awtEventQueue.poll());
		}
	}
	
}
