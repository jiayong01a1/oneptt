package onerfid.com.oneptt.ui.pusktotalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.api.LoginApiImpl;
import onerfid.com.oneptt.base.BaseActivity;
import onerfid.com.oneptt.bean.MqttServerBean;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetBean;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetResp;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.MqttManager;
import onerfid.com.oneptt.event.MessageEvent;
import onerfid.com.oneptt.event.ToastEvent;
import onerfid.com.oneptt.util.OneEleUtil;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.com.oneptt.widget.SpinerPopWindow;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * 设置子机名称
 * */
public class SetChildNameActivity extends BaseActivity {
    @BindView(R.id.key_set_child_toolbar)
    Toolbar set_toolbar;
    @BindView(R.id.tv_one_key_set_select_child)
    TextView onekey_set_select_child;
    @BindView(R.id.rl_one_key_child)
    RelativeLayout relativeLayout;

    @BindView(R.id.et_one_key_child_name1)
    EditText child_name1;
    @BindView(R.id.et_one_key_child_name2)
    EditText child_name2;
    @BindView(R.id.et_one_key_child_name3)
    EditText child_name3;
    @BindView(R.id.et_one_key_child_name4)
    EditText child_name4;
    @BindView(R.id.et_one_key_child_name5)
    EditText child_name5;
    @BindView(R.id.et_one_key_child_name6)
    EditText child_name6;
    @BindView(R.id.et_one_key_child_name7)
    EditText child_name7;
    @BindView(R.id.et_one_key_child_name8)
    EditText child_name8;

    @BindView(R.id.btn_one_key_child_check)
    Button child_check;
    @BindView(R.id.btn_one_key_child_cancel)
    Button child_cancel;

