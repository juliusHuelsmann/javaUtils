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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import log.Logger;
import log.LoggerRegistry;
import xthread.XThread;

/**
 * Utility class which is used for listing all the 
 * computers that are connected to the local Internet.
 * 
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @since 1.0
 */
public class NetworkScanner {

  /**
   * The only instance of this class.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private static NetworkScanner instance;
  
  /**
   * Vector containing the InetAddresses of all the clients that are currently
   * connected to the network the local computer is part of.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private Vector<InetAddress> iaOnline = new Vector<InetAddress>();
  
  /**
   * Vector containing the InetAddresses of all the clients that have already
   * been connected to the network the local computer is part of 
   * but that are currently offline.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private Vector<InetAddress> iaOffline = new Vector<InetAddress>();
  
  /**
   * Array which contains all the Threads that are scanning different parts of
   * the port range.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private ThreadedScanning[] thrd;
  
  /**
   * The number of threads used for scanning.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private int threadingNumber = 32; //64;
  
  
  /**
   * Constructor of {@link utils.NetworkScanner}: Initializes the 
   * threads {@link #thrd} and starts them.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private NetworkScanner() {

    
    //
    // Adds shutdown hook for terminating all the threads.
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        terminate();
      }
    });
    
    
    //
    // Print information and initialize the Scanning Threads.
    LoggerRegistry.log(
        "Scanning network using " + threadingNumber + " threads.");
    thrd = new ThreadedScanning[threadingNumber];
    final double portRange = 255.0 / threadingNumber;

    for (int i = 0; i < threadingNumber; i++) {
      thrd[i] = new ThreadedScanning(
          
          // the first port checked by the new initialized thread 
          (int) Math.round(Math.max(1, portRange * i)), 
          
          // The last port which is checked by the new initialized thread.
          (int) Math.round(portRange * (i + 1)));
      thrd[i].start();
    }
  }
  
  
  /**
   * Initialize the network-scanning threads.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void terminate() {

    LoggerRegistry.log("Termination required");
    for (int i = 0; thrd != null && i < thrd.length; i++) {
      thrd[i].interrupt();
    }
  }
  
  
  /**
   * Print information on clients that are currently online.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void printOnline() {

    String online = ("\nOnline:") + "\n";
    for (int i = 0; i < iaOnline.size(); i++) {
      final String adr = iaOnline.get(i).getHostAddress();
      final String hostname = iaOnline.get(i).getHostName();
      online += (adr + "\t" + hostname + "\n");
    }
    LoggerRegistry.log(online);
    
  }
  
  
  /**
   * Print information on clients that are currently offline.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public void printOffline() {

    String offline = ("\nOffline:") + "\n";
    for (int i = 0; i < iaOffline.size(); i++) {

      final String adr = iaOffline.get(i).getHostAddress();
      final String hostname = iaOffline.get(i).getHostName();
      offline += (adr + "\t" + hostname + "\n");
    }
    LoggerRegistry.log(offline);
  }
  
  
  /**
   * Return the only instance of this singleton class. In case the class has 
   * not been initialized yet, a new instance is created.
   * 
   * @return the only instance of this singleton class.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static NetworkScanner getInstance() {
    
    if (instance == null) {
      instance = new NetworkScanner();
    }
    return instance;
  }
  
  
  
  /**
   * Scans for client in specified range.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private class ThreadedScanning extends XThread {

    
    /**
     * The specified lower range the current instance of
     * {@link utils.NetworkScanner} is processing.
     * 
     * @author Julius Huelsmann
     * @version %I%, %U%
     * @since 1.0
     */
    private int lowerRange;
    

    /**
     * The specified upper range the current instance of
     * {@link utils.NetworkScanner} is processing.
     * 
     * @author Julius Huelsmann
     * @version %I%, %U%
     * @since 1.0
     */
    private int upperRange;
    
    
    /**
     * Constructor of this utility class: saves the specified values.
     * @param xstartValue the section's lower range,
     * @param xendValue   the section's upper range.
     * 
     * @author Julius Huelsmann
     * @version %I%, %U%
     * @since 1.0
     */
    public ThreadedScanning(final int xstartValue, final int xendValue) {
      
      super("Portscanner in range " + xstartValue + ":" + xendValue, true);
      this.lowerRange = xstartValue;
      this.upperRange = xendValue;
    }
    
    
    /**
     * Run-method: scans inside specified range.
     * 
     * @author Julius Huelsmann
     * @version %I%, %U%
     * @since 1.0
     */
    public void run() {
      LoggerRegistry.log("Start scanning " + lowerRange + " - " + upperRange + ".");
      while (!isInterrupted()) {

        final double diff = upperRange - lowerRange;
        for (int i = lowerRange; i <= upperRange; i++) {
          try {
            
            Thread.sleep(200);
            
            setPercentage((i - lowerRange) / diff);
            InetAddress localhost = InetAddress.getLocalHost();
            // this code assumes IPv4 is used
            byte[] ip = localhost.getAddress();
            ip[3] = (byte)i;
            InetAddress address = InetAddress.getByAddress(ip);
            if (address.isReachable(1000) 
                && !address.getHostAddress().equals(address.getHostName())) {
              if (!iaOnline.contains(address)) {

                iaOnline.add(address);
                printOnline();
              }
            } else if (!address.getHostAddress().equals(address.getHostName())) {
              if (!iaOffline.contains(address)) {

                iaOffline.add(address);
                printOffline();
              }
            }
          } catch (UnknownHostException e) {

            LoggerRegistry.log("Failure at NetworkScanner: " + e);
          } catch (IOException e) { 
            continue;
          } catch (InterruptedException e) {

            interrupt();

            LoggerRegistry.log("Thread interrupted: " + lowerRange 
                + " - " + upperRange + ".");
            break;
          
          }
        }
      }
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Just interrupting.
     */
    @Override public void terminateThread() {
      interrupt();
    }
  }


  
  /**
   * Method main for calling the NetworkScanner directly.
   * 
   * @param xargs   main arguments
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static void main(final String[] xargs) {
    NetworkScanner.getInstance();
  }
}
