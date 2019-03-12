package onerfid.com.oneptt.ui.pusktotalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
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
import onerfid.com.oneptt.bean.onehandlebean.OneKeyBean;
import onerfid.com.oneptt.bean.onehandlebean.OrgBean;
import onerfid.com.oneptt.bean.onehandlebean.SimHandleReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgbean;
import onerfid.com.oneptt.bean.onehandlechange.ChangeResultReq;
import onerfid.com.oneptt.bean.onehandlechange.ChangeResultResp;
import onerfid.com.oneptt.bean.onehandlechange.HandleChangeBean;
import onerfid.com.oneptt.bean.onehandlechange.HandleChangeReq;
import onerfid.com.oneptt.bean.onehandlechange.OneHandleOutput;
import onerfid.com.oneptt.bean.onekeybean.OneHandleBean;
import onerfid.com.oneptt.bean.onekeybean.OneHandleReq;
import onerfid.com.oneptt.bean.onekeybean.OneHandles;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.MqttManager;
import onerfid.com.oneptt.event.MessageEvent;
import onerfid.com.oneptt.util.GsonUtil;
import onerfid.com.oneptt.util.OneEleUtil;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.com.oneptt.widget.SpinerPopWindow;
import onerfid.com.oneptt.widget.TimeButton;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static onerfid.com.oneptt.ui.pusktotalk.OneHandleCollectActivity.isMobileNO;

/**
 * 总机更换
 */
public class OneHandleChangeActivity  extends BaseActivity{
    @BindView(R.id.handle_change_toolbar)
    Toolbar change_toolbar;
    @BindView(R.id.et_change_community_name)
    TextView change_community;
    @BindView(R.id.et_change_binder_name)
    TextView change_binder_name;
    @BindView(R.id.et_change_number)
    EditText change_number;
    @BindView(R.id.one_change_sim_btn)
    Button change_sim_btn;
    @BindView(R.id.tv_change_id_select)
    TextView change_id;
    @BindView(R.id.one_change_commit_btn)
    Button change_commit;
    @BindView(R.id.one_change_commit_query)
    TimeButton commit_query;
    @BindView(R.id.tv_change_result)
    TextView change_result;

    private Context mContext;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private String token;
    boolean connected ;
    private String newdeviceId;
    private String olddeviceId;
    private int useOrgId;
    private String useOrgName;
    private String change_num;
    private String batchId;
    private boolean isNull;

