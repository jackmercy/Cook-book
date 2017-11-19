package group32.android.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import group32.android.cookbook.models.Post;


public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomListItemRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<Post> listData = new ArrayList();
    //public ProgressBar progressBar;
    private DatabaseReference mDatabase;
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
        adapter = new CustomListItemRecyclerAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String Uid = user.getUid();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        /*progressBar =  findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }*/
        //Retrieving basic data (1 key)
        /*mDatabase.child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user information

                Post newPost = dataSnapshot.getValue(Post.class);
                listData.add(new Post(newPost));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
*/
        listData.add(new Post("Author : HB 1", "Test 1", "Sample recipes"));
        listData.add(new Post("Author : HB 2", "Test 2", "Sample recipes"));
        listData.add(new Post("Author : HB 3", "Test 3", "Sample recipes"));
        listData.add(new Post("Author : HB 4", "Test 4", "Sample recipes"));
        listData.add(new Post("Author : HB 5", "Test 1", "Sample recipes"));
        listData.add(new Post("Author : HB 6", "Test 2", "Sample recipes"));
        listData.add(new Post("Author : HB 7", "Test 3", "Sample recipes"));
        listData.add(new Post("Author : HB 8", "Test 4", "Sample recipes"));
        adapter.notifyDataSetChanged();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.cookbook_menu, menu);
        return true;
    }   */

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
                    case R.id.options_menu_edit_btn:
                        startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                        finish();
                        //Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.id_set:
                        signOut();
                        Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
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

}