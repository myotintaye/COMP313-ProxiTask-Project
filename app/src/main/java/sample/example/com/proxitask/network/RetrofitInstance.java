package sample.example.com.proxitask.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sample.example.com.proxitask.activity.Utils;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://proxi-task.herokuapp.com/api/";
    private static String idToken;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

            if(idToken != null && !Utils.isEmpty(idToken)) {
                httpClientBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("idToken", idToken).build();
                        Log.d("HTTP CLIENT", idToken);
                        return chain.proceed(request);
                    }
                });
            }
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("TEST", "TEST 123").build();
                    Log.d("HTTP CLIENT",
                            "testing");
                    return chain.proceed(request);
                }
            });




            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);

                    boolean unAuthorized = (response.code() == 401);
                    if (unAuthorized) {
                        throw new AuthorizedException();
                    }
                    return response;
                }
            });

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        }
        return retrofit;
    }

    public static void setToken(String token){
        idToken = token;
    }
}
