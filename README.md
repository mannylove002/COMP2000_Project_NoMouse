# Flock simulation

Run with a JDK installed:

```sh
javac -d bin src/*.java
java -cp bin App
```

Two predators spawn automatically:

- **ChaserPredator (red):** pursues the nearest bird anywhere in the world at 2.5 pixels per update; capture radius 20 pixels.
- **AmbushPredator (orange, larger):** waits until a bird is less than 100 pixels away, then pursues it at 1.5 pixels per update; capture radius 28 pixels. Stops when no birds remain in range.

Both extend the abstract `Predator` class, which shares nearest-prey selection, movement, capture, and rendering. `World` updates both through its existing `List<Predator>`.

Run the deterministic behaviour checks:

```sh
javac -d bin src/*.java tests/*.java
java -Djava.awt.headless=true -cp bin PredatorTest
```
