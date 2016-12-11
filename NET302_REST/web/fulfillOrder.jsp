<%@page import="NET302JavaLibrary.Order"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take ORDER and a NEW as parameters,
    // where ORDER = the GSON object to add/update,
    // where NEW = is TRUE or FALSE and establishes whether to add or update.

    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Get the parameters:
    String  orderP  = request.getParameter("ORDER");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for both parameters existing:
    if (orderP.length() > 0) {
        // Presume success unless errored:
        result = "SUCCESS: Query passed to handler method without error.";
        
        if (handler.GetConnection()) {
            {
                handler.fulfillOrder(Integer.parseInt(orderP));
            }
        } else {
            // Failed to get a connection!
            result = "ERROR: Could not get connection from database.";
        }        
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nORDER={GSON}, NEW=TRUE/FALSE.";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    