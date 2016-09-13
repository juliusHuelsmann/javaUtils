package tabOpFrame.utils;

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
public class ViewDictSwitcher extends DoubleButton {

  private final int index;
  public ViewDictSwitcher(Object o, int index) {
    super(o);
    this.index = index;
  }


  /**
   * @return the index
   */
  public int getIndex() {
    return index;
  }
}
