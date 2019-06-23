package com.practise.eatit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practise.eatit.R;
import com.practise.eatit.databinding.ActivitySignUpBinding;
import com.practise.eatit.model.User;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        initialization();
    }

    private void initialization() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        signUpBinding.signUpSignButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpSignButton:
                singUp();
                break;
        }
    }

    private void singUp() {
        dialog.setMessage("Please Wait");
        dialog.show();

        mAuth.createUserWithEmailAndPassword(signUpBinding.signUpEmailEditText.getText().toString()
                , signUpBinding.signUpUserPassEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            saveUserData();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            DynamicToast.makeError(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseUser crrentUser = mAuth.getCurrentUser();
                    User user = new User(signUpBinding.signUpFullNameEditText.getText().toString(),
                            signUpBinding.signUpUserPassEditText.getText().toString(),
                            signUpBinding.signUpPhoneNumEditText.getText().toString());
                    databaseReference.child(crrentUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                            DynamicToast.makeSuccess(getApplicationContext(), "SignUp Successful!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            DynamicToast.makeError(getApplicationContext(), "SignUp with Data is failed!"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                 }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
