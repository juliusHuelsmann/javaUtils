package opaqueFrame;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import movelistener.control.Movelistener;
import utils.Utils;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class OpFrame extends JFrame {
  
  public OpFrame() {
    this(true);
  }
  public OpFrame(final boolean addMoveListener) {
      super();

      if (addMoveListener) {
    Movelistener ml = new Movelistener(this);
    super.addMouseMotionListener(ml);
    super.addMouseListener(ml);
      }
      super.setTitle("hier");
      super.setUndecorated(true);
      setBackground(new Color(0,0,0,0));
      super.setSize(350, 450);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    final int R = 240;
                    final int G = 240;
                    final int B = 240;

                    Paint p =
                        new GradientPaint(0.0f, 0.0f, new Color(R, G, B, 0),
                            0.0f, getHeight(), new Color(R, G, B, 0), true);
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setPaint(p);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(panel);
        setLayout(null);
        
        setSize(500, 500);
        boolean test = false;
        if (test) {
          BufferedImage bi = new BufferedImage(50, 10, BufferedImage.TYPE_INT_ARGB);
          for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
              bi.setRGB(i, j, new Color(0, 0, 0, 0).getRGB());
            }
          }
          for (int j = 0; j < bi.getHeight(); j++) {
            bi.setRGB(j, j, new Color(50, 50, 50, 150).getRGB());
          }
          JLabel jbtn = new JLabel();
          jbtn.setSize(getSize());
          jbtn.setBorder(null);
          jbtn.setIcon(new ImageIcon(bi));
          jbtn.setOpaque(false);
          super.add(jbtn);
          
          BufferedImage biWindowShape = preprocessBackground("/bgShape.png",
              getSize(), new Rectangle(4, 4, getWidth() - 9, getHeight() - 9), null);

          jbtn.setIcon(new ImageIcon(biWindowShape));
        
        }
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


    
    protected static BufferedImage preprocessBackground(
        final String xpath, final Dimension xdim, final 
        Rectangle xNotProcess, BufferedImage biWindowShapeInverted) {
      
      // load image as RGBA bufferedImage
      BufferedImage processedImg = Utils.resize(xpath, xdim.width, xdim.height);

      
      for (int x = 0; x < processedImg.getWidth(); x++) {
        for (int y = 0; y < processedImg.getHeight(); y++) {

          final Color c = new Color(processedImg.getRGB(x, y));
          if (xNotProcess == null || !(x >= xNotProcess.x && y >= xNotProcess.y 
              && x <= xNotProcess.x + xNotProcess.width 
              && y <= xNotProcess.y + xNotProcess.height)) {
            
            int r = Math.max(0, -80 + c.getRed());
            int g = Math.max(0, -90 + c.getGreen());
            int b = Math.max(0, -120 + c.getBlue());
            final int sum = (r + g + b);
            final int alpha = Math.min(255, Math.max(0, 255 - sum * 255 / (135 + 165 + 175)));
            processedImg.setRGB(x, y, new Color(80, 90, 120, alpha).getRGB());
            processedImg.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB());
          } 
          
          if (biWindowShapeInverted != null) {
            biWindowShapeInverted.setRGB(processedImg.getWidth() - x - 1,
                processedImg.getHeight() - y - 1, processedImg.getRGB(x, y));
          }
        }
      }
      return processedImg;
    }
    

}