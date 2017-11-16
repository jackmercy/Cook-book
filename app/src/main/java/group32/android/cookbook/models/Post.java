package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 30-Oct-17.
 */
@IgnoreExtraProperties
public class Post {
    public String author;
    public String title;
    //public File image;
    public String recipe;
    public int StarCounter; // 1 user vote X star=> StarCount += X;
    public double totalVotes; // 1 user vote => totalVotes++;
    public double star; // avg star of post

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String author, String title, String recipe) {
        this.author = author;
        this.title = title;
        this.recipe = recipe;
        this.StarCounter = 0;
        this.totalVotes = 0;
        this.star = 0;
    }

}
