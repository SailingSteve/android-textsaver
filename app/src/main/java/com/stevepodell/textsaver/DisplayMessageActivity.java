package com.stevepodell.textsaver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DisplayMessageActivity extends AppCompatActivity {
    public final int idTxtView = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.stevepodell.textsaver.R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // https://developer.android.com/training/basics/data-storage/shared-preferences.html

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean bHasText = sharedPref.contains(getString(com.stevepodell.textsaver.R.string.saved_text));
        SharedPreferences.Editor editor = sharedPref.edit();
        String saved = sharedPref.getString(getString(com.stevepodell.textsaver.R.string.saved_text),"");
        String text = message + "\n" + saved;
        if( text.length() > 100 ) {
            text = text.substring(0, 100);
        }
        editor.putString(getString(com.stevepodell.textsaver.R.string.saved_text), text);
        editor.commit();

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(text);
        textView.setId(idTxtView);

        ViewGroup layout = (ViewGroup) findViewById(com.stevepodell.textsaver.R.id.activity_display_message);
        layout.addView(textView);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clearAll(View view) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean bHasText = sharedPref.contains(getString(com.stevepodell.textsaver.R.string.saved_text));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(com.stevepodell.textsaver.R.string.saved_text), "");
        editor.commit();
        TextView textView = (TextView) findViewById(idTxtView);
        textView.setText("");
    }
}
