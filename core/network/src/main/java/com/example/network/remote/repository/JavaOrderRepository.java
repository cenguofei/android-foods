package com.example.network.remote.repository;

import com.example.model.remoteModel.Order;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface JavaOrderApi {
    @Headers("Content-Type:application/json")
    @POST("frontorder/add")
    HashMap<String, Object> postOrder(@Body Order order);

    /**
     Retrofit的默认返回值类型为Call，当添加RxJavaCallAdapter时可以为Observable类型，或者Single、Maybe、Completable
     当函数是Kotlin挂起函数时
     if (isKotlinSuspendFunction) {
         Type[] parameterTypes = method.getGenericParameterTypes();
         Type responseType =
         Utils.getParameterLowerBound(
         0, (ParameterizedType) parameterTypes[parameterTypes.length - 1]);
         if (getRawType(responseType) == Response.class && responseType instanceof ParameterizedType) {
             // Unwrap the actual body type from Response<T>.
             responseType = Utils.getParameterUpperBound(0, (ParameterizedType) responseType);
             continuationWantsResponse = true;
         } else {
         }
         //Kotlin挂起函数Retrofit会默认对返回类型用Call将进行包裹，所以在写接口的时候可以不指定返回类型为Call，直接写需要的返回类型
         adapterType = new Utils.ParameterizedTypeImpl(null, Call.class, responseType);
         annotations = SkipCallbackExecutorImpl.ensurePresent(annotations);
     } else {
        //如果时Java或者非suspend函数，那么就不会用Call包裹，导致请求报错
        adapterType = method.getGenericReturnType();
     }


     private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
     Retrofit retrofit, Method method, Type returnType, Annotation[] annotations) {
         try {
         //noinspection unchecked
            return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
         } catch (RuntimeException e) { // Wide exception range because factories are user code.
            //在创建callAdapter时，如果找不到对应的Adapter就会抛出异常Unable to create call adapter for...
            throw methodError(method, e, "Unable to create call adapter for %s", returnType);
         }
     }
     */
    @GET("frontorder/queryOrderByUsername")
    Call<HashMap<String, Object>> getUserOrders(@Query("username") String username);
}

public class JavaOrderRepository {

    @Inject
    public JavaOrderRepository() {

    }

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.129.67.213:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final JavaOrderApi javaOrderApi = retrofit.create(JavaOrderApi.class);

    public void getUserOrders(String username,Callback<HashMap<String, Object>> callback) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
//                        HashMap<String, Object> userOrders = javaOrderApi.getUserOrders(username);
                        try {
                            Response<HashMap<String, Object>> execute = javaOrderApi.getUserOrders(username).execute();
                            HashMap<String, Object> body = execute.body();
                            callback.onResponse(body);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
    }

    public interface Callback<T> {

        void onResponse(T data);
    }
}