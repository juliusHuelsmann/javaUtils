package console.view;

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

import utils.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import log.LoggerRegistry;
import console.control.TerminalListener;



/**
 * Console view class consisting of a JPanel that displays text (through which 
 * the user is able to scroll).
 * It is also possible to type commands into the Console. The rough appearance
 * is based on a typical Unix terminal.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Console extends JPanel {

  
  /**
   * JTextArea containing both the output printed by program and the user 
   * input.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private JTextArea jtaStuff;
  
  /**
   * JScrollPane that owns the JTextArea.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private JScrollPane jsp;
  
  
  /**
   * Integer indicating the char count, at which the user prompt starts.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private int commandStarts = 0;
  
  /**
   * Prefix that is printed behind the last output and before the user prompt.
   * It indicates the possibility to type in a command.
   * 
   * <p>
   * Is initialized inside the constructor and is empty in case the Console is
   * only used for input.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private final String prefix;
  
  
  /**
   * This boolean can be set to avoid / enable auto scrolling to the latest
   * output in case new content is displayed. 
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private boolean scrollUpdate = true;
  

  /**
   * Maximal line length in characters.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private int maxLinelength = 500;

  
  /**
   * TerminalListener that can be set for listening to user input in case the 
   * Console is not set to only display text.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private TerminalListener tl;

  
  /**
   * Constructor: Initializes and alters the instance of JPanel and its 
   * contents.
   * 
   * <p>
   * Especially creates
   *    the JTextArea {@link #jtaStuff} (overrides some methods)
   *    an instance of KeyListener 
   *    an instance of CaretListener
   * 
   * @param textInput
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public Console(final boolean textInput) {
    super();
    super.setLayout(null);
    super.setBorder(BorderFactory.createLineBorder(Color.black));
    final Color clr_background = new Color(71, 71, 71);//91, 91, 91
    super.setBackground(clr_background);
    
    if (textInput) {
      prefix = "        > ";
    } else {
      prefix = "";
    }
    
    
    
    //
    // Caret listener that checks that no content of the 
    CaretListener cl = new CaretListener() {

      /**
       * {@inheritDoc}
       * 
       * <p>
       * Only allows keyboard input in case the caret position is greater than
       * {@link #commandStarts}.
       * 
       * @param xe  the caretEvent
       * 
       * @author Julius Huelsmann
       * @version %I%, %U%
       * @since 1.0
       */
      public void caretUpdate(CaretEvent xe) {

        //
        // Only allow keyboard input in case the caret position is greater than
        // commandStarts.
        if (commandStarts <= jtaStuff.getText().length()) {
          
          if (xe.getDot() < commandStarts) {
            jtaStuff.setEditable(false);
            jtaStuff.setCaretPosition(commandStarts);
          } else {
            jtaStuff.setEditable(true);
          }
        }
      }
    };
    
    
    //
    // JTextArea that automatically inserts the prefix for user input and
    // that sets the caret position in case focus is requested to the first
    // character of the typed command.
    jtaStuff = new JTextArea() {

      /**
       * {@inheritDoc}
       * 
       * <p>
       * Sets the caret position to the beginning of the text in case the 
       * focus is gained.
       * 
       * @author Julius Huelsmann
       * @version %I%, %U%
       * @since 1.0
       */
      @Override
      public final void requestFocus() {
        super.requestFocus();
        jtaStuff.setCaretPosition(jtaStuff.getText().length());
      }

      /**
       * {@inheritDoc}
       * 
       * <p>
       * Moves the {@link #prefix} to the end of the string each time text 
       * is appended. Updates the {@link #commandStarts} afterwards. 
       * 
       * @author Julius Huelsmann
       * @version %I%, %U%
       * @since 1.0
       */
      @Override
      public final void append(final String xtext) {
        

        //
        // Remove the prefix
        final String oldText = getText();
        if (oldText.endsWith(prefix)) {
          super.setText(oldText.substring(0, 
              oldText.length() - prefix.length()));
        }
        
        //
        // Append text plus prefix
        super.append(Utils.prepareText(xtext, maxLinelength) + prefix);
        
        //
        // Update the commandStarts value.
        commandStarts = getText().length();
      }
      

      /**
       * {@inheritDoc}
       * 
       * <p>
       * Updates the {@link #commandStarts} after setting text. 
       * 
       * @author Julius Huelsmann
       * @version %I%, %U%
       * @since 1.0
       */
      @Override
      public void setText(final String xtext) {
        super.setText(Utils.prepareText(xtext, maxLinelength) + prefix);
        commandStarts = getText().length();
      }
      
      
      
      
    };
    jtaStuff.addCaretListener(cl);
    jtaStuff.setEditable(textInput);
    jtaStuff.setFont(new Font("Courier new", Font.PLAIN, 9));
    jtaStuff.setForeground(new Color(215, 215, 215));
    jtaStuff.setBackground(clr_background);
    jtaStuff.setFocusable(textInput);
    
    
    //
    //
    // If the Console accepts textInput, add a keyListener that 
    if (textInput) {
      
      jtaStuff.addKeyListener(new KeyListener() {


        public void keyTyped(KeyEvent xe) { }

        /**
         * Key code of remove key.
         * 
         * @author Julius Huelsmann
         * @version %I%, %U%
         * @since 1.0
         */
        private final int keycodebackRemove = 8;

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Calls TerminalListener if enter character has been pressed. 
         * 
         * @author Julius Huelsmann
         * @version %I%, %U%
         * @since 1.0
         */
        public final void keyReleased(KeyEvent xe) {
        
          switch (xe.getKeyCode()) {
            case KeyEvent.VK_ENTER:
              if (xe.getKeyCode() == (KeyEvent.VK_ENTER)) {
                if (tl != null) {
                  final String text = jtaStuff.getText();

                  tl.keyEntered(text.substring(commandStarts,
                      text.length() - 1));    
                }
                jtaStuff.append("");
              }
              break;
            default:
              break;
          }
        }


        /**
         * {@inheritDoc}
         * 
         * <p>
         * In case the caret is at an illegal position, the remove and the
         * delete functionality is disabled. 
         * 
         * @author Julius Huelsmann
         * @version %I%, %U%
         * @since 1.0
         */
        public void keyPressed(KeyEvent evt) {
          if (evt.getKeyCode() == (keycodebackRemove) 
              && jtaStuff.getCaretPosition() <= commandStarts) {
            
            jtaStuff.setEditable(false);
            return;
          } else if (evt.getKeyCode() == (KeyEvent.VK_DELETE) 
              && jtaStuff.getCaretPosition() < commandStarts) {
            jtaStuff.setEditable(false);
            return;
          } else {
            jtaStuff.setEditable(true);
            
          }
        }
      });
    }
    
    jsp = new JScrollPane(jtaStuff);
    jsp.setSize(getWidth(), getHeight());
    jsp.setLocation(0, 0);
    jsp.setOpaque(false);
    jsp.setBorder(null);
    super.add(jsp);
  }
  


  /**
   * Indents each line of the parameter by the prefix' length and pass it to 
   * the methods {@link #logAnswer(String)} which appends the string to the
   * {@link #jtaStuff}. 
   * 
   * @see logAnswer
   * @param xtext     the text that is appended to the {@link #jtaStuff} after
   *                  being modified.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public final void log(final String xtext) {

    //
    // Indent each line by the length of prefix
    String indent = "";
    for (int in = 0; in < prefix.length(); in++) {
      indent += " ";
    }
    final String text = xtext.replaceAll("\n", "\n" + indent);
    
    //
    // Call the log method afterwards.
    logAnswer(text);
  }
  
  

  /**
   * Indents each line of the parameter by the prefix' length and pass it to 
   * the methods {@link #overwriteAnswer(String)} which sets the string as 
   * the {@link #jtaStuff}'s new content. 
   * 
   * @see overwriteAnswer
   * @param xtext     the text that is set content of the {@link #jtaStuff}
   *                  after being modified.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public final void overwrite(final String xtext) {

    //
    // Indent each line by the length of prefix
    String indent = "";
    for (int in = 0; in < prefix.length(); in++) {
      indent += " ";
    }
    final String text = xtext.replaceAll("\n", "\n" + indent);

    //
    // Call the log method afterwards.
    overwriteAnswer(text);
  }
  

  
  /**
   * Appends text to the JTextArea and in case {@link #scrollUpdate} is enabled
   * scroll down to the last line.
   * 
   * @param xtext   the text that is appended.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void logAnswer(final String xtext) {
    
    try {
      final String text = xtext;
      jtaStuff.append(text + "\n");
      if (jtaStuff.getText() != null && !jtaStuff.getText().equals("")) {
  
        jtaStuff.setSize(jtaStuff.getPreferredSize());
      }
      
      if (scrollUpdate) {
  
        jsp.getVerticalScrollBar().setValue( 
            jsp.getVerticalScrollBar().getMaximum());  
      }
    } catch (java.lang.Error xe) {
      
      // This error occurs in case the thread is interrupted while printing
      // something. Do nothing.
      LoggerRegistry.log("Interrupted View thread" + xe);
    }
    
  }
  
  
  /**
   * Sets text to the JTextArea and in case {@link #scrollUpdate} is enabled
   * scroll down to the last line.
   * 
   * @param xtext     the text that is set content of the JTextArea.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void overwriteAnswer(final String xtext) {
    
    try {

      final int prevValue = jsp.getVerticalScrollBar().getValue();
      final String text = xtext;
      jtaStuff.setText("");
      jtaStuff.append(text + "\n");
      jtaStuff.setSize(jtaStuff.getPreferredSize());

      if (scrollUpdate) {

        jsp.getVerticalScrollBar().setValue( 
            jsp.getVerticalScrollBar().getMaximum());  
      } else {
//        System.out.println(d + "\t" + Math.min(
//            prevValue, jsp.getVerticalScrollBar().getMaximum()));
        jsp.getVerticalScrollBar().setValue(Math.min(
            prevValue, jsp.getVerticalScrollBar().getMaximum()));
//        System.out.println(d + "\t" + jsp.getVerticalScrollBar().getValue());
//        System.out.println();
      }
    } catch (java.lang.Error xe) {
      
      // This error occurs in case the thread is interrupted while printing
      // something. Do nothing.
      LoggerRegistry.log("Interrupted View thread" + xe);
    }
  }

  
  
  /*
   * @Override - Methods
   */

  
  /**
   * {@inheritDoc}
   * 
   * <p>
   * Set the console's size.
   * @param xwidth  the new width.
   * @param xheight the new height.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  @Override
  public void setSize(final int xwidth, final int xheight) {
    super.setSize(xwidth, xheight);
    jsp.setSize(xwidth, xheight);
    if (jtaStuff.getText() != null && !jtaStuff.getText().equals("")) {
      jtaStuff.setSize(
          Math.max(jtaStuff.getPreferredSize().width, xwidth),
          Math.max(jtaStuff.getPreferredSize().height, xheight));  
    }
  }



  /**
   * {@inheritDoc}
   * 
   * <p>
   * Add super MouseListener and MouseListener to the JTextArea.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  @Override
  public void addMouseListener(final MouseListener xl) {
    jtaStuff.addMouseListener(xl);
    super.addMouseListener(xl);
  }

  
  /**
   * {@inheritDoc}
   * 
   * <p>
   * Add super Mouse-Motion-Listener and Mouse-Motion-Listener to the 
   * JTextArea.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  @Override
  public void addMouseMotionListener(final MouseMotionListener xl) {
    jtaStuff.addMouseMotionListener(xl);
    super.addMouseMotionListener(xl);
    
  }
  
  
  
  
  
  /*
   * Getter- and setter methods.
   */
  
  
  
  /**
   * Return the {@link #scrollUpdate}.
   * @return the scrollUpdate
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public boolean isScrollUpdate() {
    return scrollUpdate;
  }
  
  /**
   * Set the {@link #scrollUpdate}.
   * @param scrollUpdate the scrollUpdate to set
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void setScrollUpdate(boolean scrollUpdate) {
    this.jtaStuff.setAutoscrolls(scrollUpdate);
    this.scrollUpdate = scrollUpdate;
  }



  /**
   * Setter method for {@link #tl}. 
   * @see #tl
   * @param xtl   Instance of a TerminalListener.
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void setTerminalListener(final TerminalListener xtl) {
    this.tl = xtl;
  }



  /**
   * Return the {@link #maxLinelength}.
   * @return the maxLinelength
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public final int getMaxLinelength() {
    return maxLinelength;
  }



  /**
   * Set the {@link #maxLinelength}.
   * @param xmaxLinelength the maxLinelength to set
   *                  
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public final void setMaxLinelength(final int xmaxLinelength) {
    this.maxLinelength = xmaxLinelength;
  }
}
