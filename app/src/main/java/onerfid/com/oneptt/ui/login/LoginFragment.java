package onerfid.com.oneptt.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.api.LoginApiImpl;
import onerfid.com.oneptt.bean.MqttServerBean;
import onerfid.com.oneptt.bean.UserRequest;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.UserResult;
import onerfid.com.oneptt.bean.updatebean.UpdateApkBean;
import onerfid.com.oneptt.bean.updatebean.UpdateReq;
import onerfid.com.oneptt.common.Constant;
import onerfid.com.oneptt.common.UpdateManager;
import onerfid.com.oneptt.ui.MainActivity;
import onerfid.com.oneptt.ui.PaneFragment;
import onerfid.com.oneptt.util.GsonUtil;
import onerfid.com.oneptt.util.OneEleUtil;
import onerfid.com.oneptt.util.SharedPreferenceUtil;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.com.oneptt.widget.ClearEditText;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiayong on 2018/4/13.
 */
public class LoginFragment extends Fragment {
    @BindView(R.id.cet_login_name)
    ClearEditText login_name;
    @BindView(R.id.cet_login_pwd)
    ClearEditText login_pwd;
    @BindView(R.id.btn_login)
    Button login_btn;
    boolean isNull = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null);
        ButterKnife.bind(this,view);
        checkPermission();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoLogin();
    }

    private void autoLogin() {
        checkUpdate();
        if(SharedPreferenceUtil.getInstance().isLogin()){
            login_name.setText(SharedPreferenceUtil.getInstance().getUser().UserCode);
            login_pwd.setText(SharedPreferenceUtil.getInstance().getUser().Password);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void checkUpdate() {
        int versionCode = 0;
        try {
            versionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        UpdateReq req = new UpdateReq(versionCode);
        Log.e("req",req.toString());
        Observable<UserResponse<UpdateApkBean>> observable = LoginApiImpl.updateVersion(req);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResponse<UpdateApkBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserResponse<UpdateApkBean> response) {
                        Log.e("resp",response.message);
                        if(response.appendData != null){
                            UpdateApkBean bean= response.appendData;
                            Log.e("resp",response.appendData.updateInfo.toString());
                            List<UpdateApkBean.FilesBean> filesBeens = bean.files;
                            UpdateApkBean.FilesBean filesBean = filesBeens.get(0);

                            int version = 0;

                            Log.e("resp",filesBean.toString());
                            try {
                                version= getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
                                if(version < filesBean.programMajor){
                                    UpdateManager updateManager = new UpdateManager(getActivity(),filesBean.fileURL);
                                    updateManager.checkUpdateInfo();
                                }else{

                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                });

    }


    @OnClick(R.id.btn_login)
    public void OnLoginClick(){
        if(ckeckInput()){
            final UserRequest request = new UserRequest(login_name.getText().toString(),login_pwd.getText().toString(),"","");

            Log.e("TAG",request.toString());
            Observable<UserResponse<UserResult> > userObservable = LoginApiImpl.login(request);
            userObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserResponse<UserResult> >() {
                        @Override
                        public void onCompleted() {
                            if(isNull){
                                ToastUtil.show(getActivity(),R.string.login_error);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.show(getActivity(),R.string.login_net_error);
                        }

                        @Override
                        public void onNext(UserResponse<UserResult> result) {
                            if(result.resultType ==1){
                                isNull = false;
                                UserResult userResult = result.appendData;
                                SharedPreferenceUtil.getInstance().Login(true);
                                SharedPreferenceUtil.getInstance().putUser(request);
                                SharedPreferenceUtil.getInstance().putUserResult(userResult);
                                MqttServerBean bean = new MqttServerBean(Constant.ONEDTURL,request.UserCode, OneEleUtil.get32MD5Str(request.UserCode));
                                SharedPreferenceUtil.getInstance().putMqttMsg(bean);
                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                            }else{
                                isNull = true;
                            }

                        }
                    });
        }else{
        }
    }

    private boolean ckeckInput() {
        boolean isInput = false;
        if((login_name.getText().toString().trim() == null ||login_name.getText().toString().trim().isEmpty())
                || (login_pwd.getText().toString().trim() ==null||login_pwd.getText().toString().trim().isEmpty())){
            isInput = false;
        }else{
            SharedPreferenceUtil.getInstance().putUser(
                    new UserRequest(login_name.getText().toString().trim(),
                            login_pwd.getText().toString().trim(),"",""));
            isInput = true;
        }
        return  isInput;
    }


}
