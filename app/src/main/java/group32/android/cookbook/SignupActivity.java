package group32.android.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import group32.android.cookbook.models.User;

public class SignupActivity extends AppCompatActivity {

    public EditText inputDisplayName, inputEmail, inputPassword;
    public Button btnSignIn, btnSignUp, btnResetPassword;
    public ProgressBar progressBar;
    public FirebaseAuth auth;
    private DatabaseReference mDatabase;
    //private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn =  findViewById(R.id.sign_in_button);
        btnSignUp =  findViewById(R.id.sign_up_button);
        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        inputDisplayName = findViewById(R.id.display_name);
        progressBar =  findViewById(R.id.progressBar);
        btnResetPassword =  findViewById(R.id.btn_reset_password);



        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayName = inputDisplayName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                mDatabase = FirebaseDatabase.getInstance().getReference();
                //final String Uid = user.getUid();

                if (TextUtils.isEmpty(displayName)) {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //addUserToDB(displayName, email, Uid);
                                    User user = new User(displayName,email);
                                    FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = fb_user.getUid();
                                    mDatabase.child("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "You have been successfully registered", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    /*startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();*/
                                }
                            }
                        });


            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}