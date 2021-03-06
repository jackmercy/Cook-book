package group32.android.cookbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import group32.android.cookbook.EditPostActivity;
import group32.android.cookbook.PostDetailsActivity;
import group32.android.cookbook.R;
import group32.android.cookbook.models.Post;

/**
 * Created by Jack on 13-Dec-17
 */

public class CustomListItemForMyPost extends RecyclerView.Adapter<CustomListItemForMyPost.PostHolder>
{
    private List<Post> postData;
    private Context context;
    public static final String EXTRA_POST_KEY = "post_uid";
    public StorageReference imageRef;
    //Khởi tạo constructor để gọi từ HomeActivity
    public CustomListItemForMyPost(Context context, List<Post> postData)
    {
        this.context = context;
        this.postData = postData;
    }

    //Xét View cho từng item của recyclerview
    @Override
    public CustomListItemForMyPost.PostHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new CustomListItemForMyPost.PostHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_posts, parent, false));
    }

    @Override
    public void onBindViewHolder(final CustomListItemForMyPost.PostHolder holder, final int position)
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
        holder.txtStar.setText(String.valueOf(round(post.getStar(), 1)));
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
        holder.itemview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, EditPostActivity.class);
                String post_uid = postData.get(position).getUid();
                if(post_uid != null){
                    i.putExtra(EXTRA_POST_KEY, post_uid);
                    context.startActivity(i);
                }
                else
                {
                    Toast.makeText(context, "Something wrong! This post maybe deleted by author", Toast.LENGTH_SHORT).show();
                }
                return true;
                //return false;
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

    //Rounding number
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}