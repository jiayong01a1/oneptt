package onerfid.com.oneptt.ui.pusktotalk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.api.LoginApiImpl;
import onerfid.com.oneptt.base.BaseActivity;
import onerfid.com.oneptt.bean.MqttServerBean;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.onehandlebean.HandleCommitReq;
import onerfid.com.oneptt.bean.onehandlebean.HandleCommitResponse;
import onerfid.com.oneptt.bean.onehandlebean.OneKeyBean;
import onerfid.com.oneptt.bean.onehandlebean.OrgBean;
import onerfid.com.oneptt.bean.onehandlebean.SimHandleReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgbean;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.MqttManager;
import onerfid.com.oneptt.event.MessageEvent;
import onerfid.com.oneptt.event.OrgNameEvent;
import onerfid.com.oneptt.util.GsonUtil;
import onerfid.com.oneptt.util.OneEleUtil;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.StringUtils;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.com.oneptt.widget.SpinerPopWindow;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiayong on 2018/4/24.
 */
public class OneHandleCollectActivity extends BaseActivity {
    @BindView(R.id.handle_toolbar)
    Toolbar handle_toolbar;
    @BindView(R.id.et_handle_community_name)
    TextView handle_community;
    @BindView(R.id.et_handle_name_select)
    EditText handle_name;
    @BindView(R.id.et_handle_number)
    EditText handle_number;
    @BindView(R.id.one_handle_sim_btn)
    Button handle_sim;
    @BindView(R.id.tv_handle_id_select)
    TextView handle_id;
    @BindView(R.id.one_handle_commit_btn)
    Button handler_commit;

    @BindView(R.id.ll_one_handle_e)
    RelativeLayout relativeLayout;

    @BindView(R.id.et_add_onekey_phone1)
    EditText add_onekey_phone1;
    @BindView(R.id.et_add_onekey_phone2)
    EditText add_onekey_phone2;
    @BindView(R.id.et_add_onekey_phone3)
    EditText add_onekey_phone3;
    @BindView(R.id.et_add_onekey_phone4)
    EditText add_onekey_phone4;
    @BindView(R.id.et_add_onekey_phone5)
    EditText add_onekey_phone5;

