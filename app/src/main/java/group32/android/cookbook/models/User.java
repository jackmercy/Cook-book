package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 28-Oct-17.
 */
@IgnoreExtraProperties
public class User {
    public String displayName;
    public String email;
    //public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
        //this.password = password;
    }

}
