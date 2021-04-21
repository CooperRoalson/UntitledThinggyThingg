package untitled_thinggy_thingg.core.drawing.drawables;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link Drawable} that draws a {@link String} to the screen. This class also handles alignment (left, centered, etc.), text wrapping, and newline ({@code "\n"}) characters
 * @see Graphics#drawString(String, int, int)
 */

public class StringDrawable implements Drawable {
	private String string;
	private Font font;
	private Color color;
	private Color background;
	private int x, y;
	private int wrap;
	private Alignment alignment;
	
	/**
	 * 
	 * @param str The {@code String} to draw
	 * @param font The {@link Font} to use
	 * @param wrap The maximum line length before the text wraps
	 * @param alignment An {@link Alignment} representing how to align the text
	 * @param color The text color
	 * @param background The background color
	 * @param x The x-coordinate of the top-left corner of the text
	 * @param y The y-coordinate of the top-left corner of the text
	 */
	public StringDrawable(String str, Font font, int wrap, Alignment alignment, Color color, Color background, int x, int y) {
		this.string = str;
		this.font = font;
		this.color = color;
		this.background = background;
		this.x = x;
		this.y = y;
		this.wrap = wrap;
		this.alignment = alignment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Graphics g) {
		g.setFont(font);
		
		FontMetrics fm = g.getFontMetrics();
				
		if (background != null) {
			Rectangle2D rect = fm.getStringBounds(string, g);
			
			g.setColor(background);
			g.fillRect(x, y, (int) rect.getWidth(), (int) rect.getHeight());
		}
		
		g.setColor(color);
		
		if (wrap >= 0) {
			List<String> lines = lineSplit(fm);
			int xOffset = 0;
						
			for (int l = 0; l < lines.size(); l++) {
				String line = lines.get(l);
				
				if (alignment.isJustified() && l != lines.size()-1) {

					String[] words = line.split(" ");
					int spaceNum = words.length-1;
					int spaceLen = (wrap-(fm.stringWidth(line)-fm.charWidth(' ')*spaceNum))/spaceNum;
					xOffset = 0;
					
					for (String word : words) {
						g.drawString(word, x+xOffset, y+(fm.getHeight()*l));
						xOffset += fm.stringWidth(word) + spaceLen;
					}
				} else {
					xOffset = 0;
					
					if (alignment.getDirection().equals("center")) {
						xOffset = (wrap-fm.stringWidth(line))/2;
					} else if (alignment.getDirection().equals("right")) {
						xOffset = wrap-fm.stringWidth(line);
					}
					
					g.drawString(line, x+xOffset, y+(fm.getHeight()*l));
				}
			}
		} else {
			g.drawString(string, x, y + fm.getAscent());
		}
	}
	
	/** This function splits the text into lines using the wrap
	 * 
	 * @param fm The {@link FontMetrics} for the current {@code Font}, aquired using the {@code Graphics} object.
	 * @return A {@code List} of the lines of text
	 * 
	 */
	private List<String> lineSplit(FontMetrics fm) {
		List<String> lines = new ArrayList<>();
		
		String line = "";
		for (String word : string.split(" ")) {
			if (fm.stringWidth(line) + fm.stringWidth(word) > wrap) {
				try {
					lines.add(line.substring(0, line.length()-1));
				} catch (StringIndexOutOfBoundsException e) {}
				line = "";
			}
			line += word + " ";
		}
		lines.add(line.substring(0, line.length()-1));
		
		
		return lines;
	}
	
	/**
	 * An enum representing a text alignment. These can be either left, center, or right, and can also be justified. Used by the {@link StringDrawable} class.
	 */
	public enum Alignment {
		/**
		 * Left aligned
		 */
		LEFT("left"),
		/** Centered */
		CENTER("center"),
		/** Right aligned */
		RIGHT("right"),
		/** Same as {@link Alignment#JUSTIFIED_LEFT} */
		JUSTIFIED("left", true),
		/** Justified; last line left aligned */
		JUSTIFIED_LEFT("left", true),
		/** Justified; last line centered */
		JUSTIFIED_CENTER("center", true),
		/** Justified; last line right aligned */
		JUSTIFIED_RIGHT("right", true);
		
		private String direction;
		private boolean justified;
		
		/**
		 * @param direction The direction (left, right, centered)
		 * @param justified Whether it is justified
		 */
		private Alignment(String direction, boolean justified) {
			this.direction = direction;
			this.justified = justified;
		}
		
		private Alignment(String direction) {
			this(direction, false);
		}
		
		/**
		 * @return The direction (left, right, centered)
		 */
		public String getDirection() {
			return direction;
		}
		/**
		 * @return Whether it is justified
		 */
		public boolean isJustified() {
			return justified;
		}
	}
}
