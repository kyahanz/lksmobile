package lks.jabar.android_lks_jabar_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateActivity extends AppCompatActivity {
    private String name, description, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        Button btn_update = (Button) findViewById(R.id.btnUpdate);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = ((EditText) findViewById(R.id.updateMenuName)).getText().toString();
                description = ((EditText) findViewById(R.id.updateDescription)).getText().toString();
                price = ((EditText) findViewById(R.id.updatePrice)).getText().toString();

                if(name.isEmpty() && price.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Menu Name & Price are Empty!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                update(id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    public void update(String id) throws IOException {

        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

        URL url = new URL("" + id); // fill this with your own API
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization", "Bearer your_bearer_token"); // fill with your bearer token

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String param = "name=" + name + "&description=" + description + "&price=" + price;

            outputStream.writeBytes(param);

            outputStream.flush();
            outputStream.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
