package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 30-Oct-17.
 */
@IgnoreExtraProperties
public class Comment {

    private String uid;
    private String author;
    private String message;

    public Comment(){
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String author, String message){
        this.author = author;
        this.message = message;
    }

    public  String getUid() { return  this.uid; }
    public String getAuthor(){
        return this.author;
    }
    public String getMessage() {
        return this.message;
    }

    public void setUid(String UID) { this.uid = UID; }
    public void setAuthor(String displayName){
        this.author = displayName;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
