package com.sarkarinaukri.helperClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sarkarinaukri.model.PostData;
import com.sarkarinaukri.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PostSaverActivity extends Activity {

    private String url, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_saver);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        savePost();
        finish();

    }


    private void savePost(){

        Type listOfObjects = new TypeToken<List<PostData>>(){}.getType();
        ArrayList<PostData> retrievedList;

        SharedPreferences myPrefs = getSharedPreferences(Constant.SAVED_POSTS_PREF_NAME, Context.MODE_PRIVATE);
        String json = myPrefs.getString("PostDatasList", "");
        Gson gson = new Gson();
        retrievedList = gson.fromJson(json, listOfObjects);


        if(retrievedList!=null) {

            boolean PostDataAlreadyPresent = false;
            for (int i = 0; i < retrievedList.size(); i++) {
                if(retrievedList.get(i).getPostUrl().equals(url)){
                    PostDataAlreadyPresent = true;
                    break;
                }
            }

            if(PostDataAlreadyPresent){
                Toast.makeText(getApplicationContext(),"Already Saved!", Toast.LENGTH_SHORT).show();
            }else {
                retrievedList.add(new PostData(title, url));
                String strObject = gson.toJson(retrievedList, listOfObjects);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString("PostDatasList", strObject);
                prefsEditor.apply();
                Toast.makeText(getApplicationContext(),"Saved Successfully!", Toast.LENGTH_SHORT).show();
            }

        }
        else {

            List<PostData> newlist = new ArrayList<PostData>();
            newlist.add(new PostData(title, url));

            String strObject = gson.toJson(newlist, listOfObjects);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putString("PostDatasList", strObject);
            prefsEditor.apply();
            Toast.makeText(getApplicationContext(),"Saved Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

}
