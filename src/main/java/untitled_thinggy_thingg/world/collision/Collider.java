package untitled_thinggy_thingg.world.collision;

import java.awt.Rectangle;
import java.io.Serializable;

public abstract class Collider implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Rectangle boundingBox;
	
	public void loadBoundingBox() {
		this.boundingBox = calculateBoundingBox();
	}
	
	public Rectangle getBoundingBox() {
		if (boundingBox == null) {loadBoundingBox();}
		return new Rectangle(boundingBox);
	}
	
	public Rectangle getBoundingBox(int x, int y) {
		Rectangle r = getBoundingBox();
		r.translate(x, y);
		return r;
	}
	
	protected abstract Rectangle calculateBoundingBox();

	public abstract boolean canCollideWith(Collider other);
	
	public boolean collidesWith(Collider other, int thisX, int thisY, int otherX, int otherY) {
		if (this.canCollideWith(other)) {return this.checkCollide(other, thisX, thisY, otherX, otherY);}
		else if (other.canCollideWith(this)) {return other.checkCollide(this, otherX, otherY, thisX, thisY);}
		else {throw new IllegalArgumentException("Cannot calculate collision with collider of type " + other.getClass().getName());}
	}
	
	public boolean collidesWith(Collider other) {
		return collidesWith(other, 0, 0, 0, 0);
	}
	
	protected abstract boolean checkCollide(Collider other, int thisX, int thisY, int otherX, int otherY);
	
}
