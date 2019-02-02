package com.learn.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private ConstraintLayout layoutLogin_Root;
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

        // Call all OnKey method
        callAllOnKeyTap();

        // Call all OnClick method
        callAllOnClick();

    }

    @Override
    public void onBackPressed() {
        transitionToSignUpActivity();
    }

    private void assignUI() {
        layoutLogin_Root = findViewById(R.id.layoutLogin_Root);
        edtLogin_Email = findViewById(R.id.edtLogin_Email);
        edtLogin_Password = findViewById(R.id.edtLogin_Password);
        btnLogin_Login = findViewById(R.id.btnLogin_Login);
        txtLogin_SignUp = findViewById(R.id.txtLogin_SignUp);
    }

    private void transitionToSignUpActivity() {
        Intent signUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivity);
        finish();
    }

    private void callAllOnKeyTap() {
        edtLogin_Password.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        switch (view.getId()) {
            case (R.id.edtLogin_Password) :
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogin_Login);
                }
                break;
        }
        return false;
    }

    private void callAllOnClick() {
        layoutLogin_Root.setOnClickListener(this);
        btnLogin_Login.setOnClickListener(this);
        txtLogin_SignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.layoutLogin_Root) :
                dismissAllUIInterface();
                break;
            case (R.id.btnLogin_Login) :
                loginUser();
                break;
            case (R.id.txtLogin_SignUp) :
                transitionToSignUpActivity();
                break;
        }
    }

    private void dismissAllUIInterface() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginUser() {
        boolean isComplete = false;

        // Check all fields
        if (edtLogin_Email.getText().toString().isEmpty() ||
                edtLogin_Password.getText().toString().isEmpty()) {
            FancyToast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_LONG,
                    FancyToast.INFO,
                    true)
                    .show();
        } else {
            isComplete = true;
        }

        // Register new user
        if (isComplete) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Logging in...");
            dialog.show();

            ParseUser.logInInBackground(edtLogin_Email.getText().toString(),
                    edtLogin_Password.getText().toString(),
                    new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this,
                                        "Successfully logged in",
                                        Toast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        true)
                                        .show();
                            } else {
                                FancyToast.makeText(LoginActivity.this,
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
