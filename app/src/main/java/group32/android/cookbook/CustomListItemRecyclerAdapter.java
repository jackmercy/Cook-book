package group32.android.cookbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import group32.android.cookbook.models.Post;

/**
 * Created by Hai Bui on 16-Nov-17.
 */

public class CustomListItemRecyclerAdapter extends RecyclerView.Adapter<CustomListItemRecyclerAdapter.PostHolder>
{
    private List<Post> postData;
    private Context context;

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
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomListItemRecyclerAdapter.PostHolder holder, final int position)
    {
        holder.txtTitle.setText(((Post)postData.get(position)).getTitle());
        holder.txtAuthor.setText(((Post)postData.get(position)).getAuthor());
        holder.txtRecipe.setText(((Post)postData.get(position)).getRecipe());
        holder.txtStar.setText(String.valueOf(((Post)postData.get(position)).getStar()));
//        holder.txtStarCounter.setText(String.valueOf(((Post)postData.get(position)).getStarCounter()));
        holder.txtTotalVotes.setText(String.valueOf(((Post)postData.get(position)).getTotalVotes()));
        holder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,EditProfileActivity.class);//Thay đổi activity chỗ này
                i.putExtra("KEY","UID of post");
                //sử dụng dòng này khi có UID Post
//                i.putExtra("KEY",String.valueOf((Post)postData.get(position).getUID()));
                context.startActivity(i);
            }
        });
    }

    //Tạo class exten từ ViewHolder , khai báo các biến trong item_for_list_item.yml
    public class PostHolder extends RecyclerView.ViewHolder{
        TextView txtTitle,txtAuthor,txtRecipe,txtStar,txtStarCounter,txtTotalVotes;
        View itemview;
        public PostHolder(View view){
            super(view);
            txtTitle =  view.findViewById(R.id.txtTitle);
            txtAuthor =  view.findViewById(R.id.txtAuthor);
            txtRecipe =  view.findViewById(R.id.txtRecipe);
            txtStar =  view.findViewById(R.id.txtStar);
//            txtStarCounter =  view.findViewById(R.id.txtStarCounter);
            txtTotalVotes =  view.findViewById(R.id.txtTotalVotes);
            itemview = view;
        }
    }

    //Hàm có sẵn
    public int getItemCount()
    {
        return postData.size();
    }
}