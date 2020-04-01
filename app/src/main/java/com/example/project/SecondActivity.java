package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logoutbutton, firebutton, developerbutton;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    String userId;
    private CardView healthCardView,bmiCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user = firebaseAuth.getCurrentUser();
      //  userId = user.getUid();

        healthCardView = (CardView) findViewById(R.id.healths_tips_CardView_Id);
        bmiCardView = (CardView) findViewById(R.id.bmi_CardView_Id);



        healthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataFromFirebase();
                Intent intent = new Intent(SecondActivity.this,FirebaseDataActivity.class);
                startActivity(intent);
            }
        });
        bmiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this,JsonActivity.class);
                startActivity(intent);
            }
        });



/*
        developerbutton = (Button) findViewById(R.id.developerButtonId);

        developerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,JsonActivity.class));
            }
        });

 */


/*

        logoutbutton = (Button) findViewById(R.id.logoutButtonId);

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();

            }
        });

        firebutton = (Button) findViewById(R.id.getValueId);

        firebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDataFromFirebase();
                startActivity(new Intent(SecondActivity.this,FirebaseDataActivity.class));
            }
        });

 */

    }

    private  void  LogOut()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this,MainActivity.class));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logoutMenu:
            {
                LogOut();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void readDataFromFirebase()
    {
        Log.w("Himel", "User Id: " + userId );
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
             //   UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                showData(dataSnapshot);

                Toast.makeText(SecondActivity.this,"Enter to fireebase data! ",Toast.LENGTH_SHORT ).show();

               // Log.w("Himel", "UserName: " + userProfile.getUserName() );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Himel", "Failed to read value.", error.toException());
            }
        });


    }
    public void showData(DataSnapshot dataSnapshot)
    {
        for (DataSnapshot ds: dataSnapshot.getChildren())
        {
            String uName = ds.child("userName").getValue().toString();
            String uPassword = ds.child("userPassword").getValue().toString();
            String uPhone = ds.child("userPhone").getValue().toString();
            String uBlood = ds.child("userBloodGroup").getValue().toString();
            String uAddress = ds.child("userAddress").getValue().toString();
            UserProfile userProfile = new UserProfile(uName,uPassword,uPhone,uBlood,uAddress);
           // userProfile.setUserName(ds.child(userId).getValue(UserProfile.class).getUserName());   //set the name
           // userProfile.setUserPassword(ds.child(userId).getValue(UserProfile.class).getUserPassword());   //set the password
           // userProfile.setUserPhone(ds.child(userId).getValue(UserProfile.class).getUserPhone());   //set the phone


            Log.w("Himel", "UserName: " + userProfile.getUserName() );
            Log.w("Himel", "UserPass: " + userProfile.getUserPassword() );
            Log.w("Himel", "UserPhone: " + userProfile.getUserPhone() );
            Log.w("Himel", "UserBlood: " + userProfile.getUserBloodGroup() );
            Log.w("Himel", "UserAddress: " + userProfile.getUserAddress() );
        }




    }

}



