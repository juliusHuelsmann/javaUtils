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

import java.math.BigInteger;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DHClient {
  
  
  
  /**
   * The computed secretKey
   */
  private byte[] secretKey;
  
  public DHClient(final String xreceivedPKFormat,
      final byte[] xreceivedPKEncoded,
      final String xreceivedPKAlgorithm,
      final Dhserver xDHServer) {
    
    // generate secret key
    try {
        final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(xDHServer.getPrivateKey());
        DHPublicKey xreceivedPublicKey = new DHPublicKey() {
            
          public String getFormat() {
              return  xreceivedPKFormat;
            }
            
            public byte[] getEncoded() {
              return  xreceivedPKEncoded;
            }
            
            public String getAlgorithm() {
              return xreceivedPKAlgorithm;
            }

            public DHParameterSpec getParams() {
//              DHParameterSpec dh = new DHParameterSpec(p, g)
              // TODO Auto-generated method stub
              return null;
            }

            public BigInteger getY() {
              // TODO Auto-generated method stub
              return null;
            }
        };
        keyAgreement.doPhase(xreceivedPublicKey, true);

        secretKey = shortenSecretKey(keyAgreement.generateSecret());
    } catch (Exception e) {
        e.printStackTrace();
    }

  }
  public void generateCommonSecretKey() {

}

  /**
   * 1024 bit symmetric key size is so big for DES so we must shorten the key size. You can get first 8 longKey of the
   * byte array or can use a key factory
   *
   * @param   longKey
   *
   * @return
   */
  private byte[] shortenSecretKey(final byte[] longKey) {

      try {

          // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
          final byte[] shortenedKey = new byte[8];

          System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

          return shortenedKey;

          // Below lines can be more secure
          // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
          // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
          //
          // return keyFactory.generateSecret(desSpec).getEncoded();
      } catch (Exception e) {
          e.printStackTrace();
      }

      return null;
  }
  public byte[] encryptMessage(final String message) {

    try {

        // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
        final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
        final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        final byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        return (encryptedMessage);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
  
  
  public void receiveAndDecryptMessage(final byte[] message) {

    try {

        // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
        final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
        final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        final String secretMessage = new String(cipher.doFinal(message));
        System.out.println(secretMessage);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
