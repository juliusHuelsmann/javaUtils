package diffiehelman.control;

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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;


public class Dhserver {


  private final PrivateKey privateKey;
  private final PublicKey publicKey;
  
  
  public Dhserver() {
    
    
    /*
     * 
     * Generates the private and public key that will never change in runtime
     */

    KeyPairGenerator keyPairGenerator;

    try {
      
      //
      // Inside this very line, the NoSuchAlgorithmException can be raised.
      // This error may neither be fixed nor occur. 
      keyPairGenerator = KeyPairGenerator.getInstance("DH");

      
      /*
       * 
       * Initializes the key pair generator for a certain keysize using
       * a default parameter set and the {@code SecureRandom}
       * implementation of the highest-priority installed provider as the source
       * of randomness.
       * (If none of the installed providers supply an implementation of
       * {@code SecureRandom}, a system-provided source of randomness is
       * used.)
       *
       * @param keysize the keysize. This is an
       * algorithm-specific metric, such as modulus length, specified in
       *    number of bits.
       *
       * @exception InvalidParameterException if the {@code keysize} is not
       * supported by this K
       */
      keyPairGenerator.initialize(2 * 1024);


    } catch (NoSuchAlgorithmException e) {
      keyPairGenerator = null;
      e.printStackTrace();
    }
    

    //
    // Store the keys
    if (keyPairGenerator == null) {

      this.privateKey = null;
      this.publicKey = null;
    } else {

      final KeyPair keyPair = keyPairGenerator.generateKeyPair();
      this.privateKey = keyPair.getPrivate();
      this.publicKey  = keyPair.getPublic();
    }
  }
  
  
  public static void main(final String[] xargs) {
    Dhserver dhs1 = new Dhserver();
    Dhserver dhs2 = new Dhserver();

    DHClient dhc1 = new DHClient(
        dhs2.getPublicKey().getFormat(), 
        dhs2.getPublicKey().getEncoded(), 
        dhs2.getPublicKey().getAlgorithm(), dhs1);
    DHClient dhc2 = new DHClient(
        dhs1.getPublicKey().getFormat(), 
        dhs1.getPublicKey().getEncoded(), 
        dhs1.getPublicKey().getAlgorithm(), dhs2);
    
    dhc1.receiveAndDecryptMessage(dhc2.encryptMessage("das hier ists"));
  }


  /**
   * @return the privateKey
   */
  public PrivateKey getPrivateKey() {
    return privateKey;
  }


  /**
   * @return the publicKey
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }
}
