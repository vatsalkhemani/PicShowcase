/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    EditText passwordEditText;
    EditText usernameEditText;
    Boolean signUpModeActive=true;
    TextView loginTextView;
    public void showUserList(){
        Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signUpClicked(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginTextView){

            Button signUpButton=findViewById(R.id.signUpButton);
            if(signUpModeActive){
                signUpModeActive=false;
                signUpButton.setText("Login");
                loginTextView.setText("or,Sign up");
            }else{
                signUpModeActive=true;
                signUpButton.setText("Sign up");
                loginTextView.setText("or,Login");

            }
        }else if(view.getId()==R.id.logoImageView || view.getId()==R.id.backgroundLayout){
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }else{

        }
    }

    public void signUpClicked(View view){


        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this,"A username and a password is required",Toast.LENGTH_SHORT).show();
        }else{
            if(signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign up", "Sucess");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
               //Login
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            Log.i("Login","ok!");
                            showUserList();
                        }else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram 2");
      usernameEditText=findViewById(R.id.usernameEditText);
      passwordEditText=findViewById(R.id.passwordEditText);
      loginTextView=findViewById(R.id.loginTextView);
      loginTextView.setOnClickListener(this);
      passwordEditText.setOnKeyListener(this);
      ImageView logoImageView=findViewById(R.id.logoImageView);
      RelativeLayout backgroundLayout=findViewById(R.id.backgroundLayout);
      logoImageView.setOnClickListener(this);
      backgroundLayout.setOnClickListener(this);


      if(ParseUser.getCurrentUser() != null){
          showUserList();
      }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}