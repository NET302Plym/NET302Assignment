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
    
    /**
     * This is the constructor. It will initialise the AES key. 
     * An object must be created in order to use AES encryption, but some usage
     * of Encryption will not need this until the very end, hence static methods.
     */
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
    /**
     * Sets the iv (initialisation vector) and IvParameterSpec from that, so that
     * a key may be kept fresh by updating this value.
     * @param iv 
     */
    public void setIv(byte[] iv) {
        this.iv = iv;
        this.ivSpec = new IvParameterSpec(iv);
    }
    
    /**
     * Gets the value of the Iv.
     * @return byte[] - being the Iv.
     */
    public byte[] getIv() {
        return iv;
    }
    
    /**
     * Generates an Iv for the class. This is to prevent human randomness from
     * making the key insecure.
     * @throws NoSuchAlgorithmException - Thrown if the algorithm is not recognised.
     * This should never be the case, but must be thrown so that the class using
     * this method can be informed!
     */
    public void genIv() throws NoSuchAlgorithmException {
        SecureRandom randomSecure = SecureRandom.getInstance("SHA1PRNG");
        this.iv = new byte[cipher.getBlockSize()];
        randomSecure.nextBytes(iv);
        this.ivSpec = new IvParameterSpec(iv);
    }
    
    /**
     * Generates a set of public/private keys to be used.
     * This method is NOT static, as you will only be generating a set of keys 
     * if you are also about to use AES. No need to be static.
     * @throws NoSuchAlgorithmException - Thrown if the algortithm is not recognised.
     * This should never be the case, but must be thrown so that the class using
     * this method can be informed!
     */
    public void genPPKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(RSA_LEN);
        KeyPair kp = kpg.generateKeyPair();
        
        privKey = kp.getPrivate();
        publKey = kp.getPublic();
    }

    /**
     * Helper method to encourage code re-use.
     * We will want to save the Iv off in the case of the server, this ensures
     * that the key is kept in memory between sessions.
     * @param filename String - being the filename to save to.
     * @return boolean - being whether the operation succeeded or not.
     */
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

    /**
     * Helper method to encourage code re-use.
     * We will want to load the Iv for different server sessions. This prevents
     * to data from being lost in between communications.
     * @param filename String - filename to save to.
     * @return boolean - being whether the operation succeeded or not.
     */
    public boolean loadIV(String filename) {
        try {
            iv = Files.readAllBytes(new File(filename).toPath());
        } catch (IOException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    /**
     * AES encryption method. Here we use the AES key to encrypt a string.
     * To use; the object must be instantiated and the Iv established!
     * @param s String - being the data to encrypt.
     * @return String - being the encrypted version of S.
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException 
     * For all of the errors, this is to communicate to the class using this method!
     * None of them will be thrown under expected usage.
     */
    public String encryptString(String s) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);

        byte[] enc_val = cipher.doFinal(s.getBytes());
        String str_val = DatatypeConverter.printBase64Binary(enc_val);
        
        return str_val;
    }
    
    /**
     * AES decryption method. Here we use the AES key to decrypt a String.
     * To use; the object must be instantiated and the Iv established!
     * @param s String - being the encrypted data.
     * @return String - being the decrypted version of S.
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * For all of the errors, this is to communicate to the class using this method!
     * None of them will be thrown under expected usage.
     */
    public String decryptString(String s) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        cipher  = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        
        byte[] dec_val = DatatypeConverter.parseBase64Binary(s);
        String str_val = new String(cipher.doFinal(dec_val));
        
        return str_val;
    }
    
    /**
     * PublicKey encryption. Here we use the given public key to encrypt data.
     * @param k PublicKey - being the key to use.
     * @param data byte[] - being the data to encrypt.
     * @return bye[] - being the encrypted version of data.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     * For all of the errors, this is to communicate to the class using this method!
     * None of them will be thrown under expected usage.
     */
    public byte[] encrypt(PublicKey k, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * PrivateKey decryption. Here we use the given private key to decrypt data.
     * @param k PrivateKey - being the key to use.
     * @param cipherText byte[] - being the encrypted bytes.
     * @return byte[] - being the decrypted version of cipherText.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     * For all of the errors, this is to communicate to the class using this method!
     * None of them will be thrown under expected usage.
     */
    public byte[] decrypt(PrivateKey k, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(cipherText);
    }

    /**
     * Gets the Private key from this class (i.e. when generated or stored).
     * @return PrivateKey - being the key.
     */
    public PrivateKey getPrivKey() {
        return privKey;
    }

    /**
     * Gets the Public key from this class (i.e. when generated or stored).
     * @return PublicKey - being the key.
     */
    public PublicKey getPublKey() {
        return publKey;
    }
    
    /**
     * Decodes a key from byte[[]. Used when establishing a session key, because
     * the server's PublicKey will be sent Encrypted with the Client's key.
     * @param bytes byte[] - being the bytes that represent the key.
     * @return PublicKey - being the key from bytes.
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException 
     */
    public static PublicKey decodePublicKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
            new X509EncodedKeySpec(bytes));
        
        return publicKey;
    }

    /**
     * Helper method to encourage code re-use. Static so it may be employed in
     * a multitude of places.
     * @param bytes byte[] - being the bytes to hash.
     * @return byte[] - being the result after hashing.
     */
    public static byte[] hashBytes(byte[] bytes) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            return sha.digest(bytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Helper method to encourage code re-use. Static so it may be used without
     * creating an Encrypter object - saves memory!
     * @param filename String - being the location to load from.
     * @return PrivateKey - being the key found in the file location.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
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
    
    /**
     * Helper method to encourage code re-use. Static so it may be used without
     * creating an Encrypter object - saves memory!
     * @param filename String - being the location to load from.
     * @return PublicKey - being the key found in the file location.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
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
    
    /**
     * Helper method to encourage code re-use. Static so it may be used without
     * creating an Encrypter object - saves memory!
     * @param filename String - being the location to save the key into .
     * @param givenKey PublicKey - being the key to save.
     * @return boolean - true if no errors were encountered.
     */
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
    
    /**
     * Helper method to encourage code re-use. Static so it may be used without
     * creating an Encrypter object - saves memory!
     * @param filename String - being the location to save the key into.
     * @param key PrivateKey - being the key to save.
     * @return boolean - true if no errors were encountered.
     */
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
    
    /**
     * Helper method to encourage code re-use. Static so it may be used without
     * creating an Encrypter object - saves memory!
     * @param filename String - being the file location to try and delete.
     * @return boolean - true if no errors were encountered.
     */
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