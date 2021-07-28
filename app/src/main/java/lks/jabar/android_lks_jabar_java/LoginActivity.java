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
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String username, password;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_registerPage = (Button) findViewById(R.id.btnRegisterPage);
        btn_registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

        Button btn_loginPage = (Button) findViewById(R.id.btnLogin);
        btn_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = ((EditText) findViewById(R.id.usernameLogin)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordLogin)).getText().toString();

                if (username.isEmpty() && password.isEmpty()) {
                    Toast.makeText(mContext, "Fill the Username and Password !", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    }).start();
                }

            }
        });
    }

    private void login() {
        String target = ""; // fill this with your API

        URL url;
        try {
            url = new URL(target);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String param = "username=" + username + "&password=" + password;

            outputStream.writeBytes(param);

            outputStream.flush();
            outputStream.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                startActivity(new Intent(mContext, MainActivity.class));
                Looper.prepare();
                Toast.makeText(mContext, "Login Success", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                Looper.prepare();
                Toast.makeText(mContext, "User data not found!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}