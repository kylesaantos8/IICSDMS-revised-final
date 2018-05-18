package com.iicsdms.tris.iicsdms2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iicsdms.tris.iicsdms2.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Tris on 04/03/2018.
 */

public class LoginActivity extends AsyncTask<String,Void,String> {


    private String loginURL, type, email, pw;

    private ProgressDialog progressDialog;

    LoginPojo loginPojo = new LoginPojo();

    private Context context;

    LoginActivity(Context ctx) {
        context = ctx;
    }


    @Override
    protected String doInBackground(String... params) {


        loginURL = params[0];
        type = params[1];
        email = params[2];
        pw = params[3];


        String line, result;

        try
        {


            URL url = new URL(loginURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pw.trim(),"UTF-8");
            //ex = data;
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder stringBuilder = new StringBuilder();


            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();
            result = stringBuilder.toString();

            if(result.trim().equals("No results found"))
            {
                return result.trim();
            }
            else
            {
                JSONArray jsonArray = new JSONArray(result.trim());
                JSONObject jsonObject;


                for(int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);

                    loginPojo.setStatus(jsonObject.getString("status"));
                    loginPojo.setEmail(jsonObject.getString("email"));
                    loginPojo.setFullName(jsonObject.getString("full_name"));
                    loginPojo.setUserType(jsonObject.getString("user_type"));
                    loginPojo.setDept(jsonObject.getString("department"));
                }

                httpURLConnection.disconnect();
                loginPojo.setLogLoginLink(params[4]);
                Logs logs = new Logs();


                logs.Log(loginPojo.getLogLoginLink(), params[5], params[6],
                        loginPojo.getFullName().concat(" (").concat(loginPojo.getEmail()).concat(")"),
                        loginPojo.getUserType(), loginPojo.getDept());

                return loginPojo.getStatus();
            }
        }
        catch(Exception me)
        {
            me.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(String result) {

        progressDialog.dismiss();

        if(type.equals("login"))
        {
            if(result == null || result.isEmpty())
            {
                Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("No results found"))
            {
                Toast.makeText(context,"Invalid account: email/password did not match or admin account. " +
                        "Please try again.", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("active"))
            {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("logEmail", email);
                intent.putExtra("logPw", pw);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startService(new Intent(context, NotificationService.class));
                context.startActivity(intent);
            }
            else
            {
                Toast.makeText(context,"Invalid account. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
        else if(type.equals("preLogging"))
        {

            if(result == null || result.isEmpty())
            {
                Toast.makeText(context, "Unable to connect to server", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
            else if(result.equals("No results found"))
            {
                Toast.makeText(context,"Invalid account: email/password did not match or admin account. " +
                        "Please try again.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
            else if(result.equals("active"))
            {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("logEmail", email);
                intent.putExtra("logPw", pw);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startService(new Intent(context, NotificationService.class));
                context.startActivity(intent);
            }
            else
            {
                Toast.makeText(context,"Invalid account. Please try again.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
