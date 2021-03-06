<%@page import="NET302JavaLibrary.User"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302JavaLibrary.GenericLookup"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take ID, HASH as parameters,
    // where ID = the ID of the user to authenticate.
    // where HASH = the hashed password to test against.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    Encrypter.SymmetricEncrypter e = new Encrypter.SymmetricEncrypter();
    // Get the parameters:
    String  idP  = e.DecryptString(request.getParameter("ID"));
    String  passP = e.DecryptString(request.getParameter("HASH"));
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();

    // Check for all of the parameters existing:
    if (idP.length() > 1 & passP.length() > 1) {
        if (handler.GetConnection()) {
            try {
                //int id = Integer.parseInt(idP);
                
                // Test the password:
                User user = handler.authUser(idP, passP);
                
                if (user != null) {
                    result = user.GetJSONString();
                } else {
                    result = "ERROR: Failure to authenticate given username/password.";
                }
            } catch (NumberFormatException ex) {
                // ID parameter error:
                result = "ERROR: Please check the given ID parameter, it must be a number!";
            } catch (SQLException ex) {
                // SQL Error:
                    result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
            } finally {
                handler.CloseConnection();
            }
        } else {
            // Failed to get a connection!
                result = "ERROR: Could not get connection from database.";
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nID=..., HASH=...";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(e.EncryptString(result));
    out.flush();
%>