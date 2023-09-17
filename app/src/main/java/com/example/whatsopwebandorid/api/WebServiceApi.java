package com.example.whatsopwebandorid.api;

import com.example.whatsopwebandorid.api.WebClasses.*;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {
    @POST("Users")
    Call<resUser> signUp(@Body reqRegister newUser);

    @GET("Users/{username}")
    Call<resUser> getUserDetails(@Header("Authorization") String authorization,
                                 @Path("username") String username);

    @POST("Tokens")
    Call<ResponseBody> getToken(@Body reqUserPass userPass);

    @POST("Tokens/fire_token")
    Call<ResponseBody> setFirebaseToken(@Body UsernameFirebaseToken usernameFirebaseToken);
  
    @GET("Chats")
    Call<List<resChat>> getChats(@Header("Authorization") String authorization);

    @POST("Chats")
    Call<resAddChat> addChat(@Header("Authorization") String authorization, @Body reqAddChat newChat);

    @DELETE("Chats/{id}")
    Call<ResponseBody> deleteChat(@Header("Authorization") String authorization, @Path("id") int id);

    @GET("Chats/{id}/Messages")
    Call<List<resMessage>> getChatMessages(@Header("Authorization") String authorization,
                                           @Path("id") int chatId);

    @POST("Chats/{id}/Messages")
    Call<resSendMessage> sendMessage(@Header("Authorization") String authorization,
                                     @Path("id") int chatId, @Body reqMessageContent msg);
}
