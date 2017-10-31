package group32.android.cookbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group32.android.cookbook.models.User;

public class SampleActivity extends AppCompatActivity {

    private Button myBtn;
    private TextView tvName;
    private TextView tvEmail;
    private DatabaseReference mDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //instance
        myBtn = (Button) findViewById(R.id.sampleBtn);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String Uid = user.getUid();

        //event handle for button
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //How to add new user to DB
                User user = new User("Jack","khoail@gmail.com");
                // 1- Create child to root obj
                // 2- Assign some value(obj) to that child
                mDatabase.child("users").child(Uid).push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Child added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Retrieving basic data (1 key)
                /*mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information

                        User newUser = dataSnapshot.getValue(User.class);
                        String name =  newUser.displayName;
                        String email = newUser.email;
                        // put it to text view
                        tvName.setText(name);
                        tvEmail.setText(email);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });*/


                //Retrieving complex data
                mDatabase.child("comments").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // Get user information

                        User newUser = dataSnapshot.getValue(User.class);
                        String name =  newUser.displayName;
                        String email = newUser.email;


                        // put it to text view
                        tvName.setText(name);
                        tvEmail.setText(email);
                        Toast.makeText(getApplicationContext(), "Received!!!"+name+" "+email, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
