package com.pickme.autoaccept;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText etDestinations, etMinFare, etMaxDistance;
    private Button btnStart;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDestinations = findViewById(R.id.etDestinations);
        etMinFare = findViewById(R.id.etMinFare);
        etMaxDistance = findViewById(R.id.etMaxDistance);
        btnStart = findViewById(R.id.btnStart);

        sharedPreferences = getSharedPreferences("PickMeSettings", Context.MODE_PRIVATE);
        loadSettings();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        String dest = etDestinations.getText().toString().trim();
        String fareStr = etMinFare.getText().toString().trim();
        String distStr = etMaxDistance.getText().toString().trim();

        if (dest.isEmpty() || fareStr.isEmpty() || distStr.isEmpty()) {
            Toast.makeText(MainActivity.this, "සියලු විස්තර ඇතුළත් කරන්න!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float fare = Float.parseFloat(fareStr);
            float dist = Float.parseFloat(distStr);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("destinations", dest);
            editor.putFloat("minFare", fare);
            editor.putFloat("maxDistance", dist);
            editor.apply();
            Toast.makeText(MainActivity.this, "සේව් වුණා! Accessibility සේවාව On කරන්න.", Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Valid numbers ඇතුළත් කරන්න!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSettings() {
        etDestinations.setText(sharedPreferences.getString("destinations", ""));
        etMinFare.setText(String.valueOf((int) sharedPreferences.getFloat("minFare", 500f)));
        etMaxDistance.setText(String.valueOf(sharedPreferences.getFloat("maxDistance", 3.0f)));
    }
}
