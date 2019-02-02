package com.learn.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private ListView listViewMainActivity;
    private ArrayList<String> tUsers;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignUI();
        initializeVars();

        // Set title to ActionBar
        setTitle("Member Area");

        // Welcoming user to member area
        welcomeUser();

        // Load list view
        loadListView();

        // Call All OnItemClick Handler
        callAllOnItemClickHandler();
    }

    private void assignUI() {
        listViewMainActivity = findViewById(R.id.listViewMainActivity);
    }

    private void initializeVars() {
        tUsers = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, tUsers);

        listViewMainActivity.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    private void transitionToPostTweet() {
        Intent postTweetActivity = new Intent(this, PostTweetActivity.class);
        startActivity(postTweetActivity);
    }

    @Override
    public void onBackPressed() {
        Toast mToast = null;
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            mToast.cancel();
            super.onBackPressed();
            return;
        } else {
            mToast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.logoutUserItem) :
                logOutClicked();
                break;
            case (R.id.postTweet) :
                transitionToPostTweet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void welcomeUser() {
        FancyToast.makeText(this,
                "Welcome " + ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                true)
            .show();
    }

    private void loadListView() {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        try {
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null) {
                        if (users.size() > 0) {
                            for (ParseUser user : users) {
                                tUsers.add(user.getUsername());
                            }
                            listViewMainActivity.setAdapter(arrayAdapter);

                            for (String twitterUser : tUsers) {
                                if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                        listViewMainActivity.setItemChecked(tUsers.indexOf(twitterUser), true);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAllOnItemClickHandler() {
        listViewMainActivity.setOnItemClickListener(this);
    }

    private void logOutClicked() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent logOut = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(logOut);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()) {
            FancyToast.makeText(MainActivity.this,
                    tUsers.get(position) + " is now followed",
                    Toast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true)
                .show();
            ParseUser.getCurrentUser().add("fanOf", tUsers.get(position));
        } else {
            FancyToast.makeText(MainActivity.this,
                    tUsers.get(position) + " is now unfollowed",
                    Toast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true)
                    .show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    FancyToast.makeText(MainActivity.this,
                            "Saved",
                            Toast.LENGTH_SHORT,
                            FancyToast.SUCCESS,
                            true)
                        .show();
                }
            }
        });
    }
}