    private int orgId;
    List<OrgBean> orgList;
    List<String> orgNames;
    List<OneHandleBean> oneHandleList;
    List<String> oneHandleNames;
    Bundle state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_handle_change);
        state = savedInstanceState;
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
        connected = MqttManager.getInstance().creatConnect(bean.serverAddress, bean.serverName, bean.serverPwd, clientId);
        Log.e("订阅f", "！！！" + connected);
        if (connected) {
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
    /**
     *  选择小区
     * */
    @OnClick(R.id.et_change_community_name)
    public void changeCommuntitiyOnClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        btnClickAble();
        dataReset();
        loadUseOrgList();
    }

    private void dataReset() {
        change_binder_name.setText("");
        change_number.setText("");
        change_id.setText("");
    }

    /**
     * 选择需更换的总机
     * */
    @OnClick(R.id.et_change_binder_name)
    public void changeBinderOnClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        getOnehandle();
    }

    /**
     * 采集按钮
     * */
    @OnClick(R.id.one_change_sim_btn)
    public void changeSimOnClick(){
        String number = change_number.getText().toString().trim();
        if(number != null){
            Observable<UserResponse> simObservable = LoginApiImpl.handleSim(new SimHandleReq(number));
            simObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserResponse>() {
                        @Override
                        public void onCompleted() {
                            createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
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

    /**
     * 更换手柄
     * */
    @OnClick(R.id.one_change_commit_btn)
    public void changerCommitOnClick(){
        Observable<UserResponse<HandleChangeBean>> observable = LoginApiImpl.handleChange(
                new HandleChangeReq(token,newdeviceId,useOrgId,useOrgName,olddeviceId));
        Log.e("json", GsonUtil.GsonString(new HandleChangeReq(token,newdeviceId,useOrgId,useOrgName,olddeviceId)));
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<UserResponse<HandleChangeBean>>() {
                      @Override
                      public void onCompleted() {
                          showToast();
                          commit_query.setVisibility(View.VISIBLE);
                          commit_query.onCreate(state);
                      }

                      @Override
                      public void onError(Throwable e) {
                          showErrorToast();
                      }

                      @Override
                      public void onNext(UserResponse<HandleChangeBean> result) {
                          Log.e("next",result.appendData.toString());
                          HandleChangeBean bean = result.appendData;
                          batchId = bean.batchId;
                          Log.e("next",batchId);
                          OneHandleOutput oneHandleOutput = bean.oneHandleOutput;
                          Log.e("next",oneHandleOutput.toString());
                          if(oneHandleOutput.mqttUser != null && oneHandleOutput.mqttPwd != null){
                              try {
                                  isNull = false;

                                  JSONObject publishreg = new JSONObject();
                                  publishreg.put("username", oneHandleOutput.mqttUser);
                                  publishreg.put("password", oneHandleOutput.mqttUser);
                                  publishreg.put("time",oneHandleOutput.serverTime);
                                  String s = publishreg.toString();
                                  int objToAscii = OneEleUtil.strToASCII(s.substring(1,s.length()-1));
                                  publishreg.put("checksum",objToAscii);
                                  MqttManager.getInstance().publish("/oneserv/" + oneHandleOutput.deviceId + "/reg", 2, publishreg.toString().getBytes());


                                  JSONObject publishChange = new JSONObject();
                                  publishChange.put("newhandid",newdeviceId);
                                  Log.e("onNext: ", newdeviceId);
                                  String sc = publishChange.toString();
                                  int objToAsciiSC = OneEleUtil.strToASCII(sc.substring(1,sc.length()-1));
                                  publishChange.put("checksum",objToAsciiSC);
                                  Log.e("onNext: ", objToAsciiSC+"");
                                  MqttManager.getInstance().publish("/onehand/" + olddeviceId + "/status", 2, publishChange.toString().getBytes());

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }else{
                              isNull = true;
                          }

                      }
                  });


    }

    /**
     *  查询一键通更新结果
     * */
    @OnClick(R.id.one_change_commit_query)
    public void changeQueryOnClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        Observable<UserResponse<ChangeResultResp>> observable =
        LoginApiImpl.getChangeData(new ChangeResultReq(token,batchId));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<ChangeResultResp>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                        showToast();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        showErrorToast();
                    }

                    @Override
                    public void onNext(UserResponse<ChangeResultResp> result) {
                        ChangeResultResp resp = result.appendData;
                        if(resp.totalNum > 0){
                            isNull = false;
                            change_result.setVisibility(View.VISIBLE);
                            change_result.setText(String.format(getResources().getString
                                    (R.string.change_result_detail),resp.totalNum,resp.successNum));
                        }else{
                            isNull = true;
                        }

                    }
                });

    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            change_community.setText(orgList.get(position).orgName);
            orgId = orgList.get(position).orgId;
            change_binder_name.setClickable(true);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener handleItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            change_binder_name.setText(oneHandleNames.get(position));
            olddeviceId = oneHandleList.get(position).deviceId;
            useOrgId = oneHandleList.get(position).useOrgId;
            useOrgName = oneHandleList.get(position).useOrgName;

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
     * 获取 主机手柄
     * */
    private void getOnehandle() {
        Observable<UserResponse<OneHandles>> observable = LoginApiImpl.getOneHandler(new OneHandleReq(token,orgId));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<OneHandles>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                        showToast();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        showErrorToast();
                    }
                    @Override
                    public void onNext(UserResponse<OneHandles> userResponse) {
                        OneHandles oneHandle = userResponse.appendData;
                        if(oneHandle.oneHandleList.size() == 0){
                            isNull = true;
                        }else{
                            isNull = false;
                            oneHandleList = oneHandle.oneHandleList;
                            oneHandleNames = new ArrayList<String>();
                            for(OneHandleBean bean:oneHandleList){
                                if(bean.useOrgName != null ){
                                    oneHandleNames.add(bean.useOrgName);
                                }
                            }

                            if(oneHandleNames.size()>0){
                                mSpinerPopWindow = new SpinerPopWindow<String>(mContext, oneHandleNames, handleItemClickListener);
                                mSpinerPopWindow.setOnDismissListener(dismissListener);
                                mSpinerPopWindow.setWidth(change_binder_name.getWidth());
                                mSpinerPopWindow.showAsDropDown(change_binder_name);
                            }
                        }
                    }
                });

    }
    /**
     *  加载小区数据
     * */
    private void loadUseOrgList() {
        token = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        Observable<UserResponse<UseOrgbean>> userObservable = LoginApiImpl.useOrgList(new UseOrgReq(token));
        userObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<UseOrgbean>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoading();
                        showToast();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        showErrorToast();
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
                                mSpinerPopWindow.setWidth(change_community.getWidth());
                                mSpinerPopWindow.showAsDropDown(change_community);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initToolbar() {
        change_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnClickAble() {
        change_binder_name.setClickable(false);
        change_sim_btn.setClickable(false);
        change_commit.setClickable(false);
        change_sim_btn.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        change_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        change_number.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        change_number.addTextChangedListener(new TextWatcher() {
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
                        change_sim_btn.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
                        change_sim_btn.setClickable(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误", Toast.LENGTH_LONG);
                    }
                }else{
                    change_sim_btn.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
                    change_sim_btn.setClickable(false);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ThreadEvent(MessageEvent event){
        dismissLoading();
        if("OK".equals(event.message)&&newdeviceId.equals(event.deviceId)){
//            finish();
            change_commit.setClickable(false);
            change_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        }
        if(event.type.equals("success")){
            Log.e("订阅接收到的话柄采集", event.message + "   " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())));
            ToastUtil.show(mContext,event.message,Toast.LENGTH_LONG);
            if (event.message.contains(change_number.getText().toString())) {
                OneKeyBean oneKeyBean = OneKeyBean.objectFromData(event.message);
                change_num = change_number.getText().toString();
                newdeviceId = oneKeyBean.getID();
                change_id.setText(oneKeyBean.getID());
                change_commit.setClickable(true);
                change_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
            }
        }else{
            ToastUtil.show(mContext,event.message,Toast.LENGTH_LONG);
        }
    }
    private void showToast(){
        if(isNull){
            ToastUtil.show(mContext,"数据为空",Toast.LENGTH_LONG);
        }else{
            ToastUtil.show(mContext,"数据加载完成", Toast.LENGTH_LONG);
        }
    }
    private void showErrorToast(){
        ToastUtil.show(mContext,"数据加载失败",Toast.LENGTH_LONG);
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
