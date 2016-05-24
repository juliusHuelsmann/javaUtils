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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Contains static utility class for determining the currently used operating
 * system.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
public class UtilitiesSystem {

  
  /**
   * Operating system identifier that is used by system class.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String ID_WIN = "Windows";

  /**
   * Operating system identifier that is used by system class.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String ID_LIN = "Linux";
  
  /**
   * Operating system identifier that is used by system class.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static final String ID_OSX = "Mac OS X";
  

  /**
   * Return whether the current Linux distribution is Kali Linux (which does
   * not allow tray icons).
   * @return  whether the current Linux distribution is Kali Linux.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static boolean isKali() {
    return (getLinuxDistribution().equals("Kali"));
  }

  /**
   * Return whether the current Linux distribution is Kali Linux (which does
   * not allow tray icons).
   * @return  whether the current Linux distribution is Kali Linux.
   */
  public static boolean isWindows() {
    return (System.getProperty("os.name").contains(ID_WIN));
  }

  /**
   * Returns the name of the current Linux distribution (in case Linux is
   * used).
   * 
   * @return  the Linux distribution's name.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static String getLinuxDistribution() {
    String[] cmd = {
        "/bin/sh", "-c", "cat /etc/*-release" };
  
    //
    // String which will contain the Linux distribution identifier
    String totalLine = "";
    
    //
    // The identifier for the line which contains the specific Linux 
    // distribution identifier
    final String keyCodes = "DISTRIB_ID=";
    try {
      final Process p = Runtime.getRuntime().exec(cmd);
      BufferedReader br = new BufferedReader(new InputStreamReader(
          p.getInputStream()));
  
  
      // read all the lines until the identifier for the distribution line has
      // been found.
      String line = "";
      while ((line = br.readLine()) != null) {
        if (keyCodes.startsWith(keyCodes)) {
          totalLine = line.replaceFirst(keyCodes, "");
          return totalLine;
        }
      }
    } catch (IOException e) {  
      return totalLine;
    }
    return totalLine;
  }

}
