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
import java.util.Random;
import java.util.logging.Logger;



/**
 * Sorted Secure list.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <SecureListType>
 */
public class Securelistsort<SecureListType> implements Serializable {

  /**
   * Default serial version UID for being able to identify the list's 
   * version if saved to the disk and check whether it is possible to 
   * load it or whether important features have been added so that the
   * saved file is out-dated.
   */
  private static final long serialVersionUID = 1L;

  
  /**
   * The version of the secureListSort which is increased with each big
   * update of SecureListSort.
   */
  public final String listVersion = "v1.0";
  
  
  /**
   * Whether to sort ascending or descending.
   */
  private boolean sortAsc = true;

  /**
   * The id which is given to methods for transaction and closed actions if
   * there is no predecessor transaction / closed action.
   */
  public static final int ID_NO_PREDECESSOR = Securelist.ID_NO_PREDECESSOR;
  
  /**
   * The string which is added to the internal operations title for 
   * transaction or closed actions for knowing where the action has been 
   * started.
   */
  private final String internalAction = "Internal: ";


  /**
   * The list out of which the sorted secure list consists.
   */
  private Securelist<SecureListType> ls;
  

  /**
   * Constructor.
   */
  public Securelistsort(final Logger xlog) {
    
    //by default, the sorting is ascending.
    this.sortAsc = true;
    
    //initialize the list
    ls = new Securelist<SecureListType>(xlog);
  }
  

  /*
   * Functions returning the state of the list
   */

  /**
   * Returns weather is empty.
   *
   * @return weather list is empty.
   */
  public final boolean isEmpty() {
    return ls.isEmpty();
  }

  
  /**
   * Return weather it is in front of.
   *
   * @return weather list is in front of.
   */
  public final boolean isInFrontOf() {
    return ls.isInFrontOf();
  }

  
  /**
   * Return weather list is behind.
   *
   * @return weather list is behind.
   * @see List.isBehind();
   */
  public final boolean isBehind() {
    return ls.isBehind();
  }


  
  /*
   * Methods for navigating through the list
   */

  

  /*
   * Methods for navigating through the list
   */
  
  /**
   * Proceed one step in the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedactionId 
   *         the id of the closed action to which performs the
   *         method call.
   * 
   */
  public final void next(
      final int xtransactionId, final int xclosedactionId) {
    ls.next(xtransactionId, xclosedactionId);
  }

  
  /**
   * Step back in the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedactionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void previous(
      final int xtransactionId, final int xclosedactionId) {
    ls.previous(xtransactionId, xclosedactionId);
  }

  
  /**
   * Go to the beginning of the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void toFirst(
      final int xtransactionId, final int xclosedActionId) {
    ls.toFirst(xtransactionId, xclosedActionId);
  }

  
  /**
   * Go to the end of the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void toLast(
      final int xtransactionId, final int xclosedActionId) {
    ls.toLast(xtransactionId, xclosedActionId);
  }
  

  /**
   * Go to a special element (has to be inside the list).
   * 
   * @param xelemCurrent the current element in the future.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void goToElement(final Element<SecureListType> xelemCurrent,
      final int xtransactionId, final int xclosedActionId) {
    ls.goToElement(xelemCurrent, xtransactionId, xclosedActionId);
  }
  

  
  

  /*
   * Methods for getting content of the list's current element.
   */
  
  /**
   * Return current Element.
   *
   * @return current Element.
   */
  public final SecureListType getItem() {
    return ls.getItem();
  }
  
  
  /**
   * Return current Element.
   *
   * @return current Element.
   */
  public final Element<SecureListType> getElement() {
    return ls.getElement();
  }
  
  
  /*
   * Special Methods
   */

  /**
   * create subList.
   * @return list after current item.
   */
  public final List<SecureListType> subList() {
    return ls.subList();
  }

  /**
   * Return sort index of the current Element.
   *
   * @return sorted index of current Element.
   */
  public final double getItemSortionIndex() {
    return ls.getItemSortionIndex();
  }


  /**
   * Removes current element. Afterwards the current element points
   * to the predecessor of the removed item.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   */
  public final void remove(
      final int xtransactionId) {
    ls.remove(xtransactionId);
  }
  
  

