package group32.android.cookbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import group32.android.cookbook.EditPostActivity;
import group32.android.cookbook.HomeActivity;
import group32.android.cookbook.PostDetailsActivity;
import group32.android.cookbook.R;
import group32.android.cookbook.models.Post;

/**
 * Created by Hai Bui on 16-Nov-17.
 */

public class CustomListItemRecyclerAdapter extends RecyclerView.Adapter<CustomListItemRecyclerAdapter.PostHolder>
{
    private List<Post> postData;
    private Context context;
    public static final String EXTRA_POST_KEY = "post_uid";
    public StorageReference imageRef;
    //Khởi tạo constructor để gọi từ HomeActivity
    public CustomListItemRecyclerAdapter(Context context, List<Post> postData)
    {
        this.context = context;
        this.postData = postData;
    }

    //Xét View cho từng item của recyclerview
    @Override
    public CustomListItemRecyclerAdapter.PostHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new CustomListItemRecyclerAdapter.PostHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_posts, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomListItemRecyclerAdapter.PostHolder holder, final int position)
    {
        Post post = postData.get(position);
        imageRef = FirebaseStorage.getInstance().getReference().child("images/" + postData.get(position).getImage());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.imageView);

        holder.txtTitle.setText(post.getTitle());
        holder.txtAuthor.setText(post.getAuthor());
        holder.txtRecipe.setText(post.getRecipe());
        holder.txtStar.setText(String.valueOf(((Post)postData.get(position)).getStar()));
        holder.txtTotalVotes.setText(String.valueOf(post.getTotalVotes()));
        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,PostDetailsActivity.class);//Thay đổi activity chỗ này
                //i.putExtra("KEY","UID of post");
                //sử dụng dòng này khi có UID Post
                String post_uid = postData.get(position).getUid();
                if(post_uid != null){
                    i.putExtra(EXTRA_POST_KEY, post_uid);
                    context.startActivity(i);
                }
                else
                {
                    Toast.makeText(context, "Something wrong! This post maybe deleted by author", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    //Tạo class exten từ ViewHolder , khai báo các biến trong item_for_list_posts.yml
    public class PostHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtAuthor, txtRecipe,txtTotalVotes,txtStar;
        ImageView imageView;
        View itemview;
        public PostHolder(View view){
            super(view);
            txtTitle =  view.findViewById(R.id.txtTitle);
            txtAuthor =  view.findViewById(R.id.txtAuthor);
            txtRecipe =  view.findViewById(R.id.txtRecipe);
            imageView = view.findViewById(R.id.iv_item_image);
            txtTotalVotes =  view.findViewById(R.id.txtTotalVotes);
            itemview = view;
            txtStar =  view.findViewById(R.id.txtStar);
        }
    }

    //Hàm có sẵn
    public int getItemCount()
    {
        return postData.size();
    }


}