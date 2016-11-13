<%@page import="java.sql.SQLException"%>
<%@page import="NET302JavaLibrary.GenericLookup"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  
    // This page will take an IDENTIFIER, LOOKUP,
    // A second parameter of TRUE or FALSE must also exist, where:
    //  TRUE    = Add New Lookup
    //  FALSE   = Update Existing Lookup

    // Used to send back the data, presume initial failure due to unknown error.
    String  result = "ERROR: Please contact system administrator.";
    
    // Get the parameters:
    String  identP  = request.getParameter("IDENTIFIER");
    String  lookupP = request.getParameter("LOOKUP");
    String  newP    = request.getParameter("NEW");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for both parameters existing:
    if (lookupP.length() > 1 & newP.length() > 1 & identP.length() > 1) {
        // Presume success unless errored:
        // TO-DO: A bit vague, may need to adjust the result.
        result = "SUCCESS";
        
        // Create Lookup from String which should be of JSON format:
        GenericLookup lookup = new GenericLookup(lookupP);
        
        if (result.equals("SUCCESS")) {
            if (handler.GetConnection()) {
                try {
                    if (newP.equals("TRUE")) {handler.newLookup(identP, lookup);}
                    else {handler.updateLookup(identP, lookup);}
                } catch (SQLException ex) {
                    result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
                } catch (Exception ex) {
                    // When thsi is thrown, we know there is a problem with the identifer:
                    result = "ERROR: identifier not recognised, please check and retry.";
                }
            } else {
                // Failed to get a connection!
                result = "ERROR: Could not get connection from database.";
            }
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nIDENTIFIER=..., LOOKUP={...}, NEW=TRUE/FALSE.";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    