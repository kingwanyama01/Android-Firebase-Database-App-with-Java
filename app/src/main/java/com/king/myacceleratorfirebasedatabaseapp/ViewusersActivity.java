package com.king.myacceleratorfirebasedatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewusersActivity extends AppCompatActivity {
    ArrayList<User> users;
    RecyclerView mRecyclerUsers;
    CustomAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewusers);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        users = new ArrayList();
        mRecyclerUsers = findViewById(R.id.mRecyclerUsers);
        mRecyclerUsers.setHasFixedSize(true);
        mRecyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomAdapter(this,users);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                message("DATABASE LOCKED","Sorry, we couldn't access the DB. Contact you app developer for assistance");
            }
        });
        mRecyclerUsers.setAdapter(adapter);

    }


    public void message(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
