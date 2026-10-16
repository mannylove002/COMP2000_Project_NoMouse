import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Comparator;

public abstract class Predator extends MovingEntity {

    private final double speed;
    private final double size;
    private static final BufferedImage SPRITE = loadSprite();

    protected Predator(double x, double y, double speed, double size) {
        super(x, y);
        this.speed = speed;
        this.size = size;
    }

    protected abstract double getDetectionRadius();

    public void update(List<? extends Bird> birds) {
        // Reset velocity so predators stop after a capture or when no prey is in range.
        dx = 0;
        dy = 0;
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
        // Filter prey by detection range, then select the nearest bird.
        return birds.stream()
                .filter(bird -> distanceTo(bird) < getDetectionRadius())
                .min(Comparator.comparingDouble(bird -> distanceTo(bird)))
                .orElse(null);
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

    private static BufferedImage loadSprite() {
        // IDEs can copy assets onto the classpath; plain javac leaves them in src.
        URL resource = Predator.class.getResource("/assets/predator-wolf.png");
        try {
            if (resource == null) {
                Path classes = Path.of(Predator.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
                resource = classes.resolveSibling("src").resolve("assets/predator-wolf.png")
                        .toUri().toURL();
            }
            BufferedImage sprite = ImageIO.read(resource);
            if (sprite == null) throw new IOException("Unsupported predator sprite image");
            return sprite;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load assets/predator-wolf.png", e);
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        // Work on a copy so the predator cannot change the world's drawing state.
        Graphics2D spriteGraphics = (Graphics2D) g2d.create();
        try {
            spriteGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            spriteGraphics.translate(x, y);
            // The source wolf faces left; rotate its snout toward its movement.
            spriteGraphics.rotate(getHeading() - Math.PI);
            int diameter = (int) (size * 2);
            spriteGraphics.drawImage(SPRITE, -diameter / 2, -diameter / 2,
                    diameter, diameter, null);
        } finally {
            spriteGraphics.dispose();
        }
    }
}
