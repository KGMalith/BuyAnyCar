package com.example.myapplication.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import com.example.myapplication.Model.AccessoriesModel;
import com.example.myapplication.Model.AccessoriesViewHolder;
import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Accessories extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView InputAccessories;
    RecyclerView AccessoriesRecyclerView;
    FirebaseRecyclerOptions<AccessoriesModel> options;
    FirebaseRecyclerAdapter<AccessoriesModel, AccessoriesViewHolder> adapter;
    DatabaseReference DataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        InputAccessories = findViewById(R.id.inputSearch_accessoriesView);
        AccessoriesRecyclerView = findViewById(R.id.recyclerViewAccessoriesView);
        AccessoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AccessoriesRecyclerView.setHasFixedSize(true);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Add Accessories");


        ImageSlider imageSlider = findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.benzcar1,"Buy Brand New Cars"));
        slideModels.add(new SlideModel(R.drawable.benzcar2,"Buy Used Cars"));
        slideModels.add(new SlideModel(R.drawable.benzcar3,"Various Collection"));

        imageSlider.setImageList(slideModels,true);


        LoadData();

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<AccessoriesModel>().setQuery(DataRef,AccessoriesModel.class).build();
        adapter = new FirebaseRecyclerAdapter<AccessoriesModel, AccessoriesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AccessoriesViewHolder holder, int position, @NonNull AccessoriesModel model) {
                holder.aTitle.setText(model.getAccessoriesTopic());
                holder.aBrand.setText(model.getAccessoriesBrand());
                holder.aModel.setText(model.getAccessoriesModel());
                holder.aCondition.setText(model.getAccessoriesCondition());
                holder.aPrice.setText(model.getAccessoriesPrice());
                Picasso.get().load(model.getImageURL()).into(holder.imageView);

            }

            @NonNull
            @Override
            public AccessoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_accessories,parent,false);
                return new AccessoriesViewHolder(v);
            }
        };
        adapter.startListening();
        AccessoriesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }


    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_profile:
                Intent intent = new Intent(Accessories.this, UserProfile.class);
                startActivity(intent);
                break;

            case R.id.nav_share:
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
