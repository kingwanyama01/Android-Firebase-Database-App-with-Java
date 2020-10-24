package com.king.myacceleratorfirebasedatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText mEdtName, mEdtMail, mEdtPhone;
    Button mBtnSave, mBtnView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtName = findViewById(R.id.edt_name);
        mEdtMail = findViewById(R.id.edt_mail);
        mEdtPhone = findViewById(R.id.edt_phone);

        mBtnSave = findViewById(R.id.btn_save);
        mBtnView = findViewById(R.id.btn_view);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please wait...");

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start by receiving data from the user
                String name, email, phone;
                name = mEdtName.getText().toString();
                email = mEdtMail.getText().toString();
                phone = mEdtPhone.getText().toString();

                if (name.isEmpty()){
                    mEdtName.setError("Please enter name");
                }else if (email.isEmpty()){
                    mEdtMail.setError("Please enter email");
                }else if (phone.isEmpty()){
                    mEdtPhone.setError("Please enter phone");
                }else {
                    //Connect to our database table/Child
                    long time = System.currentTimeMillis();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+time);
                    User user = new User(name,email,phone,String.valueOf(time));
                    dialog.show();
                    ref.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){
                                message("SUCCESS","Saving successful");
                                clear();
                            }else {
                                message("FAILED!!","Saving failed");
                            }
                        }
                    });

                }
            }
        });

        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewusersActivity.class));
            }
        });
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

    public void clear(){
        mEdtName.setText("");
        mEdtMail.setText("");
        mEdtPhone.setText("");
    }
}











