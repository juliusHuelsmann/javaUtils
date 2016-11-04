package tabPnl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import movelistener.control.Movelistener;
import opaqueFrame.OpFrame;
import tabPnl.utils.ViewDictSwitcher;
import utils.Utils;

@SuppressWarnings("serial")
public class TabPanel extends JPanel {
  
  /**
   * The jPanel that receives the content.
   */
  private JPanel jpnlContent;
  
  private JPanel jpnlSwitcher;

  private Vector<ViewDictSwitcher> vecDict;
  private Vector<Container> veccom;

  public TabPanel() {
    
    super();
    super.setLayout(null);
    super.setOpaque(false);
    vecDict = new Vector<ViewDictSwitcher>();
    veccom = new Vector<Container>();
    
    jpnlSwitcher = new JPanel();
    jpnlSwitcher.setOpaque(false);
    jpnlSwitcher.setLayout(null);
    super.add(jpnlSwitcher);
    
    jpnlContent = new JPanel();
    jpnlContent.setOpaque(false);
    jpnlContent.setLayout(null);
    super.add(jpnlContent);
    
  }
  public TabPanel(final boolean xf) {
    super(xf);
    super.setLayout(null);
    vecDict = new Vector<ViewDictSwitcher>();
    veccom = new Vector<Container>();
    
    
    jpnlSwitcher = new JPanel();
    jpnlSwitcher.setOpaque(false);
    jpnlSwitcher.setLayout(null);
    super.add(jpnlSwitcher);
    
    jpnlContent = new JPanel();
    jpnlContent.setOpaque(false);
    jpnlContent.setLayout(null);
    super.add(jpnlContent);
    
  }
  private ActionListener al = new ActionListener() {
    
    public void actionPerformed(ActionEvent e) {

      int c = 0;
      for (ViewDictSwitcher v : vecDict) {
        v.setSelected(false);
        if (v.getActionSource().equals(e.getSource())) {

          setVisibleDictionary(veccom.elementAt(c), c);
//          view.setVisibleDictionary(getDictionary(s.getIndex()), s.getIndex());
          v.setSelected(true);
        }
        c++;
      }
          
    }
  };

  
  public void open(final int o) {

      setVisibleDictionary(veccom.elementAt(o), o);
  }
  
  public ViewDictSwitcher add(final Container c, final Object xtxt, final int i) {
    final int width = 125;
    return add(c, xtxt, i, width);
  }
  
  
  public ViewDictSwitcher add(final Container c, final Object xtxt, final int i,
      final int xwidth) {
    ViewDictSwitcher jbtnAdded = new ViewDictSwitcher(xtxt, i);
    final int height = 30;
    jbtnAdded.setSize(xwidth, height);
    jbtnAdded.setLocation((xwidth + 2) * i, 0);
    jbtnAdded.addActionListener(al);
    vecDict.add(i, jbtnAdded);
    veccom.add(i, c);
    jpnlSwitcher.add(jbtnAdded); 
    c.setSize(jpnlContent.getSize());
    jpnlContent.add(c);
    return jbtnAdded;
  }
  private Thread thrdSwap;
  
  private int visibleIndex;
  private Component visibleComp;
  public synchronized void setVisibleDictionary(final Component c, final int xvisibleIndex) {
    
    final int pev = visibleIndex;
    final Component prevC = visibleComp;
    this.visibleIndex = xvisibleIndex;
    this.visibleComp = c;
    
    if (prevC == null) {
      return;
    }
    if (thrdSwap != null) {
      thrdSwap.interrupt();
      thrdSwap = null;
    }
    
    thrdSwap = new Thread() {
      public void run() {
        final int signum;
        if (pev > xvisibleIndex) {
          signum = -1;
        } else if (pev < xvisibleIndex) {
          signum = 1;
        } else {
          signum = 0;
          return;
        }

        c.setLocation(signum * getWidth(), c.getY());
        prevC.setLocation(0, 0);
        c.setVisible(true);
        prevC.setVisible(true);

        final int steps = 30;
        for (int i = 0; i <= steps; i++) {
          c.setLocation(signum * getWidth() * (steps - i) / steps, c.getY());
          prevC.setLocation(-signum * getWidth() * i / steps, prevC.getY());
          try {
            Thread.sleep(6);
          } catch (InterruptedException e) {
            i = steps;
            doOnInterrupt();
            continue;
          }
        }
        doOnInterrupt();
      }
      private void doOnInterrupt() {

        
        for (Component x : jpnlContent.getComponents()) {
            x.setVisible(false);
            x.setLocation(0, 0);
        }
        c.setVisible(true);
        
      }
    };
    thrdSwap.start();
    
    

  }
  public void setSize(final int width, final int height) {
    super.setSize(width, height);
    if (jpnlContent != null) {
      jpnlSwitcher.setLocation(0, 0);
      jpnlSwitcher.setSize(getWidth(), 35);
      jpnlContent.setLocation(jpnlSwitcher.getX(), jpnlSwitcher.getHeight() + jpnlSwitcher.getY());
      jpnlContent.setSize(getWidth(), getHeight() - jpnlSwitcher.getHeight() - jpnlSwitcher.getY());
      
    }
  }
  
  
  public static void main(String[] args) {
    
    JFrame jf = new JFrame("hier");
    jf.setSize(300, 400);
    jf.setVisible(true);
    jf.setLocationRelativeTo(null);
    
    TabPanel tof = new TabPanel(true);
    tof.setSize(200, 200);
    tof.setVisible(true);
    jf.add(tof);

    tof.add(new JPanel(), "Spotlight-Athene", 0);
    tof.add(new JPanel(), "Allgemeine", 1);
  }
  
  
  
  
  


}



