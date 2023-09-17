package com.example.whatsopwebandorid.api;

import android.content.Context;

import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.api.WebClasses.*;

import okhttp3.ResponseBody;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatsApi {
    Retrofit retrofit;
    WebServiceApi webServiceApi;

    static final RuntimeException CommunicationError = new RuntimeException("Communication Error");

    public ChatsApi(Context appContext) {
        retrofit = new Retrofit.Builder().baseUrl(Tools.getApiConnectionString(appContext))
                .addConverterFactory(GsonConverterFactory.create()).build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public CompletableFuture<resAddChat> newContact(String token, String username) {
        reqAddChat newChat = new reqAddChat(username);
        Call<resAddChat> call = webServiceApi.addChat("bearer " + token, newChat);
        CompletableFuture<resAddChat> future = new CompletableFuture<>();

        call.enqueue(new Callback<resAddChat>() {
            @Override
            public void onResponse(Call<resAddChat> call, Response<resAddChat> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<resAddChat> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }

    public CompletableFuture<List<resChat>> getChats(String token) throws RuntimeException {
        CompletableFuture<List<resChat>> future = new CompletableFuture<>();
        Call<List<resChat>> call = webServiceApi.getChats("bearer " + token);
        call.enqueue(new Callback<List<resChat>>() {
            @Override
            public void onResponse(Call<List<resChat>> call, Response<List<resChat>> response)
            throws RuntimeException {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<resChat>> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> delete(String token, int id) {
        Call<ResponseBody> call = webServiceApi.deleteChat("bearer " + token,
                id);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    future.complete(true);
                else
                    future.complete(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                future.complete(false);
            }
        });
        return future;
    }

    public CompletableFuture<List<resMessage>> getChatMessages(String token, int chatId) {
        CompletableFuture<List<resMessage>> future = new CompletableFuture<>();
        Call<List<resMessage>> call = webServiceApi.getChatMessages("bearer " + token, chatId);
        call.enqueue(new Callback<List<resMessage>>() {
            @Override
            public void onResponse(Call<List<resMessage>> call, Response<List<resMessage>> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<resMessage>> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }

    public CompletableFuture<resSendMessage> sendMessage(String token, int chatId, String message) {
        CompletableFuture<resSendMessage> future = new CompletableFuture<>();
        reqMessageContent reqMessageContent = new reqMessageContent(message);
        Call<resSendMessage> call = webServiceApi.sendMessage("bearer " + token, chatId,
                reqMessageContent);
        call.enqueue(new Callback<resSendMessage>() {
            @Override
            public void onResponse(Call<resSendMessage> call, Response<resSendMessage> response) {
                if (response.isSuccessful()) {
                    future.complete(response.body());
                } else {
                    future.completeExceptionally(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<resSendMessage> call, Throwable t) {
                future.completeExceptionally(CommunicationError);
            }
        });
        return future;
    }
}
