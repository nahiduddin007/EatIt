package com.practise.eatit.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practise.eatit.model.User;

public class CurrentUser {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userDataRef = database.getReference("User");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user;

    public User getUserData() {
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return user;
    }


}
