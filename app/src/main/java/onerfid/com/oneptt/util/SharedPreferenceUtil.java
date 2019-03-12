package onerfid.com.oneptt.util;

import android.content.Context;
import android.content.SharedPreferences;

import onerfid.com.oneptt.bean.MqttServerBean;
import onerfid.com.oneptt.bean.UserRequest;
import onerfid.com.oneptt.bean.UserResult;
import onerfid.com.oneptt.common.AppApplication;

/**
 * Created by jiayong on 2018/4/4.
 */
public class SharedPreferenceUtil {
    private static final String TAG = "SharedPreferenceUtil";
    public static final String KEY_USER = "key_user";
    public static final String KEY_USERRESULT = "key_user_result";
    public static final String LOGIN_AUTO = "login_auto";
    public static final String MQTT_ADDRESS = "server_address";
    private static Context mContext;
    private SharedPreferences preferences;
    private static SharedPreferenceUtil sharedPreferenceUtil;

    private SharedPreferenceUtil(Context context){
        preferences = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
    }

    public static synchronized void initSharedPreferenceUtil(Context context){
        mContext = context;
        if(sharedPreferenceUtil == null){
            sharedPreferenceUtil = new SharedPreferenceUtil(context);
        }
    }

    public static synchronized SharedPreferenceUtil getInstance(){
        if(sharedPreferenceUtil == null){
            synchronized (SharedPreferenceUtil.class){
                if(sharedPreferenceUtil == null){
                    sharedPreferenceUtil = new SharedPreferenceUtil(mContext);
                }
            }
        }
        return  sharedPreferenceUtil;
    }
    public synchronized  void putUser(UserRequest request){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER,GsonUtil.GsonString(request));
        editor.commit();
    }

    public synchronized  UserRequest getUser(){
        return GsonUtil.GsonToBean(preferences.getString(KEY_USER,null),UserRequest.class);
    }

    public synchronized  void putMqttMsg(MqttServerBean url){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MQTT_ADDRESS,GsonUtil.GsonString(url));
        editor.commit();
    }

    public synchronized  MqttServerBean getMqttMsg(){
       return GsonUtil.GsonToBean(preferences.getString(MQTT_ADDRESS,null),MqttServerBean.class);
    }

    public synchronized void putUserResult(UserResult user){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERRESULT,GsonUtil.GsonString(user));
        editor.commit();
    }

    public synchronized  UserResult getUserResult(){
        return  GsonUtil.GsonToBean(preferences.getString(KEY_USERRESULT,null),UserResult.class);
    }

    public synchronized void Login(Boolean login){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGIN_AUTO,login);
        editor.commit();
    }

    public synchronized  Boolean isLogin(){
        return preferences.getBoolean(LOGIN_AUTO,false);
    }

    public synchronized void clear(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }







}
