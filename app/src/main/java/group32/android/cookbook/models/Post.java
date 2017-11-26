package group32.android.cookbook.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by khoic on 30-Oct-17.
 */
@IgnoreExtraProperties
public class Post {
    private String uid;
    private String author;
    private String title;
    //public Uri image;
    private String recipe;
    private double starCounter; // 1 user vote X star=> StarCount += X;
    private int totalVotes; // 1 user vote => totalVotes++;
    private double star; // avg star of post

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String author, String title, String recipe) {
        this.uid = null;
        this.author = author;
        this.title = title;
        this.recipe = recipe;
        this.starCounter = 0;
        this.totalVotes = 0;
        this.star = 0;
    }

    public Post(Post newPost){
        this.uid = null;
        this.author = newPost.getAuthor();
        this.title = newPost.getTitle();
        this.recipe = newPost.getRecipe();
        this.starCounter = newPost.getStarCounter();
        this.totalVotes = newPost.getTotalVotes();
        this.star = newPost.getStar();
    }

    public void setUid(String UID){ this.uid = UID; }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
    public void setStar(double star) {
        this.star = star;
    }
    public void setStarCounter(int starCounter) {
        this.starCounter = starCounter;
    }
    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getUid() {return this.uid;}
    public String getAuthor() {
        return this.author;
    }
    public String getTitle() {
        return this.title;
    }
    public String getRecipe() {
        return this.recipe;
    }
    public double getStar() {
        return  this.getStarCounter()/this.getTotalVotes();
    }
    public double getStarCounter() {
        return this.starCounter;
    }
    public int getTotalVotes() {
        return this.totalVotes;
    }

}
