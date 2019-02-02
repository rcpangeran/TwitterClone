package com.learn.ac_twitterclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtLogin_Email, edtLogin_Password;
    private Button btnLogin_Login;
    private TextView txtLogin_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignUI();

        // Set title to ActionBar
        setTitle("Log In");

        // Call all OnClick method
        callAllOnClick();

    }

    private void assignUI() {
        edtLogin_Email = findViewById(R.id.edtLogin_Email);
        edtLogin_Password = findViewById(R.id.edtSignUp_Password);
        btnLogin_Login = findViewById(R.id.btnLogin_Login);
        txtLogin_SignUp = findViewById(R.id.txtLogin_SignUp);
    }

    private void transitionToSignUpActivity() {
        Intent signUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivity);
        finish();
    }

    private void callAllOnClick() {
        btnLogin_Login.setOnClickListener(this);
        txtLogin_SignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnLogin_Login) :
                break;
            case (R.id.txtLogin_SignUp) :
                transitionToSignUpActivity();
                break;
        }
    }
}
