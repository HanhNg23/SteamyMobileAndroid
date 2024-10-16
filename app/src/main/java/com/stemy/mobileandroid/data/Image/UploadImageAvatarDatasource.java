package com.stemy.mobileandroid.data.Image;

import android.annotation.SuppressLint;
import android.util.Log;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.DefaultUpload;
import com.apollographql.apollo.api.FileUpload;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx3.Rx3Apollo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stemy.mobileandroid.UpdateAvatarMutation;
import com.stemy.mobileandroid.apiclient.graphql.GraphQLClient;
import com.stemy.mobileandroid.data.model.Result;
import com.stemy.mobileandroid.data.model.TokenManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lombok.val;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

@Singleton
public class UploadImageAvatarDatasource {

    private TokenManager tokenManager;
    private GraphQLClient graphQLClient;
    private final OkHttpClient httpClient;

    @Inject
    public UploadImageAvatarDatasource(TokenManager tokenManager, GraphQLClient graphQLClient){
        this.tokenManager = tokenManager;
        this.graphQLClient = graphQLClient;
        this.httpClient = new OkHttpClient();
    }

    @SuppressLint("CheckResult")
    public Single<Result<String>> getImageAvatar(File fileImage) {
        return Single.create(emitter -> {
            Log.d("UploadImageAvatarDatasource", "Updating staff image");

            try {
                if (fileImage == null) {
                    emitter.onSuccess(new Result.Success<>(""));
                    return;
                }

                Log.d("UploadImageAvatarDatasource", "name: " + fileImage.getName());
                Log.d("UploadImageAvatarDatasource", "content length: " + fileImage.length());

                // Tạo payload cho GraphQL multipart request
                String operations = "{\"query\":\"mutation UpdateAvatar($image: File!) {\\n  updateAvatar(image: $image) {\\n    avatar\\n  }\\n}\",\"operationName\":\"UpdateAvatar\",\"variables\":{\"image\":null}}";

                // Tạo map
                String map = "{\"0\":[\"variables.image\"]}";

                // Tạo request body
                MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("operations", operations)
                        .addFormDataPart("map", map)
                        .addFormDataPart("0", fileImage.getName(),
                                RequestBody.create(fileImage,
                                        MediaType.parse("image/*")

                                ));

                Request request = new Request.Builder()
                        .url("https://stemyb.thanhf.dev/graphql")
                        .post(requestBodyBuilder.build())
                        .addHeader("Authorization", tokenManager.getToken())
                        .build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            // Xử lý response thành công
                            String responseData = response.body().string();
                            Log.d("UploadImageAvatarDatasource", "Response: " + responseData);

                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);

                            String avatarUrl = jsonResponse.getAsJsonObject("data")
                                    .getAsJsonObject("updateAvatar")
                                    .get("avatar").getAsString();
                            Log.d("UploadImageAvatarDatasource", "AVATAR URL: " + avatarUrl);

                            emitter.onSuccess(new Result.Success<>(avatarUrl));
                        } else {
                            emitter.onError(new IOException("Error in response: " + response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }
                });

            } catch (Exception e) {
                Log.e("UploadImageAvatarDatasource", "Exception while uploading avatar image: ", e);
                emitter.onError(new IOException("Error updating image of account", e));
            }
        });
    }

}
