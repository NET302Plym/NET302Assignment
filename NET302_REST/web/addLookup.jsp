<%@page import="java.sql.SQLException"%>
<%@page import="NET302JavaLibrary.GenericLookup"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take IDENTIFIER, LOOKUP, NEW as parameters,
    // where IDENTIFIER = the lookup table name,
    // where LOOKUP = the GSON object of the item sent,
    // where NEW = is TRUE or FALSE and establishes whether to add or update.

    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Get the parameters:
    String  identP  = request.getParameter("IDENTIFIER");
    String  lookupP = request.getParameter("LOOKUP");
    String  newP    = request.getParameter("NEW");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for all of the parameters existing:
    if (lookupP.length() > 1 & newP.length() > 1 & identP.length() > 1) {
        // Presume success unless errored:
        result = "SUCCESS: Query passed to handler method without error.";
        
        // Create Lookup from String which should be of JSON format:
        GenericLookup lookup = new GenericLookup(lookupP);
        
        if (result.equals("SUCCESS")) {
            if (handler.GetConnection()) {
                try {
                    // Depending on value of newP, either update or create:
                    if (newP.equals("TRUE")) {handler.newLookup(identP, lookup);}
                    else {handler.updateLookup(identP, lookup);}
                } catch (SQLException ex) {
                    // SQL Error:
                    result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
                } catch (Exception ex) {
                    // When this is thrown, we know there is a problem with the identifer:
                    result = "ERROR: identifier not recognised, please check and retry.";
                } finally {
                    handler.CloseConnection();
                }
            } else {
                // Failed to get a connection!
                result = "ERROR: Could not get connection from database.";
            }
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nIDENTIFIER=..., LOOKUP={GSON}, NEW=TRUE/FALSE.";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    