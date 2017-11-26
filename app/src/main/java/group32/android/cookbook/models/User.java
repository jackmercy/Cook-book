package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 28-Oct-17.
 */
@IgnoreExtraProperties
public class User {
    private String displayName;
    private String email;
    //public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
        //this.password = password;
    }


    public String getDisplayName(){ return  this.displayName; }
    public String getEmail() { return  this.email; }

    public void setDisplayName(String username) { this.displayName = username; }
    public void setEmail(String email) { this.email = email; }
}
