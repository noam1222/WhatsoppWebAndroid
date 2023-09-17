package com.example.whatsopwebandorid.api;


import android.content.Context;
import android.widget.Toast;

import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.api.WebClasses.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


public class UsersApi {

    Retrofit retrofit;
    WebServiceApi webServiceApi;

    Context ctx;

    static final Exception CommunicationError = new Exception("Unknown Error");


    public UsersApi(Context appContext) {
        ctx = appContext;
        retrofit = new Retrofit.Builder().baseUrl(Tools.getApiConnectionString(appContext))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public CompletableFuture<resUser> createUser(String username, String displayName, String password, String photo) {
        reqRegister newUser = new reqRegister(username, displayName, password, photo);
        Call<resUser> call = webServiceApi.signUp(newUser);
        CompletableFuture<resUser> future = new CompletableFuture<>();
        call.enqueue(new Callback<resUser>() {
            @Override
            public void onResponse(Call<resUser> call, Response<resUser> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }
            @Override
            public void onFailure(Call<resUser> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }

    public void sendFirebaseToken(String username, String token) {
        UsernameFirebaseToken usernameFirebaseToken = new UsernameFirebaseToken(username, token);
        Call<ResponseBody> call = webServiceApi.setFirebaseToken(usernameFirebaseToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ctx, "Error: real time data",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Error: real time data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public CompletableFuture<String> createToken(String username, String password) {
        reqUserPass userPass = new reqUserPass(username, password);
        Call<ResponseBody> call = webServiceApi.getToken(userPass);
        CompletableFuture<String> future = new CompletableFuture<>();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful())
                        future.complete(response.body().string());
                    else
                        future.completeExceptionally(new Exception("Bad Details"));
                } catch (IOException e) {
                    future.completeExceptionally(new Exception("Error"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }

    public CompletableFuture<resUser> getUserDetails(String token, String username) {
        Call<resUser> call = webServiceApi.getUserDetails("bearer " + token, username);
        CompletableFuture<resUser> future = new CompletableFuture<>();
        call.enqueue(new Callback<resUser>() {
            @Override
            public void onResponse(Call<resUser> call, Response<resUser> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<resUser> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }
}
