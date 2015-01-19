import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DrawOval extends DrawObject {

    int w, h;

    public DrawOval(int lx, int ly, int lw, int lh) {
        median = new Point2D.Double(lx, ly);
        w = lw;
        h = lh;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform af = new AffineTransform();
        af.setToRotation(angle * Math.PI/180, median.getX(), median.getY());
        af.scale(scalex, scaley);
        g2.setTransform(af);

        Ellipse2D.Float ellipse = new Ellipse2D.Float((float)median.getX(), (float)median.getY(), w, h);
        g2.draw(ellipse);
    }

    public boolean contains(int mx, int my) {
        Rectangle2D rect = new Rectangle2D.Double(median.getX(), median.getY(), w, h);
        return rect.contains(mx, my);
    }

	
	public void paintBounds(Graphics g) {
		
		
	}

}