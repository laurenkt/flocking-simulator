import flocking.*;
import flocking.behaviours.*;
import javax.swing.*;

/**
 *
 */
public class JavaAssessment {
    // If 60fps is good enough for consoles it's good enough for me.
    private static final int framerate = 60;

    public static void main(String[] args) {
        // GUI Set-up, all fairly self-explanatory. Using GroupLayout as it is reasonably flexible when you know
        // your constraints in advance.
        JFrame frame = new JFrame();
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        frame.setTitle("Flocking Simulator");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Swarm must be specified with the bounds of the area where agents can be spawned
        Swarm swarm = new Swarm();
        swarm.target_population = 100;
        swarm.behaviours.add(new Cohesion());
        swarm.behaviours.add(new Separation());
        swarm.behaviours.add(new Alignment());
        swarm.behaviours.add(new Randomness());
        swarm.behaviours.add(new MouseLove(frame));

        // The scene renders the agents to view
        // The scene UI is where all the parameters are adjusted - it then adjusts the scene object directly
        SceneUI ui = new SceneUI(swarm.scene);

        // Overlap the scene and UI on top of eachother. The UI will be translucent so that the agents can
        // still be seen in the background
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(ui, 200, 200, 200) // Sets a fixed width for the UI
                        .addComponent(swarm.scene));
        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(ui)
                        .addComponent(swarm.scene));

        // We want the simulation and the user view redraw to happen on separate threads. It might be worth
        // looking into SwingWorker to prevent the GUI from locking.
        long framerate_ns = 1000000000/framerate; // Nano-seconds
        Timer simulation = new Timer(1000/framerate, e -> swarm.update(framerate_ns));
        simulation.start();

        Timer redraw = new Timer(1000/framerate, e -> swarm.scene.repaint());
        redraw.start();
    }
}
