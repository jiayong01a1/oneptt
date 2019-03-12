package onerfid.com.oneptt.ui.pusktotalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableLayout;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import onerfid.com.oneptt.bean.onekeyset.MtpersonReq;
import onerfid.com.oneptt.bean.onekeyset.OKeyBasicReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeyBasicInfo;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetBean;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetResp;
import onerfid.com.oneptt.bean.onekeyset.OneMTPhoneAdapter;
import onerfid.com.oneptt.bean.onekeyset.OneMTPhoneBean;
import onerfid.com.oneptt.bean.onekeyset.SubmitOKeySetReq;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.MqttManager;
import onerfid.com.oneptt.event.MessageEvent;
import onerfid.com.oneptt.event.ToastEvent;
import onerfid.com.oneptt.util.OneEleUtil;
import onerfid.com.oneptt.util.OnePromptUtil;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.com.oneptt.widget.SpinerPopWindow;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static onerfid.com.oneptt.ui.pusktotalk.OneHandleCollectActivity.isMobileNO;

/**
 * 一建通设置
 */
public class OneKeySetActivity extends BaseActivity {
    @BindView(R.id.ll_one_key_seta)
    LinearLayout ll_one_key_set;
    @BindView(R.id.ll_one_key_setb)
    LinearLayout ll_user_unit;
    @BindView(R.id.key_set_toolbar)
    Toolbar set_toolbar;
    @BindView(R.id.tv_one_key_set_select)
    TextView onekey_set_select;
    @BindView(R.id.tv_one_key_use_unit_name)
    TextView use_unit_name;
    @BindView(R.id.one_key_basic_btn)
    Button btn_one_key_basic;
    @BindView(R.id.one_key_delete_btn)
    Button btn_one_key_del;
    @BindView(R.id.one_key_maintenance_btn)
    Button btn_one_key_maintenacnce;

    @BindView(R.id.tl_basic_set)
    TableLayout tableLayout;
    @BindView(R.id.tv_onekey_address)
    TextView onekey_address;// 小区名称
    @BindView(R.id.tv_onekey_elevator)
    TextView onekey_elevator;//电梯名称
    @BindView(R.id.et_onekey_phone1)
    EditText onekey_phone1;
    @BindView(R.id.et_onekey_phone2)
    EditText onekey_phone2;
    @BindView(R.id.et_onekey_phone3)
    EditText onekey_phone3;
    @BindView(R.id.et_onekey_phone4)
    EditText onekey_phone4;
    @BindView(R.id.et_onekey_phone5)
    EditText onekey_phone5;
    @BindView(R.id.iv_mt_phone1)
    ImageView mt_phone1;
    @BindView(R.id.iv_mt_phone2)
    ImageView mt_phone2;
    @BindView(R.id.iv_mt_phone3)
    ImageView mt_phone3;
    @BindView(R.id.iv_mt_phone4)
    ImageView mt_phone4;
    @BindView(R.id.iv_mt_phone5)
    ImageView mt_phone5;
    @BindView(R.id.et_onekey_waittime)
    EditText onekey_waittime;
    @BindView(R.id.et_onekey_callduration)
    EditText onekey_callduration;
    @BindView(R.id.et_onekey_polling)
    EditText onekey_polling;
    @BindView(R.id.btn_onekey_cancel)
    Button onekey_cancel;
    @BindView(R.id.btn_onekey_check)
    Button onekey_ckeck;

    private Context mContext;
    private static final int TYPE_SET = 1;
    private static final int TYPE_DELETE = 2;
    private static final int TYPE_MAINTEN = 3;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<OneKeySetBean> onekeyList;
    private List<String> OneKeyNames;
    private boolean isNull = false;
    private boolean connected;
    List<String> items;
    private String[] divertPhone = new String[5];
    OneMTPhoneAdapter oneMTPhoneAdapter;
    private List<OneMTPhoneBean> mtPhoneList ;
    OneKeyBasicInfo oneKeyBasicInfo;
    private String deviceId;
    List<OneMTPhoneBean> oneMTPhoneBeen;
    EditText [] phones;

