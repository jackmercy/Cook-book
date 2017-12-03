package group32.android.cookbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group32.android.cookbook.R;
import group32.android.cookbook.models.Comment;


/**
 * Created by linhv on 11/14/2017.
 */



public class CustomCommentArrayAdapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context context;
    private int layoutID;

    public CustomCommentArrayAdapter(Context context, int layoutID, ArrayList<Comment> comments){
        super(context, layoutID, comments);
        this.context = context;
        this.layoutID = layoutID;
        this.comments = comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutID, parent, false);

        ViewHolder holder = new ViewHolder();
        Comment comment = getItem(position);

        holder.setTvAuthor((TextView) convertView.findViewById(R.id.tv_comment_author));
        holder.setTvMessage((TextView) convertView.findViewById(R.id.tv_comment_message));

        holder.getTvAuthor().setText(comment.getAuthor());
        holder.getTvMessage().setText(comment.getMessage());

        return convertView;
    }
}
