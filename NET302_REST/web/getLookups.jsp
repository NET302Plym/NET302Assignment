<%@page import="com.google.gson.Gson"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="NET302JavaLibrary.GenericLookup"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.User"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%  
    // This page will take an ID parameter to query the database for that ID
    // of the User table.
    
    // Used to send back the data, presume initial failure due to unknown error.
    String  result = "ERROR: Please contact system administrator.";
    
    // Fetch and store the identifier / ID parameters:
    String  paraIden    = request.getParameter("IDENTIFIER");
    String  paraID      = request.getParameter("ID");
    
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
                
                    // TODO: unsure if correct, guidance say this is how but
                    // it doesn't use the toGson method in the generic lookup.
                    Gson gson = new Gson();
                    Type token = new TypeToken<ArrayList<GenericLookup>>() {}.getType();
                    result = gson.toJson(list, token); 
                
                } else {
                    result = "ERROR: Identifier not as expected. Please use:"
                            + "\n'category' 'subcategory' 'container' 'location' 'status'";
                }
            } else {
                // TODO: ID is given, need to convert it and try and fetch only that ID.
            }
        }
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
<br>