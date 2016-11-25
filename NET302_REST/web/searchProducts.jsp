<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.Product"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%@page import="java.lang.reflect.Type"%>
<%  // This page will take TERM as a parameter.
    // where TERM = the search term to find Products for.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Fetch and store the ID parameter:
    String  paraTerm  = request.getParameter("TERM");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // If the ID parameter exists, then return a single Produt:
    if (paraTerm != null) {
        paraTerm = paraTerm.trim();
        
        if (handler.GetConnection()) {
            try {
                ArrayList<Product> list = handler.getProducts(paraTerm);
                
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
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nTERM=...";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
<br>