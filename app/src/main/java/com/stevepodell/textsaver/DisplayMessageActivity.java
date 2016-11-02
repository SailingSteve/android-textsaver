package com.stevepodell.textsaver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class DisplayMessageActivity extends AppCompatActivity {
    public  TextView textView;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // https://developer.android.com/training/basics/data-storage/shared-preferences.html

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean bHasText = sharedPref.contains(getString(R.string.saved_text));
        SharedPreferences.Editor editor = sharedPref.edit();
        String saved = sharedPref.getString(getString(R.string.saved_text), "");
        String text = message + "\n" + saved;
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        editor.putString(getString(R.string.saved_text), text);
        editor.apply();

        textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(text);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clearAll(View view) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean bHasText = sharedPref.contains(getString(R.string.saved_text));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_text), "");
        editor.apply();
        textView.setText("");
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Textsaver DisplayMessage Page")
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
