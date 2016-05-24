package adt.model;


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
 * Element class contains a certain content of type <code>Type</code>, 
 * a predecessor and a
 * successor element.
 *
 * <p>
 * Is one item of a list (List).
 *
 * <p>
 * Contains getter and setter methods for content, predecessor, successor and
 * implements Serializable.
 *
 * @param <Type>  the element's type
 * 
 * @author Julius Huelsmann
 * @version Milestone 2
 */
public class Element<Type> implements Serializable {

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

  /**
   * Content of the ELement.
   */
  private Type content;

  /**
   * Element which comes next.
   */
  private Element<Type> elemSuccessor;

  /**
   * Predecessor element.
   */
  private Element<Type> elemPredecessor;
  
  /**
   * index for sort. Sort ASC.
   */
  private double sortedIndex = 0;

  /**
   * Constructor: Saves values such as content, successor, predecessor.
   *
   * @param xcontent the content we want to save.
   * @param xelemSuccessor the successor element.
   * @param xelemPredecessor the predecessor element.
   */
  public Element(final Type xcontent,
      final Element<Type> xelemSuccessor,
      final Element<Type> xelemPredecessor) {
    // save values
    this.content = xcontent;
    this.elemSuccessor = xelemSuccessor;
    this.elemPredecessor = xelemPredecessor;
  }
  /**
   * Constructor: Saves values such as content, successor, predecessor.
   *
   * @param xcontent the content we want to save.
   * @param xelemSuccessor the successor element.
   * @param xelemPredecessor the predecessor element.
   * @param xsortIndex the index for inserting in a sorted way
   */
  public Element(final Type xcontent,
      final Element<Type> xelemSuccessor,
      final Element<Type> xelemPredecessor,
      final int xsortIndex) {
    // save values
    this.content = xcontent;
    this.elemSuccessor = xelemSuccessor;
    this.elemPredecessor = xelemPredecessor;
    this.sortedIndex = xsortIndex;
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
   * Sets the successor - element.
   *
   * @param xelemSuccessor the successing element.
   */
  public final void setElemSuccessor(final Element<Type> xelemSuccessor) {
    elemSuccessor = xelemSuccessor;
  }

  /**
   * Returns the successor - element.
   *
   * @return elemSuccessor.
   */
  public final Element<Type> getElemSuccessor() {
    return elemSuccessor;
  }

  /**
   * Returns the predecessor - element.
   *
   * @return elemPredecessor.
   */
  public final Element<Type> getElemPredecessor() {
    return elemPredecessor;
  }

  /**
   * Sets the predecessor - element.
   *
   * @param xelemPredecessor .
   */
  public final void setElemPredecessor(
      final Element<Type> xelemPredecessor) {
    this.elemPredecessor = xelemPredecessor;
  }
  /**
   * @return the sortedIndex
   */
  public final double getSortedindex() {
    return sortedIndex;
  }
  /**
   * @param xsortedIndex the sortedIndex to set
   */
  public final void setSortedindex(final double xsortedIndex) {
    this.sortedIndex = xsortedIndex;
  }
}
