package Encrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * TODO: 
 * JavaDoc comments
 * Tidying up.
 * Testing using REST and Connector.
 * @author Sam
 */
public class Encrypter {
    private static final int            RSA_LEN     = 128;
    private static final String         KEY         = "NET_302_Plym_KEY";
    private static  SecretKey           aesKey;
    private         byte[]              keyBytes;
    private         byte[]              iv;
    private         Cipher              cipher;
    private         IvParameterSpec     ivSpec;
    private         PrivateKey          privKey;
    private         PublicKey           publKey;
    
    public Encrypter() {     
        // AES KEY:
        keyBytes = KEY.getBytes();
        
        // DEBUGGING:
        //String test = new String(keyBytes);
        //char[] c = test.toCharArray();
        //System.out.println("L: " + c.length);
        //System.out.println("L: " + Arrays.toString(c));
        //System.out.println("L: " + Arrays.toString(keyBytes));
        
        // If the AES length is too long, shorten it the the correct size:
        // Need to confirm this is correct!
        if (keyBytes.length != 32) {
            //keyBytes = Encrypter.hashBytes(keyBytes);
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
    
    // Generates an IV:
    public void genIv() throws NoSuchAlgorithmException {
        SecureRandom randomSecure = SecureRandom.getInstance("SHA1PRNG");
        this.iv = new byte[cipher.getBlockSize()];
        randomSecure.nextBytes(iv);
        this.ivSpec = new IvParameterSpec(iv);
    }
    
    // This generates the private public keys
    // separate method to reduce memory usage when private public is not needed (?)
    // not static because if you are using the pp keys, you are then going to use
    // aes keys.
    public void genPPKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(RSA_LEN);
        KeyPair kp = kpg.generateKeyPair();
        
        privKey = kp.getPrivate();
        publKey = kp.getPublic();
    }

    // method to save off the IV:
    public boolean saveIV(String filename) {
        boolean result = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename));
            fos.write(iv);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } finally {
            try {
                if (fos != null) { fos.close(); }
            } catch (IOException ex) {
                Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
                result = false;
            }
        }
        return result;
    }

    // method to load in the IV:
    public boolean loadIV(String filename) {
        try {
            iv = Files.readAllBytes(new File(filename).toPath());
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
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
    
    // Used to decode a key from bytes, this is for the JSP pages to the encrypt a message and
    // send it back to the client.
    public static PublicKey decodePublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
            new X509EncodedKeySpec(bytes));
        
        return publicKey;
    }
    
    // Static hashing method, best make sure that NoSuchAlgorithm will never be thrown!!
    public static byte[] hashBytes(byte[] bytes) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            return sha.digest(bytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Load method for private:
    public static PrivateKey loadPrivateKey(String filename) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            // Private:
            byte[] rsaBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(rsaBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
            
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Load method for any public
    public static PublicKey loadPublicKey(String filename) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            // Public:
            byte[] rsaBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(rsaBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
            
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    // Method for saving a given key off somewhere!
    public static boolean savePublicKey(String filename, PublicKey givenKey) {
        boolean result = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename));
            fos.write(givenKey.getEncoded());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } finally {
            try {
                if (fos != null) { fos.close(); }
            } catch (IOException ex) {
                Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
                result = false;
            }
        }
        return result;
    }
    // Save any private key
    public static boolean savePrivateKey(String filename, PrivateKey key) {
        boolean result = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filename));
            fos.write(key.getEncoded());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        } finally {
            try {
                if (fos != null) { fos.close(); }
            } catch (IOException ex) {
                Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
                result = false;
            }
        }
        return result;
    }
    
    // Simple file deletion:
    public static boolean deleteFile(String filename) {
        try {
            Files.delete(new File(filename).toPath());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}