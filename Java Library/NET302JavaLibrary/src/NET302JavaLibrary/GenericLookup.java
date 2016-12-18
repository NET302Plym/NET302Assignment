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
     * 
     * @param id
     * @param value 
     */
    public GenericLookup(int id, String value) {
        this.ID = id;
        this.value = value;
    }
    
    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    public int getID() {
        return ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    //************************************************************************//
    //  -   toString                                                      -   //
    //************************************************************************//

    @Override
    public String toString() {
        return "GenericLookup{" + "ID=" + ID + ", value=" + value + '}';
    }
    
    //************************************************************************//
    //  -   GSON/JSON HELPER METHODS                                      -   //
    //************************************************************************//
    
    /**
     * 
     * @param jsonString 
     */
    public GenericLookup(String jsonString){
        Gson gson = new Gson();
        GenericLookup lookup = gson.fromJson(jsonString, GenericLookup.class);
        this.ID     = lookup.ID;
        this.value  = lookup.value;
    }
    
    /**
     * 
     * @return 
     */
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
