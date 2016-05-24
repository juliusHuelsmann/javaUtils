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

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import other.DoubleRectangle;
import separableview.control.Control;
import separableview.model.ComponentModel;
import movelistener.control.Movelistener;



/**
 * A separator is used for being able to adapt the size of adjacent components
 * by dragging their borders.
 * 
 * @author Julius Hülsmann
 * @version $I$, $U$
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Separator extends JLabel {

  /**
   * The minimum amount of size in pixel that a component may have.
   */
  private final int threshold = 5;
  
  /**
   * The separator's thickness.
   * @since 1.0
   */
  public final static int THICKNESS = 7;

  private int row;
  private int col;

  private boolean leftrightShift;

  private Control control;
  
  public Separator(final boolean xleftright, final int xrow, final int xcol, 
      final Control xcontrol) {

    // Add move listener
//    super.setFont(new Font("courier new", Font.PLAIN, 6));
//    final String s;
//    if (xleftright) {
//      s = "-";
//    } else {
//      s = "o";
//    }
//    super.setText(xrow + s +  xcol + "");
    Movelistener mpt = new Movelistener(this);
    super.addMouseListener(mpt);
    super.addMouseMotionListener(mpt);
    
    // set opaque
    super.setOpaque(true);
    super.setBorder(BorderFactory.createEtchedBorder(
        Color.gray, Color.darkGray));
    super.setBackground(Color.black);//lightGray
    
    this.leftrightShift = xleftright;
    this.row = xrow;
    this.col = xcol;
    this.control = xcontrol;
  }

  public void adaptToComponentLocation() {
    final Component cmpUpper = control.getComponentbygrid(row, col);
    final Component cmpLower;
    if (leftrightShift) {

     
      cmpLower = control.getComponentbygrid(row, col + 1);
    } else {

      cmpLower = control.getComponentbygrid(row + 1, col);
      
    }

    if (cmpUpper == null) {
      if (cmpLower == null) {
        return;
      } else {
        adaptToComponentLocationLower();
      }
    } else {
      adaptToComponentLocationUpper();
    }
  }
  
  public void adaptToComponentLocationUpper() {

    final Component cmpUpper = control.getComponentbygrid(row, col);
    final ComponentModel cmpMdlUpper = control.getModelcomponentbygrid(
        row, col);
    final int ownerWidth = control.getView().getSize().width;
    final int ownerHeight = control.getView().getSize().height;
    
    if (leftrightShift) {
      
      super.setSize(THICKNESS, 
          (int) Math.round(ownerHeight * cmpMdlUpper.getRecSizePercentage().getHeight()));
      
      final int locX = (int) Math.round(ownerWidth * (cmpMdlUpper.getRecSizePercentage().getX() 
              + cmpMdlUpper.getRecSizePercentage().getWidth()));
      final int locY = (int) Math.round(ownerHeight * (cmpMdlUpper.getRecSizePercentage().getY()));

      super.setLocation(locX, locY);
//      setLocationWithoutNotify(cmpUpper.getX() + cmpUpper.getWidth(),
//          cmpUpper.getY());
    } else {

      super.setSize((int) Math.round(ownerWidth 
          * cmpMdlUpper.getRecSizePercentage().getWidth()),
          THICKNESS);

      final int locX = (int) Math.round(ownerWidth * cmpMdlUpper.getRecSizePercentage().getX());
      final int locY = (int) Math.round(ownerHeight * (cmpMdlUpper.getRecSizePercentage().getY() 
          + cmpMdlUpper.getRecSizePercentage().getHeight()));

      super.setLocation(locX, locY);
//      setLocationWithoutNotify(cmpUpper.getX(), 
//          cmpUpper.getY() + cmpUpper.getHeight());
    }
  }

  public void adaptToComponentLocationLower() {

    final int ownerWidth = control.getView().getSize().width;
    final int ownerHeight = control.getView().getSize().height;
    
    if (leftrightShift) {

      final ComponentModel cmpMdlLower = control.getModelcomponentbygrid(
          row, col + 1);

      super.setSize(THICKNESS, 
         (int) Math.round(
             cmpMdlLower.getRecSizePercentage().getHeight() * ownerHeight));

      final int locX = (int) Math.round(cmpMdlLower.getRecSizePercentage().getX() * ownerWidth);
      final int locY = (int) Math.round( cmpMdlLower.getRecSizePercentage().getY() * ownerHeight);

      super.setLocation(locX, locY);
//      setLocationWithoutNotify(
//          ownerWidth * cmpMdlLower.getRecSizePercentage().x,
//          ownerHeight * cmpMdlUpper.getRecSizePercentage().y);

    } else {

      final ComponentModel cmpMdlLower = control.getModelcomponentbygrid(
          row + 1, col);
      super.setSize((int) Math.round(
          cmpMdlLower.getRecSizePercentage().getWidth() * ownerWidth),
          THICKNESS);

      final int locX = (int) Math.round(
          1.0 * cmpMdlLower.getRecSizePercentage().getX() * ownerWidth);
      final int locY = (int) Math.round(
          1.0 * cmpMdlLower.getRecSizePercentage().getY() * ownerHeight);

      super.setLocation(locX, locY);
//      setLocationWithoutNotify(locX, locY);

    }
  }
  
