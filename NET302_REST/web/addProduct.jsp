<%@page import="NET302JavaLibrary.Product"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take PRODUCT and a NEW as parameters,
    // where PRODUCT = the GSON object to add/update,
    // where NEW = is TRUE or FALSE and establishes whether to add or update.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Get the parameters:
    String  orderP  = request.getParameter("PRODUCT");
    String  newP    = request.getParameter("NEW");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for both parameters existing:
    if (orderP.length() > 1 & newP.length() > 1) {
        // Presume success unless errored:
        result = "SUCCESS: Query passed to handler method without error.";
        
        // Create Order from String which should be of JSON format:
        Product tempProduct = new Product(orderP);
        
        if (result.equals("SUCCESS")) {
            if (handler.GetConnection()) {
                if (newP.equals("TRUE")) {
                    try {
                        handler.newProduct(tempProduct);
                    } catch (SQLException ex) {
                        // SQL Error.
                        result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
                    } finally { handler.CloseConnection(); }
                } else {
                    try {
                        handler.newProduct(tempProduct);
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
                + "\nPRODUCT={GSON}, NEW=TRUE/FALSE.";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    