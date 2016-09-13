package tabPnl.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Utils;

@SuppressWarnings("serial")
public class DoubleButton extends JPanel {
  
  private BufferedImage biSelected;
  private BufferedImage biNormal;
  
  private JButton jbtnPress;
  private JLabel jlblBackground;
  private boolean selected;

  public DoubleButton(final Object o) {
    super();
    super.setLayout(null);
    super.setOpaque(false);
    selected = false;
    
    jbtnPress = new JButton();
    if (o instanceof String) {
      jbtnPress.setText(o.toString()); 
    } else if (o instanceof BufferedImage) {
      jbtnPress.setIcon(new ImageIcon(((BufferedImage) o)));
    }
    jbtnPress.setBorder(null);
    jbtnPress.setOpaque(false);
    jbtnPress.setContentAreaFilled(false);
    super.add(jbtnPress);
    
    
    jlblBackground = new JLabel();
    super.add(jlblBackground);
  }


  @Override
  public void setSize(final int xwidth, final int xheight) {
    super.setSize(xwidth, xheight);
    restSize(xwidth, xheight);
    
    
  }
  
  private void restSize(final int xwidth, final int xheight) {

    jbtnPress.setSize(xwidth, xheight);
    jlblBackground.setSize(xwidth, xheight);
    biSelected = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    biNormal = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    
    BufferedImage tempbiSelected = Utils.resize(
        "/res/feldSingleSelected.png",
        44, getHeight());
    BufferedImage tempbiNormal = Utils.resize("/res/feldSingle.png",
        44, getHeight());

    for (int i = 0; i < getWidth(); i++) {
      for (int j = 0; j < getHeight(); j++) {
        if (i < tempbiSelected.getWidth() / 3) {

          biSelected.setRGB(i, j, tempbiSelected.getRGB(i, j));
          biNormal.setRGB(i, j, tempbiNormal.getRGB(i, j));
        } else if (i > xwidth - tempbiSelected.getWidth() / 3) {
          final int postempbisel = i - xwidth + 3 * tempbiSelected.getWidth() / 3;
//
          biSelected.setRGB(i, j, tempbiSelected.getRGB(postempbisel, j));
          biNormal.setRGB(i, j, tempbiNormal.getRGB(postempbisel, j));
        } else {
          int ii = i % tempbiSelected.getWidth() * 2 / 3;
          if (ii > tempbiSelected.getWidth() / 3) {
            ii = - (-tempbiSelected.getWidth() * 2 / 3 + ii);
          }
          biSelected.setRGB(i, j, tempbiSelected.getRGB(tempbiSelected.getWidth() / 3 + ii, j));
          biNormal.setRGB(i, j, tempbiNormal.getRGB(tempbiSelected.getWidth() / 3 + ii, j));
        }
      }
    }
    if (selected) {
      jlblBackground.setIcon(new ImageIcon(biSelected)); 
    } else {
      jlblBackground.setIcon(new ImageIcon(biNormal)); 
    }
  }
  @Override
  public void setSize(final Dimension xdim) {
    super.setSize(xdim);
    restSize((int) xdim.getWidth(), (int) xdim.getHeight());
  }
  @Override
  public void setLocation(final Point xp) {
    super.setLocation(xp);
  }
  @Override
  public void setLocation(final int xx, final int xy) {
    super.setLocation(xx, xy);
  }
  @Override
  public void setBounds(final int xx, final int xy, final int xwidth, final int xheight) {
    super.setBounds(xx, xy, xwidth, xheight);
    restSize(xwidth, xheight);
    
  }
  @Override
  public void setBounds(final Rectangle xr) {
    super.setBounds(xr);
    restSize(xr.width, xr.height);
  }
  
  public void addActionListener(final ActionListener l) {
    jbtnPress.addActionListener(l);
  }
  
  public JButton getActionSource() {
    return jbtnPress;
  }

  /**
   * @return the selected
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * @param xselected the selected to set
   */
  public void setSelected(boolean xselected) {
    this.selected = xselected;
    if (xselected) {
      jlblBackground.setIcon(new ImageIcon(biSelected)); 
    } else {
      jlblBackground.setIcon(new ImageIcon(biNormal)); 
    }
  }

  /**
   * Set selected to be true.
   */
  public void select() {
    this.selected = true;
    jlblBackground.setIcon(new ImageIcon(biSelected)); 
  }

  /**
   * Set selected to be false.
   */
  public void unselect() {
    this.selected = false;
    jlblBackground.setIcon(new ImageIcon(biNormal)); 
  }
}
