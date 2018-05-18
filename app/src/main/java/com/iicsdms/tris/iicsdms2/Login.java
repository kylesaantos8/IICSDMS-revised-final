package com.iicsdms.tris.iicsdms2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Tris on 04/03/2018.
 */

public class Login extends AppCompatActivity {

    private Button btnLogin;
    private EditText editUsername, editPassword;
    private TextView forgetPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.button_login);
        editUsername = (EditText)findViewById(R.id.loginEmail);
        editPassword = (EditText)findViewById(R.id.loginPassword);

        forgetPw = (TextView) findViewById(R.id.forgotPassword);

        forgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
    }

    public void OnLogin(View v)
    {
        String email = editUsername.getText().toString();
        String pw = editPassword.getText().toString();
        String type = "login";
        String loginURL = getString(R.string.login_url);
        String logLoginURL = getString(R.string.logs_log);
        String logsType = getString(R.string.type);
        String logsLogin = getString(R.string.login);


        if(email.isEmpty() || pw.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter your email/password and try again.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Encryption encryption = new Encryption();
            String strEncrypt = encryption.encrypt(pw);

            LoginActivity loginActivity = new LoginActivity(this);
            loginActivity.execute(loginURL, type, email, strEncrypt, logLoginURL, logsType, logsLogin);

        }
    }

    public void forgetPassword()
    {
        Uri uri = Uri.parse(getString(R.string.forget_password));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}