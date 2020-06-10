package com.example.barath.traap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FirstPage extends AppCompatActivity {

    Button find;
    EditText srcs;
    EditText dess;
    Button route;
    TextView routeno;
    TextView seats;

    Firebase ref;
    Firebase refs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        Firebase.setAndroidContext(this);

        find = (Button) findViewById(R.id.busfind);
        route = (Button) findViewById(R.id.submit);
        srcs = (EditText) findViewById(R.id.src);
        dess = (EditText) findViewById(R.id.des);
        routeno = (TextView) findViewById(R.id.bus);
        seats = (TextView) findViewById(R.id.busseats);

        final String s = srcs.getText().toString();
        final String d = dess.getText().toString();
       // routeno.setText(s);
        System.out.println(s);
        System.out.println(d);

        ref = new Firebase("https://projectmtc-43d43.firebaseio.com/ROUTE/17D");
        refs = new Firebase("https://projectmtc-43d43.firebaseio.com/userdata/seat_left");

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmap();
            }
        });

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

             ref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                 @Override
                 public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                     for(com.firebase.client.DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                         String value = dataSnapshot1.getValue(String.class);
                         System.out.println(value);
                         String key = dataSnapshot1.getRef().getKey();
                         System.out.println(key);
                         String ans = dataSnapshot1.getRef().getParent().getKey();
                         System.out.println(ans);


                             routeno.setText("17D");

                             System.out.println(routeno);



                     }



                 }

                 @Override
                 public void onCancelled(com.firebase.client.FirebaseError firebaseError) {

                 }
             });


            }
        });

        refs.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                seats.setText(value);

            }

            @Override
            public void onCancelled(com.firebase.client.FirebaseError firebaseError) {

            }
        });


    }

    void openmap(){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
}