    private int g_fun;
    private String g_num;
    private String g_count;
    private String g_waittime;
    private int g_onphonetime;
    private int g_wbstatus;
    private int g_wbtime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_key_set);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        tableLayout.setVisibility(View.GONE);
        initToolbar();
        connectMQTT();
    }
    private void connectMQTT() {
        String clientId = System.currentTimeMillis() + "";
        MqttServerBean bean = SharedPreferenceUtil.getInstance().getMqttMsg();
        boolean conneted = MqttManager.getInstance().creatConnect(bean.serverAddress, bean.serverName, bean.serverPwd, clientId);
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
        btn_one_key_basic.setClickable(false);
        btn_one_key_del.setClickable(false);
        btn_one_key_maintenacnce.setClickable(false);
    }

    /**
     * 维保
     * */
    @OnClick(R.id.one_key_maintenance_btn)
    public void maintenanceOnClick(){
        getOneKeyPhone(deviceId,TYPE_MAINTEN);
    }

    /**
     * 删除号码
     * */
    @OnClick(R.id.one_key_delete_btn)
    public void delPhoneOnClick(){
        getOneKeyPhone(deviceId,TYPE_DELETE);
    }
    private void maintenance(){
        View mtView = LayoutInflater.from(OneKeySetActivity.this).inflate(R.layout.view_onekey_set_mt, null);
        RadioGroup rgOneKeySetMt = (RadioGroup) mtView.findViewById(R.id.one_key_set_mt_rg);
        final RadioButton rbOneKeySetMt1 = (RadioButton) mtView.findViewById(R.id.one_key_set_mt_rb1);
        final RadioButton rbOneKeySetMt2 = (RadioButton) mtView.findViewById(R.id.one_key_set_mt_rb2);
        final LinearLayout llOneKeyMt = (LinearLayout) mtView.findViewById(R.id.one_key_mt_ll);
        final EditText etOneKeyMt = (EditText) mtView.findViewById(R.id.one_key_mt);
        final TextView tvMtPrompt = (TextView) mtView.findViewById(R.id.one_key_mt_prompt);

        switch (oneKeyBasicInfo.status) {
            case 0:
                rbOneKeySetMt2.setChecked(true);
                tvMtPrompt.setVisibility(View.GONE);
                break;
            case 1:
                rbOneKeySetMt1.setChecked(true);
                llOneKeyMt.setVisibility(View.VISIBLE);
                tvMtPrompt.setVisibility(View.VISIBLE);
                etOneKeyMt.setSelection(etOneKeyMt.getText().length());
                tvMtPrompt.setText("* 维保状态下一键通不能进行转呼。");
                break;
        }
        final String mtPromptStr = "* 维保时间为30~240分钟，默认120分钟。维保状态下一键通不能进行转呼。";

        rgOneKeySetMt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.one_key_set_mt_rb1:
                        llOneKeyMt.setVisibility(View.VISIBLE);
                        tvMtPrompt.setVisibility(View.VISIBLE);
                        tvMtPrompt.setText(mtPromptStr);
                        break;

                    case R.id.one_key_set_mt_rb2:
                        llOneKeyMt.setVisibility(View.GONE);
                        tvMtPrompt.setVisibility(View.GONE);
                        break;
                }
            }
        });

        OnePromptUtil.setRegion2(etOneKeyMt, 30, 240, 120, tvMtPrompt, mtPromptStr);

        AlertDialog.Builder builderMt = new AlertDialog.Builder(OneKeySetActivity.this,AlertDialog.THEME_HOLO_LIGHT);
        builderMt.setCancelable(false)
                .setView(mtView)
                .setTitle("维保")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int status  = 0;
                        if (rbOneKeySetMt1.isChecked()) {
                            status = 1;
                        } else {
                            status = 0;
                        }

                        if (!TextUtils.isEmpty(etOneKeyMt.getText().toString())) {
                            int mtInt = Integer.valueOf(etOneKeyMt.getText().toString());
                            if (mtInt < 30 || mtInt > 240) {
                                ToastUtil.show(OneKeySetActivity.this, "维保时间为30~240分钟，请重新设置", Toast.LENGTH_SHORT);
                                return;
                            }
                        }

                        String num = "";
                        for (int i = 0;i < divertPhone.length; i++) {
                            if (!TextUtils.isEmpty(divertPhone[i])) {
                                num = num + "," + divertPhone[i];
                            }
                        }

                        if (num.length() > 0) {
                            num = num.substring(1, num.length());
                            switch (status) {
                                case 0:
                                    oneKeySet(54,num,oneKeyBasicInfo.pollingTimes+"" , oneKeyBasicInfo.divertWaitingTime+"",
                                            oneKeyBasicInfo.onphonetime,status,oneKeyBasicInfo.wbTime);
                                    break;

                                case 1:
                                    oneKeySet(54,num,oneKeyBasicInfo.pollingTimes+"" , oneKeyBasicInfo.divertWaitingTime+"",
                                            oneKeyBasicInfo.onphonetime,status,Integer.valueOf(etOneKeyMt.getText().toString()));
                                    break;
                            }
                        } else {
                            ToastUtil.show(OneKeySetActivity.this,"您不能删除全部转呼号码！",Toast.LENGTH_SHORT);
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
    private void deleteNumber(){
        final boolean selected[] = {false, false, false, false, false};
        final String[] deletePhone = new String[divertPhone.length];
        for (int i = 0; i < divertPhone.length; i++) {
            Log.e("phone",divertPhone[i]);
            if (TextUtils.isEmpty(divertPhone[i])) {
                deletePhone[i] = "未设置";
            } else {
                deletePhone[i] = divertPhone[i];
            }
        }
        AlertDialog.Builder builderDelte = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        builderDelte.setCancelable(false);
        builderDelte.setTitle("删除号码")
                .setMultiChoiceItems(deletePhone, selected, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e("shanchunage", which + "    " + isChecked);
                        selected[which] = isChecked;
                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = "";
                        for (int i = 0; i < selected.length; i++) {
                            if (!selected[i] &&  divertPhone[i]!= null) {
                                num = num + "," + divertPhone[i];
                            }
                        }
                        Log.e("num",num);
                        if (num.length() > 8) {
                            String del = null;
                            num = num.substring(1, num.length());
                            String delNum[] = num.split(",");
                            for(String str:delNum){
                                if(str.length()>10){
                                    del+=","+str;
                                }
                            }
                            oneKeySet(52,del.substring(5,del.length()),oneKeyBasicInfo.pollingTimes+"" , oneKeyBasicInfo.divertWaitingTime+"",
                                    oneKeyBasicInfo.onphonetime,oneKeyBasicInfo.status,oneKeyBasicInfo.wbTime);
                        } else {
                            ToastUtil.show(OneKeySetActivity.this,"您不能删除全部转呼号码！",Toast.LENGTH_SHORT);
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
     * 基本设置
     * */
    @OnClick(R.id.one_key_basic_btn)
    public void onekeyBasicOnClick(){
        showView(true);
        getOneKeyPhone(deviceId,TYPE_SET);
    }
    public void initView(){
        initData();
        setListener();
    }

    public void showView(boolean show){
        if(show){
            ll_one_key_set.setVisibility(View.GONE);
            ll_user_unit.setVisibility(View.GONE);
            btn_one_key_del.setVisibility(View.GONE);
            btn_one_key_maintenacnce.setVisibility(View.GONE);
            btn_one_key_basic.setVisibility(View.GONE);
            tableLayout.setVisibility(View.VISIBLE);
        }else{
            ll_one_key_set.setVisibility(View.VISIBLE);
            ll_user_unit.setVisibility(View.VISIBLE);
            btn_one_key_del.setVisibility(View.VISIBLE);
            btn_one_key_maintenacnce.setVisibility(View.VISIBLE);
            btn_one_key_basic.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        MTClick(onekey_phone1,mt_phone1,items,itemClickListener);
        MTClick(onekey_phone2,mt_phone2,items,itemClickListener2);
        MTClick(onekey_phone3,mt_phone3,items,itemClickListener3);
        MTClick(onekey_phone4,mt_phone4,items,itemClickListener4);
        MTClick(onekey_phone5,mt_phone5,items,itemClickListener5);
        onekey_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView(false);
            }
        });

        onekey_ckeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidatorNum()){
                    String num = "";
                    for (int i = 0; i < phones.length; i++) {
                        if (!TextUtils.isEmpty(phones[i].getText().toString())) {
                            if(phones[i].getText().toString().contains(":")){
                                String s = phones[i].getText().toString();
                                String [] strs = s.split(":");
                                num = num + "," + strs[1];
                            }else{
                                num = num + "," + phones[i].getText().toString();
                            }
                        }
                    }
                    if (num.length() > 0) {
                        num = num.substring(1, num.length());
                        oneKeySet(51,num,onekey_polling.getText().toString() ,
                                onekey_waittime.getText().toString(),
                                Integer.valueOf(onekey_callduration.getText().toString()) ,
                                oneKeyBasicInfo.status,
                                oneKeyBasicInfo.wbTime);
                    } else {
                        ToastUtil.show(OneKeySetActivity.this,"转呼号码未设置",Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    public boolean ValidatorNum(){
        boolean bool = false;
        int a=-1,b=0,c=0;
        if(TextUtils.isEmpty(onekey_waittime.getText().toString())){
            ToastUtil.show(mContext,"等待时间不能为空",Toast.LENGTH_LONG);
            return bool;
        }else{
            a = Integer.parseInt(onekey_waittime.getText().toString());
            if( (a>0&&a<10) || a>90 || a<0){
                ToastUtil.show(mContext,"等待时间设置0或10~90，默认30 单位秒",Toast.LENGTH_LONG);
                a=-1;
                return bool;
            }
        }

        if(TextUtils.isEmpty(onekey_callduration.getText().toString())){
            ToastUtil.show(mContext,"通话时长不能为空",Toast.LENGTH_LONG);
            return bool;
        }else{
            b = Integer.parseInt(onekey_callduration.getText().toString());
            if(b<5|| b>15){
                ToastUtil.show(mContext,"通话时长设置5~15，默认5 单位分钟",Toast.LENGTH_LONG);
                b=0;
                return bool;
            }
        }


        if(TextUtils.isEmpty(onekey_polling.getText().toString())){
            ToastUtil.show(mContext,"轮询次数不能为空",Toast.LENGTH_LONG);
            return bool;
        }else{
            c = Integer.parseInt(onekey_polling.getText().toString());
            if(c<1 || c>3){
                ToastUtil.show(mContext,"轮询次数设置1~3，默认1 单位ci",Toast.LENGTH_LONG);
                c=0;
                return bool;
            }
        }

        if(a>=0&&b>0&&c>0){
            return true;
        }


        return bool;
    }
    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_phone1.setText(items.get(position).split(":")[1]);
        }
    };
    private AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_phone2.setText(items.get(position).split(":")[1]);
        }
    };
    private AdapterView.OnItemClickListener itemClickListener3 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_phone3.setText(items.get(position).split(":")[1]);
        }
    };
    private AdapterView.OnItemClickListener itemClickListener4 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_phone4.setText(items.get(position).split(":")[1]);
        }
    };
    private AdapterView.OnItemClickListener itemClickListener5 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            mSpinerPopWindow.dismiss();
            onekey_phone5.setText(items.get(position).split(":")[1]);
        }
    };

    private void MTClick(final EditText onekey_phone, ImageView mt_phone, final List<String> beans,
                         final AdapterView.OnItemClickListener listener) {

        mt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpinerPopWindow = new SpinerPopWindow<String>(mContext, beans,listener);
                mSpinerPopWindow.setOnDismissListener(dismissListener);
                mSpinerPopWindow.setWidth(onekey_phone.getWidth());
                mSpinerPopWindow.showAsDropDown(onekey_phone);
            }
        });
    }

    private void initData() {
//        setPhoneCheck(onekey_phone1);
//        setPhoneCheck(onekey_phone2);
//        setPhoneCheck(onekey_phone3);
//        setPhoneCheck(onekey_phone4);
//        setPhoneCheck(onekey_phone5);
        items = new ArrayList<String>();
        for(OneMTPhoneBean bean:oneMTPhoneBeen){
            items.add(bean.getMtPersonName()+":"+bean.getMtPersonPhone());
//            items.add(bean.getMtPersonPhone());
        }
        if (!TextUtils.isEmpty(oneKeyBasicInfo.useOrgName)) {
            onekey_address.setText(oneKeyBasicInfo.useOrgName);
        }
        if (!TextUtils.isEmpty(oneKeyBasicInfo.useEleName)) {
            onekey_elevator.setText(oneKeyBasicInfo.useEleName);
        }
        phones = new EditText[]{onekey_phone1, onekey_phone2, onekey_phone3, onekey_phone4, onekey_phone5};
        for(int i=0;i<divertPhone.length;i++){
            phones[i].setText(divertPhone[i]);

        }
        onekey_polling.setText((oneKeyBasicInfo.pollingTimes!=0?oneKeyBasicInfo.pollingTimes:1) + "");//轮询次数 默认1次
        onekey_waittime.setText((oneKeyBasicInfo.divertWaitingTime !=0?oneKeyBasicInfo.divertWaitingTime:30)+ "");//等待转呼时间  默认30秒
        onekey_callduration.setText((oneKeyBasicInfo.onphonetime!=0?oneKeyBasicInfo.onphonetime:5)+"");//通话时长 默认5分钟
    }

    private void setPhoneCheck(final EditText onekey_phone) {
        onekey_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() ==0||(s.length()>=5 && s.length() <=11)){
                    if(s.length()==11 && !isMobileNO(s.toString())){
                        onekey_phone.setTextColor(Color.RED);
                        onekey_ckeck.setClickable(false);
                        onekey_ckeck.setBackgroundResource(R.drawable.caiji_bg_disable);
                        return;
                    }
                    onekey_phone.setTextColor(Color.BLACK);
                    onekey_ckeck.setClickable(true);
                    onekey_ckeck.setBackgroundResource(R.drawable.caiji_bg);
                }else{
                    onekey_phone.setTextColor(Color.RED);
                    onekey_ckeck.setClickable(false);
                    onekey_ckeck.setBackgroundResource(R.drawable.caiji_bg_disable);
                }
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
    /**
     * 一键通 基本设置
     * @param num 18939568751,18576474194 用，分隔
     * @param count 轮播次数  1~3，默认1
     * @param waittime 0 10~90，默认30 单位秒   等待时间,为空不变
     * @param onphonetime 5~15，默认5 单位分钟 单次通话时长
     * @param wbstatus 维保状态
     * @param  wbtime  维保时间
     */
    private void oneKeySet(final  int fun,final String num, final String count, final String waittime,
                           final int onphonetime,final int  wbstatus , final  int wbtime) {
                 try {
                     g_fun = fun;
                     g_num = num;
                     g_count = count;
                     g_waittime = waittime;
                     g_onphonetime = onphonetime;
                     g_wbstatus = wbstatus;
                     g_wbtime= wbtime;
                     createLoadingDialog(mContext,"上报中... 设置成功会提示上报ok,没有提示请重新设置").show();
                     new Timer().schedule(new TimerTask() {
                         @Override
                         public void run() {
                             EventBus.getDefault().post( new ToastEvent());
                             dismissLoading();
                         }
                     },10*1000);
                            oneKeyBasicInfo.pollingTimes = Integer.valueOf(g_count);
                            oneKeyBasicInfo.divertWaitingTime=Integer.valueOf(g_waittime);
                            oneKeyBasicInfo.onphonetime = Integer.valueOf(g_onphonetime);
                            oneKeyBasicInfo.status = g_wbstatus;
                            oneKeyBasicInfo.wbTime = g_wbtime;
                            ckeckPhoneNum(g_num);

                            //发布设置的基本信息
                            JSONObject publish = new JSONObject(new LinkedHashMap());
                            switch (fun) {
                                case 51:
                                    publish.put("fun", g_fun);                                 //功能码
                                    publish.put("num", g_num);                                //设置号码  最多设置5个号码
                                    publish.put("housename", OneEleUtil.gbEncoding(oneKeyBasicInfo.useOrgName)); //小区名称  最大值120 ，即30个中文
                                    publish.put("liftname", OneEleUtil.gbEncoding(oneKeyBasicInfo.useEleName));      //电梯名称  最大值80 ，即20个中文
                                    publish.put("count", Integer.valueOf(g_count));             //轮拨次数  取值范围1~3。默认为1。
                                    publish.put("waittime", Integer.valueOf(g_waittime));           //等待时间  取值范围0,10~90 默认30
                                    publish.put("onphonetime", Integer.valueOf(g_onphonetime));    //单次通话时长  取值范围5~15，默认5。
                                    break;

                                case 52:
                                    publish.put("fun", g_fun);
                                    publish.put("num", g_num);
                                    break;

                                case 54:
                                    publish.put("fun", g_fun);
                                    publish.put("wbstatus", g_wbstatus);
                                    publish.put("wbtime", g_wbtime);
                                    break;
                            }

                            String s = publish.toString();
                            Log.e("设置json222",s.substring(1, s.length() - 1));
                            int objToAscii = OneEleUtil.strToASCII(s.substring(1, s.length() - 1));
                            publish.put("checksum", objToAscii);
                             Log.e("set",publish.toString());

                            MqttManager.getInstance().publish("/oneserv/" + oneKeyBasicInfo.deviceId + "/set", 2,
                                    publish.toString().getBytes());
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
        
//        Observable<UserResponse> observable = LoginApiImpl.SubmitSetMsg(new SubmitOKeySetReq(fun,deviceId,num,
//                oneKeyBasicInfo.useOrgName, oneKeyBasicInfo.useEleName,count,waittime,onphonetime,wbstatus,wbtime));
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<UserResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showErrorToast();
//                    }
//
//                    @Override
//                    public void onNext(UserResponse response) {
//                        if(response.resultType ==1){
//                            createLoadingDialog(mContext,"上报中... 设置成功会提示上报ok,没有提示请重新设置").show();
//                            oneKeyBasicInfo.pollingTimes = Integer.valueOf(count);
//                            oneKeyBasicInfo.divertWaitingTime=Integer.valueOf(waittime);
//                            oneKeyBasicInfo.onphonetime = Integer.valueOf(onphonetime);
//                            oneKeyBasicInfo.status = wbstatus;
//                            oneKeyBasicInfo.wbTime = wbtime;
//                            ckeckPhoneNum(num);
//                            try{
//                            //发布设置的基本信息
//                            JSONObject publish = new JSONObject(new LinkedHashMap());
//                            switch (fun) {
//                                case 51:
//                                    publish.put("fun", "51");                                 //功能码
//                                    publish.put("num", num);                                //设置号码  最多设置5个号码
//                                    publish.put("housename", OneEleUtil.gbEncoding(oneKeyBasicInfo.useOrgName)); //小区名称  最大值120 ，即30个中文
//                                    publish.put("liftname", OneEleUtil.gbEncoding(oneKeyBasicInfo.useEleName));      //电梯名称  最大值80 ，即20个中文
//                                    publish.put("count", Integer.valueOf(count));             //轮拨次数  取值范围1~3。默认为1。
//                                    publish.put("waittime", Integer.valueOf(waittime));           //等待时间  取值范围0,10~90 默认30
//                                    publish.put("onphonetime", Integer.valueOf(onphonetime));    //单次通话时长  取值范围5~15，默认5。
//                                    break;
//
//                                case 52:
//                                    publish.put("fun", "52");
//                                    publish.put("num", num);
//                                    break;
//
//                                case 54:
//                                    publish.put("fun", "54");
//                                    publish.put("wbstatus", wbstatus);
//                                    publish.put("wbtime", wbtime);
//                                    break;
//                            }
//
//                            String s = publish.toString();
//                            Log.e("设置json222",s.substring(1, s.length() - 1));
//                            int objToAscii = OneEleUtil.strToASCII(s.substring(1, s.length() - 1));
//                            publish.put("checksum", objToAscii);
//                             Log.e("set",publish.toString());
//
//                            MqttManager.getInstance().publish("/oneserv/" + oneKeyBasicInfo.deviceId + "/set", 2,
//                                    publish.toString().getBytes());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        }
//                    }
//                });
    }
    private void ckeckPhoneNum(String num) {
        if (num.contains(",")) {
            String[] split = num.split(",");
            Log.e("设置号码成功后",num + "    " +  split.length);
            for (int i = 0 ; i < split.length; i++) {
                divertPhone[i] = split[i];
            }
            for (int i = split.length; i < 5; i++) {
                divertPhone[i] = null;
            }
            switch (split.length) {
                case 2:
                    oneKeyBasicInfo.divertPhone1 = split[0];
                    oneKeyBasicInfo.divertPhone2 = split[1];
                    break;
                case 3:
                    oneKeyBasicInfo.divertPhone1 = split[0];
                    oneKeyBasicInfo.divertPhone2 = split[1];
                    oneKeyBasicInfo.divertPhone3 = split[2];
                    break;
                case 4:
                    oneKeyBasicInfo.divertPhone1 = split[0];
                    oneKeyBasicInfo.divertPhone2 = split[1];
                    oneKeyBasicInfo.divertPhone3 = split[2];
                    oneKeyBasicInfo.divertPhone4 = split[3];
                    break;
                case 5:
                    oneKeyBasicInfo.divertPhone1 = split[0];
                    oneKeyBasicInfo.divertPhone2 = split[1];
                    oneKeyBasicInfo.divertPhone3 = split[2];
                    oneKeyBasicInfo.divertPhone4 = split[3];
                    oneKeyBasicInfo.divertPhone5 = split[4];
                    break;
            }
        }
        else{
            oneKeyBasicInfo.divertPhone1 = num;
            divertPhone[0] = num;
        }
    }

    /**
     * @param listView
     * @param imageView
     * @param mList
     */
    private void onMtPhoneClick(final ListView listView, ImageView imageView, final List<OneMTPhoneBean> mList, final EditText editText){
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oneMTPhoneAdapter = new OneMTPhoneAdapter(mList,v.getContext());
                Log.e("转呼号码1",mList.size()+"");
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(oneMTPhoneAdapter);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(mList.get(position).getMtPersonPhone());
                listView.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.tv_one_key_set_select)
    public void onekeySelectOnClick(){
        View view = LayoutInflater.from(OneKeySetActivity.this).inflate(R.layout.view_one_key_serach, null);
        final EditText etSeachOrgName = (EditText) view.findViewById(R.id.search_org_name);
        final EditText etSeachOneKeyName = (EditText) view.findViewById(R.id.search_onekey_name);

        new AlertDialog.Builder(OneKeySetActivity.this)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(etSeachOrgName.getText()) && TextUtils.isEmpty(etSeachOneKeyName.getText())) {
                            ToastUtil.show(OneKeySetActivity.this, "请您输入一键通查询信息！", Toast.LENGTH_SHORT);
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
                        showToast();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                        showErrorToast();
                    }
                    @Override
                    public void onNext(UserResponse<OneKeySetResp> result) {
                        Log.e("sfae",result.toString());
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
                            mSpinerPopWindow.setWidth(onekey_set_select.getWidth());
                            mSpinerPopWindow.showAsDropDown(onekey_set_select);
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
            onekey_set_select.setText(onekeyList.get(position).oneKeyName);
            use_unit_name.setText(onekeyList.get(position).useOrgName);
            getMtPersonInfo(onekeyList.get(position).useOrgId);
            deviceId = onekeyList.get(position).deviceId;
            btn_one_key_basic.setClickable(true);
            btn_one_key_del.setClickable(true);
            btn_one_key_maintenacnce.setClickable(true);
            btn_one_key_basic.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
            btn_one_key_del.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
            btn_one_key_maintenacnce.setBackground(getResources().getDrawable(R.drawable.caiji_bg));
        }
    };
    /**
     * 获取维保员电话
     */
    private void getMtPersonInfo(int useOrgId) {
        Observable<UserResponse<ArrayList<OneMTPhoneBean>>> observable = LoginApiImpl.getMtPerson(new MtpersonReq(useOrgId));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<ArrayList<OneMTPhoneBean>>>() {
                    @Override
                    public void onCompleted() {

                    }
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(UserResponse<ArrayList<OneMTPhoneBean>> result) {
                        Log.e("appendData",result.appendData.toString());
                       oneMTPhoneBeen = result.appendData;
                    }
                });
    }

    /**
     *  获取一键通基础信息
     */
    private void getOneKeyPhone(String deviceId,final int type) {
        Observable<UserResponse<OneKeyBasicInfo>> observable = LoginApiImpl.getOKeyBasic(new OKeyBasicReq(deviceId));
        Log.e("size",new OKeyBasicReq(deviceId).toString());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<OneKeyBasicInfo>>() {
                    @Override
                    public void onCompleted() {
                        showToast();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorToast();
                    }

                    @Override
                    public void onNext(UserResponse<OneKeyBasicInfo> result){
                        oneKeyBasicInfo = result.appendData;
                        Log.e("size",oneKeyBasicInfo.toString());
                        divertPhone[0] = (oneKeyBasicInfo.divertPhone1 != null)?oneKeyBasicInfo.divertPhone1:null;
                        divertPhone[1] = (oneKeyBasicInfo.divertPhone2 != null)?oneKeyBasicInfo.divertPhone2:null;
                        divertPhone[2] = (oneKeyBasicInfo.divertPhone3 != null)?oneKeyBasicInfo.divertPhone3:null;
                        divertPhone[3] = (oneKeyBasicInfo.divertPhone4 != null)?oneKeyBasicInfo.divertPhone4:null;
                        divertPhone[4] = (oneKeyBasicInfo.divertPhone5 != null)?oneKeyBasicInfo.divertPhone5:null;

                        Log.e("size",oneMTPhoneBeen.size()+"");
                        switch(type){
                            case TYPE_SET:
//                                initBasicView();
                                initView();
                                break;
                            case TYPE_DELETE:
                                deleteNumber();
                                break;
                            case TYPE_MAINTEN:
                                maintenance();
                                break;
                        }
                    }
                });
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
        }
    };

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

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(ToastEvent event){
        ToastUtil.show(mContext,"设置失败，请重试",Toast.LENGTH_LONG);
    }

    /**
     * 订阅接收到的消息
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent message) {
        if(deviceId.equals(message.deviceId)){
            dismissLoading();
            Log.e("订阅接收到的话柄采集", message.message + "   " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())));
            ToastUtil.show(OneKeySetActivity.this, "上报"+message.message, Toast.LENGTH_SHORT);
            submitToServer();
//            finish();
        }
    }

    private void submitToServer() {
        Observable<UserResponse> observable = LoginApiImpl.SubmitSetMsg(new SubmitOKeySetReq(g_fun,deviceId,g_num,
                oneKeyBasicInfo.useOrgName, oneKeyBasicInfo.useEleName,g_count,g_waittime,g_onphonetime,g_wbstatus,g_wbtime));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(mContext,"设置失败，请重试",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(UserResponse response) {
                        if(response.resultType ==1){
                            ToastUtil.show(mContext,"设置成功",Toast.LENGTH_LONG);
                        }
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
