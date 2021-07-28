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
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertActivity extends AppCompatActivity {

    private String name, description, price;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Button btn_register = (Button) findViewById(R.id.btnInsert);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ((EditText) findViewById(R.id.edtName)).getText().toString();
                description = ((EditText) findViewById(R.id.edtDescription)).getText().toString();
                price = ((EditText) findViewById(R.id.edtPrice)).getText().toString();

                if(name.isEmpty() && price.isEmpty()){
                    Toast.makeText(mContext, "Please enter the Menu Name and Price", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            insert();
                        }
                    }).start();
                }

            }
        });
    }

    public void insert() {

        String target = ""; // fill this with your API

        URL url;
        try {
            url = new URL(target);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestProperty("Authorization", "Bearer your bearer_token");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String param = "name=" + name + "&description=" + description + "&price=" + price;

            outputStream.writeBytes(param);

            outputStream.flush();
            outputStream.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                startActivity(new Intent(mContext, MainActivity.class));

                Looper.prepare();
                Toast.makeText(mContext, "Insert Success", Toast.LENGTH_SHORT).show();
            } else {
                Looper.prepare();
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
            Looper.loop();

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}