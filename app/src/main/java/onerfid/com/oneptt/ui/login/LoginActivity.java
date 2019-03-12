package onerfid.com.oneptt.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.util.StatusBarCompat;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity{
    @BindView(R.id.tv_default_top)
    TextView titile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        setTitle();
        StatusBarCompat.compat(this, R.color.toolbar_bg);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_login_content,new LoginFragment()).commit();
    }

    private void setTitle() {
        titile.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        AppManager.getAppManager().finishAllActivity();
    }
}
