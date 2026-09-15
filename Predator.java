import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Predator extends MovingEntity {

    private double speed = 1.5;
    private double size = 20.0;

    public Predator(double x, double y) {
        super(x, y);
    }

    public void update(List<? extends Bird> birds) {
        Bird closestBird = findClosestBird(birds);

        if (closestBird != null) {
            
            if (distanceTo(closestBird) < this.size) {
                birds.remove(closestBird);
            } else {

                moveTowards(closestBird);
            }
        }

        x += dx;
        y += dy;
    }

    private Bird findClosestBird(List<? extends Bird> birds) {
        Bird closestBird = null;
        double closestDistance = Double.MAX_VALUE;
        if(birds.isEmpty()) {
            return null;
        }
        for (Bird bird : birds) {
            double distance = distanceTo(bird);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestBird = bird;
            }
        }

        return closestBird;
    }

    private void moveTowards(Bird bird) {
        
        if (bird == null) {
            dx = 0;
            dy = 0;
            return;
        }

        double differenceX = bird.getX() - x;
        double differenceY = bird.getY() - y;

        double distance = Math.sqrt(
                differenceX * differenceX
                + differenceY * differenceY
        );

        if (distance > 0) {
            dx = (differenceX / distance) * speed;
            dy = (differenceY / distance) * speed;
        }
    }
    public double getHeading() {
        return Math.atan2(this.dy, this.dx);
    }

    @Override
    public void render(Graphics2D g2d) {
        double theta = getHeading();
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        double[] px = { size * 0.5, -size * 0.4, -size * 0.1, -size * 0.4 };
        double[] py = { 0,          -size * 0.4,  0,           size * 0.4 };

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        for (int i = 0; i < 4; i++) {
            xPoints[i] = (int) (this.x + (px[i] * cos - py[i] * sin));
            yPoints[i] = (int) (this.y + (px[i] * sin + py[i] * cos));
        }

        g2d.setColor(Color.RED);
        g2d.fillPolygon(xPoints, yPoints, 4);
    }
}
