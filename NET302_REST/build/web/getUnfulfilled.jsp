<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="NET302JavaLibrary.Order"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%@page import="java.lang.reflect.Type"%>
<%  // This page will take no parameters.
    
    // Used to send back the data, presume initial error and inform:
    String  result = "ERROR: No change of result reached. Consult system administrator.";
    Encrypter.SymmetricEncrypter e = new Encrypter.SymmetricEncrypter();
    // Create database connection:
    DB_Handler handler = new DB_Handler();

    if (handler.GetConnection()) {
        try {
            ArrayList<Order> list = handler.getUnfulfilled();

            // TODO: should work but a little uncertain!
            Gson gson = new Gson();
            Type token = new TypeToken<ArrayList<Order>>() {}.getType();
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
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(e.EncryptString(result));
    out.flush();
%>
<br>