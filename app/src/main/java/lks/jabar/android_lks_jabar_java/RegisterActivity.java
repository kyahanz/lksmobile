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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private String username, password, confirmPassword;
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_register = (Button) findViewById(R.id.btnRegister);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = ((EditText) findViewById(R.id.usernameRegister)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordRegister)).getText().toString();
                confirmPassword = ((EditText) findViewById(R.id.passwordConfirmRegister)).getText().toString();

                if (username.isEmpty() && password.isEmpty()) {
                    Toast.makeText(mContext, "Enter Username & Password", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(mContext, "Password doesn't match with confirm password", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            register();
                        }
                    }).start();

                }

            }
        });
    }

    public void register() {

        String target = ""; // fill this with your own API

        URL url;
        try {
            url = new URL(target);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   //设置内容类型

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            String param = "username=" + username + "&password=" + password;

            outputStream.writeBytes(param);

            outputStream.flush();
            outputStream.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                startActivity(new Intent(mContext, LoginActivity.class));
                Looper.prepare();
                Toast.makeText(mContext, "Register success!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                Looper.prepare();
                Toast.makeText(mContext, "username has already taken!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
