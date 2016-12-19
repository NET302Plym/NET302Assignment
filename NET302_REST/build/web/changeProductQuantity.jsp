<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.Order"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%@page import="java.lang.reflect.Type"%>
<%  // This page will take PRODUCT and NEWQUANTITY as parameters.
    // where PRODUCT = the ID of the Product to update.
    // where NEWQUANTITY = the new amount of stock for the Product.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    String  productID  = request.getParameter("PRODUCT");
    String  newQuantity  = request.getParameter("NEWQUANTITY");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();

    if (productID.length() > 0 & newQuantity.length() > 0) {
        if (handler.GetConnection()) {
            try {
                result = "SUCCESS: Query passed to handler method without error.";
                int productIDInt = Integer.parseInt(productID);
                int newQuantityInt = Integer.parseInt(newQuantity);
                handler.updateQuantity(productIDInt, newQuantityInt);
            } catch (SQLException ex) {
                // SQL Error.
                result = "ERROR: Please check DB_Handler for following error:"
                    + "\n" + ex.getMessage();
            } catch (NumberFormatException ex) {
                // Integer conversion error:
                result = "ERROR: Please ensure the NEWQUANTITY parameter is an integer!"
                    + "\n" + ex.getMessage();
            } finally { handler.CloseConnection(); }
        } else {
            // Failed to get a connection!
            result = "ERROR: Could not get connection from database."
                    + "\n Please consult adminstrator.";
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nPRODUCT=N, NEWQUANTITY=M";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
<br>