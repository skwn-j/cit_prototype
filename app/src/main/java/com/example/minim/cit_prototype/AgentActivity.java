package com.example.minim.cit_prototype;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;

public class AgentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        final Button button1 = (Button) findViewById(R.id.button1) ;
        final Button button2 = (Button) findViewById(R.id.button2) ;
        final Button button3 = (Button) findViewById(R.id.cancel_action) ;

        //Setting Storage
        String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Settings";
        File agentselection = new File(dirpath+"/agent");


        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(Color.rgb(255, 0, 0));
                button2.setBackgroundColor(Color.rgb(0, 0, 0));

            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(Color.rgb(0, 0, 0));
                button2.setBackgroundColor(Color.rgb(255, 0, 0));
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
