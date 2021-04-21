package untitled_thinggy_thingg.core.drawing.drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * An implementation of {@link Drawable} that draws a filled {@link Polygon} to the screen.
 * @see Graphics#fillPolygon(Polygon)
 */

public class PolygonDrawable implements Drawable {

	Polygon polygon;
	Color color;
	
	/**
	 * 
	 * @param polygon The {@code Polygon} to draw
	 * @param color The fill color
	 */
	public PolygonDrawable(Polygon polygon, Color color) {
		this.polygon = polygon;
		this.color = color;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillPolygon(polygon);
	}

}
