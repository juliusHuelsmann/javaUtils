package adt.model.stack;


/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//import declarations
import java.io.Serializable;

/**
 * Element class contains a certain content of type <code>Type</code> 
 * and a predecessor of which the stack consists.
 *
 * <p>
 * Contains getter and setter methods for content, predecessor, successor and
 * implements serializable.
 * 
 * @param <Type>    the stackelement's type.
 *
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class Stackelement<Type> implements Serializable {

  /*
   * variable for saving list:
   */
  
  /**
   * Default serial version UID for being able to identify the list's 
   * version if saved to the disk and check whether it is possible to 
   * load it or whether important features have been added so that the
   * saved file is out-dated.
   */
  private static final long serialVersionUID = 1L;

  /*
   * Element- specific variables.
   */
  
  /**
   * Content of the ELement.
   */
  private Type content;


  /**
   * Predecessor element.
   */
  private Stackelement<Type> elemPredecessor;
  

  /**
   * Constructor: Saves values such as content, successor, predecessor.
   *
   * @param xcontent 
   *       the content we want to save.
   * @param xelemPredecessor 
   *       the predecessor element.
   */
  public Stackelement(final Type xcontent,
      final Stackelement<Type> xelemPredecessor) {
    
    // save values
    this.content = xcontent;
    this.elemPredecessor = xelemPredecessor;
  }
  

  /**
   * Sets the content.
   *
   * @param xcontent content for current element.
   */
  public final void setContent(final Type xcontent) {
    this.content = xcontent;
  }
  

  /**
   * Returns the content.
   *
   * @return content
   */
  public final Type getContent() {
    return content;
  }

  
  /**
   * Returns the predecessor - element.
   *
   * @return elemPredecessor.
   */
  public final Stackelement<Type> getElemPredecessor() {
    return elemPredecessor;
  }

  
  /**
   * Sets the predecessor - element.
   *
   * @param xelemPredecessor .
   */
  public final void setElemPredecessor(
      final Stackelement<Type> xelemPredecessor) {
    this.elemPredecessor = xelemPredecessor;
  }
}
