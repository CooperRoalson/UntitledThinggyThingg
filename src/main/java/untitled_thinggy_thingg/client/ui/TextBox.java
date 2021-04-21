package untitled_thinggy_thingg.client.ui;

import java.awt.Color;
import java.awt.Font;

import untitled_thinggy_thingg.core.drawing.drawables.StringDrawable.Alignment;
import untitled_thinggy_thingg.core.drawing.sprites.TextureSprite;
import untitled_thinggy_thingg.util.files.ResourcePath;

public class TextBox extends UIContainer {

	private StringComponent textComponent;
	private SpriteComponentWrapper textureComponent;
	private Font font;
	private Alignment alignment;
	private Color color;

	private int width, height, xBuffer, yBuffer;
	
	public TextBox(String text, Font font, Alignment alignment, Color color, int width, int height, int xOffset, int yOffset) {
		this.font = font;
		this.color = color;
		this.width = width;
		this.alignment = alignment;
		this.height = height;
		this.xBuffer = width/10;
		this.yBuffer = height/4;
		
		TextureSprite sprite = new TextureSprite(new ResourcePath("ui/textbox/TextBox.png"));
		sprite.scaleTo(width, height);
		
		this.textureComponent = new SpriteComponentWrapper(sprite);
		
		this.textComponent = new StringComponent(text, font, width-xBuffer*2-xOffset, alignment, color);
	
		this.addComponent(this.textureComponent);
		this.addComponent(this.textComponent, xBuffer+xOffset, yBuffer+yOffset);
	}

	public void setText(String text) {
		this.textComponent.setText(text);
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public Font getFont() {
		return font;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public Color getColor() {
		return color;
	}
}
