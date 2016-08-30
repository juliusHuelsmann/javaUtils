package opaqueFrame;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class OpFrame extends JFrame {
  
  //TODO: einen eigenen gradientPaint schreiben.
    public OpFrame() {
      super();
      super.setUndecorated(true);
      setBackground(new Color(0,0,0,0));
      setLocationRelativeTo(null);
      super.setSize(50, 50);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    final int R = 240;
                    final int G = 240;
                    final int B = 240;

//                    Paint p =
//                        new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0),
//                            0.0f, getHeight(), new Color(R, G, B, 255), true);
//                    Graphics2D g2d = (Graphics2D)g;
//                    g2d.setPaint(p);
//                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(panel);
        setLayout(null);
    }

    public static void main(String[] args) {
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        boolean isPerPixelTranslucencySupported = 
            gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);

        //If translucent windows aren't supported, exit.
        if (!isPerPixelTranslucencySupported) {
            System.out.println(
                "Per-pixel translucency is not supported");
                System.exit(0);
        }

//        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              OpFrame gtw = new
                  OpFrame();

                // Display the window.
                gtw.setVisible(true);
            }
        });
    }
}