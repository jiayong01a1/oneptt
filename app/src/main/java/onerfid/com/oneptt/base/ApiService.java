package onerfid.com.oneptt.base;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jiayong on 2018/4/10.
 */
public class ApiService {
    public static final String TAG = "ApiService";
    public static final String BASE_URL = "http://www.onedt.com.cn/";
//    public static final String BASE_URL = "http://221.176.156.231:8081/"; //开封服务器地址
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
//    public static final Retrofit retrofitA = new Retrofit.Builder()
//            .baseUrl(BASE_URLA)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .build();
}
