package untitled_thinggy_thingg.client.ui.hud;

import java.awt.AWTEvent;
import java.awt.event.ComponentEvent;

import untitled_thinggy_thingg.client.ui.UIContainer;
import untitled_thinggy_thingg.client.ui.hud.health.HealthContainer;
import untitled_thinggy_thingg.client.ui.hud.items.ItemSlotContainer;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.world.entities.Player;

public class HUD extends UIContainer {
	
	private HealthContainer healthContainer;
	private ItemSlotContainer itemSlotContainer;
	
	public HUD() {
		Player player = GameManager.getInstance().getMapState().getPlayer();
		
		healthContainer = player.getHeartContainer();
		itemSlotContainer = player.getItemSlotContainer();
		
		this.addComponent(healthContainer, 15, 15);
		this.addComponent(itemSlotContainer);
		
		calculatePlacement();
	}
	
	@Override
	public void handleAWTEvent(AWTEvent e, int xOffset, int yOffset) {
		if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
			calculatePlacement();
			return;
		}
		super.handleAWTEvent(e, xOffset, yOffset);
	}
	
	public void calculatePlacement() {
		int width = (int) GameManager.getInstance().getWindowManager().getDimensionsNormalized().getWidth();
		this.moveComponent(itemSlotContainer, width-120, 15);
	}
}
