package com.example.myapplication.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.LoginRegister.Login;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewRentCar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;
    TextView carTitle,carPrice,fuelCity,fuelHighway,carBrand,carModel,carBodyType,EngineCapacity,carMileage,modelYear,Transmission,Description;
    Button rentButton;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rent_car);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.singleRentCarViewImage);
        carTitle = findViewById(R.id.singleRentCarTitle);
        carPrice = findViewById(R.id.singleRentCarPrice);
        fuelCity = findViewById(R.id.cityFuelViewRentCar);
        fuelHighway = findViewById(R.id.highwayFuelViewRentCar);
        carBrand = findViewById(R.id.textViewRentCarBrand);
        carModel = findViewById(R.id.textViewRentCarModel);
        carBodyType = findViewById(R.id.textViewRentCarBodyType);
        EngineCapacity = findViewById(R.id.textViewRentCarEngineCapacity);
        carMileage = findViewById(R.id.textViewRentCarMileage);
        modelYear = findViewById(R.id.textViewRentCarModelYear);
        Transmission = findViewById(R.id.textViewRentCarTransmission);
        Description = findViewById(R.id.textViewRentCarDescription);
        rentButton = findViewById(R.id.rentCarButton);
        ref = FirebaseDatabase.getInstance().getReference().child("Add Rent Cars");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.ProgressBarRentCar);

        final String CarKey = getIntent().getStringExtra("CarKey");
        ref.child(CarKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String rentCarFuelCity = dataSnapshot.child("FuelCity").getValue().toString();
                    String rentCarFuelHighway = dataSnapshot.child("FuelHighway").getValue().toString();
                    String rentCarImage = dataSnapshot.child("ImageURL").getValue().toString();
                    String rentCarBodyType = dataSnapshot.child("RentCarBodyType").getValue().toString();
                    String rentCarBrand = dataSnapshot.child("RentCarBrand").getValue().toString();
                    String rentCarDescription = dataSnapshot.child("RentCarDescription").getValue().toString();
                    String rentCarEngineCapacity = dataSnapshot.child("RentCarEngineCapacity").getValue().toString();
                    String rentCarMileage = dataSnapshot.child("RentCarMileage").getValue().toString();
                    String rentCarModel = dataSnapshot.child("RentCarModel").getValue().toString();
                    String rentCarPrice = dataSnapshot.child("RentCarPrice").getValue().toString();
                    String rentCarTitle = dataSnapshot.child("RentCarTopic").getValue().toString();
                    String rentCarTransmission = dataSnapshot.child("RentCarTransmission").getValue().toString();
                    String rentCarModelYear = dataSnapshot.child("RentCarYear").getValue().toString();

                    Picasso.get().load(rentCarImage).into(imageView);
                    carTitle.setText(rentCarTitle);
                    carPrice.setText("Rs." + rentCarPrice + "Per KM");
                    fuelCity.setText(rentCarFuelCity);
                    fuelHighway.setText(rentCarFuelHighway);
                    carBrand.setText("Brand Name : " + rentCarBrand);
                    carModel.setText("Model : " + rentCarModel);
                    carBodyType.setText("Body Type : " + rentCarBodyType);
                    EngineCapacity.setText("Engine Capacity : " + rentCarEngineCapacity);
                    carMileage.setText("Mileage : " + rentCarMileage);
                    modelYear.setText("Model Year : " + rentCarModelYear);
                    Transmission.setText("Transmission Type : " + rentCarTransmission);
                    Description.setText(rentCarDescription);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //toolbar

        setSupportActionBar(toolbar);


        //navigation drawer menu

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ref.child(CarKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            String RentCarBrand = dataSnapshot.child("RentCarBrand").getValue().toString();
                            String RentCarDescription = dataSnapshot.child("RentCarDescription").getValue().toString();
                            String RentModel = dataSnapshot.child("RentCarModel").getValue().toString();
                            String RentalPrice = dataSnapshot.child("RentCarPrice").getValue().toString();
                            String RentTitle = dataSnapshot.child("RentCarTopic").getValue().toString();
                            String RentImage = dataSnapshot.child("ImageURL").getValue().toString();


                            String uid = mAuth.getCurrentUser().getUid();
                            ref = FirebaseDatabase.getInstance().getReference().child("Buy Details").child(uid).child(CarKey);

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("Topic",RentTitle);
                            hashMap.put("Brand",RentCarBrand);
                            hashMap.put("Model",RentModel);
                            hashMap.put("Description",RentCarDescription);
                            hashMap.put("Price",RentalPrice);
                            hashMap.put("ImageURL",RentImage);

                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ViewRentCar.this,"You Purchased Rent Car Successfully",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        progressBar.setVisibility(View.GONE);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(ViewRentCar.this,"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(ViewRentCar.this,Dashboard.class));
                break;

            case R.id.nav_profile:
                String uid = mAuth.getCurrentUser().getUid();
                Intent intent = new Intent(ViewRentCar.this, UserProfile.class);
                intent.putExtra("UserID",uid);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(this,"Successfully Logged Out!",Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
