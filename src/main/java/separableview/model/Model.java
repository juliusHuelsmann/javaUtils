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
import java.io.Serializable;
import java.util.Vector;

import separableview.control.Control;
import separableview.view.Separator;
import log.LoggerRegistry;


/**
 * Model class which contains all the information on the added components
 * and their location (location in grid layout as well as the component's 
 * location in percent).
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
public class Model implements Serializable {
 
  
  /**
   * Default serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Table that contains the location of the original .
   */
  private final Vector<ComponentModel> vecComp;
  
  /**
   * The table's amount of columns.
   */
  private int amountCols;
  
  /**
   * The table's amount of rows.
   */
  private int amountRows;
  
  
  /**
   * Constructor: initializes the {@link #vecComp}.
   */
  public Model() {
  
    this.vecComp = new Vector<ComponentModel>();
  }
  
  
  /**
   * Insert component into the component model class.
   * @param xid             the add identifier (is equal to the added 
   *                        component's location inside the getComponents()
   *                        -array
   * @param xLocationinmap  the location of the new added component inside
   *                        rectangle of which the gridLayout consists.
   */
  public boolean addComponent(final int xid, final Point xLocationinmap,
      final Control xcontrol) {
    
    if (getComponentModel(xLocationinmap) == null) {

      // Increase the size of the current row.
      xcontrol.setMinAmountRows(xLocationinmap.x + 1);
      xcontrol.setMinAmountColumns(xLocationinmap.y + 1);

      
      vecComp.add(new ComponentModel(xid, this, xLocationinmap)); 
      return true;
    } else {
      LoggerRegistry.log("Error adding component:"
          + " Model component has already been added.");
      return false;
    }
  }
  

  public ComponentModel getComponentModel(final int xComponentaddid) {
    for (int indx = 0; indx < vecComp.size(); indx++) {
      if (vecComp.get(indx).getAddIdentifier() == xComponentaddid) {
        return vecComp.get(indx);
      }
    }
    return null;
  }

  public ComponentModel getComponentModel(final Point xpoint) {
    for (int indx = 0; indx < vecComp.size(); indx++) {
      if (vecComp.get(indx).getPntLocationinmap().x == xpoint.x
          && vecComp.get(indx).getPntLocationinmap().y == xpoint.y) {
        return vecComp.get(indx);
      }
    }
    return null;
  }

  public int getComponentModelid(final Point xpoint) {
    for (int indx = 0; indx < vecComp.size(); indx++) {
      if (vecComp.get(indx).getPntLocationinmap().x == xpoint.x
          && vecComp.get(indx).getPntLocationinmap().y == xpoint.y) {
        return indx;
      }
    }
    return -1;
  }
  

  /**
   * Return the {@link #vecComp}.
   * @return the vecSep
   */
  public Vector<ComponentModel> getVecComp() {
    return vecComp;
  }

  /**
   * Return the {@link #amountCols}.
   * @return the amountCols
   */
  public int getAmountCols() {
    
    return amountCols;
  }

  /**
   * Set the {@link #amountCols}.
   * @param xamountCols the amountCols to set
   * @param xcontrol    instance of the main controller class.
   */
  public synchronized void setAmountCols(int xamountCols, 
      final Control xcontrol) {
    

    for (int i = 0; i < xcontrol.getView().getVecSep().size(); i++) {

      //
      // if xamountCols is less than amountCols, remove the separators from view
      // that are now superficial.
      final int col = xcontrol.getView().getVecSep().get(i).getCol();
      if (col >= xamountCols) {

        // remove component from GUI and out of vector of separators.
        xcontrol.getView().remove(xcontrol.getView().getVecSep().get(i));
        xcontrol.getView().getVecSep().remove(i);
      }
    }
    
    //
    // if xamountCols is greater than amountCols, add new separators to view.
    for (int col = amountCols; col < xamountCols; col++) {
      for (int row = 0; row < amountRows; row++) {

        final Separator sep1 = new Separator(
            true, row, col, xcontrol);
        final Separator sep2 = new Separator(
            false, row, col, xcontrol);
        
        xcontrol.getView().addSeparator(sep1);
        xcontrol.getView().addSeparator(sep2);
        
      }
    }
    
    this.amountCols = xamountCols;
  }

  /**
   * Return the {@link #amountRows}.
   * @return the amountRows
   */
  public int getAmountRows() {
    return amountRows;
  }
  
  
  /**
   * Return information string for debugging.
   * @return information string for debugging.
   */
  public String print() {
    
    String result = "";
    for (int i = 0; i < vecComp.size(); i++) {
      result += vecComp.get(i).print() + "\n";
      
    }
    return result;
  }

  /**
   * Set the {@link #amountRows}.
   * @param amountRows  the amountRows to set
   * @param xcontrol    instance of the main controller class.
   */
  public void setAmountRows(int xamountRows, 
      final Control xcontrol) {

    for (int i = 0; i < xcontrol.getView().getVecSep().size(); i++) {

      //
      // if xamountCols is less than amountCols, remove the separators from view
      // that are now superficial.
      final int row = xcontrol.getView().getVecSep().get(i).getRow();
      if (row >= xamountRows) {

        // remove component from GUI and out of vector of separators.
        xcontrol.getView().remove(xcontrol.getView().getVecSep().get(i));
        xcontrol.getView().getVecSep().remove(i);
      }
    }
    
    //
    // if xamountCols is greater than amountCols, add new separators to view.
    for (int row = amountRows; row < xamountRows; row++) {
      for (int col = 0; col < amountCols; col++) {

        xcontrol.getView().addSeparator(new Separator(
            true, row, col, xcontrol));
        xcontrol.getView().addSeparator(new Separator(
            false, row, col, xcontrol));
      }
    }
    
    this.amountRows = xamountRows;
  }
  
  
  
  
  
  
  /**
   * Return the componentModel that is closest to the specified location.
   */
  public final ComponentModel getClosestComponent(
      final double xPerc, final double yPerc) {
    
    double mindist = Double.MAX_VALUE;
    ComponentModel cm = null;
    
    for (int i = 0; i < vecComp.size(); i++) {
      final ComponentModel cmc = vecComp.get(i);
      final double x = cmc.getRecSizePercentage().getX();
      final double y = cmc.getRecSizePercentage().getY();
      final double wid = cmc.getRecSizePercentage().getWidth();
      final double hei = cmc.getRecSizePercentage().getHeight();

      
      
      final double mindistx;
      if (x <= xPerc && x + wid >= xPerc) {
        mindistx = 0;
      } else {

        mindistx = Math.min(
            Math.abs(x - xPerc), 
            Math.abs(x + wid - xPerc)); 
      }

      final double mindisty;
      if (y <= yPerc && y + hei >= yPerc) {
        mindisty = 0;
      } else {

        mindisty = Math.min(
            Math.abs(y - yPerc), 
            Math.abs(y + hei - yPerc)); 
      }
      final double distC = Math.sqrt(mindistx * mindistx 
          + mindisty * mindisty);
      
      if (distC < mindist) {
        mindist = distC;
        cm = cmc;
        
      }
    }
    return cm;
  }
}
