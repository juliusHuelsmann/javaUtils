package separableview.model;

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

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import other.DoubleRectangle;
import separableview.control.Control;
import separableview.view.Separableview;
import separableview.view.Separator;


public class ComponentModel implements Serializable {
  
  
  /**
   * Identifier which contains the component's location inside the array
   * of components that is returned by calling the getComponents() method.
   * 
   * Is used for matching the model components with the view components.
   * The View components are not saved directly because the compomentModel 
   * may be stored on hard drive. Before the model is loaded, the view 
   * components are added but can be matched nevertheless.
   */
  private int addIdentifier;
  
  /**
   * The location inside the table layout.
   */
  private Point pntLocationinmap;
  
  
  /**
   * Contains the stored component's size in percent of the view size.
   */
  private DoubleRectangle recSizePercentage;
  
  
  /**
   * Instance of the main model class.
   */
  private Model mdl;
  
  /**
   * Constructor: saves the model class.
   * @param xmdl  the model class which is saved.
   */
  public ComponentModel(
      final int xaddIdentifier,
      final Model xmdl,
      final Point xpntLocationinmap) {
    this.mdl = xmdl;
    this.pntLocationinmap = xpntLocationinmap;
    this.addIdentifier = xaddIdentifier;
    this.recSizePercentage = new DoubleRectangle();
  }

  /**
   * @return the addIdentifier
   */
  public int getAddIdentifier() {
    return addIdentifier;
  }

  /**
   * @param addIdentifier the addIdentifier to set
   */
  public void setAddIdentifier(int addIdentifier) {
    this.addIdentifier = addIdentifier;
  }

  /**
   * @return the pntLocationinmap
   */
  public Point getPntLocationinmap() {
    return pntLocationinmap;
  }

  /**
   * @param pntLocationinmap the pntLocationinmap to set
   */
  public void setPntLocationinmap(Point pntLocationinmap) {
    this.pntLocationinmap = pntLocationinmap;
  }

  /**
   * @return the recSizePercentage
   */
  public DoubleRectangle getRecSizePercentage() {
    return recSizePercentage;
  }

  /**
   * @param xrecSizePercentage the recSizePercentage to set
   */
  public void setRecSizePercentage(final DoubleRectangle xrecSizePercentage,
      final Separableview xview) {
    this.recSizePercentage = xrecSizePercentage;

    // update the component's size
    updateComponentSize(xview);
  }
  
  public void updateComponentSize(final Separableview xview) {

    xview.getComponents()[addIdentifier].setSize(
        (int) Math.round(recSizePercentage.getWidth() * xview.getSize().width),
        (int) Math.round(recSizePercentage.getHeight()
            * xview.getSize().height));
    
    xview.getComponents()[addIdentifier].setLocation(
        (int) Math.round(recSizePercentage.getX() * xview.getSize().width),
        (int) Math.round(recSizePercentage.getY() * xview.getSize().height));
  }

  public void applyStandardDistribution(
      final int amountCols, final int amountRows,
      final int windowWidth, final int windowHeight) {
    
    final double width = 1.0 / amountCols;
    final double height = 1.0 / amountRows;
    
    recSizePercentage.setWidth(width
        - 1.0 * Separator.THICKNESS / windowWidth
        );
    recSizePercentage.setHeight(height
        - 1.0 * Separator.THICKNESS / windowHeight
        );
    recSizePercentage.setX(1.0 * width
        * pntLocationinmap.y);
    recSizePercentage.setY(1.0 * height
        * pntLocationinmap.x);
  }
  
  public void updateView(final Control xcontrol) {
    final int width = xcontrol.getView().getSize().width;
    final int heiht = xcontrol.getView().getSize().height;
    xcontrol.getView().getComponents()[addIdentifier].setLocation(
        (int) Math.round(width * recSizePercentage.getX()),
        (int) Math.round(heiht * recSizePercentage.getY()));

    xcontrol.getView().getComponents()[addIdentifier].setSize(
        (int) Math.round(width * recSizePercentage.getWidth()),
        (int) Math.round(heiht * recSizePercentage.getHeight()));
  }

