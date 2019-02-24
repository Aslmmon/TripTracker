package com.example.android.plannertracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    TextView creatAccount, forgetPassword;
    Button LogInButton;
    SignInButton GLogin;

    String email, password;
    ProgressDialog dialog;
    GoogleSignInClient gsi;
    FirebaseUser gmailUser;
    DatabaseReference databaseReference;
    ArrayList<User> users;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;

    public static final String userEmail = "";

    public static final String TAG = "LOGIN";


    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
users=new ArrayList<>();


        LogInButton = (Button) findViewById(R.id.loginbtn);
        GLogin = (SignInButton) findViewById(R.id.sign_in_button);
        creatAccount = findViewById(R.id.creatAccount);
        forgetPassword = findViewById(R.id.forgetPassword);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

//remember me

        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            Email.setText(loginPreferences.getString("username", ""));
            Password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "AuthStateChanged:Logout");
                }

            }
        };

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                if (isOnline()) {
                    Toast.makeText(LoginActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
                    userSign();
                } else {
                    Toast.makeText(LoginActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                }


            }


        });


        GLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick:         ana on click ya naaaaaas");
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsi = GoogleSignIn.getClient(this, gso);


        creatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.getInstance().sendPasswordResetEmail(Email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                }
                            }
                        });


            }
        });
    }


    public void doSomethingElse() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }


    private void signIn() {
        if (isOnline()) {
            Toast.makeText(LoginActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            Intent signInIntent = gsi.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
            Log.i(TAG, "signIn:       hala wallah  ");
        } else {
            Toast.makeText(LoginActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.i(TAG, "onActivityResult: " + account + "noooooooooooooooooooooooooo");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("google sighn in", "Google sign in failed", e);
                // ...
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    public void onBackPressed() {
        LoginActivity.super.finish();
    }


    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {

            Email.setError("email wrong");
            Email.requestFocus();
            return;


        } else if (TextUtils.isEmpty(password)) {
            Password.setError("Enter the correct password");
            Password.requestFocus();
            return;

        }

        dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();

                    Toast.makeText(LoginActivity.this, "Login not successfull", Toast.LENGTH_SHORT).show();

                } else {
                    //dialog.dismiss();
                    mUser = mAuth.getCurrentUser();
                    String id = mUser.getUid();
                    email = Email.getText().toString().trim();
                    password = Password.getText().toString().trim();

                    Toast.makeText(LoginActivity.this, mUser.getUid(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Login  successfull", Toast.LENGTH_SHORT).show();
                   // Intent intent = new Intent(LoginActivity.this, NewPlan.class);

                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                   // imm.hideSoftInputFromWindow(Email.getWindowToken(), 0);



                    //checkAnewUser( id, email,password);
                   /* String Name = intent.getStringExtra("SEND_TEXT1");

                    intent.putExtra("SEND_TEXT", id);
                    intent.putExtra("SEND_TEXTN", Name);
                    intent.putExtra("SEND_TEXT1", email);*/

                    if (saveLoginCheckBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", email);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }

                    doSomethingElse();

                  //  startActivity(intent);

                    //  checkIfEmailVerified();

                }
            }
        });

    }

    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        } else {
            Email.getText().clear();

            Password.getText().clear();
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            // Sending Email to Dashboard Activity using intent.
          //  intent.putExtra(userEmail, email);

           // startActivity(intent);

        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete:     ana geeeeeeeeeeeet");
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                              startActivity(intent);*/

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

    //check connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



}