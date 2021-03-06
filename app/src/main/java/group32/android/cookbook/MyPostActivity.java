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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import group32.android.cookbook.LoginFeatures.LoginActivity;
import group32.android.cookbook.adapter.CustomListItemForMyPost;
import group32.android.cookbook.models.Post;

/**
 * Created by trung on 12/3/2017.
 */

public class MyPostActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomListItemForMyPost adapter;
    private LinearLayoutManager layoutManager;
    private List<Post> listData = new ArrayList();
    //public ProgressBar progressBar;
    private DatabaseReference mPostsReference;
    private DatabaseReference root_db;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //activity instance
        recyclerView = findViewById(R.id.recyclerViewPost);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomListItemForMyPost(this, listData);
        recyclerView.setAdapter(adapter);
        // Initialize Database
        root_db = FirebaseDatabase.getInstance().getReference();
        mPostsReference = root_db.child("users").child(getUid()).child("user-posts");
//        get current user
        // final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is logged in or not
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MyPostActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        listData = new ArrayList<>();
        adapter = new CustomListItemForMyPost(this, listData);
        recyclerView.setAdapter(adapter);
        //Initialize Database
        root_db = FirebaseDatabase.getInstance().getReference();
        mPostsReference = root_db.child("users").child(getUid()).child("user-posts");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot postSnapshot, String s) {
                Post newPost = postSnapshot.getValue(Post.class);
                assert newPost != null;
                newPost.setUid(postSnapshot.getKey());
                Log.d("UID POST", String.format("Uid is %s", newPost.getUid()+ " Pre Key="+s));
                listData.add(newPost);
                //listTemp = listData;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot postSnapshot, String s) {
                Post newPost = postSnapshot.getValue(Post.class);
                assert newPost != null;
                newPost.setUid(postSnapshot.getKey());
                Log.d("UID POST", String.format("Uid is %s", newPost.getUid()+ " Pre Key="+s));
                int index = getIndexListData(listData, newPost.getUid());
                /*if (listData.size() == 0 && listTemp.size() != 0 || listData.size() > listTemp.size())
                    listData = listTemp;*/
                if(listData.size() > 0) {
                    listData.set(index, newPost);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot postSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot postSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Home activity", "Post: onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load posts.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPostsReference.addChildEventListener(childEventListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
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
                    case R.id.home_btn:
                        finish();
                        return true;
                    case R.id.new_post_btn:
                        //navigate to new post activity
                        startActivity(new Intent(MyPostActivity.this, NewPostActivity.class));
                        return true;
                    case R.id.options_menu_edit_btn:
                        startActivity(new Intent(MyPostActivity.this, EditProfileActivity.class));
                        //Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.id_set:
                        signOut();
                        Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    case R.id.my_post:
                        startActivity(new Intent(MyPostActivity.this, MyPostActivity
                                .class));
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

    private int getIndexListData(List<Post> listData, String UID) {
        for (int i = 0; i < listData.size(); i++) {
            Post post = listData.get(i);
            if( UID.equals(post.getUid()) ) {
                return i;
            }
        }
        return 0;
    }
}
