import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
NOTES:
    fix up sliders
    add lables to sliders
    add uses to sliders
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
            world.addPredator(new Predator(200, 200));
            // Setting flockPanel paramaters
            flockPanel.setBounds(0, 0, 400, 400);
            flockPanel.setBackground(Color.blue);
            // Setting sliderPanel paramaters
            sliderPanel.setBounds(0, 400, 400, 200);
            sliderPanel.setBackground(Color.gray);

            for (int i = 0; i < 100; i++) {
                world.addBird(new Bird(Math.random() * 400, Math.random() * 400));
            }

            
            
            // Add panels to Frame and set Frame layout
            frame.add(flockPanel);
            frame.add(sliderPanel);
            

            // Set frame layout.
            frame.setLayout(null);

            //Set frame size and make it visible.
            frame.setVisible(true);
            frame.setSize(400, 600);

            //Add Slider to sliderPanel
            sliderPanel.add(separation);
            //separation.bounds();
            sliderPanel.add(alignment);
            sliderPanel.add(piller);
            //sliderPanel.setLayout(new GridLayout(3, 1, 5, 5));

            frame.addComponentListener(new ComponentAdapter() {
                @Override 
                public void componentResized(ComponentEvent e) {
                    Dimension d = e.getComponent().getSize();
                    flockPanel.setBounds(0, 0, d.width, d.height * 2/3);
                    sliderPanel.setBounds(0, 400, d.width, d.height / 3);
                }
            });

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
