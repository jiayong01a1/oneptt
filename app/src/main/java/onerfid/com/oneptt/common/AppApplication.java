package onerfid.com.oneptt.common;

import android.app.Application;
import android.content.Context;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/4/10.
 */
public class AppApplication extends Application {
    private static AppApplication application;
    private Context context;
    public AppApplication(){
    }

    public static AppApplication getInstance(){
        if(application == null){
            synchronized (AppApplication.class){
                if (application ==null){
                    application = new AppApplication();
                }
            }
        }
        return  application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SharedPreferenceUtil.initSharedPreferenceUtil(getApplicationContext());
    }
}
