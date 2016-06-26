package com.linhtran.vnua.readxml_rss_demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadXMl().execute("http://vnexpress.net/rss/thoi-su.rss");
            }
        });
    }


    private  class ReadXMl extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String nodung=getXMLFromUrl(strings[0]);
            return nodung;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMPaser paser=new XMLDOMPaser();
            Document document=paser.getDocument(s);
            NodeList nodeList=document.getElementsByTagName("item");
            String kp="";
            for(int i=0;i<nodeList.getLength();i++)
            {
                Element element = (Element)nodeList.item(i);
                kp+=paser.getValue(element,"title");
            }
            Toast.makeText(MainActivity.this,kp,Toast.LENGTH_LONG).show();
        }
    }
    private String getXMLFromUrl(String urlstring)
    {
        String xml=null;
        try
        {
            DefaultHttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(urlstring);
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            xml= EntityUtils.toString(entity, HTTP.UTF_8);
        }catch (ClientProtocolException e)
        {
            if(e!=null)
            {
                Log.e("LOi",e.toString());
            }
        }
        catch (IOException e)
        {
            if(e!=null)
            {
                Log.e("LOi",e.toString());
            }
        }
        return xml;
    }
}
