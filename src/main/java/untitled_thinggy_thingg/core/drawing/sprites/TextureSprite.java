package untitled_thinggy_thingg.core.drawing.sprites;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import untitled_thinggy_thingg.core.drawing.TextureLoader;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.drawing.drawables.TextureDrawable;
import untitled_thinggy_thingg.util.files.FilePath;

/**
 * An implementation of {@link Sprite} that draws an {@link Image}
 */
public class TextureSprite implements Sprite,Serializable {
	private static final long serialVersionUID = 2L;
	
	private FilePath texturePath;
	
	private int offsetX = 0, offsetY = 0;
	private double scaleX = 1, scaleY = 1;
	
	public TextureSprite(FilePath texturePath) {
		this.texturePath = texturePath;
	}
	
	/** Sets the offset of the texture. That determines where on the {@code TextureSprite} does
	 * the top-left corner of the texture lands.
	 * 
	 * @param anchorX The x-value of the anchor
	 * @param anchorY The y-value of the anchor
	 * 
	 * @return This
	 */
	public TextureSprite setTextureOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		return this;
	}
	
	/**
	 * @return The x offset of the texture
	 */
	public int getTextureOffsetX() {
		return offsetX;
	}
	
	/**
	 * @return The y offset of the texture
	 */
	public int getTextureOffsetY() {
		return offsetY;
	}
	
	public void setTexture(FilePath path) {
		this.texturePath = path;
	}
	
	/** Sets the scale to use for the {@code TextureDrawables} to a given value. Uses one value for both x and y
	 * 
	 * @param s The new scale value
	 */
	public void setScale(double s) {
		this.scaleX = s;
		this.scaleY = s;
	}
	
	/** Sets the scale to use for the {@code TextureDrawables} such that the image drawn is of a certain resolution.
	 * 
	 * @param width The target width of the drawn image
	 * @param height The target height of the drawn image
	 */
	public void scaleTo(int width, int height) {
		if (texturePath.getPath().equals("")) {return;}
		
		this.scaleX = (double) width/getImage().getWidth(null);
		this.scaleY = (double) height/getImage().getHeight(null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Drawable> draw(int x, int y) {
		List<Drawable> drawables = new ArrayList<>();
		
		if (!checkTexture()) {return drawables;}
		
		drawables.add(new TextureDrawable(getImage(), x + offsetX, y + offsetY, scaleX, scaleY));
		
		return drawables;
	}
	
	public Image getImage() {
		return TextureLoader.get(texturePath);
	}
    
    /**
     * @return The texture's width once scaled
     * 
     * @see TextureSprite#getHeight()
     * @see TextureSprite#getTextureWidth()
     */
    public int getWidth() {
		return (int) (getTextureWidth() * scaleX);
    }
    
    /**
     * @return The texture's height once scaled
     * @see TextureSprite#getWidth()
     * @see TextureSprite#getTextureHeight()
     */
    public int getHeight() {
		return (int) (getTextureHeight() * scaleY);
    }
    
    /**
     * @return The texture's width (before scaling)
     * 
     * @see TextureSprite#getTextureHeight()
     * @see TextureSprite#getWidth()
     */
    public int getTextureWidth() {
    	if (!checkTexture()) {return 0;}
		
		return getImage().getWidth(null);
    }
    
    /**
     * @return The texture's height (before scaling)
     * 
     * @see TextureSprite#getTextureWidth()
     * @see TextureSprite#getHeight()
     */
    public int getTextureHeight() {
		if (!checkTexture()) {return 0;}
		
		return getImage().getHeight(null);
    }
    
    private boolean checkTexture() {
    	if (texturePath.getPath().equals("")) {return false;}
		if (getImage() == null) {return false;}
		
		return true;
    }
}
