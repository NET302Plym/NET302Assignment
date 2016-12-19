<%@page import="javax.crypto.BadPaddingException"%>
<%@page import="javax.crypto.IllegalBlockSizeException"%>
<%@page import="java.security.InvalidKeyException"%>
<%@page import="javax.crypto.NoSuchPaddingException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.PublicKey"%>
<%@page import="Encrypter.Encrypter"%>
<%  // This page will take CODE as a parameter,
    // where, CODE = a client given identifier.
    // this is used in the requestSessionKey page.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    String  codeP   = request.getParameter("CODE");
    String  keyP    = request.getParameter("KEY");
    
    if (codeP.length() > 0 & keyP.length() > 0) {
        // Decode the String into a Public Key:
        try {
        PublicKey key = Encrypter.decodePublicKey(keyP.getBytes());
        
        Encrypter enc = new Encrypter(); // nothing thrown.
        
        // Attempt to load existing key, else generate them:
        if (enc.loadPublicKey("s_rsa_public.txt") == null) {
            enc.genPPKeys();
            enc.savePrivateKey("s_rsa_private.txt", enc.getPrivKey());
            enc.savePublicKey("s_rsa_public.txt", enc.getPublKey());
        }
        // Save off the client's public key with the codename:
        Encrypter.savePublicKey(codeP + "_public.txt", key);
        
        // Encrypt the server public key with the client's key:
            result = new String(
                enc.encrypt(key, enc.getPublKey().getEncoded()));
            
            // Handling exceptions for the Encrypter class.
            // More detail to be added if these do fail, but everything should
            // be controlled once initial testing is complete!
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