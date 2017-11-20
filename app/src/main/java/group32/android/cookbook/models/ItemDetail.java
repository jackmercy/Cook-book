package group32.android.cookbook.models;

/**
 * Created by linhv on 11/19/2017.
 */

public class ItemDetail {
    String dataTitle;
    String dataImage;
    String dataContent;
    int ratingNumber;

    public String getDataTitle(){
        return dataTitle;
    }
    public String getDataImage(){
        return dataImage;
    }
    public String getDataContent(){
        return dataContent;
    }
    public int getRatingNumber(){
        return ratingNumber;
    }
    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public void setRatingNumber(int ratingNumber) {
        this.ratingNumber = ratingNumber;
    }
}
