package group32.android.cookbook;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import group32.android.cookbook.adapter.CustomCommentArrayAdapter;
import group32.android.cookbook.models.Comment;
import group32.android.cookbook.models.ItemDetail;
import group32.android.cookbook.models.Post;
import group32.android.cookbook.models.User;
//import com.bumptech.glide.request.RequestOptions.Error;


public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    //Data variable
    //public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference database;
    private DatabaseReference itemDatabse;
    private DatabaseReference ratingDatabase;
    private StorageReference imageRef;
    private StorageReference childImageRef;
    private ValueEventListener mPostListener;

    private ItemDetail item = new ItemDetail();
    private ArrayList<Comment> arrComments = new ArrayList<Comment>();
    private CustomCommentArrayAdapter adapter;

    private ImageView ivItemImage;
    private TextView tvItemContent, tvItemTitle;
    private EditText editComment;
    private ListView lvComments;
    private Button btnComment;
    private RatingBar ratingBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialize database
        database = FirebaseDatabase.getInstance().getReference();
        itemDatabse = database.child("itemdetail").child("01");
        imageRef = FirebaseStorage.getInstance().getReference();

        // Initialize Views
        ivItemImage = (ImageView) findViewById(R.id.iv_item_image);
        tvItemContent = (TextView) findViewById(R.id.tv_item_content);
        tvItemTitle = (TextView) findViewById(R.id.tv_item_title);
        editComment = (EditText) findViewById(R.id.edit_comment);
        lvComments = (ListView) findViewById(R.id.lv_comments);
        ratingBarView = (RatingBar) findViewById(R.id.rating_bar);
        btnComment = (Button) findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);

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
            newString= (String)savedInstanceState.getSerializable("KEY");
        }*/

        //Data initialize
        adapter = new CustomCommentArrayAdapter(ItemDetailActivity.this, R.layout.activity_item_custom_comment, arrComments);
        lvComments.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Get a post data
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ItemDetail object and use the values to update the UI
                item = dataSnapshot.getValue(ItemDetail.class);

                //Get comment array
                for (DataSnapshot commentSnapshot: dataSnapshot.child("post-comment").getChildren()){
                    Comment newComment = commentSnapshot.getValue(Comment.class);
                    arrComments.add(newComment);
                }
                // Assign data into view
                tvItemTitle.setText(item.getDataTitle());
                tvItemContent.setText(item.getDataContent());
                ratingBarView.setRating(item.getRatingNumber());
                childImageRef = imageRef.child(item.getDataImage());

                //Data processing
                Glide.with(ItemDetailActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(childImageRef)
                        .into(ivItemImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
            }
        };
        itemDatabse.addValueEventListener(itemListener);

    }

    @Override
    protected void onStop(){
        super.onStop();
        float currentStar = item.getRatingNumber();
        float star;
        star = ratingBarView.getRating();

        //If not rated
        if (currentStar >= 0){
        item.setRatingNumber((ratingBarView.getRating() + currentStar)/2);
        }
        else {
        item.setRatingNumber(ratingBarView.getNumStars());
        }

        ratingDatabase = itemDatabse.child("ratingNumber");
        ratingDatabase.setValue(item.getRatingNumber());

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //Back to previous activity
        finish();
    }

    @Override
    public void onClick (View view){
        User user = new User("Alan", "alannguyen@gmail.com");
        Comment newComment = new Comment();
        newComment.setAuthor(user.displayName);
        newComment.setMessage(editComment.getText().toString());
        arrComments.add(newComment);
        editComment.getText().clear();
        itemDatabse.child("post-comment").push().setValue(newComment);
    }
}
