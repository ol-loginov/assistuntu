package assistuntu;

import javax.swing.*;
import java.awt.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e2) {
            // nu i 4ert s nim
        }
        new App().run();
    }

    private void run() {
        Engine engine = new Engine();

        MainForm frame = new MainForm(engine);
        frame.setTitle("Assistuntu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 480));
        frame.setLocation(100, 100);
        frame.pack();
        frame.setVisible(true);

        engine.setListener(frame);
        engine.load();
    }
}
