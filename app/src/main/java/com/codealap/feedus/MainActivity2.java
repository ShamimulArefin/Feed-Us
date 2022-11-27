package com.codealap.feedus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button donarButton;
    private Button receiveButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_username,nav_fullname,nav_email,nav_type;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        donarButton = findViewById(R.id.donarButton);
        receiveButton = findViewById(R.id.receiveButton);

        donarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,DonarForm.class);
                startActivity(intent);
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,ReceviedActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feed Us");

        drawerLayout = findViewById(R.id.drawerLayout);

        nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(MainActivity2.this,drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);

        nav_profile_image =nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname =nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_username =nav_view.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_email =nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_type =nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String username=snapshot.child("username").getValue().toString();
                    nav_username.setText(username);

                    String name=snapshot.child("name").getValue().toString();
                    nav_fullname.setText(name);

                    String email=snapshot.child("email").getValue().toString();
                    nav_email.setText(email);

                    String type=snapshot.child("type").getValue().toString();
                    nav_type.setText(type);

                    if(snapshot.hasChild("profilepictureurl")){

                        String image=snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(image).into(nav_profile_image);
                    }
                    else
                    {
                        nav_profile_image.setImageResource(R.drawable.profile);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity2.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}