import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
NOTES:
    going to change GridLayout to GridBagLayout
    make flockPanel 2/3 of frame
    add lables to sliders
    add use to sliders
    create bird visule
    create piller visule
    make pretty if time
 */

public class App {
    private final World world = new World(600, 600);
    private final FlockPanel flockPanel = new FlockPanel(world);

    private final Frame frame = new Frame("Flock Simulation");
    private final Panel sliderPanel = new Panel();

    private final JSlider separation = new JSlider();
    private final JSlider alignment = new JSlider();
    private final JSlider piller = new JSlider();


        public App() {
            // Create the predator when the simulation starts.
            try {
                world.addPredator(new Predator(200, 200));
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to add predator: " + e.getMessage());
            }
            // Setting flockPanel paramaters
            //flockPanel.setBounds(0, 0, 200, 200);
            flockPanel.setBackground(Color.gray);
            // Setting sliderPanel paramaters
            //sliderPanel.setBounds(0, 400, 200, 200);
            sliderPanel.setBackground(Color.blue);

            for (int i = 0; i < 600; i++) {
                try {
                    world.addBird(new Bird(Math.random() * (int)world.width, Math.random() * (int)world.height));
                } catch (IllegalArgumentException e) {
                    
                    System.err.println("Failed to add bird " + i + ": " + e.getMessage());
                } catch (CapacityExceededException e) {
                    
                    System.err.println("Stopped adding birds: " + e.getMessage());
                    break;
                }
            }

            
            
            // Add panels to Frame and set Frame layout
            frame.add(flockPanel);
            frame.add(sliderPanel);
            

            // Set frame layout.
            frame.setLayout(new GridLayout(2, 1, 10, 10));

            //Set frame size and make it visible.
            frame.setVisible(true);
            frame.setSize((int)world.width, (int)world.height);

            //Add Slider to sliderPanel
            sliderPanel.add(separation);
            sliderPanel.add(alignment);
            sliderPanel.add(piller);
            sliderPanel.setLayout(new GridLayout(3, 1, 5, 5));

                // If window closes, exit the program.
            frame.addWindowListener(
                new WindowAdapter() {
                    @Override 
                    public void
                    windowClosing(WindowEvent we) {
                        System.exit(0);
                    }
                }
            );
            Timer timer = new Timer(16, e -> {
                world.update();
                flockPanel.repaint();
            });
            timer.start();
    }
    //for testing purposess will be moved later
    public static void main(String[] args) {
        new App();
    }
}
