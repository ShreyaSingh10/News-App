package com.example.user.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {//implements View.OnClickListener {
    NewsAdapter adapter;
    ListView lvList;
    String s;
    private TextView tvHarry=null,tvChetan =null;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";

    //SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHarry =(TextView)findViewById(R.id.tvHarry) ;
        tvChetan =(TextView)findViewById(R.id.tvChetan) ;
        lvList =(ListView)findViewById(R.id.list_items);
        adapter = new NewsAdapter(this);
                //adapter.clear();
                //s = tvSports.getText().toString();
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putString(Name,s);
                //editor.commit();
                //Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_LONG).show();
       /* sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            if(isNetworkConnected()) {
                NewsAsyncTask2 task = new NewsAsyncTask2();
                task.execute();
            }else{
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }*/
        //else{
            if(isNetworkConnected()) {
                NewsAsyncTask task = new NewsAsyncTask();
                task.execute();
            }else{
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        //}
        lvList.setAdapter(adapter);
       // tvChetan.setOnClickListener(this);
        //tvHarry.setOnClickListener(this);


        /*tvHarry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                s = tvHarry.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name,s);
                editor.commit();
                Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_LONG).show();
                if(isNetworkConnected()) {
                    NewsAsyncTask2 task = new NewsAsyncTask2();
                    task.execute();
                }else{
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lvList.setAdapter(adapter);*/
    }


    public boolean isNetworkConnected() { //check network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) //IMPORTANT as u can't call info.isConnected() if info is NULL. Will throw null point exception. So first check it's null or not
            return true;
        else
            return false;
    }

   /* @Override
    public void onClick(View v) {
        adapter.clear();
        switch(v.getId()){
            case R.id.tvHarry:
                s = tvHarry.getText().toString();
                break;
            case R.id.tvChetan:
                s = tvChetan.getText().toString();
                break;
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name,s);
        editor.commit();
        Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_LONG).show();
        if(isNetworkConnected()) {
            NewsAsyncTask2 task = new NewsAsyncTask2();
            task.execute();
        }else{
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        lvList.setAdapter(adapter);
    }*/


    //----------------------------------------------------------------------------------------------------------------------------
    private class NewsAsyncTask extends AsyncTask<Void, Void, List<News>> {
        @Override
        protected void onPreExecute() {
            //mNotFound.setVisibility(View.INVISIBLE);  //hide the message and display the progress bar
            //mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<News> doInBackground(Void... voids) {
            List<News> result =QueryUtils.fetchNewsData();//no preference this is for the first time
            return result;

        }

        @Override
        protected void onPostExecute(List<News> news) {
            // mLoader.setVisibility(View.INVISIBLE);   //hide the progress bar

            if (news == null) {
                //mNotFound.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "No results Found", Toast.LENGTH_SHORT).show();
            } else {
                //mNotFound.setVisibility(View.GONE);
                adapter.addAll(news);
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
   /* public String getText() {
        return sharedpreferences.getString(Name,"");
    }
    private class NewsAsyncTask2 extends AsyncTask<Void, Void, List<News>> {

        @Override
        protected void onPreExecute() {
            //mNotFound.setVisibility(View.INVISIBLE);  //hide the message and display the progress bar
           // mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<News> doInBackground(Void... voids) {
            List<News> result =QueryUtils2.fetchNewsData(getText());
            return result;

        }

        @Override
        protected void onPostExecute(List<News> books) {
          //  mLoader.setVisibility(View.INVISIBLE);   //hide the progress bar

           if (books == null) {
                //mNotFound.setVisibility(View.VISIBLE);
                //mNotFound.setText("No Results Found!");
               Toast.makeText(MainActivity.this, "No results Found", Toast.LENGTH_SHORT).show();
            } else {
                //mNotFound.setVisibility(View.GONE);
                adapter.addAll(books);
            }
        }
    }*/
    //---------------------------------------------------------------------------------------------------------------------------------------------------
}
