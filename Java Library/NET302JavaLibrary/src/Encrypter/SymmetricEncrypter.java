/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encrypter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * This class provides a Symmetric only version of the Encrypter class, 
 * as we could not get the system fully supporting the Asymmetric approach.
 * The class should be used by initialising an object, then using the encrypt
 * / decrypt methods to return data.
 * @author Dan
 */
public class SymmetricEncrypter {
    private final String    keyString   = "NET_302_Plym_KEY";
    private final String    ivString    = "YEK_mylP_203_TEN";
    // | Type = AesCbcPkcs7
    private final int       keyLength   = 128;
    
    private       Cipher            cipher;
    private final IvParameterSpec   ivSpec;
    private final SecretKey         aesKey;
    private final byte[]            keyBytes;
    
    public SymmetricEncrypter(){
        // Padd out the key & IV
        //while (keyString.length() < 16)
        //    keyString += "=";
        //while (ivString.length() < 16)
        //    ivString += "=";
        keyBytes = keyString.getBytes();
        ivSpec = new IvParameterSpec(ivString.getBytes());
        aesKey = new SecretKeySpec(keyBytes, "AES");
    }
    
    /**
     * Encrypts a given String using the AES key in this class. It changes the
     * string to byte[], then back to String to be read by the REST server,
     * MobileApplication, or ClientConnector.
     * @param s String - being the data to encrypt.
     * @return String - being the encrypted String.
     * @throws Exception 
     */
    public String EncryptString(String s) throws Exception {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        byte[] enc_val = cipher.doFinal(s.getBytes());
        String str_val = DatatypeConverter.printBase64Binary(enc_val);
        
        return str_val;
    }
    
    /**
     * Decrypts a given String using the AES key in this class. It changes the
     * String to byte[], and back to String to be read by the REST server,
     * MobileApplication, or ClientConnector.
     * @param s String - being the data to decrypt.
     * @return String - being the decrypted data.
     * @throws Exception 
     */
    public String DecryptString(String s) throws Exception {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        
        byte[] dec_val = DatatypeConverter.parseBase64Binary(s);
        String str_val = new String(cipher.doFinal(dec_val));
        
        return str_val;
    }
}
