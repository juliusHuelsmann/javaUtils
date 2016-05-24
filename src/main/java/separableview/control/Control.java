package separableview.control;

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

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JScrollPane;

import separableview.model.ComponentModel;
import separableview.model.Model;
import separableview.view.Separableview;


/**
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 *
 */
public class Control implements MouseListener, MouseMotionListener {
  
  
  /**
   * Instance of the model class which stores the whole data structure. Is 
   * re-instantiated during the load process.
   */
  private Model model;
  
  /**
   * Instance of the view class which displays everything. 
   */
  private final Separableview view;
  
  
  private Vector<Component> vecComp;
  
  
  
  /**
   * 
   * @param xview instance of the view class which is stored.
   */
  public Control(final Separableview xview) {
    this.view = xview;
    this.vecComp = new Vector<Component>();
    this.model = new Model();
  }
  
  

  /**
   * Add new component to specified location. If the location is already 
   * occupied, the component is not added to view.
   * 
   * <p>
   * @see separableview.view.Separableview.addComponent
   * @param xrow  The row - location
   * @param xcol  The column - location
   * @param xc    The component
   * @return      The added component or null if the process failed.
   */
  public final boolean addComponent(final int xrow, final int xcol,
      final Component xc, final int xid) {
    
    if (!model.addComponent(xid, new Point(xrow, xcol), this)) {
      return false;
    } else {

      
      applyStandardDistribution();
      return true;
    }
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
  public void setTableSize(final int xamountCols, final int xamountRows) {
    model.setAmountCols(xamountCols, this);
    model.setAmountRows(xamountRows, this);
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
    model.setAmountCols(Math.max(model.getAmountCols(), xamountCols), this);
    model.setAmountRows(Math.max(model.getAmountRows(), xamountRows), this);
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
   * @see setAmountRowsInColumn
   * @see setTableSize
   * 
   * @see separableview.view.Separableview.setAmountRowsInColumn
   * @see separableview.view.Separableview.setTableSize
   * @see separableview.view.Separableview.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedRow   The specified row
   * @param xamountCols     The amount of columns inside the specified row
   */
  public void setAmountCols(final int xamountCols) {

    model.setAmountCols(xamountCols, this);
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
   * @see separableview.view.Separableview.setAmountRowsInColumn
   * @see separableview.view.Separableview.setTableSize
   * @see separableview.view.Separableview.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedRow   The specified row
   * @param xamountCols     The amount of columns inside the specified row
   */
  public void setMinAmountColumns(final int xamountCols) {
    model.setAmountCols(Math.max(model.getAmountCols(), xamountCols), this);
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
   * @see setAmountColumnsInRow
   * @see setTableSize
   * @see setMinAmountRowsInColumn
   * 
   * @see separableview.view.Separableview.setAmountRowsInColumn
   * @see separableview.view.Separableview.setTableSize
   * @see separableview.view.Separableview.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedCol   The specified column
   * @param xamountRows     The amount of rows inside the specified column
   */
  public void setAmountRows(final int xamountRows) {
    model.setAmountRows(xamountRows, this);
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
   * @see setAmountColumnsInRow
   * @see setTableSize
   * @see setAmountRowsInColumn
   * 
   * @see separableview.view.Separableview.setAmountRowsInColumn
   * @see separableview.view.Separableview.setTableSize
   * @see separableview.view.Separableview.setAmountColumnsInRow
   * 
   * 
   * @param xspecifiedCol   The specified column
   * @param xamountRows     The amount of rows inside the specified column
   */
  public void setMinAmountRows(final int xamountRows) {
    model.setAmountRows(Math.max(model.getAmountRows(), xamountRows), this);
  }
  
  /**
   * Resets the size and location of each component following their index in 
   * grid layout.
   * 
   * @see separableview.view.Separableview.applyStandardDistribution
   * 
   */
  public final void applyStandardDistribution() {

    final int ownerWidth = view.getSize().width;
    final int ownerHeight = view.getSize().height;

    final int amountCols = model.getAmountCols();
    final int amountRows = model.getAmountRows();
  
    
//    final double unitx = 1.0 * ownerWidth / amountCols;
//    final double unity = 1.0 * ownerHeight / amountRows;
    
    for (int i = 0; i < model.getVecComp().size(); i++) {
      
      
//      final Point pntLoc = model.getVecComp().get(i).getPntLocationinmap();
      model.getVecComp().get(i).applyStandardDistribution(
          amountCols, amountRows, view.getSize().width, view.getSize().height);
      model.getVecComp().get(i).updateView(this);
//      final int addid = model.getVecComp().get(i).getAddIdentifier();
//
//      final double x = (1.0 * pntLoc.x * unitx);
//      final double y = (1.0 * pntLoc.y * unity);
//      
//      view.getComponents()[addid].setBounds((int) Math.round(x),
//          (int) Math.round(y), (int) Math.round(x + unitx), 
//          (int) Math.round(y + unity));
    }
    
    refreshSepBounds();
    
  }
  

  



  /**
   * Manually switches the location of two components in grid layout.
   * 
   * @see separableview.view.Separableview.switchEntries
   * 
   * @param xp1   the location of first component in row and column count.
   * @param xp2   the location of second component in row and column count.
   */
  public final void switchEntries(final Point xp1, final Point xp2) {
    
  }
  

  /**
   * Saves the configuration of the currently added elements (excluded) to the
   * specified path.
   * 
   * <p>
   * Is redirected to 
   * {@link separableview.view.Separableview}.
   * 
   * @see separableview.view.Separableview.saveConfiguration
   * 
   * @param xconfigPath   the path of the configuration file.
   */
  public final void saveConfiguration(final String xconfigPath) {

    ObjectOutputStream oos;
    try {
      FileOutputStream fos = new FileOutputStream(new File(xconfigPath));
      oos = new ObjectOutputStream(fos);
      oos.writeObject(model);
      oos.flush();
      oos.close();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  
  }



  



  /**
   * Loads the configuration of the currently added elements. Is to be called
   * after all elements that are to be maintained have been added to the 
   * instance of {@link xlib.separableview.control.View.Separableview}.
   * 
   * @see separableview.view.Separableview.loadConfiguration
   * 
   * @param xconfigPath   the path of the configuration file.
   */
  public final Model loadConfiguration(final String xconfigPath) {


    ObjectInputStream ois = null;
    try {
      FileInputStream fis = new FileInputStream(new File(xconfigPath));
      ois = new ObjectInputStream(fis);
      Object obj = ois.readObject();
      ois.close();
      fis.close();
      model = (Model) obj;
      refreshCompSepBounds();
      return (Model) obj;
    } catch (IOException e) { 
      return null;
    } catch (ClassNotFoundException e) {
      try {
        if (ois != null) {

          ois.close(); 
        }
      } catch (IOException e1) {
        return null;
      }
      return null;
    }
  }
  
  /**
   * Re-apply the bounds of the components which is stored inside the model.
   */
  public final void refreshCompSepBounds() {
    refreshComponentBounds();
    refreshSepBounds();
  }
  
  public final void refreshComponentBounds() {

    for (int i = 0; i < model.getVecComp().size(); i++) {
      model.getVecComp().get(i).updateView(this);
    }
    if (savePath != null && !savePath.equals("")) {

      saveConfiguration(savePath); 
    }
  }

  private void refreshSepBounds() {

    // Update the separator's locations.
    for (int i = 0; i < view.getVecSep().size(); i++) {
      view.getVecSep().get(i).adaptToComponentLocation();
    }    
  }

  
  
  public Component getComponentbygrid(final int xrow, final int xcol) {
    final int index = model.getComponentModelid(new Point(xrow, xcol));
    if (index != -1) {
      if (view.getComponents().length > index) {
        return view.getComponents()[index];
      }
    }
    return null;
  }

  /**
   * Print information string for debugging.
   * @return information string for debugging.
   */
  public void print() {

    System.out.println("Model:" + model.getAmountRows() + ", "
        + model.getAmountCols() + "\n" + model.print());
    System.out.println("View:\n" + view.print());
    

  }

  
  
  public ComponentModel getModelcomponentbygrid(
      final int xrow, final int xcol) {
    return model.getComponentModel(new Point(xrow, xcol));
  }



  /**
   * @return the view
   */
  public Separableview getView() {
    return view;
  }
  
  
  private Component compmovePressed;
  
  /**
   * The currently switched component.
   */
  private ComponentModel compswitched;
  
  /**
   * The model class of the switched component.
   */
  private ComponentModel compmodelPressed;

  private Vector<ThreadMove> threadSwitch = new Vector<ThreadMove>();
  
  private int movexdist;
  private int moveydist;


  public void mouseClicked(MouseEvent e) { }

  private int findAddedComponent(final Component xcomp) {
    return contains(view.getContentPane(), xcomp);
  }

  private int contains(final Container xc, final Component xcomp) {
    
    int found = -1;
    for (int i = 0; i < xc.getComponents().length; i++) {

      if (xcomp.equals(xc.getComponent(i))) {
        found = i;
        return i;
      }
      
      if (xc.getComponent(i) instanceof JScrollPane) {
        
        JScrollPane jsp = (JScrollPane) xc.getComponent(i);
        if (xcomp.equals(jsp.getViewport().getView())) {
          return i;
        }
      } else if (xc.getComponent(i) instanceof Container) {
        found = contains((Container) xc.getComponent(i), xcomp);
        if (found != -1) {
          return i;
        }
      }
    }
    return found;
  }

  public void mousePressed(MouseEvent e) {
    
    
    final int indx = findAddedComponent((Component) e.getSource());
    if (indx == -1) {
      return;
    }
    compmovePressed = view.getComponents()[indx];
    movexdist = e.getXOnScreen() - compmovePressed.getX();
    moveydist = e.getYOnScreen() - compmovePressed.getY();
    
    
    compmodelPressed = model.getComponentModel(
        getcomponentaddid(compmovePressed));
  }
  
  private int getcomponentaddid(final Component xcomp) {
    for (int i = 0; i < view.getComponents().length; i++) {
      if (view.getComponents()[i].equals(xcomp)) {
        return i;
      }
    }
    return -1;
  }



  public void mouseReleased(MouseEvent e) {
    
    
    if (compmovePressed == null) {
      return;
    }
    // 
    // Get the destiny model component class
    final ComponentModel cmDest = model.getClosestComponent(
        1.0 * (e.getX() + compmovePressed.getX()) / view.getSize().width, 
        1.0 * (e.getY() + compmovePressed.getY()) / view.getSize().height);

    
    //
    // Save the origin model component class.
    final ComponentModel cmOrig = compmodelPressed;

    //
    // Switch the locations in model
    final int destID = cmDest.getAddIdentifier();
    cmDest.setAddIdentifier(cmOrig.getAddIdentifier());
    cmOrig.setAddIdentifier(destID);
    
    //
    // Move the origin model component class to the destiny. The algorithm
    // seems to move the destination element to its own location, but that
    // is done due to the change of identifiers inside the model class and
    // absolutely correct.
    final double w = getView().getSize().getWidth();
    final double h = getView().getSize().getHeight();
    ThreadMove tm1 = new ThreadMove(cmDest, 
        (int) Math.round(cmDest.getRecSizePercentage().getX() * w),
        (int) Math.round(cmDest.getRecSizePercentage().getY() * h));
    tm1.start();
    threadSwitch.add(tm1);
    
    
    //
    // Update the size of the components that changed
    refreshComponentBounds();
    
    
    //
    // Set all components to their default value
    compmovePressed = null;
    compswitched = null;
    movexdist = 0;
    moveydist = 0;
    compmodelPressed = null;
  }



  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }



  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }



  public void mouseDragged(MouseEvent e) {

    
    if (compmovePressed == null) {
      return;
    }
    //
    // Adapt the component's location to the location the mouse caret has been
    // moved to.
    compmovePressed.setLocation(
        e.getXOnScreen() - movexdist,
        e.getYOnScreen() - moveydist);
    
    //
    // Find the element that has to be switched with the currently moved 
    // element.
    final ComponentModel compNowswitched = model.getClosestComponent(
        1.0 * (e.getX() + compmovePressed.getX()) / view.getSize().width, 
        1.0 * (e.getY() + compmovePressed.getY()) / view.getSize().height);

    if ((compswitched == null && 
        (compNowswitched.getPntLocationinmap().x 
            != compmodelPressed.getPntLocationinmap().x
            || compNowswitched.getPntLocationinmap().y
            != compmodelPressed.getPntLocationinmap().y))
        
        // the component differs from the previously switched one
        || (compswitched != null && (compswitched.getPntLocationinmap().x 
            != compNowswitched.getPntLocationinmap().x
        || (compswitched.getPntLocationinmap().y
            != compNowswitched.getPntLocationinmap().y)))) {

      final double w = getView().getSize().getWidth();
      final double h = getView().getSize().getHeight();

      //
      // Move the new component to the previous position of the component 
      // being dragged.
      if (!compNowswitched.equals(compmodelPressed)) {

        findThreadIdentifier(compNowswitched);
        ThreadMove tm1 = new ThreadMove(compNowswitched, 
            (int) Math.round(compmodelPressed.getRecSizePercentage().getX() 
                * w),
            (int) Math.round(compmodelPressed.getRecSizePercentage().getY() 
                * h));
        threadSwitch.add(tm1);
        tm1.start();
      }
      
      //
      // Move the component that has previously been switched to its original
      // position.
      if (compswitched != null) {
        
        
        // search for thread having the same identifier. If found, interrupt
        // it.
        findThreadIdentifier(compswitched);

        ThreadMove tm2 = new ThreadMove(compswitched, 
            (int) Math.round(compswitched.getRecSizePercentage().getX() * w),
            (int) Math.round(compswitched.getRecSizePercentage().getY() * h));
        threadSwitch.add(tm2); 
        tm2.start();
      }
      
      if (!compNowswitched.equals(compmodelPressed)) {
        this.compswitched = compNowswitched; 
      } else {
        compswitched = null;
      }
    }
  }

  
  /**
   * Find the thread that moves the component of ComponentModel, terminate
   * it and remove it out of list of move-threads.
   * @param xcm   the ThreadMove identifier.
   */
  private void findThreadIdentifier(final ComponentModel xcm) {
    for (int i = 0; i < threadSwitch.size(); i++) {
      if (threadSwitch.get(i).getCmIdent().equals(xcm)) {
        threadSwitch.get(i).interrupt();
        threadSwitch.remove(i);
      }
    }
  }


  public void mouseMoved(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  private class ThreadMove extends Thread {
    
    private ComponentModel cmIdent;
    private final int destx;
    private final int desty;
    public ThreadMove(final ComponentModel xcm, final int xdestx, 
        final int xdesty) {
      this.cmIdent = xcm;
      this.destx = xdestx;
      this.desty = xdesty;
    }
    
    
    private int step = 20;
    public void run() {
      
      final Component co = view.getComponents()[cmIdent.getAddIdentifier()];

      final double startx = co.getX();
      final double starty = co.getY(); 

      double vecX = destx - startx;
      double vecY = desty - starty;

      double distance = Math.sqrt(vecX * vecX + vecY * vecY);

      
      vecX = 1.0 * vecX / distance;
      vecY = 1.0 * vecY / distance;
      
      for (int multiplier = 0; distance > step; multiplier++) {
        
        co.setLocation((int) (startx + multiplier * vecX), 
            (int) (starty + multiplier * vecY));
        
        final double distx = co.getX() - destx;
        final double disty = co.getY() - desty;
        distance = Math.sqrt(distx * distx + disty * disty);
        
        try {
          Thread.sleep(2);
        } catch (InterruptedException e) {
          interrupt();
          break;
        }
      }
      
      //
      // Apply the final destination if not interrupted
      if (!isInterrupted()) {
        co.setLocation(destx, desty); 
      }
    }



    /**
     * @return the cmIdent
     */
    public ComponentModel getCmIdent() {
      return cmIdent;
    }



    /**
     * @param cmIdent the cmIdent to set
     */
    public void setCmIdent(ComponentModel cmIdent) {
      this.cmIdent = cmIdent;
    }
  }

  private String savePath;
  public void setSavePath(String xsavePath) {
    savePath = xsavePath;
  }
}


