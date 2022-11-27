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
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class OrganigationRegistration extends AppCompatActivity {
    private TextView backButton;


    private CircleImageView profile_image;
    private TextInputEditText registrationUserName;
    private TextInputEditText registrationFullName;
    private TextInputEditText registrationPhoneNumber;
    private TextInputEditText registrationEmail;
    private TextInputEditText registrationPassword;


    private Button registerButton;

    private Uri resultUri;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organigation_registration);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganigationRegistration.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        profile_image = findViewById(R.id.profile_image);
        registrationUserName = findViewById(R.id.registrationUserName);
        registrationFullName = findViewById(R.id.registrationFullName);
        registrationPhoneNumber = findViewById(R.id.registrationPhoneNumber);
        registrationEmail = findViewById(R.id.registrationEmail);
        registrationPassword = findViewById(R.id.registrationPassword);
        registerButton = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registrationEmail.getText().toString().trim();
                final String password = registrationPassword.getText().toString().trim();
                final String fullName = registrationFullName.getText().toString().trim();
                final String userName = registrationUserName.getText().toString().trim();
                final String phoneNumber = registrationPhoneNumber.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    registrationEmail.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registrationPassword.setError("Password is required!");
                    return;
                }

                if(TextUtils.isEmpty(fullName)){
                    registrationFullName.setError("Full Name is required!");
                    return;
                }

                if(TextUtils.isEmpty(userName)){
                    registrationUserName.setError("User Name is required!");
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    registrationPhoneNumber.setError("Phone Number is required!");
                    return;
                }

                else {
                    loader.setMessage("Registering you....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(OrganigationRegistration.this, "Error" + error, Toast.LENGTH_SHORT).show();

                            }

                            else {
                                String currentUserName = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserName);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id",currentUserName);
                                userInfo.put("name",fullName);
                                userInfo.put("email",email);
                                userInfo.put("username",userName);
                                userInfo.put("phonenumber",phoneNumber);
                                userInfo.put("type","organigation");
                                userInfo.put("search","organigation"+userName);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(OrganigationRegistration.this, "Data set successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(OrganigationRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();

                                    }
                                });

                                if (resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile image").child(currentUserName);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(OrganigationRegistration.this, "Image Upload", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl",imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(OrganigationRegistration.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(OrganigationRegistration.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                        finish();

                                                    }
                                                });

                                            }

                                        }
                                    });
                                    Intent intent = new Intent(OrganigationRegistration.this,MainActivity2.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data !=null ){
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }
}