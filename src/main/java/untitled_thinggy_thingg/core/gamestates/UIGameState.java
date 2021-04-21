package untitled_thinggy_thingg.core.gamestates;

import java.awt.AWTEvent;
import java.util.List;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;

import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.world.blocks.Block;

/**
 * An implementation of {@link GameState} that is a {@link UIContainer} for a GUI.
 * Has another {@code GameState} running in the background, and can use different {@link UIPauseMode}s.
 */
public class UIGameState extends UIContainer implements GameState {
	private GameState backgroundGameState;
	private UIPauseMode pauseMode;
	
	/**
	 * 
	 * @param backgroundGameState The {@code GameState} to run in the background
	 * @param pauseMode The {@code UIPauseMode} to use for the {@code backgroundGameState}
	 */
	public UIGameState(GameState backgroundGameState, UIPauseMode pauseMode) {
		this.backgroundGameState = backgroundGameState;
		this.pauseMode = pauseMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Drawable> draw() {
		List<Drawable> drawables = backgroundGameState.draw();
		drawables.addAll(this.draw(0,0));
		return drawables;
	}
	
	/**
	 * {@inheritDoc} Also may update the {@code backgroundGameState}, depending on the {@code UIPauseMode} used.
	 */
	@Override
	public void update() {
		super.update();
		if (pauseMode == UIPauseMode.CONTINUE) {backgroundGameState.update();}
		else if (pauseMode == UIPauseMode.BLOCK_ANIMATIONS_ONLY) {GameManager.getInstance().getMapState().updateBlocks();}
	}
	
	/**
	 * {@inheritDoc} Also may register the {@code backgroundGameState}'s keybindings, depending on the {@code UIPauseMode} used.
	 */
	@Override
	public ComponentInputMap registerKeyBindings(JComponent component) {
		ComponentInputMap map = super.registerKeyBindings(component);
		if (pauseMode == UIPauseMode.CONTINUE) {map.setParent(backgroundGameState.registerKeyBindings(component));}
		return map;
	}
	
	/**
	 * {@inheritDoc} Also registers the {@code backgroundGameState}'s action map.
	 */
	@Override
	public void registerActionMap() {
		backgroundGameState.registerActionMap();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleAWTEvent(AWTEvent e) {
		this.handleAWTEvent(e, 0, 0);
		if (pauseMode == UIPauseMode.CONTINUE) {backgroundGameState.handleAWTEvent(e);}
	}
	
	/**
	 * @return The {@code UIPauseMode} used.
	 */
	public UIPauseMode getPauseMode() {
		return this.pauseMode;
	}
	
	
	/**
	 * An enum representing the ways a {@link UIGameState} can
	 * treat its {@code backgroundGameState}.
	 */
	public enum UIPauseMode {
		/** The {@code backgroundGameState} stops completely. */
		FREEZE,
		/** Like {@link UIPauseMode#FREEZE}, except {@link Block} animations still update. */
		BLOCK_ANIMATIONS_ONLY,
		/** The {@code backgroundGameState} runs normally. */
		CONTINUE;
	}

	
	/**
	 * @return The {@code backgroundGameState}
	 */
	public GameState getBackgroundGameState() {
		return backgroundGameState;
	}
	
}
