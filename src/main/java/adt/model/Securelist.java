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

import java.awt.Point;
import java.io.Serializable;
import java.util.logging.Logger;

import adt.model.stack.Stack;


/**
 * SecureList is an indirect extension of the list class.
 * 
 * <p>
 * There are two special new features:
 *   1) Closed Actions
 *   2) Transactions
 * 
 * <p>
 * 1) Closed Actions:
 * If the current element of the list is to be maintained after a performed 
 * action which passes the list start-closed-action before the action and 
 * call end-closed-action afterwards.
 * 
 * <p>
 * During the closed action it is impossible to remove items out of the list
 * and to add items to list but it is only possible to pass the list
 * by using next and previous methods and to check the current element.
 * 
 * <p>
 * 2) Transactions:
 * If no other operation except of children of the current transaction
 * shell be able to change the state of the list, the method start-transaction
 * is called before the action and end-transaction afterwards.
 * 
 * @author Julius Huelsmann
 * @version %I%, %U%
 * @param <Securelisttype>  the type of the SecureList.
 */
public class Securelist<Securelisttype> implements Serializable {

  /**
   * Default serial version UID for being able to identify the list's 
   * version if saved to the disk and check whether it is possible to 
   * load it or whether important features have been added so that the
   * saved file is out-dated.
   */
  private static final long serialVersionUID = 1L;

  
  /**
   * The list which is the base of the SecureList and which methods
   * are used.
   */
  private List<Securelisttype> ls;
  
  
  /**
   * The id which is given to methods for transaction and closed actions if
   * there is no predecessor transaction / closed action.
   */
  public static final int ID_NO_PREDECESSOR = -1;

  
  /**
   * This boolean indicates, whether even though an error occurred that
   * destroyed the order of transactions / closed actions the action is 
   * completed for not throwing an exception and for keeping the program
   * running.
   */
  private final boolean debugStayrunning = true;
  
  /*
   * Variables for transactions
   */
  
  /**
   * Stack which contains elements used for closed action. 
   * 
   * <p>
   * If the current element of the list is to be maintained after a performed 
   * action which passes the list start-closed-action before the action and 
   * call end-closed-action afterwards.
   * 
   * <p>
   * During the closed action it is impossible to remove items out of the list
   * and to add items to list but it is only possible to pass the list
   * by using next and previous methods and to check the current element.
   */
  private  Stack<Closedaction<Securelisttype>> stckClosedaction;
  

  /**
   * Stack which contains elements used for transactions. 
   * 
   * <p>
   * If an action is not to be interrupted by other actions, this gives the
   * possibility to start a transaction (which do also allow nested scopes).
   * Thus it is impossible to perform an action outside the current 
   * transaction.
   */
  private  Stack<Transaction<Securelisttype>> stckTransaction;
  
  /*
   * Constructor
   */
  
  /**
   * Constructor of SecureList initializes the list.
   */
  public Securelist(final Logger xlog) {
    
    //initialize the list of which the SecureList consists
    ls = new List<Securelisttype>(xlog);
    
    //initialize lists for closed actions and transactions
    stckClosedaction = new Stack<Closedaction<Securelisttype>>();
    stckTransaction = new Stack<Transaction<Securelisttype>>();
  }
  
  

  /*
   * Functions returning the state of the list. They do not change the list;
   * thus they are not interesting for the closed action and transaction
   * extensions.
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
   * Methods for navigating through the list. These Methods change the state
   * of the current-element-pointer. Thus only those closed-actions and
   * transactions are allowed to perform them that are current.
   */
  