  /**
   * Check whether item does already exist in list and if that is the case
   * point at it with elemCurrent.
   * 
   * @param xtype which is checked
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @return whether the element exists or not
   */
  public final boolean find(final SecureListType xtype,
      final int xtransactionId) {
    return ls.find(xtype, xtransactionId);
  }
  
  
  
  
  /**
   * print items with search index.
   */
  public final void printIndex() {
    ls.printIndex();
  }

  
  /**
   * List to array method.
   * @return the array from list.
   */
  public final synchronized String[] toArrayString() {
    return ls.toArrayString();
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  /**
   * Start a transaction with specified operation name (for identifying the
   * not terminated transaction in case an error occurred).
   * 
   * <p>
   * If the current element of the list is to be maintained after a 
   * performed action which passes the list, this method is called 
   * before the action.
   * 
   * <p>
   * After the action has been done endTransaction has to be called.
   * Otherwise there will occur an error if a new Transaction is started
   * without terminating the old one.
   * 
   * @param xoperationName the name of specified transaction
   * @param xoldOperationid the unique id of the old transaction
   * @return the unique id of the current transaction
   */
  public final int startClosedAction(final String xoperationName,
      final int xoldOperationid) {
    return ls.startClosedAction(xoperationName, xoldOperationid);
  }

  
  /**
   * Finish transaction; reset the state of the List and afterwards the
   * transaction values.
   * @param xoldOperationid the id of the transaction which is to be closed
   * @return the unique id of the current transaction
   */
  public final int finishClosedAction(final int xoldOperationid) {
    return ls.finishClosedAction(xoldOperationid);
  }
  

  /**
   * Start a transaction with specified operation name (for identifying the
   * not terminated transaction in case an error occurred).
   * 
   * <p>
   * If no other operation except of children of the current transaction
   * shell be able to change the state of the list, this method is called
   * before the action.
   * 
   * <p>
   * After the action has been done endTransaction has to be called.
   * Otherwise there will occur an error if a new Transaction is started
   * without terminating the old one.
   * 
   * @param xtransactionName
   *           the name of specified transaction
   * 
   * @param xoldTransactionid 
   *           the unique id of the old transaction
   * 
   * @return the unique id of the current transaction
   */
  public final int startTransaction(final String xtransactionName,
      final int xoldTransactionid) {
    return ls.startTransaction(xtransactionName, xoldTransactionid);
  }
  

  
  /**
   * Finish transaction; delete the current transaction.
   * 
   * @param xoldTransactionid the id of the transaction which is to be closed
   * @return the unique id of the current transaction
   */
  public final int finishTransaction(final int xoldTransactionid) {
    return ls.finishTransaction(xoldTransactionid);
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * insert sorted ASC.
   * 
   * @param xcontent 
   *         the content to be inserted.
   * 
   * @param xsearchCriteria 
   *         the index of sorting.
   * 
   * @param xoldTransactionid
   *         the id of the current operation for being able to start a 
   *         new transaction.
   */
  public final synchronized void insertSorted(final SecureListType xcontent, 
      final double xsearchCriteria, final int xoldTransactionid) {
    
    final int transactionId = ls.startTransaction(
        internalAction + "insert Sorted", xoldTransactionid);
    
    //find the position of the first element which has got a greater index
    //than the one which is inserted.
    findSorted(xsearchCriteria, transactionId, ID_NO_PREDECESSOR);
    
    //insert the new item depending on sorting order
    if (sortAsc) {
      ls.insertInFrontOf(xcontent, transactionId);
    } else {
      ls.insertBehind(xcontent, transactionId);
    }
    
    //apply the sorting index to the new item
    ls.getElement().setSortedindex(xsearchCriteria);
    
    ls.finishTransaction(transactionId);
  }

  
  /**
   * Set sort criteria to ascending.
   */
  public final synchronized void setSortAsc() {
    if (ls.isEmpty()) {
      sortAsc = true;
    } else {
      ls.getLog().warning("tried to change sorting order without"
          + "success: The list is not empty and thus may have"
          + "been sorted in a different order. " + sortAsc + "true");
    }
  }
  
  
  /**
   * Set sort criteria to descending.
   */
  public final synchronized void setsortDesc() {
    if (ls.isEmpty()) {
      sortAsc = false;
    } else {
      ls.getLog().warning("tried to change sorting order without"
          + "success: The list is not empty and thus may have"
          + "been sorted in a different order. " + sortAsc + "false");
    }
  }
  
  
  
  /**
   * Sort the list with bubble-sort algorithm.
   * 
   */
  public final synchronized void resort() {

    //there is nothing to do if the list does not exist or is empty.
    if (ls == null || ls.isEmpty()) {
      return;
    }
    
    /* 
     * start new transaction and go to the beginning of the list.
     * Initialize the first element as temporarily maintained one.
     * Go to the second element.
     */
    final int transactionId = ls.startTransaction(
        "Resort the list", Securelist.ID_NO_PREDECESSOR);
    
    int amountSteps = 1;
    for (int i = 0; i < amountSteps; i++) {
      amountSteps = 1; //=1 - 1;
      
      ls.toFirst(transactionId, Securelist.ID_NO_PREDECESSOR);
      Element<SecureListType> elemMaintained = ls.getElement();
      ls.next(transactionId, Securelist.ID_NO_PREDECESSOR);
      
      while (!ls.isEmpty() && !ls.isBehind()) {

        //if the maintained element is to be maintained once again:
        if ((elemMaintained.getSortedindex() 
            > ls.getElement().getSortedindex()) == sortAsc) {
          
          // P  <->    elem_maintained  <->  elem_current  <->  S
          // is to be transformed into
          // P   <->    elem_current  <->  elem_maintained  <->  S
          
          final Element<SecureListType> elemP = elemMaintained
              .getElemPredecessor();
          final Element<SecureListType> elemS = ls.getElement()
              .getElemSuccessor();
          final Element<SecureListType> elemCurrent = ls.getElement();
          
          elemMaintained.setElemPredecessor(elemCurrent);
          elemCurrent.setElemSuccessor(elemMaintained);
          
          elemCurrent.setElemPredecessor(elemP);
          elemP.setElemSuccessor(elemCurrent);
          
          elemMaintained.setElemSuccessor(elemS);
          elemS.setElemPredecessor(elemMaintained);
        } else {
          
          elemMaintained = ls.getElement();
        }


        ls.next(transactionId, ID_NO_PREDECESSOR);
        amountSteps++;
      }
    }
    ls.finishTransaction(transactionId);
  }
  
  
  /**
   * Randomly fills unsorted list for being able to test the sort
   * algorithm.
   * 
   * @param xamount the amount of elements
   * @param sls     the list.
   */
  public void testInitializeUnSortedList(final int xamount,
      final Securelistsort<String> sls) {

    for (int i = 0; i < xamount; i++) {
      int rand = new Random().nextInt(10);
      sls.ls.insertBehind(rand + "", ID_NO_PREDECESSOR);
      sls.ls.getElement().setSortedindex(rand);
    }
  }
  
  /**
   * Change the sort index of the current element.
   * @param xsortedIndex the new sorted index of the current element.
   */
  public final void changeSortIndex(final int xsortedIndex) {
    
    //if the current element is not null change sort index.
    if (getElement() != null) {
      getElement().setSortedindex(xsortedIndex);
    } else {
      ls.getLog().warning("The current element is null. Thus it "
          + "is impossible to change the sort index.");
    }
  }
  
  
  /**
   * goes behind the searched position.
   * 
   * 
   * @param xsearchCriteria 
   *         the index of sorting.
   * 
   * @param xtransactionId
   *         the id of the current operation for being able to start a 
   *         new transaction.
   * 
   * @param xclosedactionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final synchronized void findSorted(final double xsearchCriteria,
      final int xtransactionId, final int xclosedactionId) {
    
    //if list is empty there is nothing to do. Thus only perform action 
    //if list is not empty.
    if (!ls.isEmpty()) {
      
      //if the list is neither behind nor in front of perform action
      //otherwise go to the last respectively the first item.
      if (!ls.isBehind() && !ls.isInFrontOf()) {
        
        //if the current element has got an inferior index proceed
        //in list while the current item has got 
        if (ls.getElement() != null 
            && ls.getElement().getSortedindex() < xsearchCriteria) {
          while (!ls.isBehind() && ls.getElement().getSortedindex() 
              < xsearchCriteria) {
            ls.next(xtransactionId, xclosedactionId);
          }
        } else if (ls.getElement() != null) {
          while (!ls.isInFrontOf()
              
              && ls.getElement().getSortedindex() 
              > xsearchCriteria) {
            ls.previous(xtransactionId, xclosedactionId);
          }
          
          //if the current element is not in front of the list
          //perform one next operation for being behind the last item
          //that has got an inferior index. (and for maintaining
          //integrity of the result independent of the starting 
          //position in the list)
          if (!ls.isInFrontOf()) {
            ls.next(xtransactionId, xclosedactionId);  
          }
        } else {
          ls.toFirst(xtransactionId, xclosedactionId);
          
          while (ls.getItem() == null) {
            ls.next(xtransactionId, xclosedactionId);
          }
          if (!ls.isBehind()) {

            findSorted(xsearchCriteria, xtransactionId, 
                xclosedactionId);
          }
        }
      } else if (ls.isBehind()) {
        ls.toLast(xtransactionId, xclosedactionId);
        findSorted(xsearchCriteria, xtransactionId, xclosedactionId);
      } else {
        ls.toFirst(xtransactionId, xclosedactionId);
        findSorted(xsearchCriteria, xtransactionId, xclosedactionId);
      }
    }
  }


  public void resetTransaction() {
    ls.resetTransaction();
  }
  
  
  public void resetClosedAction() {
    ls.resetClosedAction();
  }
  

  
}
