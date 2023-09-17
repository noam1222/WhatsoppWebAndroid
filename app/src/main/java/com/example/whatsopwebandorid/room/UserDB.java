package com.example.whatsopwebandorid.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.whatsopwebandorid.User;

@Database(entities = {User.class, roomMessages.class}, version = 8)
public abstract class UserDB extends RoomDatabase  {
    public abstract UserDao UserDao();
    public abstract MessagesDao MessagesDao();
}
