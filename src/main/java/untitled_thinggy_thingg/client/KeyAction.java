package untitled_thinggy_thingg.client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import untitled_thinggy_thingg.core.GameManager;

@SuppressWarnings("serial")
public abstract class KeyAction extends AbstractAction {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GameManager.getInstance().getEventHandler().add(this);
	}
	
	public abstract void execute();
	
	public static KeyAction from(Runnable r) {
		return new KeyAction() {
			@Override
			public void execute() {
				r.run();
			}
		};
	}

}
