package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userPhone, verificationCode,userAddress;
    private  Spinner userBloodgroup;
    private Button Regbutton,VerificationButton;
    private TextView userLogin;
    FirebaseAuth mauth,firebaseAuth;
    String CodeSent;
    String Name, Password, Phone, BloodGroup, Address;
    ListView listView;

    ArrayList<String> arrayList;
    //String blood_item[] = {"A+", "A-", "B+", "B-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
/*
        arrayList.add("A+");
        arrayList.add("A-");
        arrayList.add("B+");
        arrayList.add("B-");
        arrayList.add("O+");
        arrayList.add("O-");

 */


        setupUiViews();

        mauth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
       // listView.setAdapter(arrayAdapter);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        VerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    sendVerificationCode();

                }

            }
        });

        Regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Upload data to firebase
                verifySignInCode();

            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }


    private  void setupUiViews()
    {
        userName = (EditText) findViewById(R.id.etUserName);
        userPassword = (EditText) findViewById(R.id.etUserPassword);
        userPhone = (EditText) findViewById(R.id.etUserPhone);
        userBloodgroup = (Spinner) findViewById(R.id.spinner);
        userAddress = (EditText) findViewById(R.id.etUserAddress);
        verificationCode = (EditText) findViewById(R.id.etVerificationCode);
        Regbutton = (Button) findViewById(R.id.signupbutton);
        VerificationButton = (Button) findViewById(R.id.verificationCodeSentButton);
        userLogin = (TextView) findViewById(R.id.tvbacktologin);
    }

    private Boolean validate()
    {
        Boolean result = false;

         Name = userName.getText().toString();
         Password= userPassword.getText().toString();
         Phone = userPhone.getText().toString();
         BloodGroup = userBloodgroup.getSelectedItem().toString();
         Address = userAddress.getText().toString();
        if(Name.isEmpty() || Password.isEmpty() || Phone.isEmpty() || BloodGroup.isEmpty() || Address.isEmpty())
        {
            Toast.makeText(this,"Please Enter All Details! ",Toast.LENGTH_SHORT ).show();
        }
        else
        {
            result = true;
        }
        return result;
    }

    private void  sendVerificationCode()
        {
            String PhoneNumber = userPhone.getText().toString();

            if(PhoneNumber.length() <14)
            {
                userPhone.setError("Please Enter a valid Phone Number!");
                userPhone.requestFocus();
                return;
            }
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    PhoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks


        }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(getApplicationContext(),"Verification Completed",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this,SecondActivity.class));

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),"Code Failed",Toast.LENGTH_SHORT).show();
                Log.e("Verification", e.getMessage());

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"Code Sent",Toast.LENGTH_SHORT).show();
                CodeSent = s;
            }
        };


    private  void  verifySignInCode()
    {
        String code = verificationCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserData();       // Storing User Information into Firebase
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            Toast.makeText(getApplicationContext(),"Sign Up Successfull! ",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this,SecondActivity.class));

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"Incorrect Code! ",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });
    }

    private void sendUserData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(Name, Password, Phone, BloodGroup, Address);
        myRef.setValue(userProfile);
    }
}

