package com.example.pranjali.amssqlite1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
Button register,reset;
    EditText name,username,password;
    DBhelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper=new DBhelper(this);
        name=(EditText) findViewById(R.id.reg_name);
        username=(EditText) findViewById(R.id.reg_username);
        password=(EditText) findViewById(R.id.reg_pass);
        register=(Button) findViewById(R.id.Reg_submit);
        reset=(Button) findViewById(R.id.reg_reset);


        reset=(Button)findViewById(R.id.reg_reset);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(Register.this, "Register sucessfully.....!", Toast.LENGTH_SHORT).show();
                Log.i("Register before ", " "+name.getText().toString()+username.getText().toString()+password.getText().toString());
                Boolean status= databaseHelper.regUser(name.getText().toString(),username.getText().toString(),password.getText().toString());
                if(status)
                    Toast.makeText(Register.this, "Registerd", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Register.this, "Not Registered", Toast.LENGTH_SHORT).show();
                Cursor c=databaseHelper.getdata();
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++) {
                    Log.i("Register Log ", " "+c.getString(0)+c.getString(1));
                    c.moveToNext();
                }

            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                username.setText("");
                password.setText("");
            }
        });

    }

}
