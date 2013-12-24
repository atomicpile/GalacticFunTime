import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

public class Sprite implements Shape {

	private Image image;
	private ImageObserver observer;
	private Entity attachedEnt ;
	private Component attachedComponent ;


	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	public Sprite(Image image) {
		this.image = image;
	}

	public void update(long time) {
		// TODO Auto-generated method stub
		// Maybe use this method for sprite animation?
	}

	public void draw(Graphics2D g2D, double interpX, double interpY, double theta) {
		AffineTransform trans = new AffineTransform();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (this.attachedEnt != null)
		{
			trans.rotate(theta, interpX + attachedEnt.getWidth() / 2 , interpY + attachedEnt.getHeight() / 2);
			trans.translate(interpX, interpY);			
		}
		else if (this.attachedComponent != null)
		{
			trans.translate(interpX,  interpY);
		}
		g2D.drawImage(image, trans, observer);
	}

	public double getSpriteWidth() {
		double spriteWidth = image.getWidth(observer);
		return spriteWidth;
	}

	public double getSpriteHeight() {
		double spriteHeight = image.getHeight(observer);
		return spriteHeight;
	}

	@Override
	public boolean contains(Point2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean intersects(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the attachedEnt
	 */
	public Entity getAttachedEnt() {
		return attachedEnt;
	}

	/**
	 * @param attachedEnt the attachedEnt to set
	 */
	public void setAttachedEnt(Entity attachedEnt) {
		this.attachedEnt = attachedEnt;
	}

	public void setAttachedComponent(Component component) {
		this.attachedComponent = component;

	}

}
