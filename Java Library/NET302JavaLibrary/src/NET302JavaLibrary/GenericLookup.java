/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NET302JavaLibrary;

import com.google.gson.Gson;

// TODO: JavaDoc comments throughout.

/**
 *
 * @author Sam
 */
public class GenericLookup {
    //************************************************************************//
    //  -   VARIABLES AND CONSTRUCTORS                                    -   //
    //************************************************************************//
    private final int ID;
    private String value;

    /**
     * Constructor for the GenericLookup.
     * @param id int - being the ID.
     * @param value String - being the data.
     */
    public GenericLookup(int id, String value) {
        this.ID = id;
        this.value = value;
    }
    
    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    /**
     * Gets the ID.
     * @return int - being the ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the string value.
     * @return String - being the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the string value.
     * @param value String - being the value.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    //************************************************************************//
    //  -   toString                                                      -   //
    //************************************************************************//

    /**
     * toString override to provide object information.
     * @return String - being the contents of the object.
     */
    @Override
    public String toString() {
        return "GenericLookup{" + "ID=" + ID + ", value=" + value + '}';
    }
    
    //************************************************************************//
    //  -   GSON/JSON HELPER METHODS                                      -   //
    //************************************************************************//
    
    /**
     * Creates the object using a JSON string.
     * @param jsonString String - being the JSON string representing this object.
     */
    public GenericLookup(String jsonString){
        Gson gson = new Gson();
        GenericLookup lookup = gson.fromJson(jsonString, GenericLookup.class);
        this.ID     = lookup.ID;
        this.value  = lookup.value;
    }
    
    /**
     * Uses GSON to get the JSON string representing this object.
     * @return String - being the JSON string.
     */
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
