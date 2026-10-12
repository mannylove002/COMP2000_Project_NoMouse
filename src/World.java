import java.util.ArrayList;
import java.util.List;
public class World {
    public final double width, height;
    private final int MAX_BIRDS = 500;
    private final List<Bird> birds = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Predator> predators = new ArrayList<>();



    public World(double width, double height) {
        if(width <=0 || height <=0){
            throw new IllegalArgumentException(
                    "World dimensions must be positive"
            );
        }
        this.width = width;
        this.height = height;
    }


    public void addBird(Bird bird) throws CapacityExceededException {
        if (birds.size() >= MAX_BIRDS) {
            throw new CapacityExceededException("World capacity reached. Cannot exceed " + MAX_BIRDS + " birds.");
        }
        if (bird == null) {
            throw new IllegalArgumentException("Cannot add a null Bird to the world.");
        }
        birds.add(bird);
    }
    public void addObstacle(Obstacle obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException("Cannot add a null Obstacle to the world.");
        }
        obstacles.add(obstacle);
    }

    public void addPredator(Predator predator){
        if (predator == null) {
            throw new IllegalArgumentException("Cannot add a null Predator to the world.");
        }
        predators.add(predator);
        
    }
    public void update() {
        for (Predator predator : predators) {
            predator.update(birds);
        }
        for (Bird bird : birds) {
            bird.update(this);
        }
    }
    public <T extends Entity> List<T> getEntitiesInRange(List<T> entities, Entity center, double radius) {
        if (entities == null || center == null) {
        throw new IllegalArgumentException("Entities list and center entity cannot be null.");
        }
        if (radius < 0) {
            throw new IllegalArgumentException("Search radius cannot be negative.");
        }
        List<T> inRange = new ArrayList<>();
        for (T entity : entities) {
            if (entity != center && center.distanceTo(entity) < radius) {
                inRange.add(entity);
            }
        }
        return inRange;
    }

    public List<Bird> getBirds() {
        return birds;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Predator> getPredators(){
        return predators;

    }


} 
