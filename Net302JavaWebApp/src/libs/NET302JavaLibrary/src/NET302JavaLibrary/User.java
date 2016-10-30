package NET302JavaLibrary;
import com.google.gson.Gson;

public class User {
    // Local Variables
    public int ID;
    public String username;
    public String password;
    public String custContact;
    public Boolean authenticated;
    
    // Constructors
    /**
     * Constuctor for the login screens
     * @param username
     * @param password 
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    /**
     * Constructor for the middleware, this requires all variables (except password as no longer needed)
     * @param ID
     * @param username
     * @param custContact
     * @param authenticated 
     */
    public User(int ID, String username, String custContact, Boolean authenticated)
    {
        this.ID = ID;
        this.username = username;
        this.custContact = custContact;
        this.authenticated = authenticated; 
    }
    
    // Set / Gets
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    // ToString Override
    @Override
    public String toString() {
        return "User{" + "ID=" + ID + ", username=" + username + ", password=" + password + ", custContact=" + custContact + ", authenticated=" + authenticated + '}';
    }
        
    // JSON Conversion
    public User(String jsonString){
        Gson gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        this.username = user.username;
        this.password = user.password;
        this.authenticated = user.authenticated;
    }
    
    public String GetJSONString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }    
}
