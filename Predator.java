import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Predator extends MovingEntity {

    private static final double SPEED = 1.5;
    private static final double SIZE = 20.0;

    public Predator(double x, double y) {
        super(x, y);
    }

    public void update(List<Bird> birds) {
        Objects.requireNonNull(birds, "Bird list cannot be null");

        findClosestBird(birds).ifPresent(closestBird -> {
            if (distanceTo(closestBird) < SIZE) {
                birds.remove(closestBird);
            } else {
                moveTowards(closestBird);
            }
        });

        x += dx;
        y += dy;
    }

    /*
     * Uses a stream to compare every bird by its distance from the predator.
     * Optional represents the case where the list contains no birds.
     */
    private Optional<Bird> findClosestBird(List<Bird> birds) {
        return birds.stream()
                .min(Comparator.comparingDouble(this::distanceTo));
    }

    private void moveTowards(Bird bird) {
        double differenceX = bird.getX() - x;
        double differenceY = bird.getY() - y;

        double distance = Math.sqrt(
                differenceX * differenceX
                + differenceY * differenceY
        );

        if (distance > 0) {
            dx = (differenceX / distance) * SPEED;
            dy = (differenceY / distance) * SPEED;
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

        double[] px = {
                SIZE * 0.5,
                -SIZE * 0.4,
                -SIZE * 0.1,
                -SIZE * 0.4
        };
        double[] py = {
                0,
                -SIZE * 0.4,
                0,
                SIZE * 0.4
        };

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        for (int i = 0; i < 4; i++) {
            xPoints[i] = (int) (
                    this.x + (px[i] * cos - py[i] * sin)
            );
            yPoints[i] = (int) (
                    this.y + (px[i] * sin + py[i] * cos)
            );
        }

        g2d.setColor(Color.RED);
        g2d.fillPolygon(xPoints, yPoints, 4);
    }
}
