package onerfid.com.oneptt.ui.pusktotalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.api.LoginApi;
import onerfid.com.oneptt.api.LoginApiImpl;
import onerfid.com.oneptt.base.BaseActivity;
import onerfid.com.oneptt.bean.MqttServerBean;
import onerfid.com.oneptt.bean.QueryEleInfoBean;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.onehandlebean.OneKeyBean;
import onerfid.com.oneptt.bean.onehandlebean.OrgBean;
import onerfid.com.oneptt.bean.onehandlebean.SimHandleReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgbean;
import onerfid.com.oneptt.bean.onekeybean.ElevatorBean;
import onerfid.com.oneptt.bean.onekeybean.ElevatorReq;
import onerfid.com.oneptt.bean.onekeybean.ElevatorResp;
import onerfid.com.oneptt.bean.onekeybean.ElevatorResponse;
import onerfid.com.oneptt.bean.onekeybean.OneHandleBean;
import onerfid.com.oneptt.bean.onekeybean.OneHandleReq;
import onerfid.com.oneptt.bean.onekeybean.OneHandles;
import onerfid.com.oneptt.bean.onekeybean.OneKeyReq;
import onerfid.com.oneptt.bean.onekeybean.OneKeyResp;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.MqttManager;
import onerfid.com.oneptt.event.MessageEvent;
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

import static onerfid.com.oneptt.ui.pusktotalk.OneHandleCollectActivity.isMobileNO;

/**
 *  一键通绑定
 */
public class OneKeyCollectActivity extends BaseActivity {
    @BindView(R.id.one_key_toolbar)
    Toolbar key_toolbar;
    @BindView(R.id.et_key_community_name)
    TextView key_community_name;
    @BindView(R.id.et_key_binder_name)
    TextView key_binder_name;
    @BindView(R.id.tv_onekey_elevator_select)
    TextView elevator_select;
    @BindView(R.id.et_onekey_name)
    EditText onekey_name;
    @BindView(R.id.et_onekey_number)
    EditText onekey_number;
    @BindView(R.id.one_key_sim_btn)
    Button btn_key_sim;
    @BindView(R.id.tv_onekey_id_show)
    TextView onekey_id_show;
    @BindView(R.id.one_key_commit_btn)
    Button btn_key_commit;

    List<OrgBean> orgList;
    List<String> orgNames;
    List<String> elevatorNames;
    List<OneHandleBean> oneHandleList;
    List<String> oneHandleNames;
    private Context mContext;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private int orgId;
    private String onekeyId = null;
    private String onkeyNum;
    private String elevatorId;
    private String handleId;
    private int handlername;
    private String token;
    private boolean isNull = false;
    private List<QueryEleInfoBean> eleAddressList; //电梯信息

