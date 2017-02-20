package com.ellomix.android.ellomix.ServerAPI;

import com.ellomix.android.ellomix.ServerAPI.ServerModels.ServerChat;
import com.ellomix.android.ellomix.ServerAPI.ServerModels.ServerUser;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jhurt on 2/16/17.
 */

public class Server {
    // http://stackoverflow.com/questions/21398598/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request
    private static final String API_URL = "https://damp-mountain-40418.herokuapp.com/api/";

    public interface ServerAPI {
        // TODO(jhurt): Basic Auth is so insecure. Switch to session based authentication ASAP!
        @Headers("Authorization: Basic YWRtaW46ZWxsb21peGRldg==")
        @GET("chat/")
        Call<List<ServerChat>> getChats();

        @Headers("Authorization: Basic YWRtaW46ZWxsb21peGRldg==")
        @POST("chat/joinchat/")
        Call<ResponseBody> joinChat(@Body RequestBody data);

        @Headers("Authorization: Basic YWRtaW46ZWxsb21peGRldg==")
        @PATCH("chat/{id}/")
        Call<ServerChat> updateMessage(@Path("id") String chatId, @Body RequestBody data);

        @Headers("Authorization: Basic YWRtaW46ZWxsb21peGRldg==")
        @GET("user/")
        Call<List<ServerUser>> getUsers();

    }

    private static final ServerAPI SERVICE = new Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServerAPI.class);

    public static ServerAPI getService() {
        return SERVICE;
    }
}
