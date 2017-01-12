package com.hetrich.khanacademyproject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Data.BadgeInfo;

/**
 * Created by Bradley on 12/15/2015.
 * Okhttp code taken and modified from http://square.github.io/okhttp/
 */
public class MainActivity extends Activity {

    ListView badgeList;
    BadgeAdapter adapter;
    ArrayList<BadgeInfo> badgeInfoList;
    Typeface newFontBold, newFontRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set custom font for title
        newFontBold = Typeface.createFromAsset(getAssets(),"Fonts/Signika-Bold.ttf");
        TextView Title = (TextView)findViewById(R.id.TVTitle);
        Title.setTypeface(newFontBold);

        //set up the listview that will display the badges
        badgeList = (ListView)findViewById(R.id.listBadge);
        badgeInfoList = new ArrayList<BadgeInfo>();

        //will pass url through params
       new BadgesAnsycTask().execute("http://www.khanacademy.org/api/v1/badges");

    }

    //create AysncTask passes String which is the url, void, then boolean yes or no if success
    public class BadgesAnsycTask extends AsyncTask<String, Void, Boolean>
    {
        //runs on background thread not UI thread
        @Override
        protected Boolean doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            String jsonCode;

            Request request = new Request.Builder().url(params[0]).build();

            //if not valid url return null
                try {
                Response response = client.newCall(request).execute();

                   jsonCode =  response.body().string();
                    try {

                        //Json code is already an array no need for JsonObject
                        JSONArray jArray = new JSONArray(jsonCode);

//                        Toast.makeText(MainActivity.this, jArray + "",Toast.LENGTH_LONG).show();
                        //get each badge one by one
                        for(int i = 0; i < jArray.length(); i++)
                        {
                            BadgeInfo badgeInfo = new BadgeInfo();

                            //object with key value pairs
                            JSONObject jBadgeObject = jArray.getJSONObject(i);

                            //send data to BadgeInfo class
                            //change the placeholders later
                            badgeInfo.setName(jBadgeObject.getString("description"));
                            badgeInfo.setShortDescription(jBadgeObject.getString("safe_extended_description"));
                            badgeInfo.setDetailedDescription(jBadgeObject.getString("safe_extended_description"));
                            badgeInfo.setBadgeCount(i);


                            //get icon object
                                JSONObject jIconObj = jBadgeObject.getJSONObject("icons");
                                badgeInfo.setSmallImageUrl(jIconObj.getString("large"));
                               // badgeInfo.setLargeImageUrl(jIconObj.getString("large"));



                           // saved data into list
                            badgeInfoList.add(badgeInfo);
                        }

                        //pass to postexecute to check value in toast method
                       return true;
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }

                }   catch (IOException e){
                       e.printStackTrace();
                    }

               return false;
        }
        //onpost runs after doinBackground

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

           if(!success) {
               Log.d("STATUS", "Failed");
               Toast.makeText(MainActivity.this, "Could not retrieve data", Toast.LENGTH_SHORT).show();
           }
            else
            {
                //correct json code was retrieved
               Log.d("STATUS", "SUCCESS");
                //set adapter
                BadgeAdapter adapter = new BadgeAdapter(MainActivity.this, R.layout.row, badgeInfoList);

                badgeList.setAdapter(adapter);
            }

        }

    }






}
