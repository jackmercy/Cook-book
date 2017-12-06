package group32.android.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import group32.android.cookbook.LoginFeatures.LoginActivity;
import group32.android.cookbook.adapter.CustomListItemRecyclerAdapter;
import group32.android.cookbook.models.Post;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomListItemRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Post> listData = new ArrayList<>();
    //public ProgressBar progressBar;
    private DatabaseReference mPostsReference;
    private DatabaseReference root_db;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //activity instance
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize Database
        root_db = FirebaseDatabase.getInstance().getReference();
        mPostsReference = root_db.child("posts");

        //get current user
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is logged in or not
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        listData = new ArrayList<>();
        adapter = new CustomListItemRecyclerAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        //activity instance
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize Database
        root_db = FirebaseDatabase.getInstance().getReference();
        mPostsReference = root_db.child("posts");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        listData = new ArrayList<>();
        //activity instance
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize Database
        root_db = FirebaseDatabase.getInstance().getReference();
        mPostsReference = root_db.child("posts");
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        //Retrieving basic data (1 key)
        // Add value event listener to the post
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get post information

                Log.d("Home Activity", "Post details: ");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post newPost = postSnapshot.getValue(Post.class);
                    assert newPost != null;
                    newPost.setUid(postSnapshot.getKey());
                    Log.d("UID POST", String.format("Uid is %s", newPost));
                    listData.add(newPost);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
            }
        };
        mPostsReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void showPopup(View v){
        final PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.cookbook_menu, popup.getMenu());
        ImageButton optionsMenu = findViewById(R.id.options_menu);

        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_post_btn:
                        //navigate to new post activity
                        startActivity(new Intent(HomeActivity.this, NewPostActivity.class));
                        return true;
                    case R.id.options_menu_edit_btn:
                        startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                        //Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.id_set:
                        signOut();
                        Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