    private String num,housename,liftname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_key_collet);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        btnClickAble();
        connectMQTT();
    }

    private void btnClickAble() {
        key_binder_name.setClickable(false);
        elevator_select.setClickable(false);
        btn_key_sim.setClickable(false);
        btn_key_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        btn_key_commit.setClickable(false);
        btn_key_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
        onekey_number.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        onekey_number.addTextChangedListener(new TextWatcher() {
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
                        btn_key_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
                        btn_key_sim.setClickable(true);
                    }else{
                        ToastUtil.show(mContext,"号码有误",Toast.LENGTH_LONG);
                    }
                }else{
                    btn_key_sim.setBackground(getResources().getDrawable(R.drawable.caiji_bg_disable));
                    btn_key_sim.setClickable(false);
                }
            }
        });
    }


    /**
     * 创建MQTT连接  订阅
     */
    private void connectMQTT() {
        String clientId = System.currentTimeMillis() + "";
        MqttServerBean bean = SharedPreferenceUtil.getInstance().getMqttMsg();
        boolean conneted = MqttManager.getInstance().creatConnect(bean.serverAddress, bean.serverName, bean.serverPwd, clientId);
        if (conneted) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    MqttManager.getInstance().subscribe(Constant.ONE_KEY_COLLECT, 2);
                    MqttManager.getInstance().subscribe(Constant.ONE_KEY_CHECK, 2);
                    Log.e("订阅", "订阅成功！！！");
                }
            }).start();
        }
    }

    @OnClick(R.id.one_key_commit_btn)
    public void oneKeyCommitOnClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        OneKeyReq req = new OneKeyReq();
        req.AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        req.OneKeyDeviceName = onekey_name.getText().toString().trim() ;
        req.OneHandleId = handleId;
        req.UseOrgId = handlername;
        req.OneKeyId = onekeyId;
        req.SimCardNo = onekey_number.getText().toString().trim() ;

        ArrayList<ElevatorBean> e = new ArrayList<ElevatorBean>();
        e.add(new ElevatorBean(0,elevatorId));
        req.ELes = e;
        Log.e("req",req.toString());
        Observable<UserResponse<OneKeyResp>> observable = LoginApiImpl.bindOneKey(req);
        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<UserResponse<OneKeyResp>>() {
                      @Override
                      public void onCompleted() {
                          dismissLoading();
                      }
                      @Override
                      public void onError(Throwable e) {
                          dismissLoading();
                          showErrorToast();
                      }

                      @Override
                      public void onNext(UserResponse<OneKeyResp> userResponse) {
                          Log.e("error",userResponse.toString());
                          if(userResponse.resultType ==1){
                              OneKeyResp resp = userResponse.appendData;
                              JSONObject publish = new JSONObject();
                              try {
                                  if(StringUtils.isEmpty(resp.mqttUser) || StringUtils.isEmpty(resp.mqttPwd)){
                                      ToastUtil.show(mContext,userResponse.message,Toast.LENGTH_LONG);
                                  }else{
                                      Log.e("resp",resp.toString());
                                      ToastUtil.show(mContext,userResponse.message,Toast.LENGTH_LONG);
                                      publish.put("handid", resp.oneHandleId);
                                      publish.put("username",  resp.mqttUser);
                                      publish.put("password",  resp.mqttPwd);
                                      publish.put("time",  resp.serverTime);
                                      publish.put("num",num);
                                      publish.put("housename",OneEleUtil.gbEncoding(housename));
                                      publish.put("liftname",OneEleUtil.gbEncoding(liftname));
                                      String s = publish.toString();
                                      int objToAscii = OneEleUtil.strToASCII(s.substring(1, s.length() - 1));
                                      publish.put("checksum", objToAscii);
                                      Log.e("mqtt",publish.toString());
                                      // String str = appendData.getString("mqttUser") + appendData.getString("mqttPwd") + appendData.getString("oneHandleId") + appendData.getString("serverTime") ;
                                      MqttManager.getInstance().publish("/oneserv/" + resp.oneKeyId + "/reg", 2, publish.toString().getBytes());
                                  }
                              } catch (JSONException e1) {
                                  e1.printStackTrace();
                              }
                          }else{
                              ToastUtil.show(mContext,userResponse.message,Toast.LENGTH_LONG);
                          }

                      }
                  });
    }

    @OnClick(R.id.one_key_sim_btn)
    public void simKeyOnClick(){
        String number = onekey_number.getText().toString().trim();
        if(number != null){
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
                            ToastUtil.show(mContext,"号码已采集",Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onNext(UserResponse getIpInfoResponse) {
                        }
                    });
        }

    }

    @OnClick(R.id.et_key_community_name)
    public void keyCommunityOnClick(){
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        dataReset();
        btnClickAble();
        loadUseOrgList();
    }

    private void dataReset() {
        key_binder_name.setText("");
        elevator_select.setText("");
        onekey_id_show.setText("");
    }

    @OnClick(R.id.et_key_binder_name)
    public void binderOnClick(){
        elevator_select.setText("");
        onekey_id_show.setText("");
        createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
        getOnehandle();
    }

    @OnClick(R.id.tv_onekey_elevator_select)
    public void bindElevatorOnClick(){
            onekey_id_show.setText("");
            View view = LayoutInflater.from(OneKeyCollectActivity.this).inflate(R.layout.view_ele_serach_onekey, null);
            final EditText etSeachEleName = (EditText) view.findViewById(R.id.search_ele_name);
            final EditText etSeachEleAddress = (EditText) view.findViewById(R.id.search_ele_address);

            new AlertDialog.Builder(OneKeyCollectActivity.this)
                    .setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(etSeachEleName.getText()) && TextUtils.isEmpty(etSeachEleAddress.getText())) {
                                //return;
                                ToastUtil.show(OneKeyCollectActivity.this, "请您输入电梯查询信息！", Toast.LENGTH_SHORT);
                            } else {
                                queryEleInfo(etSeachEleName.getText() + "", etSeachEleAddress.getText() + "");
                                eleAddressList = new ArrayList<>();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

    }
    /**
     * 根据电梯名称地址查询电梯信息
     *
     * @param EleName
     * @param Address
     */
    private void queryEleInfo(String EleName, String Address) {
        Observable<UserResponse<ElevatorResp>> observable = LoginApiImpl.getElevators(new ElevatorReq(EleName,Address));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<ElevatorResp>>() {
                    @Override
                    public void onCompleted() {
                        showToast();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorToast();
                    }

                    @Override
                    public void onNext(UserResponse<ElevatorResp> result) {
                        ElevatorResp resp = result.appendData;
                        if (resp.total == 0) {
                               isNull = true;
                        } else {
                            isNull = false;
                            List<QueryEleInfoBean> beens = resp.eleList;
                            for(QueryEleInfoBean bean:beens){
                                Log.e("elevator", bean.toString());
                                eleAddressList.add(bean);
                            }
                            elevatorNames = new ArrayList<String>();
                            for(QueryEleInfoBean bean:eleAddressList){
                                elevatorNames.add(bean.equipmentName);
                            }
                            mSpinerPopWindow = new SpinerPopWindow<String>(mContext, elevatorNames,elevatorItemClickListener);
                            mSpinerPopWindow.setOnDismissListener(dismissListener);
                            mSpinerPopWindow.setWidth(elevator_select.getWidth());
                            mSpinerPopWindow.showAsDropDown(elevator_select);
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
            key_community_name.setText(orgList.get(position).orgName);
            orgId = orgList.get(position).orgId;
            housename = orgList.get(position).orgName;
            key_binder_name.setClickable(true);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener handleItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            key_binder_name.setText(oneHandleNames.get(position));
            handleId = oneHandleList.get(position).id;
            handlername = oneHandleList.get(position).useOrgId;
            num = oneHandleList.get(position).phoneNumber;//add
            elevator_select.setClickable(true);
        }
    };

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener elevatorItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            elevator_select.setText(eleAddressList.get(position).equipmentName);
            liftname = eleAddressList.get(position).equipmentName;
            elevatorId = eleAddressList.get(position).id;
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
                            UseOrgbean useOrgbean = getIpInfoResponse.appendData;
                            if(useOrgbean.total == 0){
                                isNull = true;
                            }else {
                                isNull = false;
                                orgList =  useOrgbean.orgList;
                                orgNames = new ArrayList<String>();
                                for(OrgBean bean : orgList){
                                    orgNames.add(bean.orgName);
                                }
                                mSpinerPopWindow = new SpinerPopWindow<String>(mContext, orgNames, itemClickListener);
                                mSpinerPopWindow.setOnDismissListener(dismissListener);
                                mSpinerPopWindow.setWidth(key_community_name.getWidth());
                                mSpinerPopWindow.showAsDropDown(key_community_name);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

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
                        if(oneHandle.oneHandleList.size() ==0){
                            isNull = true;
                        }else{
                            isNull = false;
                            oneHandleList = oneHandle.oneHandleList;
                            oneHandleNames = new ArrayList<String>();
                            for(OneHandleBean bean:oneHandleList){

                                Log.e("resp",bean.toString());
                                oneHandleNames.add(bean.useOrgName);
                            }
                            mSpinerPopWindow = new SpinerPopWindow<String>(mContext, oneHandleNames, handleItemClickListener);
                            mSpinerPopWindow.setOnDismissListener(dismissListener);
                            mSpinerPopWindow.setWidth(key_binder_name.getWidth());
                            mSpinerPopWindow.showAsDropDown(key_binder_name);
                        }
                    }
                });

    }
    private void showToast(){
        if(isNull){
            ToastUtil.show(mContext,"暂无数据",Toast.LENGTH_LONG);
        }else{
            ToastUtil.show(mContext,"数据加载完成", Toast.LENGTH_LONG);
        }
    }
    private void showErrorToast(){
        ToastUtil.show(mContext,"数据加载失败",Toast.LENGTH_LONG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ThreadEvent(MessageEvent event){
        dismissLoading();
        if("OK".equals(event.message)&&onekeyId.equals(event.deviceId)){
            finish();
        }
        if(event.type.equals("success")){
            Log.e("订阅接收到的话柄采集", event.message + "   " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())));
            if (event.message.contains(onekey_number.getText().toString())) {
                OneKeyBean oneKeyBean = OneKeyBean.objectFromData(event.message);
                onkeyNum = onekey_number.getText().toString();
                onekeyId = oneKeyBean.getID();
                onekey_id_show.setText(oneKeyBean.getID());
                btn_key_commit.setClickable(true);
                btn_key_commit.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
            }
        }else{
            ToastUtil.show(mContext,event.message,Toast.LENGTH_LONG);
        }
    }

    private void initToolbar() {
        key_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
