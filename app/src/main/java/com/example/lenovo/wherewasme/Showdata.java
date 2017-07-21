package com.example.lenovo.wherewasme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Lenovo on 10-08-2016.
 */
public class Showdata extends AppCompatActivity {
    TextView textView;
    EditText uid;
    String name, id;
    Button you,other;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);
        uid=(EditText)findViewById(R.id.uid);
        you=(Button)findViewById(R.id.traceme);
        other=(Button)findViewById(R.id.traceother);

        Intent admin= getIntent();
        name = admin.getStringExtra("User").toString();
        id= admin.getStringExtra("pqr").toString();
        textView=(TextView)findViewById(R.id.fullname);
        textView.setText("Hello "+name+"\n Your Unique id: "+id);
    }
}
