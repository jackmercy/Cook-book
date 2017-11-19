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

    public Post(Post newPost){
        this.author = newPost.author;
        this.title = newPost.title;
        this.recipe = newPost.recipe;
        this.StarCounter = newPost.StarCounter;
        this.totalVotes = newPost.totalVotes;
        this.star = newPost.StarCounter/newPost.totalVotes;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getStarCounter() {
        return StarCounter;
    }

    public void setStarCounter(int starCounter) {
        StarCounter = starCounter;
    }

    public double getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(double totalVotes) {
        this.totalVotes = totalVotes;
    }
}
