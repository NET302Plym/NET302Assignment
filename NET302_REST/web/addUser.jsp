<%@page import="NET302JavaLibrary.User"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take USER, NEW, and HASH as parameters,
    // where USER = the GSON object to add/update,
    // where NEW = is TRUE or FALSE and establishes whether to add or update,
    // where HASH = is the hashed value for the password.

    // TODO: Check that this is how the password should be dealt, hash goes up
    // hash goes into DB right?
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    Encrypter.SymmetricEncrypter e = new Encrypter.SymmetricEncrypter();
    // Get the parameters:
    String  userP   = e.DecryptString(request.getParameter("USER"));
    String  newP    = e.DecryptString(request.getParameter("NEW"));
    String  passP   = e.DecryptString(request.getParameter("HASH"));
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for both parameters existing:
    if (userP.length() > 1 & newP.length() > 1 & passP.length() > 1) {
        // Presume success unless errored:
        result = "SUCCESS: Query passed to handler method without error.";
        
        // Create Order from String which should be of JSON format:
        User tempUser = new User(userP);
        
        if (result.equals("SUCCESS")) {
            if (handler.GetConnection()) {
                if (newP.equals("TRUE")) {
                    try {
                        handler.newUser(tempUser, passP);
                    } catch (SQLException ex) {
                        // SQL Error.
                        result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
                    } finally { handler.CloseConnection(); }
                }
                else {
                    try {
                        handler.updateUser(tempUser);
                    } catch (SQLException ex) {
                        // SQL Error.
                        result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
                    } finally { handler.CloseConnection(); }
                }
            } else {
                // Failed to get a connection!
                result = "ERROR: Could not get connection from database.";
            }
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nUSER={GSON}, NEW=TRUE/FALSE, HASH=...";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(e.EncryptString(result));
    out.flush();
%>
    