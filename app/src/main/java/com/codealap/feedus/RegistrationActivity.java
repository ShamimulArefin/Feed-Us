package com.codealap.feedus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {
    private Button individualButton;
    private Button organisationButton;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        individualButton = findViewById(R.id.individualButton);
        organisationButton = findViewById(R.id.organisationButton);
        backButton = findViewById(R.id.backButton);


        organisationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,OrganigationRegistration.class);
                startActivity(intent);
            }
        });

        individualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,IndividualRegistration.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}