    private Context mContext;
    private SpinerPopWindow<String> mSpinerPopWindow;
    List<OrgBean> orgList;
    List<String> orgNames;
    private boolean conneted;
    private boolean isNull = false;
    private int orgId;
    private String handleNum;
    private String deviceId = null;
    private String phone1,phone2,phone3,phone4,phone5;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_handle_collect);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        btnClickAble();
        connectMQTT();
    }

    /**
     * 创建MQTT连接  订阅
     */
    private void connectMQTT() {
        String clientId = System.currentTimeMillis() + "";
        MqttServerBean bean = SharedPreferenceUtil.getInstance().getMqttMsg();
        conneted = MqttManager.getInstance().creatConnect(bean.serverAddress, bean.serverName, bean.serverPwd, clientId);
        Log.e("订阅f", "！！！" + conneted);
        if (conneted) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    MqttManager.getInstance().subscribe(Constant.ONE_HANDLE_COLLECT, 2);
                    MqttManager.getInstance().subscribe(Constant.ONE_HANDLE_CHECK, 2);
                    Log.e("订阅", "订阅成功！！！");
                }
            }).start();
        }
    }

    private void btnClickAble() {
        handle_sim.setClickable(false);
        handle_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        handler_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        handler_commit.setClickable(false);
        handle_number.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        add_onekey_phone1.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        add_onekey_phone2.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        add_onekey_phone3.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        add_onekey_phone4.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        add_onekey_phone5.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        handle_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        handle_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
                        handle_sim.setClickable(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    handle_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
                    handle_sim.setClickable(false);
                }
            }
        });
        add_onekey_phone2.setEnabled(false);
        add_onekey_phone3.setEnabled(false);
        add_onekey_phone4.setEnabled(false);
        add_onekey_phone5.setEnabled(false);
        add_onekey_phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                boolean  isDel = false;
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        handler_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
                        handler_commit.setClickable(true);
                        Drawable nav_up=getResources().getDrawable(R.mipmap.add_phone_ok);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        add_onekey_phone1.setCompoundDrawables(null, null, nav_up, null);
                        add_onekey_phone2.setEnabled(true);
                        phone1 = s.toString();
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    add_onekey_phone2.setEnabled(false);
                    handler_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
                    handler_commit.setClickable(false);
                    Drawable nav_up=getResources().getDrawable(R.mipmap.sanjiaoxing);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    add_onekey_phone1.setCompoundDrawables(null, null,nav_up, null);
                }
            }
        });

        add_onekey_phone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        Drawable nav_up=getResources().getDrawable(R.mipmap.add_phone_ok);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        add_onekey_phone2.setCompoundDrawables(null, null, nav_up, null);
                        phone2 = s.toString();
                        add_onekey_phone3.setEnabled(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    add_onekey_phone3.setEnabled(false);
                    Drawable nav_up=getResources().getDrawable(R.mipmap.sanjiaoxing);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    add_onekey_phone2.setCompoundDrawables(null, null,nav_up, null);
                }
            }
        });

        add_onekey_phone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        Drawable nav_up=getResources().getDrawable(R.mipmap.add_phone_ok);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        add_onekey_phone3.setCompoundDrawables(null, null, nav_up, null);
                        phone3 = s.toString();
                        add_onekey_phone4.setEnabled(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    add_onekey_phone4.setEnabled(false);
                    Drawable nav_up=getResources().getDrawable(R.mipmap.sanjiaoxing);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    add_onekey_phone3.setCompoundDrawables(null, null,nav_up, null);
                }
            }
        });

        add_onekey_phone4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        Drawable nav_up=getResources().getDrawable(R.mipmap.add_phone_ok);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        add_onekey_phone4.setCompoundDrawables(null, null, nav_up, null);
                        phone4 = s.toString();
                        add_onekey_phone5.setEnabled(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    add_onekey_phone5.setEnabled(false);
                    Drawable nav_up=getResources().getDrawable(R.mipmap.sanjiaoxing);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    add_onekey_phone4.setCompoundDrawables(null, null,nav_up, null);
                }
            }
        });

        add_onekey_phone5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    if(isMobileNO(s.toString())){
                        Drawable nav_up=getResources().getDrawable(R.mipmap.add_phone_ok);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        add_onekey_phone5.setCompoundDrawables(null, null, nav_up, null);
                        phone5 = s.toString();
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    Drawable nav_up=getResources().getDrawable(R.mipmap.sanjiaoxing);
                    nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                    add_onekey_phone5.setCompoundDrawables(null, null,nav_up, null);
                }
            }
        });

    }

    @OnClick(R.id.et_handle_community_name)
    public void onHandleClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        dataReset();
        btnClickAble();
        loadUseOrgList();
    }

    private void dataReset() {
        handle_number.setText("");
        handle_id.setText("");
    }

    @OnClick(R.id.et_handle_name_select)
    public void handleNumOnClick(){
       handle_name.setText("");
    }

    @OnClick(R.id.one_handle_sim_btn)
    public void onSimClick(){
        String number = handle_number.getText().toString().trim();
        if(number != null ){
            Observable<UserResponse> simObservable = LoginApiImpl.handleSim(new SimHandleReq(number));
            simObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserResponse>() {
                        @Override
                        public void onCompleted() {
                            createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg1)).show();
                        }
                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(UserResponse getIpInfoResponse) {
                        }
                    });
        }
    }

    @OnClick(R.id.one_handle_commit_btn)
    public void onCommitClick(){

        if(handleNum == null){
            ToastUtil.show(mContext,"主机名称为空",Toast.LENGTH_LONG);
        }else{
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        String str_number = checkPhone(phone1,phone2,phone3,phone4,phone5);
        Log.e("string_number",str_number);
        HandleCommitReq commitReq = new HandleCommitReq(SharedPreferenceUtil.getInstance().getUserResult().access_Token,
                                                        orgId,deviceId,handleNum,str_number.toString());
        Log.e("log",commitReq.toString());
        Observable<UserResponse<HandleCommitResponse>> submitObservable = LoginApiImpl.handleSubmit(commitReq);
        submitObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<HandleCommitResponse>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        ToastUtil.show(mContext,"连接失败",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(UserResponse<HandleCommitResponse> result) {
                        Log.e("resp",result.appendData.toString());

                            int resultType = result.resultType;
                            if(resultType ==1){
                                try {
                                    HandleCommitResponse response = result.appendData;
                                    Log.e("resp",response.toString());
                                    JSONObject publish = new JSONObject();
                                    if(StringUtils.isEmpty(response.mqttUser)||StringUtils.isEmpty( response.mqttPwd)){
                                        ToastUtil.show(mContext,"总机已绑定",Toast.LENGTH_LONG);
                                    }else{
                                        ToastUtil.show(mContext,result.message,Toast.LENGTH_LONG);
                                        publish.put("username", response.mqttUser);
                                        publish.put("password", response.mqttPwd);
                                        publish.put("time",response.serverTime);
                                        String s = publish.toString();
                                        int objToAscii = OneEleUtil.strToASCII(s.substring(1,s.length()-1));
                                        publish.put("checksum",objToAscii);
                                        Log.e("resp",publish.toString());
                                       MqttManager.getInstance().publish("/oneserv/" + response.deviceId + "/reg", 2, publish.toString().getBytes());
                                   }
                                } catch (JSONException e) {
                                  e.printStackTrace();
                                }
                            }else{
                                ToastUtil.show(mContext,result.message,Toast.LENGTH_LONG);
                            }


                    }
                });
        }
    }

    private String checkPhone(String phone1, String phone2, String phone3, String phone4, String phone5) {
        StringBuilder str = new StringBuilder();
        str.append(phone1);
        if(phone2 != null){
             str.append(",");
             str.append(phone2);
        }
        if(phone3 != null){
            str.append(",");
            str.append(phone3);
        }
        if(phone4 != null){
            str.append(",");
            str.append(phone4);
        }
        if(phone5 != null){
            str.append(",");
            str.append(phone5);
        }
        return str.toString();
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            handle_community.setText(orgList.get(position).orgName);
            handle_name.setText(orgList.get(position).orgName);
            orgId = orgList.get(position).orgId;
        }
    };

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
        }
    };

    /**
     *  加载小区数据
     * */
    private void loadUseOrgList() {
        String token = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        Log.e("token",token);
        Observable<UserResponse<UseOrgbean>> userObservable = LoginApiImpl.useOrgList(new UseOrgReq(token));
        userObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<UseOrgbean>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                        if(isNull){
                            ToastUtil.show(mContext,"暂无数据",Toast.LENGTH_LONG);
                        }else{
                            ToastUtil.show(mContext,"数据加载完成", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        ToastUtil.show(mContext,"数据加载失败",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(UserResponse<UseOrgbean> getIpInfoResponse) {
                        try {
                            UseOrgbean useOrgbean =getIpInfoResponse.appendData;
                            if(useOrgbean.total == 0){
                                isNull = true;
                            }else {
                                isNull = false;
                                orgList =  useOrgbean.orgList;
                                orgNames = new ArrayList<String>();
                                for(OrgBean bean : orgList){
                                    orgNames.add(bean.orgName);
                                }
                                mSpinerPopWindow = new SpinerPopWindow<String>(mContext, orgNames,itemClickListener);
                                mSpinerPopWindow.setOnDismissListener(dismissListener);
                                mSpinerPopWindow.setWidth(handle_community.getWidth());
                                mSpinerPopWindow.showAsDropDown(handle_community);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ThreadEvent(MessageEvent event){
        dismissLoading();
            if(event.message.equals("OK")&&deviceId.equals(event.deviceId)){
                finish();
            }
            if(event.type.equals("success")){
                Log.e("订阅接收到的话柄采集", event.toString() + "   " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())));
                Log.e("订阅接收到的话柄采集",handle_number.getText().toString());
                if (event.message.contains(handle_number.getText().toString())) {
                    OneKeyBean oneKeyBean = OneKeyBean.objectFromData(event.message);
                    handleNum = handle_name.getText().toString();
                    deviceId = oneKeyBean.getID();
                    handle_id.setText(oneKeyBean.getID());
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }


    }

    private void initToolbar() {
        handle_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }
        else return mobiles.matches(telRegex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MqttManager.getInstance().disConnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
