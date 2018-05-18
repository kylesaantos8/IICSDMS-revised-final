package com.iicsdms.tris.iicsdms2;

import android.util.Log;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tris on 14/03/2018.
 */

class SeenStatus {

    String line, result;
    InputStream inputStream;
    private JSONArray jsonArray;
    private List<Map<String,String>> file = new ArrayList<Map<String, String>>();

    private static final String test = "Test";

    String seen(String linkSeen, String mailId, String logEmail)
    {
        try
        {
            URL url = new URL(linkSeen);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(mailId.trim(),"UTF-8") +"&"+
                    URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(logEmail.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return line;
    }

    void seenUpdate(String linkSeen, String id, String update, String logEmail)
    {
        try
        {
            URL url = new URL(linkSeen);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("update", "UTF-8")+"="+URLEncoder.encode(update.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("mail", "UTF-8")+"="+URLEncoder.encode(logEmail.trim(),"UTF-8");
            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();


            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void seenUpdate(String linkSeen, String mailId, String update, String timeStamp, String logEmail)
    {
//        try
//        {
//            URL url = new URL(linkSeen);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//
//            OutputStream outputStream = httpURLConnection.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//
//            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(mailId.trim(),"UTF-8")+
//                    "&"+URLEncoder.encode("update", "UTF-8")+"="+URLEncoder.encode(update.trim(),"UTF-8")+
//                    "&"+URLEncoder.encode("time", "UTF-8")+"="+URLEncoder.encode(timeStamp.trim(),"UTF-8")+
//                    "&"+URLEncoder.encode("logEmail", "UTF-8")+"="+URLEncoder.encode(logEmail.trim(),"UTF-8");
//            bufferedWriter.write(data1);
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            outputStream.close();
//
//            inputStream = httpURLConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//
//            line = bufferedReader.readLine();
//            bufferedReader.close();
//            inputStream.close();
//
//
//            httpURLConnection.disconnect();
//
//        } catch(Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    void response(String link, String id, String email, String response, String timeStamp)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("status", "UTF-8")+"="+URLEncoder.encode(response.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("date", "UTF-8")+"="+URLEncoder.encode(timeStamp.trim(),"UTF-8");
            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();


            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    String responseSeen(String link, String id, String email)
    {
        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id.trim(),"UTF-8")+
                    "&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8");
            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            line = bufferedReader.readLine();
            bufferedReader.close();
            inputStream.close();


            httpURLConnection.disconnect();

            return line;
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    List getNotif(String link, String notifId)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(notifId.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setNotifEmail(jsonObject.getString("email"));

                    fileMap.put("notifEmail", notifPojo.getNotifEmail());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    List getNotifName(String link, String email)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return file;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Map<String,String> fileMap = new HashMap<>();


                    jsonObject = jsonArray.getJSONObject(i);

                    notifPojo = new NotifPojo();
                    notifPojo.setFullName(jsonObject.getString("full_name"));

                    fileMap.put("fullName", notifPojo.getFullName());

                    file.add(fileMap);
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

    String getDept(String link, String email)
    {
        NotifPojo notifPojo;

        try
        {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String data1 = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email.trim(),"UTF-8");

            bufferedWriter.write(data1);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();

            result = stringBuilder.toString();

            if(result.trim().isEmpty() || result.trim().equals("No Results found"))
            {
                return result;
            }
            else
            {
                jsonArray = new JSONArray(result);
                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++)
                {
//                    Map<String,String> fileMap = new HashMap<>();
//
//
                    jsonObject = jsonArray.getJSONObject(i);
//
//                    notifPojo = new NotifPojo();
//                    notifPojo.setDept(jsonObject.getString("department"));
//
//                    fileMap.put("dept", notifPojo.getDept());
//
//                    file.add(fileMap);
                    result = jsonObject.getString("department");
                }
            }

            httpURLConnection.disconnect();

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
