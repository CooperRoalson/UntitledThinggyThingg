package untitled_thinggy_thingg.core.drawing.window;

import java.awt.Dimension;

import javax.swing.JLabel;

import untitled_thinggy_thingg.core.drawing.window.WindowManager.GamePanel;

/**
 * A class that stores data used by a {@link WindowManager}, such as the width, height, and scale of the {@link GamePanel}
 */
public class WindowData {
	public int width, height;
	private int panelWidth, panelHeight;
	public double scale;
	public double baseScaleValue;
	private double minAspectRatio;
	private double maxAspectRatio;
	
	/**
	 * @param width The base width of the window
	 * @param height The base height of the window
	 * @param scale The base scale of the window
	 * @param minAspectRatio The minimum allowed aspect ratio
	 * @param maxAspectRatio The maximum allowed aspect ratio
	 */
	public WindowData(int width, int height, double scale, double minAspectRatio, double maxAspectRatio) {
		this.width = width;
        this.height = height;
        this.panelWidth = width;
        this.panelHeight = height;
        this.baseScaleValue = scaleValue()/scale;
        this.scale = scale;
        this.minAspectRatio = minAspectRatio;
        this.maxAspectRatio = maxAspectRatio;
	}
	
	/**
	 * Same as {@link WindowData#WindowData(int, int, double, double, double)}, but with no minimum/maximum aspect ratio.
	 * @param width The base width of the window
	 * @param height The base height of the window
	 * @param scale The base scale of the window
	 */
	public WindowData(int width, int height, double scale) {
		this(width, height, scale, 0, Double.POSITIVE_INFINITY);
	}
	
	/**
	 * Recalculates the scale and {@link JLabel} sizes based on a new width and height.
	 * 
	 * @param width The new width of the {@code GamePanel}
	 * @param height The new height of the {@code GamePanel}
	 * @param borders The {@code JLabel} window borders
	 */
	public void recalculateSize(int width, int height, JLabel[] borders) {
    	this.width = width;
    	this.height = height;
    	this.panelWidth = width;
        this.panelHeight = height;
    	if (aspectRatio() < minAspectRatio) {
    		panelHeight = (int) (width/minAspectRatio);
    		int barHeight = (height - panelHeight)/2;
    		borders[0].setPreferredSize(new Dimension(width, barHeight));
    		borders[1].setPreferredSize(new Dimension(width, barHeight));
    	}
    	else if (aspectRatio() > maxAspectRatio) {
    		panelWidth = (int) (height*maxAspectRatio);
    		int barWidth = (width - panelWidth)/2;
    		borders[2].setPreferredSize(new Dimension(barWidth, height));
    		borders[3].setPreferredSize(new Dimension(barWidth, height));
    	}
    	else {
    		borders[0].setPreferredSize(new Dimension(width, 0));
    		borders[1].setPreferredSize(new Dimension(width, 0));
    		borders[2].setPreferredSize(new Dimension(0, height));
    		borders[3].setPreferredSize(new Dimension(0, height));
    	}
    	this.scale = scaleValue()/baseScaleValue;
		
    	/*System.out.println("Frame size: "+width+"x"+height);
		System.out.println("Panel size: "+panelWidth+"x"+panelHeight);
		System.out.println("Scale: "+scale);*/
    }
	
	/**
	 * A utility function to find the value used for scaling the {@code GamePanel}.
	 * 
	 * @return The scale value
	 */
    public int scaleValue() {
    	return Math.min(panelWidth, panelHeight);
    }
    
    /**
     * @return The current aspect ratio
     */
    public double aspectRatio() {
    	return (double) width/height;
    }
    
    /**
     * @return A "normalized" version of the dimensions, where the width and height are divided by the scale.
     */
	public Dimension getPanelDimensionsNormalized() {
		return new Dimension((int) (panelWidth/scale), (int) (panelHeight/scale));
	}
}
