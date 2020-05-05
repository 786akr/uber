package com.example.uber;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    public void onClick(View v) {
        if(edtDriverOrPassenger.getText().toString().equals("driver")||edtDriverOrPassenger.getText().toString().equals("Passenger")){
            if(ParseUser.getCurrentUser()==null){
                 ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null&&user!=null){
                            FancyToast.makeText(MainActivity.this,"Logged in",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                            user.put("as",edtDriverOrPassenger.getText().toString());
                            user.saveInBackground();
                        }
                    }
                });
            }


        }else {
            FancyToast.makeText(MainActivity.this,
                    "are you a driver or passeneger",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    true).show();

        }
    }  enum State {
        SIGNUP, LOGIN
    }
    private Button btnsign,btnoneTimeLogin;
    private RadioButton DriverRadioButton,PassengerRadioButton;
    private EditText edtUsername,edtPassword,edtDriverOrPassenger;
    private State state;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();// build a connection with parse server
          if(ParseUser.getCurrentUser()!=null){
              ParseUser.logOut();
          }
        btnsign = findViewById(R.id.btnsignUp);
        state=State.SIGNUP;
        edtPassword=findViewById(R.id.edtPassword);
        edtUsername=findViewById(R.id.edtUsername);
        btnoneTimeLogin=findViewById(R.id.OneTimeLogin);
        edtDriverOrPassenger=findViewById(R.id.edtDriverorPasseneger);
        DriverRadioButton=findViewById(R.id.rdoDriver);
        PassengerRadioButton=findViewById(R.id.rdoPassenger);
       btnoneTimeLogin.setOnClickListener(MainActivity.this);
        btnsign.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(state==State.SIGNUP) {
                if (edtUsername.getText().toString().equals("") && edtPassword.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Fill Email and Password", Toast.LENGTH_SHORT).show();
                } else if (DriverRadioButton.isChecked() == false && PassengerRadioButton.isChecked() == false) {
                    Toast.makeText(MainActivity.this, "Please Describe Driver or Passenger ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());
                    if (DriverRadioButton.isChecked()) {
                        appUser.put("as", "Driver");
                    } else if (PassengerRadioButton.isChecked()) {
                        appUser.put("as", "Passenger");
                    }
                        FancyToast.makeText(MainActivity.this, "Done", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();


                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(MainActivity.this, "saved", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            }
                        }
                    });
                }
            }
            if(state==State.LOGIN){
                ParseUser.logInInBackground(edtUsername.getText().toString(),edtPassword.getText().toString(),(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null&&user==null){
                            FancyToast.makeText(MainActivity.this,"Logged in",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }
                    }
                }));

            }
        }
    });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu); }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LoginItem :
                if (state == State.SIGNUP) {
                    state = State.LOGIN;
                    item.setTitle("Login");
                    btnsign.setText("Sign Up");
                } else if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    item.setTitle("Sign Up");
                    btnsign.setText("Login");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

