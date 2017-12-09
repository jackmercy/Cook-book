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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class NewPostActivity extends AppCompatActivity {
    //View
    private Button btnUploadImage, btnDone;
    private EditText titleEdtText, recipeEdtText;
    private ImageView uploadedImgView;

    //Variable
    private User _user;
    private String image;
    private Uri selectedImage;
    private static final int SELECT_PHOTO = 100;
    //Firebase instance
    private DatabaseReference postsReference, userRef, userPostRef;
    private DatabaseReference root_db;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        //View
        btnDone = findViewById(R.id.btn_done);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        titleEdtText = findViewById(R.id.edt_title);
        recipeEdtText = findViewById(R.id.edt_recipe);
        uploadedImgView = findViewById(R.id.iv_uploaded_photo);


        //Firebase Reference
        root_db = FirebaseDatabase.getInstance().getReference();
        postsReference = root_db.child("posts");
        userRef = root_db.child("users").child(getUid());
        userPostRef = root_db.child("users").child(getUid()).child("user-posts");
        //get user information
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                _user = dataSnapshot.getValue(User.class);
                Log.d("POST DETAILS","USER: "+ dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("New Post debug", "loadPost:onCancelled", databaseError.toException());
            }
        });

        //handle button event
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), SELECT_PHOTO);
            }

        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoneProcess();
            }
        });
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
        String title = titleEdtText.getText().toString();
        String recipe = recipeEdtText.getText().toString();
        String image;

        // titleEdtText is required
        if (TextUtils.isEmpty(title)) {
            titleEdtText.setError("Required");
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(recipe)) {
            recipeEdtText.setError("Required");
            return;
        }

        // Image is required
        if (selectedImage == null) {
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            image = handleUploadImage();
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        //initial new post
        final String key = postsReference.push().getKey();
        final Post _newPost = new Post(_user.getDisplayName(), title, recipe, image);
        Log.w("NEW Post", "Created a post instance " + _newPost.getAuthor() + _newPost.getTitle() + _newPost.getRecipe() +_newPost.getUid() + _newPost.getStar());
        //put new post to firebase

        postsReference.child(key).setValue(_newPost);
        //postsReference.push().setValue(_newPost);
        Log.w("NEW Post", "set value to post ref");
        postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Post post = dataSnapshot.getValue(Post.class);
                userPostRef.child(key).setValue(_newPost);
                Log.w("NEW Post", "Jump into user-posts");
                setEditingEnabled(true);
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
        titleEdtText.setEnabled(enabled);
        recipeEdtText.setEnabled(enabled);
        if (enabled) {
            btnDone.setVisibility(View.VISIBLE);
        } else {
            btnDone.setVisibility(View.GONE);
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
            Toast.makeText(NewPostActivity.this, "Image: " + path, Toast.LENGTH_LONG).show();
            uploadedImgView.setImageURI(selectedImage);
        }
    }

}
