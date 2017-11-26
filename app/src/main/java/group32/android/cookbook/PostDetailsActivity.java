package group32.android.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
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
import group32.android.cookbook.models.User;
//import com.bumptech.glide.request.RequestOptions.Error;


public class PostDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    //Data variable
    public static final String EXTRA_POST_KEY = "post_uid";

    private DatabaseReference database;
    private DatabaseReference itemDatabse;
    private DatabaseReference ratingDatabase;
    private StorageReference imageRef;
    private StorageReference childImageRef;
    private ValueEventListener mPostListener;
    private DatabaseReference userRef;
    private DatabaseReference dbRef;
    private ItemDetail item = new ItemDetail();
    private ArrayList<Comment> arrComments = new ArrayList<Comment>();
    private CustomCommentArrayAdapter adapter;
    private User _user;

    private ImageView ivItemImage;
    private TextView tvItemContent, tvItemTitle;
    private EditText editComment;
    private ListView lvComments;
    private Button btnComment;
    private RatingBar ratingBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //get current user
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = getUid();
        dbRef = FirebaseDatabase.getInstance().getReference();
        userRef = dbRef.child("users").child(UserID);
        Log.d("POSE DETAILS","user id " + UserID + userRef);

        /*ValueEventListener UserInformation = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object once
                _user = dataSnapshot.getValue(User.class);
                Log.d("POST DETAILS","USER: "+ dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("User debug", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userRef.addListenerForSingleValueEvent(UserInformation);*/
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object once
                _user = dataSnapshot.getValue(User.class);
                Log.d("POST DETAILS","USER: "+ dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("User debug", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Initialize Views
        ivItemImage = findViewById(R.id.iv_item_image);
        tvItemContent = findViewById(R.id.tv_item_content);
        tvItemTitle = findViewById(R.id.tv_item_title);
        editComment = findViewById(R.id.edit_comment);
        lvComments = findViewById(R.id.lv_comments);
        ratingBarView = findViewById(R.id.rating_bar);
        btnComment = findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);

        //Retrive uid from put extra
        String newPostUid;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newPostUid = null;
                Intent intent = new Intent(PostDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                newPostUid = extras.getString(EXTRA_POST_KEY);
            }
        } else {
            newPostUid = (String) savedInstanceState.getSerializable(EXTRA_POST_KEY);
        }

        // Initialize database
        database = FirebaseDatabase.getInstance().getReference();
        assert newPostUid != null;
        itemDatabse = database.child("posts").child(newPostUid);
        imageRef = FirebaseStorage.getInstance().getReference();

        //Data initialize
        adapter = new CustomCommentArrayAdapter(PostDetailsActivity.this, R.layout.activity_item_custom_comment, arrComments);
        lvComments.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get a post data
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ItemDetail object and use the values to update the UI
                item = dataSnapshot.getValue(ItemDetail.class);

                //Get comment array
                for (DataSnapshot commentSnapshot : dataSnapshot.child("post-comment").getChildren()) {
                    Comment newComment = commentSnapshot.getValue(Comment.class);
                    arrComments.add(newComment);
                }
                // Assign data into view
                tvItemTitle.setText(item.getDataTitle());
                tvItemContent.setText(item.getDataContent());
                ratingBarView.setRating(item.getRatingNumber());
                childImageRef = imageRef.child(item.getDataImage());

                //Data processing
                Glide.with(PostDetailsActivity.this)
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
    protected void onStop() {
        super.onStop();
        float currentStar = item.getRatingNumber();
        float star;
        star = ratingBarView.getRating();

        //If not rated
        if (currentStar >= 0) {
            item.setRatingNumber((ratingBarView.getRating() + currentStar) / 2);
        } else {
            item.setRatingNumber(ratingBarView.getNumStars());
        }

        ratingDatabase = itemDatabse.child("ratingNumber");
        ratingDatabase.setValue(item.getRatingNumber());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back to previous activity
        finish();
    }

    @Override
    public void onClick(View view) {

        if(_user.getDisplayName() != null){
            Comment newComment = new Comment();
            newComment.setAuthor(_user.getDisplayName());
            newComment.setMessage(editComment.getText().toString());
            arrComments.add(newComment);
            editComment.getText().clear();
            itemDatabse.child("post-comment").push().setValue(newComment);
        }
        else {
            Toast.makeText(getApplicationContext(),"Username is null",Toast.LENGTH_SHORT).show();
        }
        //Log.d("POST DETAILS","USER: "+ _user.getDisplayName() + " " + _user.getEmail());
        //User user = new User("Alan", "alannguyen@gmail.com");
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(PostDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
