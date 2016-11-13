<%@page import="NET302JavaLibrary.Order"%>
<%@page import="java.sql.SQLException"%>
<%@page import="NET302_Handlers.DB_Handler"%>
<%
    // This page will take an existing Order object as a parameter,
    // it will then send that off to the database.
    // A second parameter of TRUE or FALSE must also exist, where:
    //  TRUE    = Add New
    //  FALSE   = Update

    // Used to send back the data, presume initial failure due to unknown error.
    String  result = "ERROR: Please contact system administrator.";
    
    // Get the parameters:
    String  orderP  = request.getParameter("ORDER");
    String  newP    = request.getParameter("NEW");
    
    // Create database connection:
    DB_Handler handler = new DB_Handler();
    
    // Check for both parameters existing:
    if (orderP.length() > 1 & newP.length() > 1) {
        // Presume success unless errored:
        // TO-DO: A bit vague, may need to adjust the result.
        result = "SUCCESS";
        
        // Create Order from String which should be of JSON format:
        Order tempOrder = new Order(orderP);
        
        if (result.equals("SUCCESS")) {
            if (handler.GetConnection()) {
                if (newP.equals("TRUE")) {
                    try {
                        handler.newOrder(tempOrder);
                    } catch (SQLException ex) {
                        System.out.println("SQL Error: " + ex.getMessage() + "\n");
                        result = "ERROR: Could not prepare or action statement."
                                + " Please ensure the data submitted is of"
                                + " correct format and check the error log.";
                    } finally {
                        handler.CloseConnection();
                    }
                }
                else {
                    try {
                        handler.updateOrder(tempOrder);
                    } catch (SQLException ex) {
                        System.out.println("SQL Error: " + ex.getMessage() + "\n");
                        result = "ERROR: Could not prepare or action statement."
                                + " Please ensure the data submitted is of"
                                + " correct format and check the error log.";
                    } finally {
                        handler.CloseConnection();
                    }
                }
            } else {
                // Failed to get a connection!
                result = "ERROR: Could not get connection from database.";
            }
        }
    }
    // Print out the result & flush:
    handler.CloseConnection();
    out.print(result);
    out.flush();
%>
    