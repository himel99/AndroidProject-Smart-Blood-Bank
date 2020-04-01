package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDataActivity extends AppCompatActivity {

    ListView listView;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    String userId;
    String number;
    private static int REQEST_CALL = 1;
    androidx.appcompat.widget.SearchView searchView;
    ArrayList<UserProfile> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_data);

        setContentView(R.layout.activity_firebase_data);

        listView = (ListView) findViewById(R.id.listViewId);

        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchViewd);

        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //  user = firebaseAuth.getCurrentUser();


        // public void readDataFromFirebase()
        {
            //  Log.w("Himel", "User Id: " + userId );
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //   UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);


                    Toast.makeText(FirebaseDataActivity.this, "Enter to fireebase data! ", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uName = ds.child("userName").getValue().toString();
                        String uPassword = ds.child("userPassword").getValue().toString();
                        String uPhone = ds.child("userPhone").getValue().toString();
                        String uBlood = ds.child("userBloodGroup").getValue().toString();
                        String uAddress = ds.child("userAddress").getValue().toString();
                        UserProfile userProfile = new UserProfile(uName, uPassword, uPhone, uBlood, uAddress);
                        // userProfile.setUserName(ds.child(userId).getValue(UserProfile.class).getUserName());   //set the name
                        // userProfile.setUserPassword(ds.child(userId).getValue(UserProfile.class).getUserPassword());   //set the password
                        // userProfile.setUserPhone(ds.child(userId).getValue(UserProfile.class).getUserPhone());   //set the phone

/*
                        arrayList.add(userProfile.getUserName());
                        arrayList.add(userProfile.getUserPhone());
                        arrayList.add(userProfile.getUserPassword());
                        arrayList.add(userProfile.getUserBloodGroup());
                        arrayList.add(userProfile.getUserAddress());

 */

                        arrayList.add(userProfile);

                        if (searchView != null) {
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    ArrayList<UserProfile> mylist = new ArrayList<>();

                                    for (UserProfile ob : arrayList) {
                                        if (ob.getUserBloodGroup().toLowerCase().contains(newText.toLowerCase())) {

                                            mylist.add(ob);

                                        }

                                    }
                                    PersonalListAdapter adapter = new PersonalListAdapter(FirebaseDataActivity.this, R.layout.adapter_view_layout, mylist);
                                    listView.setAdapter(adapter);
                                    return true;
                                }
                            });
                        }


                        Log.w("Himel", "UserName: " + userProfile.getUserName());
                        Log.w("Himel", "UserPass: " + userProfile.getUserPassword());
                        Log.w("Himel", "UserPhone: " + userProfile.getUserPhone());
                        Log.w("Himel", "UserBlood: " + userProfile.getUserBloodGroup());
                        Log.w("Himel", "UserAddress: " + userProfile.getUserAddress());
                    }
                    //ArrayAdapter adapter = new ArrayAdapter<UserProfile>(FirebaseDataActivity.this,R.layout.support_simple_spinner_dropdown_item,arrayList);
                    PersonalListAdapter adapter = new PersonalListAdapter(FirebaseDataActivity.this, R.layout.adapter_view_layout, arrayList);
                    listView.setAdapter(adapter);


                    // Log.w("Himel", "UserName: " + userProfile.getUserName() );
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Himel", "Failed to read value.", error.toException());
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserProfile userProfile = arrayList.get(position);

                    number = userProfile.getUserPhone();


                    AlertDialog.Builder b1 = new AlertDialog.Builder(FirebaseDataActivity.this);
                    b1.setTitle("Phonecall");
                    b1.setMessage("Do you want to call for donor?");
                    b1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ContextCompat.checkSelfPermission(FirebaseDataActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(FirebaseDataActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQEST_CALL);

                            } else {

                                String dial = "tel:" + number;
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                            }
                        }
                    });
                    b1.setNegativeButton("no", null);
                    b1.create();
                    b1.show();

                }
            });


        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



