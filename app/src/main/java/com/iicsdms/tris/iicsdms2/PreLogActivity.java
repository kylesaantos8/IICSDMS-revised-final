package com.iicsdms.tris.iicsdms2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreLogActivity extends AppCompatActivity {

    static final String TAG = "test";
    String email, pw, type, loginURL, logLoginURL, logsType, logsLogin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_log);

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        if(!sharedPreferences.getString("logEmail", TAG).equals(TAG) ||
                !sharedPreferences.getString("logPw", TAG).equals(TAG))
        {
            email = sharedPreferences.getString("logEmail", TAG);
            pw = sharedPreferences.getString("logPw", TAG);

            type = "preLogging";
            loginURL = getString(R.string.login_url);
            logLoginURL = getString(R.string.logs_log);
            logsType = getString(R.string.type);
            logsLogin = getString(R.string.login);

            LoginActivity loginActivity = new LoginActivity(this);
            loginActivity.execute(loginURL, type, email, pw, logLoginURL, logsType, logsLogin);
        }
        else
        {
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
