<%@page import="javax.crypto.NoSuchPaddingException"%>
<%@page import="java.security.spec.InvalidKeySpecException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.io.IOException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.spec.InvalidKeySpecException"%>
<%@page import="java.security.PublicKey"%>
<%@page import="Encrypter.Encrypter"%>
<%  // This page will take KEY as a parameter.
    // where KEY - the public key to use to encrypt and return a session key.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Get the parameters:
    String  keyP  = request.getParameter("KEY");
    
    if (keyP.length() > 0) {
        // Translate the parameter to bytes:
        byte[] keyBytes = keyP.getBytes();
        
        try {
            // Decode into public key:
            Encrypter enc = new Encrypter();
            PublicKey key = enc.decodePublicKey(keyBytes);
        
            // Get a session IV:
            byte[] iv = enc.getIv();
            
            // Encrypt the session IV:
            byte[] resultData = enc.encrypt(key, iv);
            
            // Get the result:
            result = new String(resultData);
            
            // TODO: Error messages / handling:
        } catch (InvalidKeySpecException ex) {
            
        } catch (NoSuchAlgorithmException ex) {
            
        } catch (IOException ex) {

        } catch (NoSuchPaddingException ex) {
            
        }
    }
%>