package movelistener.control;

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

//import declarations
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;


/**
 * The controller class which changes the position of a component by 
 * a user dragging it.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Movelistener extends MouseMotionAdapter 
    implements MouseListener, Serializable {
  
  /**
   * contains whether the user currently drags the component.
   */
  private boolean pressed = false;

  /**
   * the component which is to be moved.
   */
  private final Component cmpMoved;

  /**
   * the position from where the drag started.
   */
  private Point pntStartPosition;


  /**
   * Constructor saves component of which this class changes the location.
   * @param xview the component
   */
  public Movelistener(final Component xview) {

    //save component
    this.cmpMoved = xview;
  }


  /**
   * Empty.
   * {@inheritDoc}
   */
  public final void mouseClicked(final MouseEvent xevent) { }

  /**
   * Empty.
   * {@inheritDoc}
   */
  public final void mouseEntered(final MouseEvent xevent) { }

  /**
   * Empty.
   * {@inheritDoc}
   */
  public final void mouseExited(final MouseEvent xevent) { }

  /**
   * Empty.
   * {@inheritDoc}
   */
  public final void mousePressed(final MouseEvent xevent) {

    //save start position and set pressed
    pntStartPosition = new Point(xevent.getXOnScreen() - cmpMoved
        .getLocation().x, xevent.getYOnScreen() - cmpMoved
        .getLocation().y);
    pressed = true;

  }

  /**
   * Invoked when a mouse button has been released on a component.
   * @param xevent  the event.
   */
  public final void mouseReleased(final MouseEvent xevent) {
    
    //set not pressed and repaint component if mouse is released
    pressed = false;
    cmpMoved.repaint();
  }

  /**
   * {@inheritDoc}
   */
  public final synchronized void mouseDragged(final MouseEvent xevent) {


    //if pressed change location
    if (pressed) {
      cmpMoved.setLocation(xevent.getXOnScreen() - pntStartPosition.x,
          xevent.getYOnScreen() - pntStartPosition.y);
    }

    //repaint
    cmpMoved.repaint();
  }
  
}
