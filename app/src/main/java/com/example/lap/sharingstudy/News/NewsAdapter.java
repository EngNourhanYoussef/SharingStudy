package com.example.lap.sharingstudy.News;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lap.sharingstudy.R;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }


    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        News news = getItem(position);
        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        titleView.setText(news.getTitle());
        TextView sectionView = (TextView) listItemView.findViewById(R.id.news_section);
        sectionView.setText(R.string.news_category + news.getSection());
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(R.string.news_contributors + news.getAuthor());

        return listItemView;
    }
}
