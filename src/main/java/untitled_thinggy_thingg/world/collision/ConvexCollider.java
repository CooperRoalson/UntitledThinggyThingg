package untitled_thinggy_thingg.world.collision;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

public class ConvexCollider extends Collider {
	private static final long serialVersionUID = 1L;

	private Point[] points;
	
	private int crossProductCheck;
	
	public ConvexCollider(Point...points) {
		if (points.length < 3) {
			throw new IllegalArgumentException("Must have at least 3 points!");
		}
		
		this.points = points;
		
		crossProductCheck = crossProductZ(minus(points[1], points[0]), minus(points[2], points[0]));
	}
	
	
	public static ConvexCollider rectFromCorners(int left, int top, int right, int bottom) {
		return new ConvexCollider(new Point[] {new Point(left, top), new Point(right, top), new Point(right, bottom), new Point(left, bottom)});
	}
	
	
	
	
	public Point[] getPoints() {
		return points;
	}
	
	public int numOfPoints() {
		return points.length;
	}
	
	
	private static int crossProductZ(Point p1, Point p2) {
		return p1.x * p2.y - p1.y * p2.x;
	}
	private static Point minus(Point p1, Point p2) {
		return new Point(p1.x-p2.x, p1.y-p2.y);
	}
	
	
	public boolean pointInsideLine(Point p, Point sidePoint1, Point sidePoint2) {
		int cross = crossProductZ(minus(sidePoint2, sidePoint1), minus(p, sidePoint1));
		return cross * crossProductCheck >= 0;
	}
	
	private static boolean checkPartialCollide(ConvexCollider c1, ConvexCollider c2) {
		Point[] pts1 = c1.getPoints();
		Point[] pts2 = c2.getPoints();
				
		boolean flag;
		
		for (int i = 0; i < c1.numOfPoints(); i++) {
			flag = false;
			for (Point p : pts2) {
				if (c1.pointInsideLine(p, pts1[i], pts1[(i+1) % pts1.length])) {
					flag = true;
				}
			}
			if (!flag) {return false;}
		}
			
			return true;
	}
	
	@Override
	protected boolean checkCollide(Collider other, int thisX, int thisY, int otherX, int otherY) {
		if (other instanceof ConvexCollider) {
			ConvexCollider cc = (ConvexCollider) other;
			Arrays.stream(this.getPoints()).forEach(p -> p.translate(thisX, thisY));
			Arrays.stream(cc.getPoints()).forEach(p -> p.translate(otherX, otherY));
			
			boolean result = checkPartialCollide(this, cc) || checkPartialCollide(cc, this);
		
			Arrays.stream(this.getPoints()).forEach(p -> p.translate(-thisX, -thisY));
			Arrays.stream(cc.getPoints()).forEach(p -> p.translate(-otherX, -otherY));
			
			return result;
		}
		
		return false;
	}
	
	@Override
	public boolean canCollideWith(Collider other) {
		return other instanceof ConvexCollider;
	}

	@Override
	protected Rectangle calculateBoundingBox() {
		int minX = Integer.MAX_VALUE,
			maxX = Integer.MIN_VALUE,
			minY = Integer.MAX_VALUE,
			maxY = Integer.MIN_VALUE;
		
		for (Point p : points) {
			minX = Math.min(minX, p.x);
			maxX = Math.max(maxX, p.x);
			minY = Math.min(minY, p.y);
			maxY = Math.max(maxY, p.y);
		}
		return new Rectangle(minX, minY, maxX-minX, maxY-minY);
	}

}
