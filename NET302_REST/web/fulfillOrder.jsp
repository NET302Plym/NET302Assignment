<%@page import="NET302JavaLibrary.Order"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take ORDER.
    // where ORDER = the ID of the ORDER to set as fulfilled.

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
            try {
                handler.fulfillOrder(Integer.parseInt(orderP));
            } catch (SQLException ex) {
                // SQL Error.
                result = "ERROR: Please check DB_Handler for following error:"
                    + "\n" + ex.getMessage();
            } catch (NumberFormatException ex) {
                // Integer conversion error:
                result = "ERROR: Please ensure the ORDER parameter is an integer!"
                    + "\n" + ex.getMessage();
            } finally { handler.CloseConnection(); }
        } else {
            // Failed to get a connection!
            result = "ERROR: Could not get connection from database.";
        }        
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nORDER=N";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    