package com.example.whatsopwebandorid.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.api.UsersApi;
import com.example.whatsopwebandorid.api.WebClasses.resAddChat;
import com.example.whatsopwebandorid.api.WebClasses.resChat;
import com.example.whatsopwebandorid.api.WebClasses.resUser;
import com.example.whatsopwebandorid.room.UserDB;
import com.example.whatsopwebandorid.room.UserDao;
import com.example.whatsopwebandorid.api.ChatsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class UsersRepository {
    private UserDao userDao;
    private UserListData userListData;

    private ChatsApi chatsApi;

    private UsersApi usersApi;

    private final Context context;
    private final String token;

    public UsersRepository(Context context, String token) {
        UserDB db = Room.databaseBuilder(context, UserDB.class, "UserDB")
                .allowMainThreadQueries().build();
        userDao = db.UserDao();
        userListData = new UserListData();
        this.chatsApi = new ChatsApi(context);
        this.usersApi = new UsersApi(context);
        this.context = context;
        this.token = token;
    }

    class UserListData extends MutableLiveData<List<User>> {
        public UserListData() {
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() throws RuntimeException {
            super.onActive();

            new Thread(() -> {
                userListData.postValue(userDao.index()); //post local data
                updateChats();
            }).start();
        }
    }

    public void updateChats() {
        //fetch remote data
        CompletableFuture<List<resChat>> future = chatsApi.getChats(token)
                .thenApply(chats -> chats)
                .exceptionally(e -> {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
        future.thenAccept(chats -> {
            if (chats != null) {
                List<User> users = User.UsersFromChats(chats);
                userListData.postValue(users);
                userDao.deleteAll();
                userDao.insertAll(users);
            }
        });
    }

    public LiveData<List<User>> getAll() {
        return userListData;
    }
    public CompletableFuture<User> getCurrentUserDetails(String username) {
        CompletableFuture<User> retFuture = new CompletableFuture<>();
        CompletableFuture<resUser> future = usersApi.getUserDetails(token, username)
                .thenApply(resUser -> resUser)
                .exceptionally(e -> {
                    retFuture.completeExceptionally(e);
                    return null;
                });
        future.thenAccept(resUser -> {
            if (resUser != null) {
                User loggedUser = new User(-1, resUser.getDisplayName(),
                        Tools.extractImageViewBase64(resUser.getPhoto()),
                        null, null);
                retFuture.complete(loggedUser);
            }
        });
        return retFuture;
    }

    public User get(int id) {
        return userDao.get(id);
    }

    public CompletableFuture<Boolean> add(String username) {
        CompletableFuture<Boolean> retFuture = new CompletableFuture<>();
        CompletableFuture<resAddChat> future = chatsApi.newContact(token, username)
                .thenApply(resAddChat -> resAddChat)
                .exceptionally(e -> {
                    retFuture.completeExceptionally(e);
                    return null;
                });

        future.thenAccept(results -> {
            if (results != null) {
                retFuture.complete(Boolean.TRUE);
            } else {
                retFuture.complete(Boolean.FALSE);
            }
        });

        return retFuture;
    }


    public void delete(final User user) {
        new Thread(() -> {
            userDao.Delete(user);
            userListData.postValue(userDao.index());
            CompletableFuture<Boolean> future = chatsApi.delete(token, user.getId())
                    .thenApply(result -> result)
                    .exceptionally(e -> false);
            future.thenAccept(result -> {
                if (!result) {
                    userDao.Insert(user);
                    userListData.postValue(userDao.index());
                    Toast.makeText(context, "Error in delete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Delete success!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    public void clearLocalDB() {
        userDao.deleteAll();
    }

    public void update(final User user) {
        userDao.Update(user);
    }
}
