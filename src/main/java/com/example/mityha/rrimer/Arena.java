package com.example.mityha.rrimer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.File;

public class Arena extends AppCompatActivity {

    private int generations = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_arena);
    LinearLayout layout = findViewById(R.id.layoutArena);

    generations = getIntent().getIntExtra("generation", -1);

    for (int gen = 1; gen < generations; gen += 1)
        if((new File(getFilesDir(), "rat-" + 0 + " " + gen + ".dat")).exists()) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(gen);
            checkBox.setChecked(false);
            checkBox.setText("Использовать поколение " + gen);
            checkBox.setTextColor(Color.BLACK);
            layout.addView(checkBox);
        }
    }

    public void onStart(View v){
        Intent intent = new Intent();
        boolean f = false;
        for(int i = 0; i < generations; i += 1){
            CheckBox checkBox = findViewById(i);
            if(checkBox != null)
            if(checkBox.isChecked()) {
                intent.putExtra("gen" + i, i);
                f = true;
            }
        }
        if(f) {
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
