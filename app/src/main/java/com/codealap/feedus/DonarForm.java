package com.codealap.feedus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DonarForm extends AppCompatActivity {

    private TextInputEditText donarName;
    private TextInputEditText donarPhoneNumber;
    private TextInputEditText donarAddress;
    private TextInputEditText donationDate;
    private TextInputEditText totalPerson;
    private TextInputEditText donaremail;

    private Spinner city;
    private Spinner donationTime;

    private Button submitButton;

    private Uri resultUri;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_form);


        donarName = findViewById(R.id.donarName);
        donarPhoneNumber = findViewById(R.id.donarPhoneNumber);
        donarAddress = findViewById(R.id.donarAddress);
        donationDate = findViewById(R.id.donationDate);
        donationTime = findViewById(R.id.donationTime);
        totalPerson = findViewById(R.id.totalPerson);
        city = findViewById(R.id.city);
        submitButton = findViewById(R.id.submitButton);
        donaremail = findViewById(R.id.donaremail);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = donarName.getText().toString().trim();
                final String email = donaremail.getText().toString().trim();
                final String phonenumber = donarPhoneNumber.getText().toString().trim();
                final String address = donarAddress.getText().toString().trim();
                final String date = donationDate.getText().toString().trim();
                final String time = donationTime.getSelectedItem().toString();
                final String person = totalPerson.getText().toString().trim();
                final String area = city.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    donarName.setError("Name is required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    donarName.setError("Name is required!");
                    return;
                }
                if(TextUtils.isEmpty(phonenumber)){
                    donarPhoneNumber.setError("phone number is required!");
                    return;
                }

                if(TextUtils.isEmpty(address)){
                    donarAddress.setError("address  is required!");
                    return;
                }

                if(TextUtils.isEmpty(date)){
                    donationDate.setError("Date is required!");
                    return;
                }

                if(TextUtils.isEmpty(person)){
                    totalPerson.setError("person is required!");
                    return;
                }
                if(time.equals("Select time")){
                    Toast.makeText(DonarForm.this, "Select time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(area.equals("Select your City")){
                Toast.makeText(DonarForm.this, "Select your City", Toast.LENGTH_SHORT).show();
                return;
                }

                else {
                    loader.setMessage("Registering you....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,phonenumber).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(DonarForm.this, "Error" + error, Toast.LENGTH_SHORT).show();

                            }

                            else {
                                String currentUserName = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("donars").child(currentUserName);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id",currentUserName);
                                userInfo.put("name",name);
                                userInfo.put("email",email);
                                userInfo.put("address",address);
                                userInfo.put("city",area);
                                userInfo.put("time",time);
                                userInfo.put("date",date);
                                userInfo.put("person",person);
                                userInfo.put("phonenumber",phonenumber);
                                userInfo.put("type","donar");
                                userInfo.put("search","doner"+area);


                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(DonarForm.this, "Data set successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(DonarForm.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();

                                    }
                                });
                                Intent intent = new Intent(DonarForm.this,ReceviedActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }

                        }
                    });
                }
            }
        });
    }

}
