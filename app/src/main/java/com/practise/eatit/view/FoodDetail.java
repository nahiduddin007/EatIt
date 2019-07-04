package com.practise.eatit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practise.eatit.R;
import com.practise.eatit.database.DatabaseHandler;
import com.practise.eatit.model.Food;
import com.practise.eatit.model.Order;
import com.practise.eatit.utils.Common;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    private TextView foodNameTV, foodPriceTv, foodDescription;
    private ImageView foodImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton cartButton;
    ElegantNumberButton numberButton;

    String foodId = "";
    Food currentFood;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initialization();
    }

    private void initialization() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Foods");
        databaseHandler = new DatabaseHandler(this);

        numberButton = findViewById(R.id.numberButton);
        cartButton = findViewById(R.id.btnCart);
        foodDescription = findViewById(R.id.food_description);
        foodNameTV = findViewById(R.id.food_name);
        foodPriceTv = findViewById(R.id.food_price);
        foodImageView = findViewById(R.id.img_food);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppBar);

        if (getIntent() != null){
            foodId = getIntent().getStringExtra("foodId");
        }
        if (!foodId.isEmpty()){
            if (Common.isConnectedToInternet(getApplicationContext())) {
                getFoodDetails(foodId);
            } else {
                DynamicToast.makeError(getApplicationContext(), "Please turn on your internet", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order order = new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                );
                databaseHandler.addCart(order);
                DynamicToast.makeSuccess(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFoodDetails(final String foodId) {
        databaseReference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                collapsingToolbarLayout.setTitle(currentFood.getName());
                Picasso.get().load(currentFood.getImage()).into(foodImageView);
                foodNameTV.setText(currentFood.getName());
                foodPriceTv.setText(currentFood.getPrice());
                foodDescription.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
