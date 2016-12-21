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
 *
 * @author Dan
 */
public class SymmetricEncrypter {
    private String keyString = "NET_302_Plym_KEY";
    private String ivString = "YEK_mylP_203_TEN";
    // | Type = AesCbcPkcs7
    private int keyLength = 128;
    
    private Cipher cipher;
    private IvParameterSpec ivSpec;
    private SecretKey aesKey;
    
    private byte[] keyBytes;
    
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
    
    public String EncryptString(String s) throws Exception {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        byte[] enc_val = cipher.doFinal(s.getBytes());
        String str_val = DatatypeConverter.printBase64Binary(enc_val);
        
        return str_val;
    }
    
    public String DecryptString(String s) throws Exception {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        
        byte[] dec_val = DatatypeConverter.parseBase64Binary(s);
        String str_val = new String(cipher.doFinal(dec_val));
        
        return str_val;
    }
}