  /**
   * Proceed one step in the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionid 
   *         the id of the closed action to which performs the
   *         method call.
   * 
   */
  public final synchronized void next(
      final int xtransactionId, final int xclosedActionid) {
    
    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "next";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(xclosedActionid, methodName)) {
      
      //perform method call.
      ls.next();
    } else if (debugStayrunning) {
      ls.next();
    }
  }

  



  /**
   * Step back in the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionid 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void previous(
      final int xtransactionId, final int xclosedActionid) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "previous";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(xclosedActionid, methodName)) {
      
      //perform method call.
      ls.previous();
    } else if (debugStayrunning) {
      ls.previous();
    }
  }

  
  /**
   * Go to the beginning of the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedActionid 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void toFirst(
      final int xtransactionId, final int xclosedActionid) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "toFirst";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(xclosedActionid, methodName)) {
      
      //perform method call.
      ls.toFirst();
    } else if (debugStayrunning) {
      ls.toFirst();
    }
  }

  
  /**
   * Go to the end of the list.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   * @param xclosedactionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void toLast(
      final int xtransactionId, final int xclosedactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "toLast";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(xclosedactionId, methodName)) {
      
      //perform method call.
      ls.toLast();
    } else if (debugStayrunning) {
      ls.toLast();
    }
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
   * @param xclosedactionId 
   *         the id of the closed action to which performs the
   *         method call.
   */
  public final void goToElement(final Element<Securelisttype> xelemCurrent,
      final int xtransactionId, final int xclosedactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "goToElement";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(xclosedactionId, methodName)) {
      
      //perform method call.
      ls.goToElement(xelemCurrent);
    } else if (debugStayrunning) {
      ls.goToElement(xelemCurrent);
    }
  }
  
  

  /*
   * Methods for getting content of the list's current element or for printing
   * the content of the list or transforming the list into an array. 
   * They are not changing the current state of the list; thus it is not 
   * necessary to check the state of transaction or closedAction.
   */
  
  /**
   * Return current Element.
   *
   * @return current Element.
   */
  public final Securelisttype getItem() {
    return ls.getItem();
  }
  
  
  /**
   * Return current Element.
   *
   * @return current Element.
   */
  public final Element<Securelisttype> getElement() {
    return ls.getElement();
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
  public final synchronized Point[] toPntArray() {
    return ls.toPntArray();
  }
  /**
   * List to array method.
   * @return the array from list.
   */
  public final synchronized String[] toArrayString() {
    return ls.toArrayString();
  }
  
  
  
  /*
   * Special Methods
   */

  /**
   * create subList.
   * @return list after current item.
   */
  public final List<Securelisttype> subList() {
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

  
  /*
   * Method that change the list (not only by changing the current pointer 
   * position). 
   * 
   * In Closed actions that is forbidden because the state of the list
   * is not reproducible.
   */
  
  /**
   * Replaces current element with newContent.
   *
   * @param xnewContent contains the content which is to be inserted.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   */
  public final void replace(final Securelisttype xnewContent,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "replace";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.replace(xnewContent);
    } else if (debugStayrunning) {
      ls.replace(xnewContent);
    }
  }

  
  /**
   * Insert element after current position.
   *
   * @param xnewContent contains the content which is to be inserted.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   */
  public final void insertBehind(final Securelisttype xnewContent,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "insertBehind";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.insertBehind(xnewContent);
    } else if (debugStayrunning) {
      ls.insertBehind(xnewContent);
    }
  }

  
  /**
   * Insert element in front of current position.
   *
   * @param xnewContent contains the content which is to be inserted.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   * 
   */
  public final void insertInFrontOf(final Securelisttype xnewContent,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "insertInFrontOf";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.insertInFrontOf(xnewContent);
    } else if (debugStayrunning) {
      ls.insertInFrontOf(xnewContent);
    }
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

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "remove";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.remove();
    } else if (debugStayrunning) {
      ls.remove();
    }
  }

  
  /**
   * Inserts s.th. at the beginning of the list.
   *
   * @param xnewContent contains the content which is to be inserted.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   */
  public final void insertAfterHead(final Securelisttype xnewContent,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "insertAfterHead";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.insertAfterHead(xnewContent);
    } else if (debugStayrunning) {
      ls.insertAfterHead(xnewContent);
    }
  }

  
  /**
   * Inserts thing at the end of the list.
   *
   * @param xnewContent contains the content which is to be inserted.
   * 
   * @param xtransactionId 
   *         the id of the transaction to which performs the
   *         method call.
   */
  public final void insertAtTheEnd(final Securelisttype xnewContent,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "insertAtTheEnd";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      ls.insertAtTheEnd(xnewContent);
    } else if (debugStayrunning) {
      ls.insertAtTheEnd(xnewContent);
    }
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
  public final boolean find(final Securelisttype xtype,
      final int xtransactionId) {

    //the method name for being able to print additional information in 
    //check methods
    final String methodName = "find";
    
    //check whether the current transaction and the current closed action
    //are okay
    if (checkTransaction(xtransactionId, methodName) 
        && checkClosedaction(ID_NO_PREDECESSOR, methodName)) {
      
      //perform method call.
      return ls.find(xtype);
    } else if (debugStayrunning) {
      return ls.find(xtype);
    }
    
    //return false if unable to perform action.
    return false;
  }
  
  
  
  
  /*
   * Method for starting and finishing closed action and transactions and
   * for checking whether it is okay to perform an action in special 
   * action and transaction.
   */
  
  
  

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
    
    
    //threshold: the stack must not be null. catch this error.
    if (stckClosedaction == null) {
      ls.getLog().severe("The stack is null! That should not "
          + "happen!");
      stckClosedaction = new Stack<Closedaction<Securelisttype>>();
      return -1;
    }
    
    //check whether it is valid to start a new closed action
    if (!stckClosedaction.isEmpty()) {

      //the old closedAciton
      Closedaction<Securelisttype> caOld = 
          ((Closedaction<Securelisttype>)  
          stckClosedaction.getElemlast().getContent());
      
      
      //if the given id does not equal the one of the last transaction
      //and the current element is null and the stack is not empty
      if (caOld != null 
          && !stckClosedaction.isEmpty()
          && caOld.getId_secureList() != xoldOperationid) {
        
        ls.getLog().severe("Transaction " + caOld.getName() 
            + " not terminated! and new transaction");
        return -1;
      } 
    }
    

    //create new closed action and insert it into the stack
    Closedaction<Securelisttype> caNew = new Closedaction<Securelisttype>(
        xoperationName, ls.getElement());
    stckClosedaction.insert(caNew);
    return caNew.getId_secureList();
  }

  
  /**
   * Finish transaction; reset the state of the List and afterwards the
   * transaction values.
   * @param xoldOperationid the id of the transaction which is to be closed
   * @return the unique id of the current transaction
   */
  public final int finishClosedAction(final int xoldOperationid) {

    //threshold: the stack must not be null. catch this error.
    if (stckClosedaction == null) {
      ls.getLog().severe("The stack is null! That should not "
          + "happen!");
      stckClosedaction = new Stack<Closedaction<Securelisttype>>();
      return -1;
    }
    
    

    //the old closedAciton
    Closedaction<Securelisttype> caOld = 
        ((Closedaction<Securelisttype>)  
        stckClosedaction.getElemlast().getContent());
    
    
    //if the given id does not equal the one of the last transaction
    if (caOld != null 
        && caOld.getId_secureList() != xoldOperationid) {
      
      ls.getLog().severe("Wrong closed action to be terminated."
          + " Current one: " 
          + caOld.getName()
          + " Old id: " + xoldOperationid);
      return caOld.getId_secureList();
    } else {

      //apply state before secure action.
      ls.goToElement(caOld.getElem_secure());
      stckClosedaction.remove();

      if (!stckClosedaction.isEmpty()) {

        //return the new current id.
        Closedaction<Securelisttype> caCurrent = 
            ((Closedaction<Securelisttype>)  
            stckClosedaction.getElemlast().getContent());
        return caCurrent.getId_secureList();
      } else {
        return -1;
      }
    }
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
    
    
    //threshold: the stack must not be null. catch this error.
    if (stckTransaction == null) {
      ls.getLog().severe("The stack is null! That should not "
          + "happen! New transaction name: "
            + xtransactionName);
      stckTransaction = new Stack<Transaction<Securelisttype>>();
      return -1;
    }
    
    //check whether it is valid to start a new closed action
    if (!stckTransaction.isEmpty()) {

      //the old closedAciton
      Transaction<Securelisttype> caOld = 
          ((Transaction<Securelisttype>)  
              stckTransaction.getElemlast().getContent());
      
      
      //if the given id does not equal the one of the last transaction
      //and the current element is null and the stack is not empty
      if (caOld != null 
          && !stckTransaction.isEmpty()
          && caOld.getidSecurelist() != xoldTransactionid) {
        
        ls.getLog().severe("Transaction " + caOld.getName() 
            + " not terminated! New transaction name: "
            + xtransactionName);
        return ID_NO_PREDECESSOR;
      } 
    }
    

    //create new closed action and insert it into the stack
    Transaction<Securelisttype> caNew = new Transaction<Securelisttype>(
        xtransactionName);
    stckTransaction.insert(caNew);
    return caNew.getidSecurelist();
  }

  
  /**
   * Finish transaction; delete the current transaction.
   * 
   * @param xoldTransactionid the id of the transaction which is to be closed
   * 
   * @return the unique id of the current transaction
   */
  public final int finishTransaction(final int xoldTransactionid) {

    //threshold: the stack must not be null. catch this error.
    if (stckTransaction == null) {
      ls.getLog().severe("The stack is null! That should not "
          + "happen!");
      stckTransaction = new Stack<Transaction<Securelisttype>>();
      return -1;
    }
    
    

    //the old closedAciton
    Transaction<Securelisttype> caOld = 
        ((Transaction<Securelisttype>)  
            stckTransaction.getElemlast().getContent());
    
    
    //if the given id does not equal the one of the last transaction
    if (caOld != null 
        && caOld.getidSecurelist() != xoldTransactionid) {
      
      ls.getLog().severe("Wrong closed action to be terminated."
          + " Current one: " 
          + caOld.getName()
          + " Old id: " + xoldTransactionid);
      return caOld.getidSecurelist();
    } else {

      //remove the current transaction.
      stckTransaction.remove();

      if (!stckTransaction.isEmpty()) {

        //return the new current id.
        Transaction<Securelisttype> caCurrent = 
            ((Transaction<Securelisttype>)  
                stckTransaction.getElemlast().getContent());
        return caCurrent.getidSecurelist();
      } else {
        return -1;
      }
    }
  }
  
  
  
  
  
  
  
  
  
  

  
  

  /**
   * Check whether the closed action with given id is okay to start. Otherwise
   * print different errors. The error messages depend on the current state
   * of the stack and contain the name of the calling method.
   * 
   * @param xclosedactionId
   *         the id of the closed action to which the action belong
   * 
   * @param xmethodName
   *         the name of the method which calls the startClosedAction
   *         check method used for printing detailed error message.
   *
   * @return whether it is okay to start the action or not.
   */
  private boolean checkClosedaction(final int xclosedactionId, 
      final String xmethodName) {
    
    /*
     * Initialize values. 
     */
    
    //boolean containing the return value
    boolean success = false;
    
    //integer containing the currently active closed action id. Used
    //for detailed error message in case of no success.
    int currentCaid = -1 - 1;

    //String containing the currently active closed action name. Used
    //for detailed error message in case of no success.
    String currentCaname = "Undefined (currentCAID == -2)";
    
    //String containing the error message which is appended to additional
    //information on the action which is to be performed, the closed action
    //which is given to this method and the closed action which is currently
    //active and then logged in case an error occurred.
    String errorMessage = "";
    
    /*
     * Check whether the action is okay to start
     */
    
    //threshold: the stack must not be null. catch this error.
    if (stckClosedaction == null) {
      
      //print error
      errorMessage = ("The stack is null! That should never "
          + "happen!");
      
      //re-initialize the stack for being able to proceed without 
      //throwing this error each time a function is called that
      //deals with closed actions.
      stckClosedaction = new Stack<Closedaction<Securelisttype>>();
      
      //return true because obviously no closed action is running; 
      //thus it is impossible that the current closed action is not 
      //suitable.
      success = true;
    } else if (
        //check whether it is valid to start a new closed action
        !stckClosedaction.isEmpty()) {

      //the current closed action
      Closedaction<Securelisttype> caOld = 
          ((Closedaction<Securelisttype>)  
          stckClosedaction.getElemlast().getContent());
      

      //if the closed action id is not equal to the id which is passed
      //if there is nor closed action running print an error. Otherwise
      //everything is okay and the next - method can be performed.
      if (caOld == null) {

        //print error
        errorMessage = ("Trying to perform an action "
            + "which is part of a not - existing closed action."
            + " There is not closed action running."
            + " The isEmpty method is not working or the "
            + "method is called twice at a time.");

        //print error message because otherwise the error might 
        //disappear if the second approach is successful
        ls.getLog().severe(errorMessage); 
        
        //remove the current (null) - closed action for being able to 
        //proceed without  throwing this error each time a function 
        //is called that deals with closed actions.
        stckClosedaction.remove();
        
        //re - all this method with the altered stack. 
        success = checkClosedaction(xclosedactionId, xmethodName);

      } else {
        
        //fetch the values for printing information in case an
        //error occurs.
        currentCaname = caOld.getName();
        currentCaid = caOld.getId_secureList();
        
        if (
            //if the given id does not equal the one of the current 
            //closed action
            caOld.getId_secureList() != xclosedactionId) {

          //print error message.
          errorMessage = ("Trying to perform an action "
              + "which is part a different closed action.");
          
          //return false because obviously this is the wrong closed 
          //action
          success = false;
        } else {
          success = true;
        }
      }
          
    } else {
      
      //if the closed action id is not equal to the id which is passed
      //if there is nor closed action running print an error. Otherwise
      //everything is okay and the next - method can be performed.
      if (xclosedactionId != ID_NO_PREDECESSOR) {

        //print error
        errorMessage = ("Trying to perform an action "
            + "which is part of a not - existing closed action."
            + " There is not closed action running.");
        
        //return true because if there is no closed action that
        //is running, the action can be completed.
        success =  true;
      } else {
        
        success = true;
      }
    }
    
    
    //if an error occurred print error message.
    if (!success) {
      
      //the text which is appended to the error string generated during 
      //a pass of this method.
      final String errorInformationText = 
          "Error starting closed action."
              + "\n\tDemanded for Action:  " + xmethodName
              + "\n\tDemanded action-id:   " + xclosedactionId
              + "\n\tCurrenct action-id:   " + currentCaid
              + "\n\tCurrent  action-name: " + currentCaname;
      
      //print error message
      ls.getLog().severe(errorInformationText 
          + ":\n" + errorMessage); 
      
    }
    
    //return success
    return success;
    
  }
  

  
  

  /**
   * Check whether the transaction with given id is okay to start. Otherwise
   * print different errors. The error messages depend on the current state
   * of the stack and contain the name of the calling method.
   * 
   * @param xtransactionId
   *         the id of the transaction to which the action belong
   * 
   * @param xmethodName
   *         the name of the method which calls the startTransaction
   *         check method used for printing detailed error message.
   *
   * @return whether it is okay to start the action or not.
   */
  private boolean checkTransaction(final int xtransactionId, 
      final String xmethodName) {

    /*
     * Initialize values. 
     */
    
    //boolean containing the return value
    boolean success = false;
    
    //integer containing the currently active transaction id. Used
    //for detailed error message in case of no success.
    int currentCaid = -1 - 1;

    //String containing the currently active transaction name. Used
    //for detailed error message in case of no success.
    String currentCaname = "";
    
    //String containing the error message which is appended to additional
    //information on the action which is to be performed, the transaction
    //which is given to this method and the transaction which is currently
    //active and then logged in case an error occurred.
    String errorMessage = "";
    
    /*
     * Check whether the action is okay to start
     */
    //threshold: the stack must not be null. catch this error.
    if (stckTransaction == null) {
      
      //print error
      errorMessage = ("The stack is null! That should never "
          + "happen!");
      
      //re-initialize the stack for being able to proceed without 
      //throwing this error each time a function is called that
      //deals with transactions.
      stckTransaction = new Stack<Transaction<Securelisttype>>();
      
      //return true because obviously no transaction is running; 
      //thus it is impossible that the current transaction is not 
      //suitable.
      success = true;
    }
    
    //check whether it is valid to start a new transaction
    if (!stckTransaction.isEmpty()) {

      //the current transaction
      Transaction<Securelisttype> caOld = 
          ((Transaction<Securelisttype>)  
              stckTransaction.getElemlast().getContent());
      

      //if the transaction id is not equal to the id which is passed
      //if there is nor transaction running print an error. Otherwise
      //everything is okay and the next - method can be performed.
      if (caOld == null) {

        //print error
        errorMessage = ("Trying to perform an action "
            + "which is part of a not - existing transaction."
            + " There is not transaction running."
            + " The isEmpty method is not working or the "
            + "method is called twice at a time.");

        //print error message because otherwise the error might 
        //disappear if the second approach is successful
        ls.getLog().severe(errorMessage); 
        
        //remove the current (null) - transaction for being able to 
        //proceed without  throwing this error each time a function 
        //is called that deals with transactions.
        stckClosedaction.remove();
        
        //re - all this method with the altered stack. 
        success = checkClosedaction(xtransactionId, xmethodName);
        
      } else {
        
        //fetch the values for printing information in case an
        //error occurs.
        currentCaname = caOld.getName();
        currentCaid = caOld.getidSecurelist();
        
        if (
            //if the given id does not equal the one of the current 
            //transaction
            caOld.getidSecurelist() != xtransactionId) {

          //print error message.
          errorMessage = ("Trying to perform an action "
              + "which is part a different transaction.");
          
          //return false because obviously this is the wrong closed 
          //action
          success = false;
        } else {
          success = true;
        }
      }
          
    } else {
      
      //if the transaction id is not equal to the id which is passed
      //if there is nor transaction running print an error. Otherwise
      //everything is okay and the next - method can be performed.
      if (xtransactionId != ID_NO_PREDECESSOR) {

        //print error
        errorMessage = ("Trying to perform an action "
            + "which is part of a not - existing transaction."
            + " There is not transaction running.");
        
        //return true because if there is no transaction that
        //is running, the action can be completed.
        success = true;
      } else {
        
        success = true;
      }
    }

    //if an error occurred print error message.
    if (!success) {
      
      //the text which is appended to the error string generated during 
      //a pass of this method.
      final String errorInformationText = 
          "Error starting transaction."
              + "\n\tDemanded for Action:  " + xmethodName
              + "\n\tDemanded action-id:   " + xtransactionId
              + "\n\tCurrenct action-id:   " + currentCaid
              + "\n\tCurrent  action-name: " + currentCaname;
      
      //print error message
      ls.getLog().severe(errorInformationText 
          + ":\n" + errorMessage); 
      
    }
    
    //return success
    return success;
    
  }



  public void resetTransaction() {
    stckTransaction = new Stack<Transaction<Securelisttype>>();
  }

  public void resetClosedAction() {
    stckClosedaction = new Stack<Closedaction<Securelisttype>>();
  }


  /**
   * Return the logger.
   * @return the log
   */
  public Logger getLog() {
    return ls.getLog();
  }
}

