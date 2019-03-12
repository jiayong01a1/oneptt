package onerfid.com.oneptt.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.api.LoginApiImpl;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.updatebean.UpdateApkBean;
import onerfid.com.oneptt.bean.updatebean.UpdateReq;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.common.UpdateManager;
import onerfid.com.oneptt.ui.login.LoginActivity;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.ToastUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiayong on 2018/4/12.
 */
public class PaneFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.btn_quit_login)
    Button quit_login;
    @BindView(R.id.iv_menu_login)
    ImageView image_login;
    @BindView(R.id.tv_menu_login)
    TextView show_name;
    @BindView(R.id.rl_modify_pwd)
    RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.left_menu_layout, null);
        ButterKnife.bind(this,contentView);
        initListener();
        return contentView;
    }

    private void initListener() {
        quit_login.setOnClickListener(this);
        image_login.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_quit_login:
                 loginOut();
                 break;
            case R.id.rl_modify_pwd:
                modifyPwd();
                break;
            case R.id.iv_menu_login:
                loginIn();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loginIn();
    }

    private void loginIn() {
        if(SharedPreferenceUtil.getInstance().isLogin()){
            show_name.setText(SharedPreferenceUtil.getInstance().getUserResult().name);
        }else{
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void modifyPwd() {
    }


    private void loginOut() {
        if(SharedPreferenceUtil.getInstance().isLogin()){

            new AlertDialog.Builder(getContext()).setTitle("确定退出登录吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferenceUtil.getInstance().Login(false);
                            SharedPreferenceUtil.getInstance().clear();
                            show_name.setText("点击登录");
                            AppManager.getAppManager().finishActivity();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ToastUtil.show(getActivity(), "请先登录！", Toast.LENGTH_SHORT);
        }
        }


}
