package com.learn.ac_twitterclone;

import android.app.ProgressDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostTweetActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout layoutPostTweet_Root;
    private EditText edtPostTweet_Post;
    private Button btnPostTweet_Send, btnPostTweet_SeeOthersTweets;
    private ListView listPostTweet_OthersTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);
        assignUI();

        // Set title to ActionBar
        setTitle("Post Tweet And See Tweets");

        // Call All OnClick Event Handler
        callAllOnClickHandler();
    }

    private void assignUI() {
        layoutPostTweet_Root = findViewById(R.id.layoutPostTweet_Root);
        edtPostTweet_Post = findViewById(R.id.edtPostTweet_Post);
        btnPostTweet_Send = findViewById(R.id.btnPostTweet_Send);
        btnPostTweet_SeeOthersTweets = findViewById(R.id.btnPostTweet_SeeOthersTweets);
        listPostTweet_OthersTweets = findViewById(R.id.listPostTweet_OthersTweets);
    }

    private void callAllOnClickHandler() {
        layoutPostTweet_Root.setOnClickListener(this);
        btnPostTweet_Send.setOnClickListener(this);
        btnPostTweet_SeeOthersTweets.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.layoutPostTweet_Root) :
                dismissAllUIInterface();
                break;
            case (R.id.btnPostTweet_Send) :
                sendTweetNow();
                break;
            case (R.id.btnPostTweet_SeeOthersTweets) :
                seeOthersTweets();
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

    private void sendTweetNow() {
        boolean isComplete = false;

        // Check all fields
        if (edtPostTweet_Post.getText().toString().isEmpty()) {
            FancyToast.makeText(this,
                    "Please enter what you want to tweet",
                    Toast.LENGTH_LONG,
                    FancyToast.INFO,
                    true)
                    .show();
        } else {
            isComplete = true;
        }

        if (isComplete) {
            String currentUser = ParseUser.getCurrentUser().getUsername();

            ParseObject parseTweetNow = new ParseObject("MyTweet");
            parseTweetNow.put("user", currentUser);
            parseTweetNow.put("tweet", edtPostTweet_Post.getText().toString());

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Saving tweet ...");
            dialog.show();

            parseTweetNow.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.dismiss();
                    if (e == null) {
                        FancyToast.makeText(PostTweetActivity.this,
                                "Tweet saved!",
                                Toast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                true)
                            .show();
                    } else {
                        FancyToast.makeText(PostTweetActivity.this,
                                "Error : " + e.getMessage(),
                                Toast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                true)
                                .show();
                    }
                }
            });
        }
    }

    private void seeOthersTweets() {
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(PostTweetActivity.this,
                tweetList,
                android.R.layout.simple_list_item_2,
                new String[]{"tweetUsername", "tweetValue"},
                new int[] {android.R.id.text1, android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));

            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUsername", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        listPostTweet_OthersTweets.setAdapter(adapter);
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
