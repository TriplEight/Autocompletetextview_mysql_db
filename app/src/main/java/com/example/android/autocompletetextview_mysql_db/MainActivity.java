package com.example.android.autocompletetextview_mysql_db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    InputStream is=null;
    String result=null;
    String line=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.0.2.2/text.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("Pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
            Log.e("Pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {
            JSONArray JA=new JSONArray(result);
            JSONObject json= null;
            final String[] str1 = new String[JA.length()];

            for(int i=0;i<JA.length();i++)
            {
                json=JA.getJSONObject(i);
                str1[i]=json.getString("name");
            }

            final AutoCompleteTextView text = (AutoCompleteTextView)
                    findViewById(R.id.autoCompleteTextView1);
            final List<String> list = new ArrayList<String>();

            for(int i=0;i<str1.length;i++)
            {
                list.add(str1[i]);
            }

            Collections.sort(list);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            text.setThreshold(1);
            text.setAdapter(dataAdapter);

            text.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getBaseContext(), list.get(arg2).toString(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