    private Context mContext;
    private boolean isNull = false;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<OneKeySetBean> onekeyList;
    private List<String> OneKeyNames;
    private String deviceId;
    private boolean isSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_child_name);
        mContext = this;
        ButterKnife.bind(this);
        relativeLayout.setVisibility(View.GONE);
        initToolbar();
        connectMQTT();
    }

    private void connectMQTT() {
        String clientId = System.currentTimeMillis() + "";
        MqttServerBean bean = SharedPreferenceUtil.getInstance().getMqttMsg();
        boolean conneted = MqttManager.getInstance().creatConnect(bean.serverAddress, bean.serverName, bean.serverPwd, clientId);
        MqttManager.getInstance().subscribe(Constant.ONE_KEY_CHILD, 2);
        MqttManager.getInstance().subscribe(Constant.ONE_KEY_CHECK, 2);
        Log.e("订阅f", "！！！" + conneted);
    }

    private void initToolbar() {
        set_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_one_key_child_cancel)
    public void cancelChildName(){
        onekeyList.clear();
        OneKeyNames.clear();
        onekey_set_select_child.setText("");
        child_name1.setText("");
        child_name2.setText("");
        child_name3.setText("");
        child_name4.setText("");
        child_name5.setText("");
        child_name6.setText("");
        child_name7.setText("");
        child_name8.setText("");
        relativeLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_one_key_child_check)
    public void submitChildName(){
        JSONObject publish = new JSONObject(new LinkedHashMap());
        try {
            publish.put("slaveliftname1",OneEleUtil.gbEncoding(child_name1.getText().toString().trim()));
            publish.put("slaveliftname2",OneEleUtil.gbEncoding(child_name2.getText().toString().trim()));
            publish.put("slaveliftname3",OneEleUtil.gbEncoding(child_name3.getText().toString().trim()));
            publish.put("slaveliftname4",OneEleUtil.gbEncoding(child_name4.getText().toString().trim()));
            publish.put("slaveliftname5",OneEleUtil.gbEncoding(child_name5.getText().toString().trim()));
            publish.put("slaveliftname6",OneEleUtil.gbEncoding(child_name6.getText().toString().trim()));
            publish.put("slaveliftname7",OneEleUtil.gbEncoding(child_name7.getText().toString().trim()));
            publish.put("slaveliftname8",OneEleUtil.gbEncoding(child_name8.getText().toString().trim()));
            String s = publish.toString();
            int objToAscii = OneEleUtil.strToASCII(s.substring(1, s.length() - 1));
            publish.put("checksum", objToAscii);
            Log.e("publish",publish.toString());
            MqttManager.getInstance().publish("/APP/" + deviceId + "/setslavename", 2,
                    publish.toString().getBytes());
            createLoadingDialog(mContext,"上报中... 设置成功会提示上报ok,没有提示请重新设置").show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!isSend){
                        EventBus.getDefault().post( new ToastEvent());
                        dismissLoading();
                    }
                }
            },10*1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一键通信息
     * */
    @OnClick(R.id.tv_one_key_set_select_child)
    public void selectOneKey(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_one_key_serach, null);
        final EditText etSeachOrgName = (EditText) view.findViewById(R.id.search_org_name);
        final EditText etSeachOneKeyName = (EditText) view.findViewById(R.id.search_onekey_name);

        new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(etSeachOrgName.getText()) && TextUtils.isEmpty(etSeachOneKeyName.getText())) {
                            ToastUtil.show(mContext, "请您输入一键通查询信息！", Toast.LENGTH_SHORT);
                        } else {
                            createLoadingDialog(mContext,getResources().getString(R.string.show_loading_msg)).show();
                            queryOneKeyInfo(etSeachOrgName.getText() + "", etSeachOneKeyName.getText() + "");
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

    private void queryOneKeyInfo(String name, String onkeyname) {
        Observable<UserResponse<OneKeySetResp>> observable = LoginApiImpl.getOneKeySetList(new OneKeySetReq(name,onkeyname));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<OneKeySetResp>>() {
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
                    public void onNext(UserResponse<OneKeySetResp> result) {
                        OneKeySetResp resp = result.appendData;
                        if (resp.total == 0) {
                            isNull = true;
                        } else {
                            isNull = false;
                            onekeyList = resp.oneKeyList;
                            OneKeyNames = new ArrayList<String>();
                            for(OneKeySetBean bean:onekeyList){
                                OneKeyNames.add(bean.oneKeyName);
                            }
                            mSpinerPopWindow = new SpinerPopWindow<String>(mContext, OneKeyNames,onekeySetItemClickListener);
                            mSpinerPopWindow.setOnDismissListener(dismissListener);
                            mSpinerPopWindow.setWidth(onekey_set_select_child.getWidth());
                            mSpinerPopWindow.showAsDropDown(onekey_set_select_child);
                        }
                    }
                });
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener onekeySetItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_set_select_child.setText(onekeyList.get(position).oneKeyName);
            deviceId = onekeyList.get(position).deviceId;

            JSONObject publish = new JSONObject(new LinkedHashMap());
                try {
                    relativeLayout.setVisibility(View.VISIBLE);
                    publish.put("data","check");
                    String s = publish.toString();
                    int objToAscii = OneEleUtil.strToASCII(s.substring(1, s.length() - 1));
                    publish.put("checksum", objToAscii);
                    MqttManager.getInstance().publish("/APP/" + deviceId + "/checkslavename", 2,
                            publish.toString().getBytes());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(ToastEvent event){
        ToastUtil.show(mContext,"设置失败，请重试",Toast.LENGTH_LONG);
    }

    public String getGBStr(String s){
        String s1 = null ;
        if (s == null || "".equals(s)){
            return s1;
        }else{
            s1 = OneEleUtil.decodeUnicode(newGBKString(s));
        }
        return s1;
    };

    public String newGBKString(String str){
        String newStr = "\\u";
        int i = str.length()/4;
        System.out.println(i);
        for(int j=0;j<i;j++){
            if(j<i-1){
                newStr =newStr+str.substring(j*4,(j+1)*4)+"\\u";
            }else{
                newStr =newStr+str.substring(j*4,(j+1)*4);
            }
        }
        return newStr;
    };
    /**
     * 订阅接收到的消息
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent message) {
        if(deviceId.equals(message.deviceId)){
            if(message.message.startsWith("{\"slaveliftname1\"")){
                try {
                    JSONObject jsonObject = new JSONObject(message.message);
                    child_name1.setText(getGBStr((String) jsonObject.get("slaveliftname1")));
                    child_name2.setText(getGBStr((String) jsonObject.get("slaveliftname2")));
                    child_name3.setText(getGBStr((String) jsonObject.get("slaveliftname3")));
                    child_name4.setText(getGBStr((String) jsonObject.get("slaveliftname4")));
                    child_name5.setText(getGBStr((String) jsonObject.get("slaveliftname5")));
                    child_name6.setText(getGBStr((String) jsonObject.get("slaveliftname6")));
                    child_name7.setText(getGBStr((String) jsonObject.get("slaveliftname7")));
                    child_name8.setText(getGBStr((String) jsonObject.get("slaveliftname8")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if ("OK".equals(message.message)){
                Log.e("订阅接收到的话柄采集", message.message);
                isSend = true;
                dismissLoading();
                ToastUtil.show(mContext, "上报"+message.message, Toast.LENGTH_SHORT);
                finish();
            }
        }
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
