package untitled_thinggy_thingg.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import untitled_thinggy_thingg.client.KeyAction;
import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.StringDrawable.Alignment;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.world.entities.InteractableEntity;

//A collection of dialog boxes that forms the basis for an interaction
public class Dialog extends UIContainer {
	private InteractableEntity owner;
	private ButtonComponent button;
	private TextBox textbox;
	private SpriteComponentWrapper icon;
	private String[] texts;
	private int dialogLocation = 0;
	
	public Dialog(InteractableEntity owner) {
		this(owner, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public Dialog(InteractableEntity owner, String[] texts) {
		this.owner = owner;
		this.icon = new SpriteComponentWrapper(this.owner.getIcon());
		this.textbox = new TextBox(texts[0], new Font("Arial", Font.PLAIN, 15), Alignment.LEFT, Color.BLACK, 200, 100, ((TextureSprite)icon.getSprite()).getWidth()/2, 0);
		this.texts = texts;
		this.button = new ButtonComponent(200, 100) {
			@Override
			public void onRelease() {
				continueDialog();
			}
		};
				
		this.addComponent(textbox, 0, -50);
		this.addComponent(icon, 0, -50);
		this.addComponent(button, 0, -50);

	}
	
	@Override
	public void registerContainerActionMap() {
		ActionMap actionMap = new ActionMap();
		
		actionMap.put("continue dialog", KeyAction.from(() ->
			continueDialog()
		));
		
		GameManager.getInstance().addActionMap(actionMap);
	}
	
	@Override
	public ComponentInputMap registerContainerKeyBindings(JComponent component) {
		ComponentInputMap inputMap = new ComponentInputMap(component);
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "continue dialog");
		
		return inputMap;
	}
	
	public void continueDialog() {
		if (dialogLocation+1 < texts.length) {
			dialogLocation++;
			textbox.setText(texts[dialogLocation]);
		}
		else {
			endOfDialog();
			dispose();
		}
	}
	
	public void setTextBoxes(String[] texts) {
		this.texts = texts;
	}
	
	public String getText(int i) {
		return texts[i];
	}
	
	public void dispose() {
		GameManager.getInstance().removeUI(this);
		dialogLocation = 0;
		textbox.setText(texts[dialogLocation]);
	}

	

	
	//Optional scripts that can be overwritten if wanted. 
	//For instance, if an entity will only say certain text the first time interracting with them,
	//then the endOfDialog method could change the entity's dialog state.
	private void endOfDialog() {}
}
