package com.example.whatsopwebandorid.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.User;

import java.util.ArrayList;

public class ChatsListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private LayoutInflater inflater;

    public ChatsListAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.chat_item, users);
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = users.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.profile_image);
        TextView userName = convertView.findViewById(R.id.user_name);
        TextView lastMsg = convertView.findViewById(R.id.last_massage);
        TextView time = convertView.findViewById(R.id.time);

        // Decode the Base64 string to ImageView
        byte[] decodedBytes = Base64.decode(user.getProfilePic(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        imageView.setImageBitmap(bitmap);

        userName.setText(user.getDisplayName());
        lastMsg.setText(user.getLastMassage());
        time.setText(user.getLastMassageSendingTime());

        return convertView;
    }
}
