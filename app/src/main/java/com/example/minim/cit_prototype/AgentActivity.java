package com.example.minim.cit_prototype;

import Utils.PreferencesManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;

public class AgentActivity extends AppCompatActivity {

    PreferencesManager prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        final Button button1 = (Button) findViewById(R.id.button1) ;
        final Button button2 = (Button) findViewById(R.id.button2) ;
        final Button button3 = (Button) findViewById(R.id.cancel_action) ;
        int agent = prefs.loadIntegerSharedPreferences(getApplicationContext(), "agent");
        switch(agent) {
            case 1 :
                button1.setBackgroundColor(Color.rgb(255, 0, 0));
                button2.setBackgroundColor(Color.rgb(0, 0, 0));
            case 2 :
                button1.setBackgroundColor(Color.rgb(0, 0, 0));
                button2.setBackgroundColor(Color.rgb(255, 0, 0));
            default :
                button1.setBackgroundColor(Color.rgb(0, 0, 0));
                button2.setBackgroundColor(Color.rgb(0, 0, 0));
        }

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(Color.rgb(255, 0, 0));
                button2.setBackgroundColor(Color.rgb(0, 0, 0));
                prefs.saveIntegerPreferencesco(getApplicationContext(), "agent", 1);

            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(Color.rgb(0, 0, 0));
                button2.setBackgroundColor(Color.rgb(255, 0, 0));
                prefs.saveIntegerPreferencesco(getApplicationContext(), "agent", 2);
            }
        });

        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
