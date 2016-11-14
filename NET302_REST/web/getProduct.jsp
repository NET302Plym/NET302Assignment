<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.Product"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%@page import="java.lang.reflect.Type"%>
<%  // This page will take ID as an optional parameter.
    // where ID = the ID of the Product to return.
    // If no parameter, all Products are returned.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Fetch and store the ID parameter:
    String  paraID  = request.getParameter("ID");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // If the ID parameter exists, then return a single Produt:
    if (paraID != null) {
        paraID = paraID.toUpperCase().trim();

            if (handler.GetConnection()) {
                try {
                    int id = Integer.parseInt(paraID);
                    result = handler.getProduct(id).GetJSONString();
                } catch (NumberFormatException | SQLException ex) {
                    // SQL Error.
                        result = "ERROR: Please check DB_Handler for following error:"
                            + "\n" + ex.getMessage();
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
            try {
                ArrayList<Product> list = handler.getAllProducts();
                
                // TODO: should work but a little uncertain!
                Gson gson = new Gson();
                Type token = new TypeToken<ArrayList<Product>>() {}.getType();
                result = gson.toJson(list, token); 
                
            } catch (SQLException ex) {
                // SQL Error.
                result = "ERROR: Please check DB_Handler for following error:"
                        + "\n" + ex.getMessage();
            } finally { handler.CloseConnection(); }
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