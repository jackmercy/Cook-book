package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 30-Oct-17.
 */
@IgnoreExtraProperties
public class Comment {
    public String author;
    public String message;

    public Comment(){
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String author, String message){
        this.author = author;
        this.message = message;
    }
}