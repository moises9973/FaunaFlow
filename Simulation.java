import javax.swing.*;
import java.awt.*;

public class Simulation {

    public static void main(String[] args) {
        final Timer[] timer = new Timer[1];

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("Simulation");
        SimulationPanel panel = new SimulationPanel();

        frame.add(panel);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        panel.spawnPrey();

        timer[0] = new Timer(16, e -> {
            panel.update();
            panel.repaint();

            if (panel.allDead) {
                timer[0].stop();
                System.out.print("Simulation finished\n");
                panel.printResults();
                System.exit(0);
            }
        });
        timer[0].start();
    }
}