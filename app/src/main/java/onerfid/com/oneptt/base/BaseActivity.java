package onerfid.com.oneptt.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import onerfid.com.oneptt.R;
import onerfid.com.oneptt.util.StatusBarCompat;
import onerfid.com.oneptt.util.ToastUtil;

/**
 * Created by jiayong on 2018/4/24.
 */
public class BaseActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;
    protected Dialog loadingDialog;
//    protected Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        StatusBarCompat.compat(this, getResources().getColor(R.color.light_blue));
        createLoadingDialog(getApplicationContext(),"");
    }

    public void dismissLoading() {
        loadingDialog.dismiss();
    }
    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.iv_loading);
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_loading);// 提示文字
        tipTextView.setText(msg);
        AnimationDrawable ad = (AnimationDrawable) spaceshipImage.getDrawable();
        ad.start();
         loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        return loadingDialog;

    }

}
