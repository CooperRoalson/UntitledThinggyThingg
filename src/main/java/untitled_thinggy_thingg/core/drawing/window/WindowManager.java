package untitled_thinggy_thingg.core.drawing.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import untitled_thinggy_thingg.core.GameManager;
import untitled_thinggy_thingg.core.drawing.drawables.Drawable;
import untitled_thinggy_thingg.core.gamestates.GameState;

/**
 * A class which manages the game window (a {@link JFrame}). The
 * window is constructed using a {@link GamePanel} surrounded by
 * four {@link JLabel}s. These labels are used to enforce a maximum
 * and minimum aspect ratio on the {@code GamePanel}. This class
 * also uses a {@link WindowData} to store data regarding the size
 * and scale of the window.
 */
public class WindowManager {
    private GamePanel gamePanel;
    private JLabel[] borders;
	private JFrame frame;
    
	private WindowData windowData;
	private WindowListener eventListener;
	
	/**
	 * 
	 * @param windowData The window's size information
	 */
    public WindowManager(WindowData windowData) {
    	this.windowData = windowData;
        this.eventListener = new WindowListener();

        this.frame = new JFrame("Untitled Game");
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        this.gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(windowData.width, windowData.height));
        gamePanel.setBackground(Color.DARK_GRAY);
        
        gamePanel.addMouseListener(eventListener);
        gamePanel.addMouseMotionListener(eventListener);
        gamePanel.addComponentListener(eventListener);
        
        frame.add(gamePanel, BorderLayout.CENTER);
        
        borders = new JLabel[4];
        for (int i = 0; i < 4; i++) {
        	JLabel label = new JLabel();
        	label.setOpaque(true);
        	label.setBackground(Color.BLACK);
        	borders[i] = label;
        }
        frame.add(borders[0], BorderLayout.NORTH);
        frame.add(borders[1], BorderLayout.SOUTH);
        frame.add(borders[2], BorderLayout.WEST);
        frame.add(borders[3], BorderLayout.EAST);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    /**
     * Makes the {@code JFrame} visible.
     */
    public void start() {
        frame.setVisible(true);
    }
    
    /**
     * Re-draws the window. This happens asynchronously through the
     * AWT Event Queue.
     * 
     * @see JFrame#repaint()
     */
    public void render() {
        this.frame.repaint();
    }
    
    /**
     * Draws all of the {@link Drawable}s returned by {@link GameState#draw()}.
     * 
     * @param g The {@code Graphics} object recieved from {@link GamePanel#paintComponent(Graphics)}
     */
    private void paintWindow(Graphics g) {

    	if (!frame.isVisible()) {return;}
    	if (GameManager.getInstance().getGameState() == null) {return;}
    	
    	if (getFrameWidth() != this.windowData.width || getFrameHeight() != this.windowData.height) {
    		this.windowData.recalculateSize(getFrameWidth(),getFrameHeight(), borders);
    	}
    	
    	((Graphics2D) g).scale(windowData.scale, windowData.scale);

    	List<Drawable> drawables = new ArrayList<>();
    	drawables.addAll(GameManager.getInstance().getGameState().draw());
    	drawables.forEach(d -> d.render(g));

    }
    
    /**
     * A subclass of {@link JPanel} that calls {@link WindowManager#paintWindow(Graphics)}
     *  Not really necessary, but makes the code nicer.
     */
    @SuppressWarnings("serial")
	protected class GamePanel extends JPanel {
       @Override
        public void paint(Graphics g) {
    	   paintWindow(g);
    	   g.dispose();
        }
    }
    
    /**
     * @return The {@code GamePanel}
     */
    public JPanel getGamePanel() {
    	return gamePanel;
    }
    
    /**
     * @return The {@code JFrame}'s width
     */
    public int getFrameWidth() {
    	return this.frame.getContentPane().getWidth();
    }
    
    /**
     * @return The {@code JFrame}'s height
     */
    public int getFrameHeight() {
    	return this.frame.getContentPane().getHeight();
    }
    
    /**
     * @return The current scale value stored in the {@code WindowData}
     */
    public double getScale() {
    	return this.windowData.scale;
    }
    
    /**
     * @return The "normalized" dimensions of the {@code GamePanel},
     * provided by {@link WindowData#getPanelDimensionsNormalized()}
     */
    public Dimension getDimensionsNormalized() {
    	return this.windowData.getPanelDimensionsNormalized();
    }
    
    /**
     * @return The {@code WindowListener} added to the {@code GamePanel}
     */
    public WindowListener getEventListener() {
    	return eventListener;
    }
}