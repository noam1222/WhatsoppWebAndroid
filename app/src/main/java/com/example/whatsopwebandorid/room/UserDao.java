package com.example.whatsopwebandorid.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsopwebandorid.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE id = :id")
    User get(int id);

    @Insert
    void Insert(User... users);

    @Update
    void Update(User... users);

    @Delete
    void Delete(User... users);

    @Query("DELETE FROM user")
    void deleteAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);
}
