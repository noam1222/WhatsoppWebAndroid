package com.example.whatsopwebandorid.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.whatsopwebandorid.Message;
import com.example.whatsopwebandorid.R;

import java.util.ArrayList;

public class MessagesListAdapter extends ArrayAdapter<Message> {
    LayoutInflater inflater;
    private String loggedUsername;

    public MessagesListAdapter(Context ctx, ArrayList<Message> userArrayList, String loggedUsername) {
        super(ctx, R.layout.message_item, userArrayList);

        this.inflater = LayoutInflater.from(ctx);
        this.loggedUsername = loggedUsername;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message msg = getItem(position);

        convertView = inflater.inflate(R.layout.message_item, parent, false);

        LinearLayout msgLayout = convertView.findViewById(R.id.messageLayout);
        TextView content = convertView.findViewById(R.id.messageContentTv);
        TextView time = convertView.findViewById(R.id.messageTimeTv);



        //check if incoming ot outcoming message
        if (msg.getSenderUsername() == null || this.loggedUsername.equals(msg.getSenderUsername())) {
            msgLayout.setBackgroundResource(R.color.outcoming);
        } else {
            msgLayout.setBackgroundResource(R.color.incoming);
            //attach to right
            LinearLayout parentMessageLayout = convertView.findViewById(R.id.parentMessageLayout);
            parentMessageLayout.setGravity(Gravity.RIGHT);
        }
        content.setText(msg.getContent() + "   ");
        time.setText(msg.getTime());

        return convertView;
    }
}

