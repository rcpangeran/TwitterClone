package com.learn.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private EditText edtSignUp_Email, edtSignUp_Username, edtSignUp_Password;
    private Button btnSignUp_SignUp;
    private TextView txtSignUp_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Save the current Installation to Back4App
        installToBack4App();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        assignUI();

        // Set title to ActionBar
        setTitle("Sign Up");

        // Call all OnKey method
        callAllOnKeyTap();

        // Call all onClick method
        callAllOnClick();
    }

    private void installToBack4App() {
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void assignUI() {
        edtSignUp_Email = findViewById(R.id.edtSignUp_Email);
        edtSignUp_Username = findViewById(R.id.edtSignUp_Username);
        edtSignUp_Password = findViewById(R.id.edtSignUp_Password);
        btnSignUp_SignUp = findViewById(R.id.btnSignUp_SignUp);
        txtSignUp_Login = findViewById(R.id.txtSignUp_Login);
    }

    private void transitionToLoginActivity() {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    private void transitionToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void callAllOnKeyTap() {
        edtSignUp_Password.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        switch (view.getId()) {
            case (R.id.edtSignUp_Password) :
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp_SignUp);
                }
                break;
        }

        return false;
    }

    private void callAllOnClick() {
        btnSignUp_SignUp.setOnClickListener(this);
        txtSignUp_Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnSignUp_SignUp) :
                createNewUser();
                // transitionToMainActivity();
                break;
            case (R.id.txtSignUp_Login) :
                transitionToLoginActivity();
                break;
        }
    }

    private void createNewUser() {
        boolean isComplete = false;

        // Check all fields
        if (edtSignUp_Email.getText().toString().equals("") ||
                edtSignUp_Username.getText().toString().equals("") ||
                edtSignUp_Password.getText().toString().equals("")) {
            FancyToast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true)
                .show();
        } else {
            isComplete = true;
        }

        // Register new user
        if (isComplete) {
            ParseUser newUser = new ParseUser();
            newUser.setEmail(edtSignUp_Email.getText().toString());
            newUser.setUsername(edtSignUp_Username.getText().toString());
            newUser.setPassword(edtSignUp_Password.getText().toString());

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Signing up " + edtSignUp_Username.getText().toString());
            dialog.show();

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUpActivity.this,
                                "Signed up success",
                                Toast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                true)
                                .show();
                    } else {
                        FancyToast.makeText(SignUpActivity.this,
                                "Error : " + e.getMessage(),
                                Toast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                true)
                                .show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }
}
