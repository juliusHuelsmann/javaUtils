package utils;

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
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import log.LoggerRegistry;

/**
 * Contains static utility methods of all sorts.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
public class Utils {

  /**
   * Constants that indicate whether an execution (of a command inside the
   * terminal) was successful or not.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String EXECUTION_SUCCESS = "Success: ";
  
  /**
   * Constants that indicate whether an execution (of a command inside the
   * terminal) was successful or not.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String EXECUTION_FAILED = "Failure: ";

  /**
   * Invalid characters of different level. Level 3 is the level which is
   * most inside the data structure.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static char invalidCharLevel3 = ',';

  /**
   * Invalid characters of different level. Level 3 is the level which is
   * most inside the data structure.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static char invalidCharLevel2 = ';';

  /**
   * Invalid characters of different level. Level 3 is the level which is
   * most inside the data structure.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static char invalidCharLevel1 = ':';
  
  
  /**
   * String array containing all the invalidCharacters.
   * {@link #invalidCharLevel1}, {@link #invalidCharLevel2} and
   * {@link #invalidCharLevel3}.
   * 
   * @see invalidCharLevel1
   * @see invalidCharLevel2
   * @see invalidCharLevel3
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private static String[] invalidCharacters = new String[]{
    "" + invalidCharLevel3, 
    "" + invalidCharLevel2,
    "" + invalidCharLevel1};

  /**
   * Checks whether a string contains invalid characters.
   * @param xstring which is tested
   * @return        whether the parameter contains invalid characters.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static boolean containsInvalidCharacters(final String xstring) {

    for (String s : invalidCharacters) {
      if (xstring.contains(s)) {
        return true;
      }
    }
    
    return false;
  }
  
  /**
   * Removes all the invalid characters from string passed to the function.
   * 
   * @param xstring the string which may contain invalid characters
   * @return        the input string without invalid characters.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String removeInvalidCharacters(final String xstring) {
    
    String strRet = xstring;
    for (String s : invalidCharacters) {
      strRet = strRet.replaceAll(s, "");
    }
    
    return strRet;
  }
  
  /**
   * Returns human readable string of invalid characters.
   * @return  human readable string of invalid characters.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String getListOfInvalidCharacters() {

    String strRet = "(";
    for (String s : invalidCharacters) {
      strRet += "\"" + s + "\",";
    }
    return strRet.substring(0, strRet.length() - 1) + ")";
    
    
  }
  
  
  /**
   * Print error message. Is to be called in case the user entered something
   * which contains invalid characters.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void printMessageInvalidCharacters() {
    LoggerRegistry.log("Error: The name must not "
        + " contain invalid characters: "
        + Utils.getListOfInvalidCharacters() 
        + ". The invalid characters will"
        + " be removed.");
  }
  

    
  /**
   * Function for resizing a bufferedImage.
   * 
   * @param xbi       the bufferedImage that is to be resized
   * @param xwidth    its new width 
   * @param xheight   its new height
   * 
   * @return          the resized BufferedImage.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static BufferedImage resize(
      final BufferedImage xbi, final int xwidth, final int xheight) { 
     
      
    Image imgScaled = xbi.getScaledInstance(
        xwidth, 
        xheight, 
        Image.SCALE_SMOOTH);
      
    BufferedImage bi = new BufferedImage(
        xwidth, 
        xheight, 
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = bi.createGraphics();
    g2d.drawImage(imgScaled, 0, 0, null);
    g2d.dispose();

    return bi;
  }  

  /**
   * Read an image from file and resize it afterwards.
   * 
   * @param xpath     the path to the bufferedImage that is to be resized
   * @param xwidth    its new width 
   * @param xheight   its new height
   * 
   * @return          the resized BufferedImage.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static BufferedImage resize(
      final String xpath, final int xwidth, final int xheight) { 
     
    BufferedImage imgScaled;
    try {

      String myPath = Utils.class.getResource(xpath).getPath();
      myPath = convertString(myPath); 

      imgScaled = ImageIO.read(Utils.class.getResourceAsStream(xpath));
      return resize(imgScaled, xwidth, xheight);
    } catch (IOException e) {


      LoggerRegistry.log("" + xpath);
      e.printStackTrace();
      return null;
    }
  }  


  /**
   * Replace miss-built characters from string.
   * @param xinput  input string which is altered and returned.
   * @return        string that contains characters instead of ASCII codes.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String convertString(final String xinput) {
    return xinput.replaceAll("%20"," ")
        .replaceAll("%c3%bc","ü")
        .replaceAll("%c3%a4","ä")
        .replaceAll("%c3%b6","ö");
  }

    
    
    
    
    

  /**
   * Apply stroke on background.
   * @param xjlblStroke  the background carrying item.
   * @return              the stroked image.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static BufferedImage getStroke(final JComponent xjlblStroke) {
    
    
    final int strokeDistance = 10;
    if (xjlblStroke.getWidth() > 0 
        && xjlblStroke.getHeight() > 0
//        && jlbl_stroke.isShowing()
        ) {
      BufferedImage biStroke = new BufferedImage(xjlblStroke.getWidth(),
          xjlblStroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
          
      for (int x = 0; x < biStroke.getWidth(); x++) {
        for (int y = 0; y < biStroke.getHeight(); y++) {
                

          try {
            if ((x + xjlblStroke.getLocationOnScreen().x
                + y + xjlblStroke.getLocationOnScreen().y) 
                % strokeDistance == 0) {
              final int clr = 10;
              biStroke.setRGB(x, y, 
                  new Color(clr, clr, clr, clr).getRGB());
            } else {  

              final int clr = 0;
              biStroke.setRGB(x, y, 
                  new Color(clr, clr, clr, clr).getRGB());
            }
          } catch (IllegalComponentStateException e) {
            x = biStroke.getWidth();
            y = biStroke.getHeight();
          }
              
        }  
      }
      if (xjlblStroke instanceof JButton) {

        ((JButton)xjlblStroke).setIcon(new ImageIcon(biStroke));  
      } else if (xjlblStroke instanceof JLabel) {

        ((JLabel)xjlblStroke).setIcon(new ImageIcon(biStroke));  
      }
      return biStroke;
    }
    return null;
    
  }   
  
  
  
  
  
  
  
  
  /**
   * Apply stroke on background.
   * @param xjlblStroke  the background carrying item.
   * @return              the stroked image.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static BufferedImage getStrokeMove(
      final BufferedImage xbi, final JLabel xjlblStroke, 
      final int xdX, final int xdY) {
    
    if (xdY >= xbi.getHeight() || xdX >= xbi.getWidth()) {
      return getStroke(xjlblStroke);
    } 
    
    
    final int strokeDistance = 10;
    if (xbi.getWidth() > 0 
        && xbi.getHeight() > 0) {
      

      if (Math.abs(xdX) > xbi.getWidth() / 2
          || Math.abs(xdY) > xbi.getHeight() / 2) {

        return getStroke(xjlblStroke);
      } else {
        Rectangle recMaintainStart = new Rectangle();
        Rectangle recMaintainDest = new Rectangle();
        Rectangle recNew1 = new Rectangle();
        Rectangle recNew2 = new Rectangle();

        if (xdX > 0) {

          recMaintainStart.x = xdX;
          recMaintainDest.x = 0;
          recMaintainDest.width = xbi.getWidth() - xdX;

          recNew1.x = recMaintainDest.width;
          recNew1.width = xdX;
          recNew1.height = xbi.getHeight();
          recNew1.y = 0;
        } else {

          recMaintainStart.x = 0;
          recMaintainDest.x = -xdX;
          recMaintainDest.width = xbi.getWidth() + xdX;

          recNew1.x = 0;
          recNew1.width = -xdX;
          recNew1.height = xbi.getHeight();
          recNew1.y = 0;
        }

        if (xdY > 0) {
          

          recMaintainStart.y = xdY;
          recMaintainDest.y = 0;
          recMaintainDest.height = xbi.getHeight() - xdY;

          recNew2.y = xbi.getHeight() - xdY;
          recNew2.height = xdY;
          recNew2.x = 0;
          recNew2.width = xbi.getWidth();
        } else {
          recMaintainStart.y = 0;
          recMaintainDest.y = -xdY;
          recMaintainDest.height = xbi.getHeight() + xdY;

          recNew2.y = 0;
          recNew2.height = -xdY;
          recNew2.x = 0;
          recNew2.width = xbi.getWidth();
        }
//        rec_new1.height = rec_new2.y + rec_new2.height - rec_new1.y;
        recMaintainStart.width = recMaintainDest.width;
        recMaintainStart.height = recMaintainDest.height;

            
        int[] bt = new int[recMaintainDest.width 
                           * recMaintainDest.height];
        bt = xbi.getRGB(recMaintainStart.x, recMaintainStart.y, 
            recMaintainStart.width, recMaintainStart.height, 
            bt, 0, recMaintainStart.width - 1);
        
        xbi.setRGB(recMaintainDest.x,
            recMaintainDest.y,
            recMaintainDest.width, 
            recMaintainDest.height, 
            bt, 
            0, 
            recMaintainDest.width - 1);


        for (int x = recNew1.x; x < recNew1.x + recNew1.width; x++) {
          for (int y = recNew1.y;
              y < recNew1.y + recNew1.height; y++) {
                  
            try {
              if ((x + xjlblStroke.getLocationOnScreen().x
                  + y + xjlblStroke.getLocationOnScreen().y) 
                  % strokeDistance == 0) {  

                final int rgba = 10;
                xbi.setRGB(x, y, 
                    new Color(rgba, rgba, rgba, 
                        rgba).getRGB());
              } else {

                xbi.setRGB(x, y, 
                    new Color(0, 0, 0, 0).getRGB());
              }
            } catch (IllegalComponentStateException e) {
                    
              //interrupt
              x = recNew1.width;
              y = recNew1.height;
            }
          }  
        }
        for (int x = recNew2.x; x < recNew2.x + recNew2.width; x++) {
          for (int y = recNew2.y;
              y < recNew2.y + recNew2.height; y++) {
                  
            try {
              if ((x + xjlblStroke.getLocationOnScreen().x
                  + y + xjlblStroke.getLocationOnScreen().y) 
                  % strokeDistance == 0) {

                final int colorRgb = 10;
                xbi.setRGB(x, y, new Color(colorRgb, colorRgb,
                    colorRgb, colorRgb).getRGB());
              } else {

                xbi.setRGB(x, y, 
                    new Color(0, 0, 0, 0).getRGB());
              }
            } catch (IllegalComponentStateException e) {
                    
              //interrupt
              x = recNew2.width;
              y = recNew2.height;
            }
          }  
        }
        xjlblStroke.setIcon(new ImageIcon(xbi));
        return xbi;
      }
    }
    return getStroke(xjlblStroke);
  }   
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * Apply stroke on background.
   * @param xjlblStroke  the background carrying item.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void getStrokeRec(
      final JLabel xjlblStroke) {
    
    
    final int strokeDistance = 10;
    if (xjlblStroke.getWidth() > 0 
        && xjlblStroke.getHeight() > 0
//        && jlbl_stroke.isShowing()
        ) {
      BufferedImage biStroke = new BufferedImage(
          xjlblStroke.getWidth(),
          xjlblStroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
          
      for (int x = 0; x < biStroke.getWidth(); x++) {
        for (int y = 0; y < biStroke.getHeight(); y++) {
                
          try {
            if ((x + xjlblStroke.getLocationOnScreen().x
                - y - xjlblStroke.getLocationOnScreen().y) 
                % strokeDistance == 0
                ||  (x + xjlblStroke.getLocationOnScreen().x
                    + y 
                    + xjlblStroke.getLocationOnScreen().y) 
                    % strokeDistance == 0) {

              final int clr = 10;
              biStroke.setRGB(x, y, 
                  new Color(clr, clr, clr, clr).getRGB());
            } else {

              final int clr = 0;
              biStroke.setRGB(x, y, 
                  new Color(clr, clr, clr, clr).getRGB());
            }
          } catch (IllegalComponentStateException e) {
            x = biStroke.getWidth();
            y = biStroke.getHeight();
          }
        }  
      }
      xjlblStroke.setIcon(new ImageIcon(biStroke));
    }
  }

  
  /**
   * Apply stroke on background.
   * @param xjlblStroke the background carrying item.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void getScrollStroke(final JButton xjlblStroke) {
    
    
    final int strokeDistance = 10;
    if (xjlblStroke.getWidth() > 0 
        && xjlblStroke.getHeight() > 0
        ) {
      BufferedImage biStroke = new BufferedImage(
          xjlblStroke.getWidth(), 
          xjlblStroke.getHeight(), BufferedImage.TYPE_INT_ARGB);
          
      for (int x = 0; x < biStroke.getWidth(); x++) {
        for (int y = 0; y < biStroke.getHeight(); y++) {
          try {
            if (
                (x + xjlblStroke.getLocationOnScreen().x
                    - y - xjlblStroke.getLocationOnScreen().y) 
                    % strokeDistance == 0
                ) {

              biStroke.setRGB(x, y, Color.lightGray.getRGB());
            } else {

              final int rb = 255;
              final int g = 250;
              biStroke.setRGB(
                  x, y, new Color(rb, g, rb).getRGB());
            }
          } catch (IllegalComponentStateException e) {
            x = biStroke.getWidth();
            y = biStroke.getHeight();
          }
              
        }  
      }
      xjlblStroke.setIcon(new ImageIcon(biStroke));
    }
  }

  /**
   * Apply stroke on background.
   * @param xjlblStroke  the background carrying item.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void getRoughStroke(final JLabel xjlblStroke) {
    
    
    final int strokeDistance = 20;
    if (xjlblStroke.getWidth() > 0 
        && xjlblStroke.getHeight() > 0
        ) {
      BufferedImage biStroke = new BufferedImage(
          xjlblStroke.getWidth(), 
          xjlblStroke.getHeight(),
          BufferedImage.TYPE_INT_ARGB);
            
      for (int x = 0; x < biStroke.getWidth(); x++) {
        for (int y = 0; y < biStroke.getHeight(); y++) {
          if ((x + y) 
              % strokeDistance == 0) {

            final int r = 40;
            final int g = 50;
            final int alpha = 90;
            biStroke.setRGB(x, y,
                new Color(r, g, g, alpha).getRGB());
          } else {
            biStroke.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
          }
        }  
      }
      xjlblStroke.setIcon(new ImageIcon(biStroke));
    }
  }
  

  /**
   * Read lines from specified path.
   * @param xpath         the specified path to the file on disk
   * @return              the content of the specified file.
   * @throws IOException  in case IOException occurs while reading.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final Vector<String> readLines(final String xpath)
      throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(xpath));
    final Vector<String> vec_text = new Vector<String>();
    try {
      String line = br.readLine();

      while (line != null) {
        vec_text.add(line);
        line = br.readLine();
      }
        
    } finally {
      br.close();
    }
    return vec_text;
  }
  

  /**
   * Return broadcast address of local host.
   * @return  the broadcast address of local host. 
   * @see getBroadcastAddress
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static InetAddress getBroadcastAddressLocalhost() {
    try {
      return Utils.getBroadcastAddress(InetAddress.getLocalHost());
    } catch (UnknownHostException e) {

      e.printStackTrace();
      return null;
    }
  }
  /**
   * Return broadcast address.
   * <table><tr><th>Class</th><th>subnet mask</th><th>broadcast address</th></tr>
   * <tr><td><code>Class A (0-127)</code></td>
   * <td><code>255.0.0.0</code></td><td><code>5.255.255.255</code></td></tr>
   * <tr><td><code>Class B (128-191)</code></td>
   * <td><code>255.255.0.0</code></td><td><code>190.100.255.255</code></td></tr>
   * <tr><td><code>Class C (192-223)</code></td>
   * <td><code>255.255.255.0</code></td><td><code>192.168.120.255</code></td></tr></table>
   * @param xaddress     the address
   * @return            the broadcast address.
   */
  public static InetAddress getBroadcastAddress(final InetAddress xaddress) {
    byte[] ipBytes = xaddress.getAddress();
    if (ipBytes[0] < (byte) 128) {
      ipBytes[1] = (byte) 255;
    }
    if (ipBytes[0] < (byte) 192) {
      ipBytes[2] = (byte) 255;
    }
    
    ipBytes[3] = (byte) 255;
    try {
      return InetAddress.getByAddress(ipBytes);
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  /**
   * Return the ipv4 inetAddress that suites to the given IP-address.
   * 
   * @param xip   the IP-address
   * @return      the suitable instance of InetAddress
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static InetAddress getInetAddres(final String xip) {
    byte[] ipBytes = new byte[4];

    try {
      final int occurrence = xip.indexOf('.');
      final String str0 = xip.substring(0, occurrence);
      final String rest = xip.substring(occurrence + 1, xip.length());
      
      final int occurrence2 = rest.indexOf('.');
      final String str1 = rest.substring(0, occurrence2);
      final String rest2 = rest.substring(occurrence2 + 1, rest.length());
      
      final int occurrence3 = rest2.indexOf('.');
      final String str2 = rest2.substring(0, occurrence3);
      final String str3 = rest2.substring(occurrence3 + 1, rest2.length());
      
      final int int0 = Integer.parseInt(str0);
      final int int1 = Integer.parseInt(str1);
      final int int2 = Integer.parseInt(str2);
      final int int3 = Integer.parseInt(str3);
  
      ipBytes[0] = (byte) int0;
      ipBytes[1] = (byte) int1;
      ipBytes[2] = (byte) int2;
      ipBytes[3] = (byte) int3;
    
      return InetAddress.getByAddress(ipBytes);
    } catch (UnknownHostException xe) {
      xe.printStackTrace();
      return null;
    } catch (NumberFormatException xe) {
      xe.printStackTrace();
      return null;
    } catch (StringIndexOutOfBoundsException xe) {
      xe.printStackTrace();
      return null;
    }
  }
  
  
  
  /**
   * Create datagramPackage with specified message.
   * @param xserverInformation  the message.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static DatagramPacket createDatagramPackage(
      final String xserverInformation) {
    
    final String serverInformation;
    if (xserverInformation != null) {
      serverInformation = xserverInformation;
    } else {
      serverInformation = "";
    }
       
    byte[] buf = serverInformation.getBytes();
    return new DatagramPacket(buf, buf.length);
  }

  
  /**
   * Returns the path to the jar file which is currently executed. In case
   * the program is run by Eclipse or other IDE, this points to the binary
   * folder which contains all the class files.
   * @return  the path to the currently executed jar file.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String getPathOfExecutedJarFile() {
   
    try {
      return Utils.class.getProtectionDomain().getCodeSource()
          .getLocation().toURI().getPath();
    } catch (URISyntaxException e) {
      return "";
    }
  }

  /**
   * Execute command in terminal and return the result.
   * 
   * @param xcommand the command that will be executed.
   * @return the answer of the command.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String executeCommandLinux(final String xcommand) {
        
        
    final Process proc;
    String answer = "";
    String currLine;
    try {
            
      //execute command
      proc = Runtime.getRuntime().exec(xcommand);
      BufferedReader br = new BufferedReader(
          new InputStreamReader(proc.getInputStream()));
      
      //fetch answer
      while ((currLine = br.readLine()) != null) {
        answer += currLine;
      }
      proc.waitFor();
              
      //if normal termination
      if (proc.exitValue() == 0) {
        answer = EXECUTION_SUCCESS  + answer;
      } else {
        answer = EXECUTION_FAILED   + answer;
      }
              
      //destroy execution process and return the result
      proc.destroy();
      return answer;
            
    } catch (Exception e) {
            
      //print stack trace and return the failure message.
      return EXECUTION_FAILED;
    }
  }
  
  
  /**
   * Generate executable script at specified directory.
   * @param xpath     the path to the executable script that is going to be 
   *                  created.
   * @param xcontent  the code that will be written into the script file.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void generateExecutableScript(
      final String xpath, 
      final String xcontent) {
    
    try {
        
      //e.g. copy into target folder.
      writeToFile(xpath, xcontent);
        
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
      
    executeCommandLinux("chmod u+rwx " + xpath);
  }

  
  /**
   * Write text into text file.
   * @param xfilename                     the name of the text file
   * @param xtext                         the text that is going to be written
   *                                      into the text file
   * @throws FileNotFoundException        if the file is not found
   * @throws UnsupportedEncodingException if the encoding UTF-8 is not 
   *                                      supported. 
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final void writeToFile(
      final String xfilename, final String xtext) 
          throws FileNotFoundException, 
          UnsupportedEncodingException {
        
    final PrintWriter writer = new PrintWriter(
        xfilename,
        "UTF-8");
      
      // write user id
    writer.println(xtext);
    writer.close();
  }
    
   
  
  /**
   * Extract executable script from jar file.
   * @param xscriptPath   the path to the script file inside jar file
   * @param xdestination  the destination of the script file
   * @return              whether the operation successfully finished.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static boolean extractExecutableScript(String xscriptPath,
      final String xdestination) {
    if (extractFileFromJar(xscriptPath, xdestination)) {

      executeCommandLinux("chmod u+x " + xdestination);
      return true;
    }
    return false;
  }

  /** Extract file from jar file.
  * @param xscriptPath   the path to the file inside jar file
  * @param xdestination  the destination of the file
  * @return              whether the operation successfully finished.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
  */
  public static boolean extractFileFromJar(String xscriptPath,
      final String xdestination) {
    InputStream stream = null;
    OutputStream resStreamOut = null;
    try {
      stream = Utils.class.getResourceAsStream(xscriptPath);
     
     
      // note that each / is a directory down in the "jar tree" been the 
      // jar the root of the tree
      if (stream == null) {
        throw new Exception("Cannot extract resource \"" + xscriptPath 
            + "\" out of Jar file.");
      }

      int readBytes;
      byte[] buffer = new byte[4096];
      resStreamOut = new FileOutputStream(xdestination);
      while ((readBytes = stream.read(buffer)) > 0) {
        resStreamOut.write(buffer, 0, readBytes);
      }
     
      stream.close();
      resStreamOut.close();
      return true;
    } catch (Exception ex) {
      LoggerRegistry.log(
          "Failure at Utils.extractFileFromJar: " + ex.toString());
      return false;
    }
  }
 
  

  /**
   * Return the time and date.
   * 
   * @return  the time and date.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String getTimeAndDate() {
    
    
    DateFormat dateFormat = new SimpleDateFormat(
        "yyyy MM dd   HH:mm:ss");
    Calendar cal = Calendar.getInstance();
        
    String wochentag = "";
    switch (cal.get(Calendar.DAY_OF_WEEK)) {
      case 0:
        wochentag = "Sat";
        break;
      case 1:
        wochentag = "Sun";
        break;
      case 2:
        wochentag = "Mon";
        break;
      case 2 + 1:
        wochentag = "Tue";
        break;
      case 2 + 2:
        wochentag = "Wed";
        break;
      case 2 + 2 + 1:
        wochentag = "Thu";
        break;
      case (2 + 1) * 2:
        wochentag = "Fri";
        break;
      case (2 + 1) * 2 + 1:
        wochentag = "Sat";
        break;
      default:
        wochentag = "";
        break;
    }
        
    return (wochentag + ", " + dateFormat.format(cal.getTime()));
  
  }
  
  
  public static String getShortHumanReadableTime(final double xtime) {

    
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    return (dateFormat.format(xtime));
  }
  
  /**
   * Call script that is contained by jar file. At first, the script is copied
   * to specified destination, the executable bit is set and it is executed
   * with given set of parameters.
   * 
   * @param xorig           the original location inside jar file
   * @param xdest           the destination outside jar file
   * @param xparam          the parameter
   * @param xisShellScript  whether the script is a windows shell script or 
   *                        not.
   * @return                whether the operation terminated successfully.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static boolean callScript(final String xorig,
      final String xdest, final String xparam,
      final boolean xisShellScript) {
    if (!new File(xdest).exists()) {
      extractExecutableScript(xorig, xdest);
    }
      
    if (xisShellScript && UtilitiesSystem.isWindows()) {
        
      return executeCommandLinux("sh " + xdest + " " + xparam)
          .startsWith(EXECUTION_SUCCESS);
    } else {

      return executeCommandLinux(xdest + " " + xparam)
          .startsWith(EXECUTION_SUCCESS);
    }
  }

  /**
   * Insert newline characters followed by a char sequence in case a
   * line length exceeds the maximum string length.
   * 
   * @param text     the text that is altered
   * @return          the altered text.
   */
  public static final String prepareText(final String xtext, 
      final int xmaxLinelength) {
    String processedString = "";
    String text;
    String restingString;
    if (!xtext.endsWith("\n")) {
      text = xtext + "\n";
    } else {

      text = xtext;
    }
    restingString = text;
    int currentOccurrence = restingString.indexOf("\n");
    
    while (currentOccurrence != -1) {
      
      
      //
      // In case the maximal line length is not exceeded, everything is
      // okay.
      if (currentOccurrence <= xmaxLinelength) {

        processedString += restingString.substring(
            0, currentOccurrence + 1);
        restingString = restingString.substring(currentOccurrence + 1,
            restingString.length());
      } else {

        // Otherwise, subdivide the char sequence 
        int currLinebreak = xmaxLinelength;
        for (; currLinebreak < currentOccurrence;) {

          processedString += restingString.substring(
              0, currLinebreak) + "\n";
          restingString = restingString.substring(currLinebreak,
              restingString.length());
          
          currentOccurrence -= xmaxLinelength;
        }

        currLinebreak -= xmaxLinelength;
        if (currLinebreak < currentOccurrence - 1) {

          processedString += restingString.substring(
              0, currentOccurrence - currLinebreak + 1);
          restingString = restingString.substring(
              currentOccurrence - currLinebreak + 1,
              restingString.length());
        }
        
      }

      currentOccurrence = restingString.indexOf("\n");

      
    }
    
    return processedString;
    
  }
  /**
   * Insert newline characters followed by a char sequence in case a
   * line length exceeds the maximum string length.
   * 
   * @param text     the text that is altered
   * @return          the altered text.
   */
  public static final Vector<String> prepareTextV(
      final String xtext, 
      final int xmaxLinelength) {
    
    String text;
    String restingString;
    if (!xtext.endsWith("\n")) {
      text = xtext + "\n";
    } else {

      text = xtext;
    }
    restingString = text;
    int currentOccurrence = restingString.indexOf("\n");
    
    final Vector<String> vecret = new Vector<String>();
    
    while (currentOccurrence != -1) {
      
      
      //
      // In case the maximal line length is not exceeded, everything is
      // okay.
      if (currentOccurrence <= xmaxLinelength) {

        vecret.add(restingString.substring(
            0, currentOccurrence));
        restingString = restingString.substring(currentOccurrence + 1,
            restingString.length());
      } else {

        // Otherwise, subdivide the char sequence 
        int currLinebreak = xmaxLinelength;
        for (; currLinebreak < currentOccurrence;) {

          vecret.add(restingString.substring(
              0, currLinebreak));
          restingString = restingString.substring(currLinebreak,
              restingString.length());
          
          currentOccurrence -= xmaxLinelength;
        }

        currLinebreak -= xmaxLinelength;
        if (currLinebreak < currentOccurrence - 1) {

          vecret.add(restingString.substring(
              0, currentOccurrence - currLinebreak + 1));
          restingString = restingString.substring(
              currentOccurrence - currLinebreak + 1,
              restingString.length());
        }
        
      }

      currentOccurrence = restingString.indexOf("\n");
      

      
    }
    
    return vecret;
    
  }
  
  
  public static void main(String[] args) {
    
    System.out.println(getShortHumanReadableTime(System.currentTimeMillis()));
    
    Vector<String> v = prepareTextV("hey du \nbist echt mega scheißeasdfasdfasdfasdfasdf", 15);
    for (int i = 0; i < v.size(); i++) {
      System.out.println(v.get(i));
    }
  }
}
