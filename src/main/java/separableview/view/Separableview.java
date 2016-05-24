package separableview.view;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import separableview.control.Control;



/**
 * Contains JFrame to which new Components are added using the method 
 * {@link #addComponent(int, int, Component)} which gets as parameters the 
 * location inside a layout table. By adding new components to this table, 
 * the table's size is automatically increased. 
 * 
 * <p>
 * For manually setting the size of the table, the methods 
 * {@link #setAmountColumnsInRow(int, int)}, 
 * {@link #setAmountRowsInColumn(int, int)} and {@link #setTableSize(int, int)}
 * can be used. 
 * 
 * <p> 
 * For only increasing the table's size, the methods
 * {@link #setMinAmountRowsInColumn(int, int)}, 
 * {@link #setMinAmountColumnsInRow(int, int)} and
 * {@link #setMinTableSize(int, int)} can me used.
 * 
 * <p>
 * Each controller method is redirected to the controller class 
 * {@link separableview.control.Control}
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
public class Separableview {

  
  /**
   * Instance of adapted JFrame class which displays the view.
   * This class does not extend JFrame itself for reducing the visibility
   * of JFrame methods that are now deprecated.
   */
  private Editedjframe ejf;

  
  /**
   * Instance of the controller class which handles the complete computation.
   */
  private Control control;
  
  
  private final Vector<Separator> vecSep;
  
  /**
   * Constructor: instantiates the controller and the JFrame class and stores
   * their instances.
   */
  public Separableview() {

    this.vecSep = new Vector<Separator>();
    this.ejf = new Editedjframe();
    this.ejf.setVisible(true);
    this.control = new Control(this);
  }
  
  public Separableview(final String xtitle) {

    this();
    ejf.setTitle(xtitle);
  }
  
  

  /*
   * Redirected to control.
   */

  
  
  
  /**
   * Add new component to specified location. If the location is already 
   * occupied, the component is not added to view.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * @see separableview.control.Control.addComponent
   * @param xrow  The row - location
   * @param xcol  The column - location
   * @param xc    The component
   * @return      The added component or null if the process failed.
   */
  public final Component addComponent(final int xrow, final int xcol,
      final Component xc) {
    
    
    final Component compAdded = ejf.add(xc);
    xc.addMouseListener(control);
    xc.addMouseMotionListener(control);
    int addIndex = getComponents().length - 1;
    if (!control.addComponent(xrow, xcol, xc, addIndex)) {
      ejf.remove(ejf);  
      return null;
    }
    return compAdded;
  }
  
  /**
   * Sets the amount of columns in each row and the amount of rows in each 
   * column to the specified values.
   * 
   * <p>
   * If the amount of entries is greater than the specified value, 
   * all the entries having row respectively column index greater than 
   * <code>xamountCols</code> respectively <code>xamountCols</code> are 
   * removed.
   * 
   * <p>
   * In case the amount of entries is less than the specified value,
   * null entries are inserted.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * @see setAmountRowsInColumn
   * @see setAmountColumnsInRow
   * 
   * @see separableview.control.Control.setAmountRowsInColumn
   * @see separableview.control.Control.setTableSize
   * @see separableview.control.Control.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedRow   The specified row
   * @param xamountCols     The amount of columns inside the specified row
   */
  public void setTableSize(final int xamountCols, final int xamountRows) {
    control.setTableSize(xamountCols, xamountRows);
  }

  /**
   * In case the table's size is less than the specified parameters, the amount
   * of columns in each row and the amount of rows in each 
   * column are increased to the specified values.
   * 
   * <p>
   * If the amount of entries is greater than the specified value, the table
   * is not altered.
   * 
   * <p>
   * In case the amount of entries is less than the specified value,
   * null entries are inserted.
   * 
   * @see setAmountRowsInColumn
   * @see setAmountColumnsInRow
   * 
   * @see separableview.view.Separableview.setAmountRowsInColumn
   * @see separableview.view.Separableview.setTableSize
   * @see separableview.view.Separableview.setAmountColumnsInRow
   * 
   * 
   * @param xamountRows     The amount of rows inside each column
   * @param xamountCols     The amount of columns inside each row
   */
  public void setMinTableSize(final int xamountCols, final int xamountRows) {
    control.setMinTableSize(xamountCols, xamountRows);
  }
  

  /**
   * Sets the amount of columns inside the specified row
   * to <code>xamountCols</code>.
   * 
   * <p>
   * If the amount of columns in specified row <code>xspecifiedRow</code> is 
   * greater than <code>xamountCols</code>, all the columns having column index 
   * greater than <code>xamountCols</code> inside the row 
   * <code>xspecifiedRow</code> are removed.
   * 
   * <p>
   * In case the amount of columns in specified row <code>xspecifiedRow</code>
   * is less than <code>xamountCols</code>, null values are inserted.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * @see setAmountRowsInColumn
   * @see setTableSize
   * 
   * @see separableview.control.Control.setAmountRowsInColumn
   * @see separableview.control.Control.setTableSize
   * @see separableview.control.Control.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedRow   The specified row
   * @param xamountCols     The amount of columns inside the specified row
   */
  public void setAmountCols(final int xamountCols) {
    control.setAmountCols(xamountCols);
    
  }


  /**
   * Sets the amount of columns inside the specified row
   * to <code>xamountCols</code>.
   * 
   * <p>
   * If the amount of columns in specified row <code>xspecifiedRow</code> is 
   * greater than <code>xamountCols</code>, the table is not altered. 
   * 
   * <p>
   * In case the amount of columns in specified row <code>xspecifiedRow</code>
   * is less than <code>xamountCols</code>, null values are inserted.
   * 
   * @see setAmountRowsInColumn
   * @see setTableSize
   * 
   * @see separableview.control.Control.setAmountRowsInColumn
   * @see separableview.control.Control.setTableSize
   * @see separableview.control.Control.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedRow   The specified row
   * @param xamountCols     The amount of columns inside the specified row
   */
  public void setMinAmountColumns(final int xamountCols) {
    control.setMinAmountColumns(xamountCols);
    
  }
  
  
  /**
   * Sets the amount of rows inside the specified column
   * to <code>xamountRows</code>.
   * 
   * <p>
   * If the amount of rows in specified column <code>xspecifiedCol</code> is 
   * greater than <code>xamountRows</code>, all the rows having row index 
   * greater than <code>xamountRows</code> inside the column 
   * <code>xspecifiedCol</code> are removed.
   * 
   * <p>
   * In case the amount of rows in specified column <code>xspecifiedCol</code>
   * is less than <code>xamountRows</code>, null values are inserted.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * @see setAmountColumnsInRow
   * @see setTableSize
   * 
   * @see separableview.control.Control.setAmountRowsInColumn
   * @see separableview.control.Control.setTableSize
   * @see separableview.control.Control.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedCol   The specified column
   * @param xamountRows     The amount of rows inside the specified column
   */
  public void setAmountRows(final int xamountRows) {
    control.setAmountRows(xamountRows);
  }
  

  /**
   * Increases the amount of rows inside the specified column
   * to <code>xamountRows</code>.
   * 
   * <p>
   * If the amount of rows in specified column <code>xspecifiedCol</code> is 
   * greater than <code>xamountRows</code>, the table is not altered. 
   * 
   * <p>
   * In case the amount of rows in specified column <code>xspecifiedCol</code>
   * is less than <code>xamountRows</code>, null values are inserted.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * @see setAmountColumnsInRow
   * @see setTableSize
   * @see setAmountRowsInColumn
   * 
   * @see separableview.control.Control.setAmountRowsInColumn
   * @see separableview.control.Control.setAmountColumnsInRow
   * 
   * @param xspecifiedCol   The specified column
   * @param xamountRows     The amount of rows inside the specified column
   */
  public void setMinAmountRows(final int xamountRows) {
    control.setMinAmountRows(xamountRows);
  }
  
  
  /**
   * Resets the size and location of each component following their index in 
   * grid layout.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * 
   * @see separableview.control.Control.applyStandardDistribution
   * 
   */
  public final void applyStandardDistribution() {
    control.applyStandardDistribution();
  }
  

  

  /**
   * Manually switches the location of two components in grid layout.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * 
   * @see separableview.control.Control.switchEntries
   * 
   * @param xp1   the location of first component in row and column count.
   * @param xp2   the location of second component in row and column count.
   */
  public final void switchEntries(final Point xp1, final Point xp2) {
    control.switchEntries(xp1, xp2);
  }
  

  /**
   * Saves the configuration of the currently added elements (excluded) to the
   * specified path.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * 
   * @see separableview.control.Control.saveConfiguration
   * 
   * @param xconfigPath   the path of the configuration file.
   */
  public final void saveConfiguration(final String xconfigPath) {
    control.saveConfiguration(xconfigPath);
  }

  /**
   * Loads the configuration of the currently added elements. Is to be called
   * after all elements that are to be maintained have been added to the 
   * instance of {@link xlib.separableview.control.View.Separableview}.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.control.Control}.
   * 
   * @see separableview.control.Control.loadConfiguration
   * 
   * @param xconfigPath   the path of the configuration file.
   */
  public final void loadConfiguration(final String xconfigPath) {
    control.loadConfiguration(xconfigPath);
  }
  
  

  
  public void setSize(final int xwidth, final int xheigth) {
    ejf.setSize(xwidth, xheigth);
  }

  public Dimension getSize() {
    return new Dimension(ejf.getWidth(), ejf.getHeight() - 20);
  }
  public Component[] getComponents() {
    return ejf.getContentPane().getComponents();
  }
  /**
   * Sets the location of the window relative to the specified
   * component according to the following scenarios.
   * 
   * <p>
   * The target screen mentioned below is a screen to which
   * the window should be placed after the setLocationRelativeTo
   * method is called.
   * <ul>
   * <li>If the component is {@code null}, or the {@code
   * GraphicsConfiguration} associated with this component is
   * {@code null}, the window is placed in the center of the
   * screen. The center point can be obtained with the {@link
   * GraphicsEnvironment#getCenterPoint
   * GraphicsEnvironment.getCenterPoint} method.
   * <li>If the component is not {@code null}, but it is not
   * currently showing, the window is placed in the center of
   * the target screen defined by the {@code
   * GraphicsConfiguration} associated with this component.
   * <li>If the component is not {@code null} and is shown on
   * the screen, then the window is located in such a way that
   * the center of the window coincides with the center of the
   * component.
   * </ul>
   * 
   * <p>
   * If the screens configuration does not allow the window to
   * be moved from one screen to another, then the window is
   * only placed at the location determined according to the
   * above conditions and its {@code GraphicsConfiguration} is
   * not changed.
   * 
   * <p>
   * <b>Note</b>: If the lower edge of the window is out of the screen,
   * then the window is placed to the side of the {@code Component}
   * that is closest to the center of the screen. So if the
   * component is on the right part of the screen, the window
   * is placed to its left, and vice versa.
   * 
   * <p>
   * If after the window location has been calculated, the upper,
   * left, or right edge of the window is out of the screen,
   * then the window is located in such a way that the upper,
   * left, or right edge of the window coincides with the
   * corresponding edge of the screen. If both left and right
   * edges of the window are out of the screen, the window is
   * placed at the left side of the screen. The similar placement
   * will occur if both top and bottom edges are out of the screen.
   * In that case, the window is placed at the top side of the screen.
   * 
   * <p>
   * The method changes the geometry-related data. Therefore,
   * the native windowing system may ignore such requests, or it may modify
   * the requested data, so that the {@code Window} object is placed and sized
   * in a way that corresponds closely to the desktop settings.
   *
   * @param xc  the component in relation to which the window's location
   *           is determined
   * @see java.awt.GraphicsEnvironment#getCenterPoint
   * @since 1.4
   */
  public void setLocationRelativeTo(final Component xc) {
    ejf.setLocationRelativeTo(xc);
  }
  

  /**
   * Sets the background color of this window.
   * 
   * <p>
   * If the windowing system supports the {@link
   * GraphicsDevice.WindowTranslucency#PERPIXEL_TRANSLUCENT PERPIXEL_TRANSLUCENT}
   * translucency, the alpha component of the given background color
   * may effect the mode of operation for this window: it indicates whether
   * this window must be opaque (alpha equals {@code 1.0f}) or per-pixel translucent
   * (alpha is less than {@code 1.0f}). If the given background color is
   * {@code null}, the window is considered completely opaque.
   * 
   * <p>
   * All the following conditions must be met to enable the per-pixel
   * transparency mode for this window:
   * <ul>
   * <li>The {@link GraphicsDevice.WindowTranslucency#PERPIXEL_TRANSLUCENT
   * PERPIXEL_TRANSLUCENT} translucency must be supported by the graphics
   * device where this window is located
   * <li>The window must be undecorated (see {@link Frame#setUndecorated}
   * and {@link Dialog#setUndecorated})
   * <li>The window must not be in full-screen mode (see {@link
   * GraphicsDevice#setFullScreenWindow(Window)})
   * </ul>
   * 
   * <p>
   * If the alpha component of the requested background color is less than
   * {@code 1.0f}, and any of the above conditions are not met, the background
   * color of this window will not change, the alpha component of the given
   * background color will not affect the mode of operation for this window,
   * and either the {@code UnsupportedOperationException} or {@code
   * IllegalComponentStateException} will be thrown.
   * 
   * <p>
   * When the window is per-pixel translucent, the drawing sub-system
   * respects the alpha value of each individual pixel. If a pixel gets
   * painted with the alpha color component equal to zero, it becomes
   * visually transparent. If the alpha of the pixel is equal to 1.0f, the
   * pixel is fully opaque. Interim values of the alpha color component make
   * the pixel semi-transparent. In this mode, the background of the window
   * gets painted with the alpha value of the given background color. If the
   * alpha value of the argument of this method is equal to {@code 0}, the
   * background is not painted at all.
   * 
   * <p>
   * The actual level of translucency of a given pixel also depends on window
   * opacity (see {@link #setOpacity(float)}), as well as the current shape of
   * this window (see {@link #setShape(Shape)}).
   * 
   * <p>
   * Note that painting a pixel with the alpha value of {@code 0} may or may
   * not disable the mouse event handling on this pixel. This is a
   * platform-dependent behavior. To make sure the mouse events do not get
   * dispatched to a particular pixel, the pixel must be excluded from the
   * shape of the window.
   * 
   * <p>
   * Enabling the per-pixel translucency mode may change the graphics
   * configuration of this window due to the native platform requirements.
   *
   * @param bgColor the color to become this window's background color.
   *
   * @throws IllegalComponentStateException if the alpha value of the given
   *     background color is less than {@code 1.0f} and the window is decorated
   * @throws IllegalComponentStateException if the alpha value of the given
   *     background color is less than {@code 1.0f} and the window is in
   *     full-screen mode
   * @throws UnsupportedOperationException if the alpha value of the given
   *     background color is less than {@code 1.0f} and {@link
   *     GraphicsDevice.WindowTranslucency#PERPIXEL_TRANSLUCENT
   *     PERPIXEL_TRANSLUCENT} translucency is not supported
   *
   * @see Window#getBackground
   * @see Window#isOpaque
   * @see Window#setOpacity(float)
   * @see Window#setShape(Shape)
   * @see Frame#isUndecorated
   * @see Dialog#isUndecorated
   * @see GraphicsDevice.WindowTranslucency
   * @see GraphicsDevice#isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency)
   * @see GraphicsConfiguration#isTranslucencyCapable()
   */
  public void setBackground(final Color xc) {
    ejf.setBackground(xc);
  }

  public void dispose() {
    ejf.superDispose();
  }
  
  
  public void remove(final Component xc) {
    ejf.remove(xc);
  }
  
  
  
  
  
  
  
  
  

  public void setFocusable(final boolean xfoc) {
    ejf.setFocusable(xfoc);
  }

  public void setEnabled(final boolean xen) {
    ejf.setEnabled(xen);
  }
  
  
  public void setDefaultCloseOperation(int xop) {
    ejf.setDefaultCloseOperation(xop);
  }

  public Container getContentPane() {
    return ejf.getContentPane();
  }
  

  
  
  /**
   * Private class for decreasing visibility of methods.
   * @author juli
   *
   */
  private class Editedjframe extends JFrame {

    private JPanel jpnlSeparators;


    public Editedjframe() {
      super.setLayout(null);
      jpnlSeparators = new JPanel();
      jpnlSeparators.setLayout(null);
      jpnlSeparators.setOpaque(false);
      super.add(jpnlSeparators);
      
    }
    
    public void dispose() {
      Separableview.this.dispose();
    }

    public void superDispose() {
      super.dispose();
    }
    
    public void setSize(final int xwidth, final int xheigth) {
      
      
      super.setSize(xwidth, xheigth);
      jpnlSeparators.setSize(xwidth, xheigth);
      if (control != null) {
        control.refreshCompSepBounds();
      }
    }
    
    
    public void validate() {
      super.validate();
      this.setSize(getWidth(), getHeight());
    }

  }

  /**
   * Return information string for debugging.
   * @return information string for debugging.
   */
  public String print() {
    String result = "";
    for (int i = 0; i < vecSep.size(); i++) {
      result += vecSep.get(i).print() + "\n";
      
    }
    for (int i = 0; i < getComponents().length; i++) {
      result += "CMP" + getComponents()[i].getX() + ", "
          + getComponents()[i].getY() 
          + ", " + getComponents()[i].getWidth() + ", " 
          + getComponents()[i].getHeight() + "\n";
    }
    
    return result;
  }
  
  public void setSavePath(final String xsavePath) {
    control.setSavePath(xsavePath);
  }
  
  public static void main(final String[] xargs) throws InterruptedException {
//    JFrame jf = new JFrame();
//    jf.setVisible(true);
//    jf.setResizable(false);
//    jf.setSize(500, 500);
//    jf.setVisible(true);
    int i = 0;
    while (true) {

      i = i +1;
      Thread.sleep(50);
    }
    
//    final Separableview sv = new Separableview();
//    sv.ejf.setTitle("Separable view test environment");
//    sv.ejf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    sv.setSize(500, 500);
//    sv.setLocationRelativeTo(null);
//    sv.setBackground(Color.WHITE);
//    for (int row = 0; row < 5; row++) {
//      for (int col = 0; col < 4; col++) {
//        sv.addComponent(row, col, generateComponent(row * 4 + col));
//      }
//    }
//    sv.loadConfiguration("/Users/juli/Desktop/configsv");
//    new Thread() {
//      public void run() {
//        try {
//          Thread.sleep(5000);
//        } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//        sv.saveConfiguration();
//      }
//    }.start();
  }
  
  private static JLabel generateComponent(final int xnu) {
    JLabel jpnlTr = new JLabel(""  + xnu);
    jpnlTr.setBackground(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
    jpnlTr.setVisible(true);
    jpnlTr.setOpaque(true);
    return jpnlTr;
  }


  /**
   * @return the vecSep
   */
  public Vector<Separator> getVecSep() {
    return vecSep;
  }


  public void addSeparator(Separator sep1) {
    vecSep.add(sep1);
    ejf.jpnlSeparators.add(sep1);
  }

  public void setVisible(boolean xvis) {
    ejf.setVisible(xvis);
    
  }


  
}
