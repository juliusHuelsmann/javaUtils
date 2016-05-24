package adt.model;


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
 * Secure element which contains the element and a string identifying the 
 * element. This element is used inside the list of ClosedActions in 
 * SecureList.
 * 
 * <p>
 * It contains the current element of a single closed action and its title.
 * 
 * @author Julius Huelsmann
 * @since 1.0
 * @version %I%, %U%
 * @param <Closedactiontype> the type of the closedAction.
 */
public class Closedaction<Closedactiontype> {

  
  /**
   * The element which is the current element inside closed action.
   */
  private Element<Closedactiontype> elemSecure;

    
  /**
   * The unique id of the current closed action.
   */
  private int idSecurelist = 0;

    
  /**
   * The unique id of the current closed action.
   */
  private static int maxID = 0;

    
  /**
   * The name identifying the closed action for debugging purpose and
   * for being able to print the current list action.
   */
  private String name;

  
  /**
   * Constructor: (initializes) the content with start values.
   */
  public Closedaction() {

    //set title of transaction
    this.name = "";
    this.elemSecure = null;

    //set unique id and change the max id.
    idSecurelist = maxID++;
  }
  
  
  /**
   * Constructor: (initializes) the content with start values passed by 
   * calling methods.
   * @param xname 
   *           the name of the transaction
   * @param xcontent  
   *           the current element in new closed action..
   */
  public Closedaction(
      final String xname, final Element<Closedactiontype> xcontent) {
    
    //set title of transaction
    this.name = xname;
    this.elemSecure = xcontent;
  }
  
  
  /**
   * @return the elem_secure
   */
  public final Element<Closedactiontype> getElem_secure() {
    return elemSecure;
  }
  
  
  /**
   * @param xelemsecure the elem_secure to set
   */
  public final void setElem_secure(
      final Element<Closedactiontype> xelemsecure) {
    this.elemSecure = xelemsecure;
  }
  
  
  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }
  
  
  /**
   * @param xname the name to set
   */
  public final void setName(final String xname) {
    this.name = xname;
  }


  /**
   * @return the id_secureList
   */
  public final int getId_secureList() {
    return idSecurelist;
  }


}
