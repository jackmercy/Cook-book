package group32.android.cookbook.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by linhv on 11/15/2017.
 */

public class ViewHolder {
    private TextView tvAuthor;
    private TextView tvMessage;

    public void setTvAuthor(TextView tvAuthor){
        this.tvAuthor = tvAuthor;
    }

    public void setTvMessage(TextView tvMessage){
        this.tvMessage = tvMessage;
    }

    public TextView getTvAuthor(){
        return tvAuthor;
    }

    public TextView getTvMessage(){
        return tvMessage;
    }
}
