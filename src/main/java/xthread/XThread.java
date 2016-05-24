package xthread;

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

import java.util.Vector;


/**
 * This class provides useful extensions of the class {@link java.lang.#Thread}
 * which allows to 
 *   -   provide information on the thread's process
 *   -   interrupt all instances of XTread because all the instances are 
 *       inserted into a vector of XTreads.
 *     
 * @author Julius Huelsmann
 * @since 1.0
 * @version %I%, %U%
 */
public abstract class XThread extends Thread {


  /**
   * This value may inform the rest of the program on the XThread's progress.  
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private double percentage;
  
  
  /**
   * Is true in case the thread runs until it is terminated from outside
   * or an error occurs.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private final boolean infiniteLoop;

  /**
   * Static vector of XThreads which logs all instances of XTread.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  private static final Vector<XThread> threads = new Vector<XThread>();
  
  
  /**
   * Constructor: saves the {@link #getName()} and whether the thread is
   * {@link #infiniteLoop}.
   * 
   * @param xname           the XThread's name which is used for identification
   * @param xinfiniteloop   is equal to false if the instance of XTread is 
   *                        going to terminate if not interrupted.
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public XThread(
      final String xname,
      final boolean xinfiniteloop) {
    
    //
    // store name and infinite loop
    super(xname);
    this.infiniteLoop = xinfiniteloop;
    
    
    //
    // add this to list of threads
    getThreads().add(this);
  }
  

  /**
   * Returns the state of progress of the current instance of XTread.
   * @return The {@link #percentage}.
   * @see percentage
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public double getPercentage() {
    return this.percentage;
  }
  
  
  /**
   * Set the xThread's percentage.
   * @param xperc       the new state of progress of the current instance of
   *                    XThread.
   * 
   * @see percentage
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0   */
  protected void setPercentage(final double xperc) {
    this.percentage = xperc;
  }
  
  
  /**
   * This method is implemented by implementations of 
   * {@link xthread.XThread} and performs all the necessary steps
   * for interrupting the Thread.
   * 
   * @since 1.0
   */
  public abstract void terminateThread();


  /**
   * Return whether the current Thread is an infinteLoop which never terminates
   * or not.
   * @return the infiniteLoop
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public boolean isInfiniteLoop() {
    return infiniteLoop;
  }
  
  

  /**
   * Returns the Threads.
   * @return the threads
   * 
   * @see threads
   * 
   * @author Julius Huelsmann
   * @version %I%, %U%
   * @since 1.0
   */
  public static Vector<XThread> getThreads() {
    return threads;
  }
}
