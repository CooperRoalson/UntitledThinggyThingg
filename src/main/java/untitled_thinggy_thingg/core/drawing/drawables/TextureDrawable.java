package untitled_thinggy_thingg.core.drawing.drawables;

import java.awt.Graphics;
import java.awt.Image;

/**
 * An implementation of {@link Drawable} that draws an {@link Image} to the screen.
 * @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int, java.awt.image.ImageObserver)
 */

public class TextureDrawable implements Drawable {
    private Image texture;
    
    private int x, y;
    private double scaleX;
    private double scaleY;
    
    /**
     * @param tex The {@code Image} to draw
     * @param x The x-coordinate of the top-left corner of the image
     * @param y The y-coordinate of the top-left corner of the image
     * @param scaleX The x scale of the image
     * @param scaleY The y scale of the image
     */
    public TextureDrawable(Image tex, int x, int y, double scaleX, double scaleY) {
        this.texture = tex;
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    } 
    
    /**
     * Same as {@link TextureDrawable#TextureDrawable(Image, int, int, double, double)}, but the scale values default to 1
     * @param tex The {@code Image} to draw
     * @param x The x-coordinate of the top-left corner of the image
     * @param y The y-coordinate of the top-left corner of the image

     */
    public TextureDrawable(Image tex, int x, int y) {
    	this(tex, x, y, 1, 1);
    }
    
    /**
	 * {@inheritDoc}
	 */
    public void render(Graphics g) {
    	int w = texture.getWidth(null);
    	int h = texture.getHeight(null);
    	
    	g.drawImage(texture, x, y, x + (int) (w * scaleX), y + (int) (h * scaleY), 0, 0, w, h, null);
    	
    }
}