//  private void setLocationWithoutNotify(final int _x, final int _y) {
//
//    final int x;
//    final int y;
//    if (leftrightShift) {
//      x = _x;
//      y = (int) (1.0 * percY * cmp_owner.getHeight());
//    } else {
//      x = (int) (1.0 * percX * cmp_owner.getWidth());
//      y = _y;
//    }
//    super.setLocation(x, y);
//    
//    
//    if (leftrightShift) {
//
//      int difX = x + thickness - cmpLower.getX();
//      cmpLower.setLocation(x + thickness, cmpLower.getY());
//      cmpLower.setSize(cmpLower.getWidth() - difX, cmpLower.getHeight());
//      cmpUpper.setSize(-cmpUpper.getX() + x, cmpUpper.getHeight());
//    } else {
//
//      int difY = y + thickness - cmpLower.getY();
//      cmpLower.setLocation(cmpLower.getX() , y + thickness);
//      cmpLower.setSize(cmpLower.getWidth(), cmpLower.getHeight() - difY);
//      cmpUpper.setSize(cmpUpper.getWidth(), y - cmpUpper.getY());
//    }
//  }
  
//  public void onOwnersizechanged() {
//    setLocationWithoutNotify((int) (1.0 * percX * cmp_owner.getWidth()),
//        (int) (1.0 * percY * cmp_owner.getHeight()));
//  }

  /**
   * Return information string for debugging.
   * @return information string for debugging.
   */
  public String print() {
    
    return "SEP\t" + row + ", " + col + ": " + getX() + ", " + getY() + "," 
        + getWidth() + ", " + getHeight() + leftrightShift;
  }
  
  
  public void changeComponents(final int xrow, final int xcol) {
    this.row = xrow;
    this.col = xcol;
    adaptToComponentLocation();
//    setLocation(getX(), getY());
  }

  public void setLocation(final int xx, final int xy) {
    
    final int prevx = getX();
    final int prevy = getY();
    
    if (leftrightShift) {
      super.setLocation(xx, getY());
    } else {
      super.setLocation(getX(), xy);
    }
    
    
    //
    // If the shift is illegal, restore the old configuration
    if (!adaptModelComponents()) {
      super.setLocation(prevx, prevy);
    }
  }
  
  /**
   * Adapt location and size of model components after the separator moved.
   */
  private boolean adaptModelComponents() {

    
    //
    // Get the indices of the predecessor and the successor element
    final int rowPre = row;
    final int colPre = col;
    
    final int rowSuc;
    final int colSuc;
    if (leftrightShift) {
 
      colSuc = col + 1;
      rowSuc = row;
    } else {
      colSuc = col;
      rowSuc = row + 1;
    }
    
    double prex = 0;
    double prey = 0;
    double prew = 0;
    double preh = 0;
    
    
    
    
    // 
    // Adapt the location of the predecessor model component
    if (control.getModelcomponentbygrid(rowPre, colPre) != null) {

      prex = control.getModelcomponentbygrid(rowPre, colPre)
          .getRecSizePercentage().getX();
      prey = control.getModelcomponentbygrid(rowPre, colPre)
          .getRecSizePercentage().getY();
      
      if (leftrightShift) {

        preh = control.getModelcomponentbygrid(rowPre, colPre)
            .getRecSizePercentage().getHeight();
        prew = 1.0 * getX() / control.getView().getSize().width - prex;
      } else {

        prew = control.getModelcomponentbygrid(rowPre, colPre)
            .getRecSizePercentage().getWidth();
        preh = 1.0 * getY() / control.getView().getSize().height - prey;
        
      }
    }

    double sucx = 0;
    double sucy = 0;
    double sucw = 0;
    double such = 0;

    //
    // Adapt the location of the successor model component
    if (control.getModelcomponentbygrid(rowSuc, colSuc) != null) {
      final double suc1wCoordperc = control.getModelcomponentbygrid(
          rowSuc, colSuc).getRecSizePercentage().getX()
          + control.getModelcomponentbygrid(rowSuc, colSuc)
          .getRecSizePercentage().getWidth();
      final double suc1hCoordperc = control.getModelcomponentbygrid(
          rowSuc, colSuc).getRecSizePercentage().getY()
          + control.getModelcomponentbygrid(rowSuc, colSuc)
          .getRecSizePercentage().getHeight();
      
      if (leftrightShift) {

        sucx = 1.0 * getX() / control.getView().getSize().width
            + 1.0 * Separator.THICKNESS / control.getView().getSize().width;
        sucy = 1.0 * getY() / control.getView().getSize().height;
      } else {

        sucx = 1.0 * getX() / control.getView().getSize().width;
        sucy = 1.0 * getY() / control.getView().getSize().height
            + 1.0 * Separator.THICKNESS / control.getView().getSize().height;
      }

      sucw = suc1wCoordperc - sucx;
      such = suc1hCoordperc - sucy;
      
    }
    
    
    //
    // Check if the move operation is valid
    final int wpred = (int) Math.round(
        1.0 * prew * control.getView().getSize().width);
    final int hpred = (int) Math.round(
        1.0 * preh * control.getView().getSize().height);
    final int wsucc = (int) Math.round(
        1.0 * sucw * control.getView().getSize().width);
    final int hsucc = (int) Math.round(
        1.0 * such * control.getView().getSize().height);
    
    if (wpred >= threshold &&  hpred >= threshold 
        && wsucc >= threshold && hsucc >= threshold) {
      if (control.getModelcomponentbygrid(rowPre, colPre) != null) {

        control.getModelcomponentbygrid(rowPre, colPre).setRecSizePercentage(
            new DoubleRectangle(prex, prey, prew, preh), control.getView()); 
      }

      //
      // Adapt the location of the successor model component
      if (control.getModelcomponentbygrid(rowSuc, colSuc) != null) {

        control.getModelcomponentbygrid(rowSuc, colSuc).setRecSizePercentage(
            new DoubleRectangle(sucx, sucy, sucw, such), control.getView());
      }
      
      //
      // update the component's location in view.
      control.refreshCompSepBounds();
      
      
      return true;
    } else {
      return false;
    }
    

   
    
  }

  /**
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * @param row the row to set
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * @return the col
   */
  public int getCol() {
    return col;
  }

  /**
   * @param col the col to set
   */
  public void setCol(int col) {
    this.col = col;
  }

}
