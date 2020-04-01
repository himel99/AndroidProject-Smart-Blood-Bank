package com.example.project;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fetchdata extends AsyncTask {

    String line="";
    String data = "";
    String sinlgleparsed="";
    String dataparsed="";

    @Override
    protected Void doInBackground(Object... objects) {

        try {
            URL url = new URL("https://api.myjson.com/bins/nttlc");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (line!=null)
            {
                line = bufferedReader.readLine();
                data = data+line;
            }

            JSONArray JA  =new JSONArray(data);
            for (int i = 0; i<JA.length();i++)
            {
                JSONObject JO = (JSONObject) JA.get(i);
                sinlgleparsed = "Name:  "+JO.get("name")+"\n\n"+
                        "Roll:  "+JO.get("roll")+"\n\n"+
                        "Section:  "+JO.get("Section")+"\n\n"+
                        "Dept:  "+JO.get("dept")+"\n\n"+
                        "University:  "+JO.get("university")+"\n\n";


                dataparsed = dataparsed+sinlgleparsed+"\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        JsonActivity.textView.setText(dataparsed);
        super.onPostExecute(o);
    }
}
