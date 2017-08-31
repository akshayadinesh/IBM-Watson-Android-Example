package com.akshayadinesh.toneanalyzerexample;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText inputField;
    TextView resultsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = (EditText) findViewById(R.id.inputText);
        resultsText = (TextView) findViewById(R.id.results);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                .permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    public void submitButtonClick(View view) {

        String text = inputField.getText().toString();
        makeRequest(text);

    }

    public void makeRequest(String text) {
        try {

            URL url = new URL("https://watson-api-explorer.mybluemix.net/tone-analyzer/api/v3/tone?version=2016-05-19&text=" + text);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            try {

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

                JSONObject json = new JSONObject(result.toString());
                JSONArray categories = json.getJSONObject("document_tone").getJSONArray("tone_categories");
                JSONArray tones = categories.getJSONObject(0).getJSONArray("tones");
                double anger = Double.valueOf(tones.getJSONObject(0).getString("score"));
                double disgust = Double.valueOf(tones.getJSONObject(1).getString("score"));
                double fear = Double.valueOf(tones.getJSONObject(2).getString("score"));
                double joy = Double.valueOf(tones.getJSONObject(3).getString("score"));
                double sadness = Double.valueOf(tones.getJSONObject(4).getString("score"));

                resultsText.setText("Anger: "+ anger +"\nDisgust: "+ disgust +"\nFear: "+ fear +"\nJoy: "+ joy +"\nSadness: "+ sadness);

            } finally {
                urlConnection.disconnect();
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

    }
}
