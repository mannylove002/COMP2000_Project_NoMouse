import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Predator extends MovingEntity {

    private static final double SPEED = 1.5;
    private static final double CATCH_RADIUS = 20.0;
    private static final double DRAW_SIZE = 30.0;

    public Predator(double x, double y) {
        super(x, y);
    }

    public void update(List<Bird> birds) {
        Objects.requireNonNull(birds, "Bird list cannot be null");

        findClosestBird(birds).ifPresent(closestBird -> {
            if (distanceTo(closestBird) < CATCH_RADIUS) {
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
        Graphics2D predatorGraphics = (Graphics2D) g2d.create();

        try {
            predatorGraphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            predatorGraphics.translate(x, y);
            predatorGraphics.rotate(getHeading());
            predatorGraphics.setStroke(new BasicStroke(1.2f));

            drawMane(predatorGraphics);
            drawHead(predatorGraphics);
            drawFace(predatorGraphics);
        } finally {
            predatorGraphics.dispose();
        }
    }

    private void drawMane(Graphics2D g2d) {
        Path2D mane = new Path2D.Double();
        int points = 24;

        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 * i) / points;
            double radius = i % 2 == 0
                    ? DRAW_SIZE * 0.58
                    : DRAW_SIZE * 0.43;

            double pointX = Math.cos(angle) * radius - DRAW_SIZE * 0.12;
            double pointY = Math.sin(angle) * radius;

            if (i == 0) {
                mane.moveTo(pointX, pointY);
            } else {
                mane.lineTo(pointX, pointY);
            }
        }

        mane.closePath();

        g2d.setColor(new Color(190, 25, 25));
        g2d.fill(mane);
        g2d.setColor(new Color(115, 15, 20));
        g2d.draw(mane);
    }

    private void drawHead(Graphics2D g2d) {
        Ellipse2D head = new Ellipse2D.Double(
                -DRAW_SIZE * 0.30,
                -DRAW_SIZE * 0.31,
                DRAW_SIZE * 0.62,
                DRAW_SIZE * 0.62
        );

        Path2D ear = new Path2D.Double();
        ear.moveTo(-DRAW_SIZE * 0.16, -DRAW_SIZE * 0.22);
        ear.lineTo(-DRAW_SIZE * 0.23, -DRAW_SIZE * 0.48);
        ear.lineTo(DRAW_SIZE * 0.02, -DRAW_SIZE * 0.30);
        ear.closePath();

        g2d.setColor(new Color(230, 105, 35));
        g2d.fill(ear);
        g2d.fill(head);

        g2d.setColor(new Color(125, 35, 20));
        g2d.draw(ear);
        g2d.draw(head);
    }

    private void drawFace(Graphics2D g2d) {
        Ellipse2D muzzle = new Ellipse2D.Double(
                DRAW_SIZE * 0.05,
                -DRAW_SIZE * 0.03,
                DRAW_SIZE * 0.39,
                DRAW_SIZE * 0.27
        );

        g2d.setColor(new Color(250, 175, 85));
        g2d.fill(muzzle);
        g2d.setColor(new Color(125, 35, 20));
        g2d.draw(muzzle);

        g2d.setColor(Color.BLACK);
        g2d.fill(new Ellipse2D.Double(
                DRAW_SIZE * 0.08,
                -DRAW_SIZE * 0.16,
                DRAW_SIZE * 0.07,
                DRAW_SIZE * 0.07
        ));

        Path2D nose = new Path2D.Double();
        nose.moveTo(DRAW_SIZE * 0.39, DRAW_SIZE * 0.01);
        nose.lineTo(DRAW_SIZE * 0.50, DRAW_SIZE * 0.06);
        nose.lineTo(DRAW_SIZE * 0.39, DRAW_SIZE * 0.11);
        nose.closePath();
        g2d.fill(nose);

        g2d.draw(new Line2D.Double(
                DRAW_SIZE * 0.35,
                DRAW_SIZE * 0.13,
                DRAW_SIZE * 0.18,
                DRAW_SIZE * 0.20
        ));
    }
}
