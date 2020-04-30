package com.example.myapplication.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.LoginRegister.Login;
import com.example.myapplication.Model.BuyCarModel;
import com.example.myapplication.Model.MyViewHolder;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddCarView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText InputAddCar;
    RecyclerView DeleteCarRecyclerView;
    FirebaseRecyclerOptions<BuyCarModel> options;
    FirebaseRecyclerAdapter<BuyCarModel, MyViewHolder> adapter;
    DatabaseReference DataRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_view);

        drawerLayout = findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.nav_view_admin);
        toolbar = findViewById(R.id.toolbar);
        InputAddCar = findViewById(R.id.inputSearch_AddCarView);
        DeleteCarRecyclerView = findViewById(R.id.recyclerViewAddCarView);
        DeleteCarRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DeleteCarRecyclerView.setHasFixedSize(true);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Add Cars");
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);

        //navigation drawer menu

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        ImageSlider imageSlider = findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.benzcar1,"Buy Brand New Cars"));
        slideModels.add(new SlideModel(R.drawable.benzcar2,"Buy Used Cars"));
        slideModels.add(new SlideModel(R.drawable.benzcar3,"Various Collection"));

        imageSlider.setImageList(slideModels,true);


        LoadData("");
        InputAddCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString() != null){
                    LoadData(s.toString());
                }else{
                    LoadData("");
                }
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
                startActivity(new Intent(AddCarView.this,AdminDashboard.class));
                break;

            case R.id.nav_profile:
                String uid = mAuth.getCurrentUser().getUid();
                Intent intent = new Intent(AddCarView.this, AdminUserProfile.class);
                intent.putExtra("UserID",uid);
                startActivity(intent);
                break;
            case R.id.nav_adduser:
                startActivity(new Intent(AddCarView.this,RegisterAdmin.class));
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                Toast.makeText(this,"Successfully Logged Out!",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void LoadData(String data) {

        Query query = DataRef.orderByChild("CarTopic").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<BuyCarModel>().setQuery(query,BuyCarModel.class).build();
        adapter = new FirebaseRecyclerAdapter<BuyCarModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull BuyCarModel model) {
                holder.bCarTitle.setText(model.getCarTopic());
                holder.bCarModel.setText("Car Model : " + model.getCarModel());
                holder.bCarModelYear.setText("Model Year : " + model.getCarModelYear());
                holder.bCarCondition.setText("Car Condition : " +model.getCarCondition());
                holder.bCarTransmission.setText("Transmission Type : " + model.getCarTransmission());
                holder.bCarPrice.setText("Price : " + model.getCarPrice());
                Picasso.get().load(model.getImageURL()).into(holder.imageView);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddCarView.this, DeleteAddCar.class);
                        intent.putExtra("CarKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_buy_car,parent,false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        DeleteCarRecyclerView.setAdapter(adapter);

    }
}
