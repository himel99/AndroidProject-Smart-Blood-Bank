package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Loginbutton;
    private TextView Info,SignUp;
    private int counter = 5,flagg = 0;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.etLoginName);
        Password = (EditText) findViewById(R.id.etpassword);
        Loginbutton = (Button) findViewById(R.id.loginButton);
        Info = (TextView) findViewById(R.id.tvInfo);
        SignUp = (TextView) findViewById(R.id.tvSignUp);

        Info.setText("No of available attempts: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));

        }


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });
    }

    private void validate(final String userName, final String userPassword)
    {
        progressDialog.setMessage("Logging In under process");
        progressDialog.show();


        table_user.addValueEventListener(new ValueEventListener(){
            @Override

            public void onDataChange(DataSnapshot dataSnapshot){

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("Himel",""+ childDataSnapshot.getKey()); //displays the key for the node
                    Log.v("Himel",""+ childDataSnapshot.child("userName").getValue());   //gives the value for given keyname

                    if(childDataSnapshot.child("userName").getValue().equals(userName)) {
                        Toast.makeText(MainActivity.this, "Name Matched !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        String uName = childDataSnapshot.child("userName").getValue().toString();
                        String uPassword = childDataSnapshot.child("userPassword").getValue().toString();
                        String uPhone = childDataSnapshot.child("userPhone").getValue().toString();
                        String uBlood = childDataSnapshot.child("userBloodGroup").getValue().toString();
                        String uAddress = childDataSnapshot.child("userAddress").getValue().toString();
                        Log.v("Himel","Name"+ uName);
                        Log.v("Himel","Pass"+ uPassword);
                        Log.v("Himel","Phone"+ uPhone);
                        Log.v("Himel","Blood "+ uBlood);
                        Log.v("Himel","Address "+ uAddress);


                        UserProfile user = new UserProfile(uName,uPassword,uPhone,uBlood,uAddress);
                        if (user.getUserPassword().equals(userPassword)) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Log in successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,SecondActivity.class));
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                            counter--;
                            Info.setText("No of available attempts: "+counter);
                            if(counter==0)
                                Loginbutton.setEnabled(false);
                        }
                        flagg = 1;


                        break;

                    }
                }

                Log.w("Himel", "UserName: "+ userName );


                if(flagg==0)
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"user do not exist in database",Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No of available attempts: "+counter);
                    if(counter==0)
                        Loginbutton.setEnabled(false);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }


        });












/*
        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful())
            {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Log In Successfull !",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Log In Failed!",Toast.LENGTH_SHORT).show();
                counter--;
                Info.setText("No of available attempts: 5");
                if(counter==0)
                    Loginbutton.setEnabled(false);
            }
        }
    });

 */
    }
}
