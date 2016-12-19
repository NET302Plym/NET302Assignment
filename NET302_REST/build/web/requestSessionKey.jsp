<%@page import="javax.crypto.BadPaddingException"%>
<%@page import="javax.crypto.IllegalBlockSizeException"%>
<%@page import="java.security.InvalidKeyException"%>
<%@page import="javax.crypto.NoSuchPaddingException"%>
<%@page import="java.security.spec.InvalidKeySpecException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.io.IOException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.spec.InvalidKeySpecException"%>
<%@page import="java.security.PublicKey"%>
<%@page import="Encrypter.Encrypter"%>
<%  // This page will CODE as a parameter - this should be encrypted with the 
    // servers PUBLIC key so to authorise the sender.
    // CODE = identifier for the client's key.
    // It will then return a session key encrypted with the public key that
    // the client should have given.

    // Set a temporary result to address catch statements:
    String aes_iv   = "s_aes_iv.txt";
    String result   = "TEMP";
    String codeP    = request.getParameter("CODE");
    
    if (codeP.length() > 0) {
        // Decrypt the code using server's saved key:
        Encrypter enc = new Encrypter();
        try {
            // Attempt to load the server's private key:
            if (enc.loadPrivateKey("s_rsa_private.txt") != null) {
                String code = new String(
                        enc.decrypt(enc.getPrivKey(), codeP.getBytes()));
                
                // Load, if not, generate an IV:
                if (!enc.loadIV(aes_iv)) {
                    enc.genIv();
                    enc.saveIV(aes_iv);
                }
                
                // Check for freshness here - maybe have it generate a new one?
                // TODO:
                
                // Attempt to load the client's public key:
                if (enc.loadPublicKey(code + "_public.txt") != null) {
                    // Encrypt the IV:
                    result = new String(
                        enc.encrypt(enc.getPublKey(), enc.getIv()));
                    
                    // Delete the code file so replay attacks are reduced:
                    Encrypter.deleteFile(code + "_public.txt");
                    
                } else { // ERROR: Client does not have a saved key!
                    result = "ERROR:";
                }
            } else  { // ERROR: Server's private key is not there!
                result = "ERROR: Loading the server's key has failed. Please try again.";
                // TODO: Shall we generate one here then?
            }
        } catch (NoSuchAlgorithmException ex) {
            result = "ERROR: Failure when using encrypter:"
                    + "\n" + ex.getMessage();
        } catch (NoSuchPaddingException ex) {
            result = "ERROR: Failure when using encrypter:"
                    + "\n" + ex.getMessage();
        } catch (InvalidKeyException ex) {
            result = "ERROR: Failure when using encrypter:"
                    + "\n" + ex.getMessage();
        } catch (IllegalBlockSizeException ex) {
            result = "ERROR: Failure when using encrypter:"
                    + "\n" + ex.getMessage();
        } catch (BadPaddingException ex) {
            result = "ERROR: Failure when using encrypter:"
                    + "\n" + ex.getMessage();
        }
    }
    // Print out the result & flush:
    out.print(result);
    out.flush();
%>
<br>