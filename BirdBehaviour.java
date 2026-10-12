import java.util.*;

public final class BirdBehaviour {
    public static class Separation implements Behaviour<Bird> {
        public /*Maybe make vector class*/ void  calculate(Bird self, World world) {
            List<Bird> neighbors = world.getEntitiesInRange(world.getBirds(), self, 25.0);
            double forceX = 0, forceY = 0;

            for (Bird other : neighbors) {
                double dist = self.distanceTo(other);
                if (dist > 0) {
                    forceX += (self.getX() - other.getX()) / dist; 
                    forceY += (self.getY() - other.getY()) / dist;
                }
            }
            self.applyForce(forceX * 1.5, forceY * 1.5);
        }
    }
    public static class Alignment implements Behaviour<Bird> {
        public void calculate(Bird self, World world) {
            List<Bird> neighbors = world.getEntitiesInRange(world.getBirds(), self, 50.0);
            if (neighbors.isEmpty()) return;
 
            double avgDx = neighbors.stream().mapToDouble(Bird::getVelocityX).average().orElse(0);
            double avgDy = neighbors.stream().mapToDouble(Bird::getVelocityY).average().orElse(0);
 
            self.applyForce((avgDx - self.getVelocityX()) * 0.05, (avgDy - self.getVelocityY()) * 0.05);
        }
    }
    public static class Cohesion implements Behaviour<Bird> {
        public void calculate(Bird self, World world) {
            List<Bird> neighbors = world.getEntitiesInRange(world.getBirds(), self, 50);
            if (neighbors.isEmpty()) return;

            double centerX = neighbors.stream().mapToDouble(Bird::getX).average().orElse(0);
            double centerY = neighbors.stream().mapToDouble(Bird::getY).average().orElse(0);

            self.applyForce((centerX - self.getX()) * 0.01, (centerY - self.getY()) * 0.01);
        }
    } 
}

