package group32.android.cookbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;

import group32.android.cookbook.adapter.CustomCommentArrayAdapter;
import group32.android.cookbook.models.Comment;
//import com.bumptech.glide.request.RequestOptions.Error;


public class ItemDetailActivity extends AppCompatActivity {

    //Data variable
    String dataTitle;
    String dataImage;
    String dataContent;
    int ratingNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //Declare view variables
        ImageView ivItemImage;
        TextView tvItemContent, tvItemTitle;
        EditText editComment;
        ListView lvComments;
        final RatingBar ratingBar;

        //Get view
        ivItemImage = (ImageView) findViewById(R.id.iv_item_image);
        tvItemContent = (TextView) findViewById(R.id.tv_item_content);
        tvItemTitle = (TextView) findViewById(R.id.tv_item_title);
        editComment = (EditText) findViewById(R.id.edit_comment);
        lvComments = (ListView) findViewById(R.id.lv_comments);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        ArrayList<Comment> arrComments = new ArrayList<Comment>();
        CustomCommentArrayAdapter adapter;

        //Retrive uid from put extra
/*        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("KEY");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("KEY");
        }*/

        //Array mock data
        arrComments.add(new Comment("Alan", "This is a good recipe."));
        arrComments.add(new Comment("Alan", "This is a good recipe."));
        arrComments.add(new Comment("Alan", "This is a good recipe."));
        arrComments.add(new Comment("Alan", "This is a good recipe."));
        arrComments.add(new Comment("Alan", "Thank you, good one"));

        //Data initialize
        adapter = new CustomCommentArrayAdapter(ItemDetailActivity.this, R.layout.activity_item_custom_comment, arrComments);
        //adapter = new ArrayAdapter<Comment>(this, android.R.layout.simple_list_item_1, arrComments);
        lvComments.setAdapter(adapter);


        //Mock data
        dataTitle = "Banh Que Hoa";
        dataContent = "– 1 thìa Quế hoa phơi khô\n" +
                "\n" +
                "– 115gr bột củ năng hoặc củ sắn\n" +
                "\n" +
                "– 120gr đường (nếu muốn có màu nâu đen thì mua loại đường thẻ nâu hoặc dùng mật quế hoa)\n" +
                "\n" +
                "– 1/2 lá gelatin (chưng cách thủy cho tan chảy)";
        ratingNumber = 5;

        //Data processing
        Glide.with(this)
                .load("https://blog.beemart.vn/wp-content/uploads/2016/07/hoc-cach-lam-banh-hoa-que-don-gian-ma-hap-dan-vo-cung-1.jpg")
                .into(ivItemImage);
        tvItemTitle.setText(dataTitle);
        tvItemContent.setText(dataContent);
        ratingBar.setRating(ratingNumber);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ratingNumber = ratingBar.getNumStars();
                return false;
            }
        });


        adapter.notifyDataSetChanged();
    }
}
