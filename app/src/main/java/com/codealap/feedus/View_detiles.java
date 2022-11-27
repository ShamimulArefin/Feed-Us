package com.codealap.feedus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class View_detiles extends AppCompatActivity {

    private TextView detileName,detilePhoneNumber,detileCity,detileArea,detileDate,detileTime,detilePerson;

    private DatabaseReference userRef;
    private Button detileReceiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detiles);

        detileReceiveButton = findViewById(R.id.detileReceiveButton);

        detileName =findViewById(R.id.detileName);
        detileCity = findViewById(R.id.detileCity);
        detileDate = findViewById(R.id.detileDate);
        detilePerson = findViewById(R.id.detilePerson);
        detilePhoneNumber = findViewById(R.id.detilePhoneNumber);
        detileArea = findViewById(R.id.detileArea);
        detileTime = findViewById(R.id.detileTime);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String city = intent.getStringExtra("city");
        String date = intent.getStringExtra("date");
        String person = intent.getStringExtra("person");
        detileName.setText(name);
        detileCity.setText(city);
        detileDate.setText(date);
        detilePerson.setText(person);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String phonenumber=snapshot.child("phonenumber").getValue().toString();
                    detilePhoneNumber.setText(phonenumber);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        detileReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(View_detiles.this,RecivedConform.class);
                startActivity(intent);
            }
        });

    }
}