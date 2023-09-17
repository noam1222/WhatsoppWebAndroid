package com.example.whatsopwebandorid.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.whatsopwebandorid.Message;

import java.util.List;

@Dao
public interface MessagesDao {
    @Query("SELECT * FROM roomMessages WHERE id = :id")
    roomMessages getMessagesByChatId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewMessageList(roomMessages roomMessages);

    @Query("DELETE FROM roomMessages")
    void deleteAll();

    @Query("DELETE FROM roomMessages WHERE id = :id")
    void deleteChatMessages(int id);

}

