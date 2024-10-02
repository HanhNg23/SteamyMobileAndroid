package com.stemy.mobileandroid.apiclient;

import android.util.Log;

import com.apollographql.apollo.api.ApolloRequest;
import com.apollographql.apollo.api.Operation;
import com.apollographql.java.client.ApolloCallback;
import com.apollographql.java.client.ApolloClient;
import com.apollographql.java.client.interceptor.ApolloInterceptor;
import com.apollographql.java.client.interceptor.ApolloInterceptorChain;
import com.apollographql.java.client.network.http.HttpEngine;
import com.apollographql.java.client.network.http.HttpInterceptor;
import com.stemy.mobileandroid.data.model.TokenManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class GraphQLClient {

    private static final String BASE_URL = "https://stemyb.thanhf.dev/graphql";
    private static ApolloClient apolloClient;
    private static TokenManager tokenManager;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_BEARER_TYPE = "Bearer ";
    private static final String AUTHORIZATION_NO_TYPE_TOKEN_ONLY = "";

    @Inject
    public GraphQLClient(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    public GraphQLClient(){

    }
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String token = tokenManager.getToken();
                    Request original = chain.request();
                    // Thêm token vào header nếu có
                    Request.Builder requestBuilder = original.newBuilder();
                    if (token != null) {
                        requestBuilder.header(AUTHORIZATION_HEADER, AUTHORIZATION_NO_TYPE_TOKEN_ONLY + token);
                    }
                    Request request = requestBuilder
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            }).build();

    private HttpInterceptor httpInterceptor = (request, chain, callback) -> {
        String token = tokenManager.getToken();
        if(token != null){
            request = request.newBuilder().addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_NO_TYPE_TOKEN_ONLY + token).build();
        }
        chain.proceed(request, callback);
    };

    private ApolloInterceptor apolloInterceptor = new ApolloInterceptor() {
        @Override
        public <D extends Operation.Data> void intercept(@NotNull ApolloRequest<D> request, @NotNull ApolloInterceptorChain chain, @NotNull ApolloCallback<D> callback) {
            System.out.println("Executing operation: " + request.getOperation().name());

            // Kiểm tra trạng thái kết nối Internet
            boolean isConnected = isInternetAvailable();
            Log.d("CONNECT INTERNET STATUS: ", String.valueOf(isConnected));

            if (!isConnected) {
                // Nếu không có kết nối Internet, gửi lỗi
                //callback.onResponse("NO INTERNET CONNECTION");
                System.out.println("NO INTERNET CONNECTION ");
                return; // Dừng lại không tiếp tục thực hiện yêu cầu
            }

            // Nếu có kết nối, tiếp tục thực hiện yêu cầu
            chain.proceed(request, callback);
        }
    };

    public ApolloClient getClient() {
        if (apolloClient == null) {
            apolloClient = new ApolloClient.Builder()
                    .serverUrl(BASE_URL)
                    .addInterceptor(apolloInterceptor)
                    .addHttpInterceptor(httpInterceptor)
                    //.httpEngine((HttpEngine) okHttpClient)  // Thêm OkHttpEngine với OkHttpClient vào ApolloClient
                    .build();
        }
        return apolloClient;
    }

    public boolean isInternetAvailable() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // Trả về true nếu nhận được phản hồi thành công
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
}
