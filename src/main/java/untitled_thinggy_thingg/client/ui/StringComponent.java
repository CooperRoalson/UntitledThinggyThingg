package untitled_thinggy_thingg.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.drawables.StringDrawable;
import untitled_thinggy_thingg.core.drawing.drawables.StringDrawable.Alignment;

public class StringComponent implements UIComponent {
		
	private String string;
	private Font font;
	private Color textColor, backgroundColor;
	private int wrap;
	private Alignment alignment;
	
	public StringComponent(String str, Font font, int wrap, Alignment alignment, Color textColor, Color textHighlight) {
		this.string = str;
		this.font = font;
		this.textColor = textColor;
		this.backgroundColor = textHighlight;
		this.wrap = wrap;
		this.alignment = alignment;
	}
	
	public StringComponent(String str, Font font, int wrap, Alignment alignment, Color textColor) {
		this(str, font, wrap, alignment, textColor, null);
	}
	
	public StringComponent(String str, Font font, Alignment alignment, Color textColor) {
		this(str, font, -1, alignment, textColor, null);
	}
	
	public StringComponent(String str, Font font) {
		this(str, font, -1, Alignment.LEFT, Color.BLACK, null);
	}
	
	public void setWrap(int wrap) {
		this.wrap = wrap;
	}
	
	public void setText(String text) {
		this.string = text;
	}
	
	@Override
	public List<Drawable> draw(int xOffset, int yOffset) {
		return Arrays.<Drawable>asList(new StringDrawable(string, font, wrap, alignment, textColor, backgroundColor, xOffset, yOffset));
	}
	
	

}