  public String print() {

    return pntLocationinmap.getX() + ", " + pntLocationinmap.getY() + ": " 
        + recSizePercentage.getX() + ", " + recSizePercentage.getY() + ", "
        + recSizePercentage.getWidth() + ", " + recSizePercentage.getHeight();
  }
  

//  private void printComponents() {
//    for (int row = 0; row < vecComponents.size(); row++) {
//      for (int col = 0; col < vecComponents.get(row).size(); col++) {
//        System.out.print(vecComponents.get(row).get(col) + "\t");
//      }
//      System.out.println();
//    }
//  }
  
  
  

//  /**
//   * Add Component into grid.
//   * @param xrow
//   * @param xcol
//   */
//  public void add(final int xrow, final int xcol, final Component comp) {
//    
//    // case 1: the row does not already exist
//    //
//    if (vecComponents.size() <= xrow) {
//      
//      
//      Vector<Component> vecInsertedRow = new Vector<Component>();
//      for (int i = 0; i < xcol; i++) {
//        vecInsertedRow.add(i, null);
//      }
//      
//      for (int i = vecComponents.size(); i < xrow; i++) {
//        vecComponents.add(i, null);
//      }
//      vecComponents.add(xrow, vecInsertedRow);
//    } else if (vecComponents.get(xrow) == null) {
//      // case 2: A row underneath has already been added
//
//      Vector<Component> vecCurrentRow = new Vector<Component>();
//      for (int i = 0; i < xcol; i++) {
//        vecCurrentRow.add(i, null);
//      }
//      vecCurrentRow.add(xcol, comp);
//      
//      // replace 
//      vecComponents.remove(xrow);
//      vecComponents.add(xrow, vecCurrentRow);
//    } else {
//      
//      // An element has already been added to that specific row
//      
//      // add columns 
//      if (vecComponents.get(xrow).size() < xcol) {
//        for (int i = 0; i < xcol; i++) {
//          vecComponents.get(xrow).add(i, null);
//        }
//        vecComponents.get(xrow).add(xcol, comp);
//      } else if (vecComponents.get(xrow).get(xcol) == null) {
//        
//        // a column underneath has already been added
//        vecComponents.get(xrow).remove(xcol);
//        vecComponents.get(xrow).add(xcol, comp);
//      } else {
//        // The specified element is overwritten
//        remove(xrow, xcol);
//
//        vecComponents.get(xrow).add(xcol, comp);
//      }
//    }
//  }
  
  
//  private void updateSeparators() {
//    
//    
//    // check if the size of the 2d vector of separators matches the size of the
//    // 2d vector of components.
//    for (int row = 0; row < vecComponents.size(); row++) {
//      for (int col = vecSepUp.get(row).size();
//          col < vecComponents.get(row).size(); col++) {
//        
//        final Component predecessor = vecComponents.get(row).get(col);
//        final Component successor;
//        if (col + 1 >= vecComponents.get(row).size()) {
//          successor = null;
//        } else {
//          successor = vecComponents.get(row).get(col);
//        }
//        vecSepUp.get(row).add(new Separator(false, predecessor, successor, this))
//        
//      }
//    }
//    
//  }
  
//  /**
//   * Add Component into grid.
//   * @param xx
//   * @param xy
//   */
//  public void remove(final int xrow, final int xcol) {
//    
//  }
  

  
//  private void refreshComponentSize() {
//    for (int i = 0; vecSepUp != null && i < vecSepUp.size(); i++) {
//      vecSepUp.get(i).onOwnersizechanged();
//    }
//
//    for (int i = 0; vecSepLeft != null && i < vecSepLeft.size(); i++) {
//      vecSepLeft.get(i).onOwnersizechanged();
//    }
//  }

  
  
}
