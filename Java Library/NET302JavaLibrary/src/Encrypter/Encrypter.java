package Encrypter;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Sam
 */
public class Encrypter {
    private static final String         KEY         = "NET_302_Plym_KEY";
    private static  SecretKey           aesKey;
    private         Cipher              cipher;
    private         byte[]              keyBytes;
    private         byte[]              iv;
    private         IvParameterSpec     ivSpec;
    private         PrivateKey          privKey;
    private         PublicKey           publKey;
    
    public Encrypter() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {     
        // AES KEY:
        keyBytes = KEY.getBytes();
        
        // DEBUGGING:
        //String test = new String(keyBytes);
        //char[] c = test.toCharArray();
        //System.out.println("L: " + c.length);
        //System.out.println("L: " + Arrays.toString(c));
        //System.out.println("L: " + Arrays.toString(keyBytes));
        
        // If the AES length is too long, shorten it the the correct size:
        // This may need to be the char array from above?
        if (keyBytes.length != 32) {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            
            // Deactivated - unsure if necessary or working:
            //keyBytes = sha.digest(keyBytes);
        }
        
        // Set up the AES key:
        aesKey  = new SecretKeySpec(keyBytes, "AES");
    }
    
    // Set the IV, used when updating freshness of the AES:
    public void setIv(byte[] iv) {
        this.iv = iv;
        this.ivSpec = new IvParameterSpec(iv);
    }
    
    // Return the IV:
    public byte[] getIv() {
        return iv;
    }
    
    // This generates the private public keys
    // separate method to reduce memory usage when private public is not needed (?)
    // not static because if you are using the pp keys, you are then going to use
    // aes keys.
    public void genPPKeys(int rsaLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(rsaLength);
        KeyPair kp = kpg.generateKeyPair();
        
        privKey = kp.getPrivate();
        publKey = kp.getPublic();
    }
    
    // Used to decode a key from bytes, this is for the JSP pages to the encrypt a message and
    // send it back to the client.
    public PublicKey decodePublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
            new X509EncodedKeySpec(bytes));
        
        return publicKey;
    }
    
    // Encrypt with AES - to be used when returning from JSP and sending to JSP:
    public String encryptString(String s) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        byte[] enc_val = cipher.doFinal(s.getBytes());
        String str_val = DatatypeConverter.printBase64Binary(enc_val);
        
        return str_val;
    }
    
    // Decrypt with AES - to be used when returning from the JSP and sending to JSP:
    public String decryptString(String s) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        
        byte[] dec_val = DatatypeConverter.parseBase64Binary(s);
        String str_val = new String(cipher.doFinal(dec_val));
        
        return str_val;
    }
    
    // Encrypt byte to byte - to be used when handling session keys:
    public byte[] encrypt(PublicKey k, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }
    
    // Decrypt byte to byte - to be used when handlind session keys:
    public byte[] decrypt(PrivateKey k, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(cipherText);
    }

    public PrivateKey getPrivKey() {
        return privKey;
    }

    public PublicKey getPublKey() {
        return publKey;
    }
}