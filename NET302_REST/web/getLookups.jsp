<%@page import="com.google.gson.Gson"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="NET302JavaLibrary.GenericLookup"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.User"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  // This page will take IDENTIFIER and ID as parameters,
    // where IDENTIFIER = the lookup table name,
    // where ID = the ID of the lookup in that table. [UNSUPPORTED]
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    
    // Fetch and store the identifier / ID parameters:
    String  paraIden    = request.getParameter("IDENTIFIER");
    //String  paraID      = request.getParameter("ID");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // If the ID parameter exists, then return a single Object
    if (paraIden != null) {
        paraIden = paraIden.toLowerCase().trim();
        if (handler.GetConnection()) {
            if (paraID != null) {
                ArrayList<GenericLookup> list;
            
                if (paraIden.equals("category")
                    || paraIden.equals("subcategory")
                    || paraIden.equals("container")
                    || paraIden.equals("location")
                    || paraIden.equals("status")) {
                
                    list = handler.getAllLookups(paraIden);
                
                    // TODO: should work but a little uncertain!
                    Gson gson = new Gson();
                    Type token = new TypeToken<ArrayList<GenericLookup>>() {}.getType();
                    result = gson.toJson(list, token); 
                
                } else {
                    result = "ERROR: Identifier not as expected. Please use:"
                            + "\n'category' 'subcategory' 'container' 'location' 'status'";
                }
            } else {
                // TODO: ID is given. Fetch that particular one.
                // Currently not supported in handler and unlikely to be.
                // There is no forseeable need for this method.
            }
        }
    } else {
        result = "ERROR: Parameters not correct. Please give:"
                + "\nIDENTIFIER=...";
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
<br>