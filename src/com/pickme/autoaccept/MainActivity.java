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

        // UI Components සම්බන්ධ කරගැනීම
        etDestinations = (EditText) findViewById(R.id.etDestinations);
        etMinFare     = (EditText) findViewById(R.id.etMinFare);
        etMaxDistance = (EditText) findViewById(R.id.etMaxDistance);
        btnStart      = (Button)   findViewById(R.id.btnStart);

        sharedPreferences = getSharedPreferences("PickMeSettings", Context.MODE_PRIVATE);

        // කලින් සේව් කරපු ඩේටා තිබේ නම් Auto-fill කිරීම
        loadSettings();

        // බටන් ක්ලික් කරද්දී ඩේටා සේව් කිරීම
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        String dest    = etDestinations.getText().toString().trim();
        String fareStr = etMinFare.getText().toString().trim();
        String distStr = etMaxDistance.getText().toString().trim();

        // BUG FIX: Empty check කිරීම
        if (dest.isEmpty() || fareStr.isEmpty() || distStr.isEmpty()) {
            Toast.makeText(this, "කරුණාකර සියලුම විස්තර ඇතුළත් කරන්න!", Toast.LENGTH_SHORT).show();
            return;
        }

        // BUG FIX: NumberFormatException catch කිරීම - invalid numbers දෙද්දී crash වෙයි
        float fare, dist;
        try {
            fare = Float.parseFloat(fareStr);
            dist = Float.parseFloat(distStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Fare සහ Distance සඳහා valid numbers ඇතුළත් කරන්න!", Toast.LENGTH_SHORT).show();
            return;
        }

        // BUG FIX: Negative values validate කිරීම
        if (fare <= 0 || dist <= 0) {
            Toast.makeText(this, "Fare සහ Distance ශුන්‍යයට වඩා වැඩි විය යුතුයි!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("destinations", dest);
        editor.putFloat("minFare", fare);
        editor.putFloat("maxDistance", dist);
        editor.apply();

        Toast.makeText(this, "සෙටින්ග්ස් සාර්ථකව සේව් වුණා! කරුණාකර Accessibility සේවාව On කරන්න.", Toast.LENGTH_LONG).show();
    }

    private void loadSettings() {
        // BUG FIX: getFloat() default values නිවැරදිව set කිරීම
        String dest   = sharedPreferences.getString("destinations", "");
        float minFare = sharedPreferences.getFloat("minFare", 500f);
        float maxDist = sharedPreferences.getFloat("maxDistance", 3.0f);

        etDestinations.setText(dest);
        // BUG FIX: fare integer ලෙස, distance decimal ලෙස format කිරීම
        etMinFare.setText(String.valueOf((int) minFare));
        etMaxDistance.setText(String.valueOf(maxDist));
    }
}
