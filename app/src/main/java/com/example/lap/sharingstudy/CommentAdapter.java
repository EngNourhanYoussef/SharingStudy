package com.example.lap.sharingstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        super(context, 0, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.comment_list_item, parent, false);
        }

        Comment comment = getItem(position);
        TextView titleView =  listItemView.findViewById(R.id.comment_body);
        titleView.setText(comment.getComment());
        TextView titleView2 = listItemView.findViewById(R.id.comment_username);
        titleView2.setText(comment.getUsername());

        return listItemView;
    }
}
