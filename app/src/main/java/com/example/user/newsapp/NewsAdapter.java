package com.example.user.newsapp;

/**
 * Created by user on 6/21/2017.
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News>{


    public NewsAdapter(Context context) {
        super(context, -1);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView is the view which is supposed to be visible
        News news =getItem(position);
        if(convertView == null){
            //if there is no view and we are doing search for the first time we do this
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent,false);
            //we inflate the view with list_items.xml
        }
        TextView tvTitle =(TextView)convertView.findViewById(R.id.title);//initiatliste tvTitle and tvAuthor
        TextView tvSection =(TextView)convertView.findViewById(R.id.section);
        TextView tvInfo =(TextView)convertView.findViewById(R.id.info);
        tvTitle.setText(news.getTitle()); //getting books title from Books custom class
        tvSection.setText(news.getSection());//getting books author from Books custom class
        tvInfo.setText(news.getInfo());
        return convertView;//return the view after setting dez things up
    }
}
