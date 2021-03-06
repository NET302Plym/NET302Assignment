package NET302JavaLibrary;
import com.google.gson.Gson;

public class User {
    //************************************************************************//
    //  -   VARIABLES AND CONSTRUCTORS                                    -   //
    //************************************************************************//
    private final int ID;
    private String  username;
    private String  password;
    private String  contact;
    private String  name;
    private boolean authenticated;
    private GenericLookup staffType;

    /**
     * Constructor for creating a new User object.
     * @param ID int - being the ID.
     * @param username String - being the Username.
     * @param password String - being the Password (leave blank in almost all cases)
     * @param contact String - being the Contact (email).
     * @param name String - being the Name.
     * @param authenticated boolean - being if the User has authenticated or not.
     * @param staff_type GenericLookup - being the ID/Value for this status.
     */
    public User(int ID, String username, String password, String contact, String name, boolean authenticated, GenericLookup staff_type) {
        this.ID             = ID;
        this.username       = username;
        this.password       = password;
        this.contact        = contact;
        this.name           = name;
        this.authenticated  = authenticated; 
        this.staffType      = staff_type;
    }
    
    /**
     * Simple constructor for use when not all details are necessary .
     * @param ID int - being the ID.
     * @param username String - being the Username.
     * @param name String - being the Name.
     */
    public User(int ID, String username, String name)
    {
        this.ID = ID;
        this.username = username;
        this.name = name;
    }
    
    //************************************************************************//
    //  -   GETTERS + SETTERS                                             -   //
    //************************************************************************//

    /**
     * Gets the ID of the User object.
     * @return int - being the ID of this User.
     */
    public int getID() {
        return ID;
    }

    /**
     * Gets the username of the User object.
     * @return String - being the username of this User.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the User object.
     * @param username String - being the desired username of this User.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the User object.
     * @return String - being the password of this User.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the User object.
     * @param password String - being the desired password of this User.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the contact of the User object.
     * @return String - being the contact of this User.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact of the User object.
     * @param contact String - being the desired contact of this User.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the name of the User object.
     * @return String - being the name of this User.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the User object.
     * @param name String - being the desired name of this User.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the authenticated value of the User object.
     * @return boolean - being whether or not this User is authenticated.
     */
    public boolean isAuthenticated() {    
        return authenticated;
    }

    /**
     * Sets the authenticated value of the User object.
     * @param authenticated boolean - being whether or not this User is authenticated.
     */
    public void setAuthenticated(boolean authenticated) {    
        this.authenticated = authenticated;
    }

    /**
     * Gets the staffType of the User object.
     * @return GenericLookup - representing the Staff_type.
     */
    public GenericLookup getStaffType() {
        return staffType;
    }

    /**
     * Sets the staffType of the User object.
     * @param staffType GenericLookup - representing the desired Staff_type.
     */
    public void setStaffType(GenericLookup staffType) {
        this.staffType = staffType;
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
        return "User{" + "ID=" + ID + ", username=" + username 
                + ", password=" + password + ", contact=" + contact 
                + ", name=" + name + ", authenticated=" + authenticated 
                // Objects below use their toString method:
                + ", staff_type=" + staffType.toString() + '}';
    }
    
    //************************************************************************//
    //  -   GSON/JSON HELPER METHODS                                      -   //
    //************************************************************************//
    
    /**
     * Creates the object using a JSON string.
     * @param jsonString String - being the JSON string representing this object.
     */
    public User(String jsonString){
        Gson gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        this.ID             = user.getID();
        this.username       = user.getUsername();
        this.password       = user.getPassword();
        this.contact        = user.getContact();
        this.name           = user.getName();
        this.authenticated  = user.isAuthenticated();
        this.staffType     = user.getStaffType();
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
