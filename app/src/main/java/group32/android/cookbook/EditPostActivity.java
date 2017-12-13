package group32.android.cookbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import group32.android.cookbook.models.Post;
import group32.android.cookbook.models.User;

/**
 * Created by Hai Bui on 30-Nov-17.
 */

public class EditPostActivity extends AppCompatActivity {
    //Data variable
    public static final String EXTRA_POST_KEY = "post_uid";

    //View
    private EditText editpost_edt_title;
    private EditText editpost_edt_recipe;
    private Button editpost_btn_done;
    //private Button editpost_btn_change;
    private ImageView editpost_iv_uploaded_photo;
    private Post postDetail = new Post();
    private StorageReference childImageRef,imageRef;;
    private  String newPostUid;

    private Uri selectedImage;
    private static final int SELECT_PHOTO = 100;
    private User _user;

    //Firebase instance
    private DatabaseReference PostsReference, userPostRef,itemDatabse, database, userRef;
    private DatabaseReference root_db;
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    private FirebaseAuth.AuthStateListener authListener;
//    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        //get current user
        root_db = FirebaseDatabase.getInstance().getReference();
        userRef = root_db.child("users").child(getUid());
        PostsReference = root_db.child("posts");
        userPostRef = root_db.child("users").child(getUid()).child("user-posts");

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

        editpost_edt_title = (EditText) findViewById(R.id.editpost_edt_title);
        editpost_edt_recipe = (EditText)findViewById(R.id.editpost_edt_recipe);
        editpost_btn_done = (Button)findViewById(R.id.editpost_btn_done);
        //editpost_btn_change = (Button)findViewById(R.id.editpost_btn_change);
        editpost_iv_uploaded_photo = (ImageView)findViewById(R.id.editpost_iv_uploaded_photo);

        //Retrive uid from put extra
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Log.d("EXTRA_POST_KEY : ","Fail");
                newPostUid = null;
                Intent intent = new Intent(EditPostActivity.this, HomeActivity.class);
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

        //Handle Button
        /*editpost_btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image*//*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), SELECT_PHOTO);
            }
        });*/

        editpost_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDoneProcess();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get a post data
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ItemDetail object and use the values to update the UI
                postDetail = dataSnapshot.getValue(Post.class);

                // Assign data into view
                editpost_edt_title.setText(postDetail.getTitle());
                editpost_edt_recipe.setText(postDetail.getRecipe());
                //Data processing
                childImageRef = imageRef.child("images/" + postDetail.getImage());
                Glide.with(EditPostActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(childImageRef)
                        .into(editpost_iv_uploaded_photo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
            }
        };
        itemDatabse.addValueEventListener(itemListener);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Back to previous activity
//        startActivity(new Intent(PostDetailsActivity.this,HomeActivity.class));
        finish();
    }

    private String handleUploadImage() {
        //TODO: upload image to fire storage then update it to line 90 (handle image goes here handleDoneProcess
        //
        // Image Storage references
        String imageName = "";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef;
        UploadTask uploadTask;

        if (selectedImage != null){
            imageName = selectedImage.getLastPathSegment();
            imageRef = storageRef.child("images/" + imageName);
            uploadTask = imageRef.putFile(selectedImage);
        }
        else {
            return imageName;
        }

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
        return imageName;
    }

    private void handleDoneProcess() {
        String title = editpost_edt_title.getText().toString();
        String recipe = editpost_edt_recipe.getText().toString();
        String image ;

        // Title is required
        if (TextUtils.isEmpty(title)) {
            editpost_edt_title.setError("Required");
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(recipe)) {
            editpost_edt_recipe.setError("Required");
            return;
        }

        // Image is required
        //TH chưa có ảnh
        if (editpost_iv_uploaded_photo.getDrawable() == null) {
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //Nếu có ảnh r xử lí s ...

            //Thay đổi ảnh từ photo
            image = handleUploadImage();
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        //initial new post
        final Post _editPost = new Post(_user.getDisplayName(), title, recipe, image);
        Log.w("Edit Post", "Update a post instance " + _editPost.getAuthor() + _editPost.getTitle() + _editPost.getRecipe() +_editPost.getUid() + _editPost.getStar());

        //put edit post to firebase
        PostsReference.child(newPostUid).child("author").setValue(_editPost.getAuthor());
        PostsReference.child(newPostUid).child("recipe").setValue(_editPost.getRecipe());
        PostsReference.child(newPostUid).child("title").setValue(_editPost.getTitle());
        Log.w("Edit Post", "update value to post ref");
        PostsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPostRef.child(newPostUid).child("author").setValue(_editPost.getAuthor());
                userPostRef.child(newPostUid).child("recipe").setValue(_editPost.getRecipe());
                userPostRef.child(newPostUid).child("title").setValue(_editPost.getTitle());
                Log.w("Edit Post", "Jump into user-posts");
                setEditingEnabled(true);
                Toast.makeText(EditPostActivity.this, "Update post success!!", Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setEditingEnabled(true);
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void setEditingEnabled(boolean enabled) {
        editpost_edt_title.setEnabled(enabled);
        editpost_edt_recipe.setEnabled(enabled);
        if (enabled) {
            editpost_btn_done.setVisibility(View.VISIBLE);
        } else {
            editpost_btn_done.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path;
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            selectedImage = data.getData();
            path = selectedImage.getLastPathSegment();
            Toast.makeText(EditPostActivity.this, "Image: " + path, Toast.LENGTH_LONG).show();
            //Set name of picked image here
        }
    }
}
