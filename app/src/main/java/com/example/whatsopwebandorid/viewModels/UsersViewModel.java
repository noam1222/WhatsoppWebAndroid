package com.example.whatsopwebandorid.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.repositories.UsersRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class UsersViewModel extends ViewModel {

    private static UsersViewModel usersViewModel;
    private UsersRepository repository;
    private LiveData<List<User>> users;

    public static UsersViewModel getInstance(Context context, String token) {
        if (usersViewModel == null) {
            usersViewModel = new UsersViewModel(context, token);
        }
        return usersViewModel;
    }

    public static UsersViewModel observe() {
        return usersViewModel;
    }

    private UsersViewModel(Context context, String token) {
        repository = new UsersRepository(context, token);
        users = repository.getAll();
    }

    public LiveData<List<User>> get() {
        return users;
    }

    public void updateChats() {
        new Thread(() -> {
            repository.updateChats();
        }).start();
    }

    public User get(int id) {
        return repository.get(id);
    }

    public CompletableFuture<User> getLoggedUser(String username) {
        return repository.getCurrentUserDetails(username);
    }

    public CompletableFuture<Boolean> add(String username) {
        return repository.add(username);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void clearDB() {
        usersViewModel = null;
        repository.clearLocalDB();
    }

    public void update(User user) {
        repository.update(user);
    }
}
