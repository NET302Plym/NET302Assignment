<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.User"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  
    // This page will take an ID parameter to query the database for that ID
    // of the User table.
    
    // Used to send back the data, presume initial failure due to unknown error.
    String  result = "ERROR: Please contact system administrator.";
    
    // Fetch and store the ID parameter:
    String  paraID  = request.getParameter("ID");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // If the ID parameter exists, then return a single Object
    if (paraID != null) {
        paraID = paraID.toUpperCase().trim();

            if (handler.GetConnection()) {
                try {
                    int id = Integer.parseInt(paraID);
                    result = handler.getUser(id).GetJSONString();
                } catch (NumberFormatException | SQLException ex) {
                    result = "ERROR: " + ex.getMessage();
                } finally { handler.CloseConnection(); }
            } else {
                // Failed to get a connection!
                result = "ERROR: Could not get connection from database."
                        + "\n Please consult adminstrator.";
            }
    } 
    // NO PARAMETER SPECIFIED - Return all Products
    else {
        if (handler.GetConnection()) {
            ArrayList<User> list = handler.getAllUsers();
            
            // TODO : GSON arrayList.
            
            handler.CloseConnection();
        } else {
            // Failed to get a connection!
            result = "ERROR: Could not get connection from database."
                    + "\n Please consult adminstrator.";
        }
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
<br>