package console.control;

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

/**
 * The listener interface for receiving keyboard inputs, e. g. from terminal.
 * 
 * <p>
 * The class that is interested in processing an action on the input string
 * implements this interface, and the object created with that
 * class is registered with a component, using the component's
 * <code>setTerminalListener</code> method. When the action event
 * occurs, that object's <code>keyEntered</code> method is
 * invoked.
 * @author Julius Hülsmann
 * @version %I%, %U%
 * @since 1.0
 *
 */
public interface TerminalListener {

  
  /**
   * This method is invoked if a String is entered.
   * @param xkey    the key that is entered
   *
   * @author Julius Hülsmann
   * @since 1.0
   */
  public void keyEntered(final String xkey);
}
