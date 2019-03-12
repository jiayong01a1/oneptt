package onerfid.com.oneptt.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import onerfid.com.oneptt.R;
import onerfid.com.oneptt.common.AppManager;
import onerfid.com.oneptt.ui.pusktotalk.OneHandleChangeActivity;
import onerfid.com.oneptt.ui.pusktotalk.OneHandleCollectActivity;
import onerfid.com.oneptt.ui.pusktotalk.OneKeyCollectActivity;
import onerfid.com.oneptt.ui.pusktotalk.OneKeySetActivity;
import onerfid.com.oneptt.ui.pusktotalk.SetChildNameActivity;
import onerfid.com.oneptt.util.StatusBarCompat;
import onerfid.com.oneptt.util.ToastUtil;
import onerfid.jy.com.moudle_slidingmenu.SlidingMenu;
import onerfid.jy.com.moudle_slidingmenu.app.SlidingFragmentActivity;

/**
 * Created by jiayong on 2018/5/3.
 */
public  class MainActivity extends SlidingFragmentActivity {
    @BindView(R.id.one_handle_collect_ll)
    LinearLayout handle;
    @BindView(R.id.one_handle_change_ll)
    LinearLayout change;
    @BindView(R.id.one_key_collect_ll)
    LinearLayout key;
    @BindView(R.id.one_key_set_ll)
    LinearLayout set;
    @BindView(R.id.one_key_set_child_ll)
    LinearLayout set_child;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, getResources().getColor(R.color.light_blue));
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mContext = this;
        initLeftMenu();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
//        mToolbar.inflateMenu(R.menu.main_menu);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch(item.getItemId()){
//                    case R.id.server_mqtt:
//                        ToastUtil.show(mContext,"设置服务器为Mqtt官网", Toast.LENGTH_LONG);
//                        break;
//                    case R.id.server_onedt:
//                        ToastUtil.show(mContext,"设置服务器为Onedt", Toast.LENGTH_LONG);
//                        break;
//                }
//                return false;
//            }
//        });
    }

    /**
     * 总机采集
     * */
    @OnClick(R.id.one_handle_collect_ll)
    public void onClickHandle(){
        jumpActivity(OneHandleCollectActivity.class);
    }
    /**
     * 总机采集
     * */
    @OnClick(R.id.one_handle_change_ll)
    public void onClickChange(){
        jumpActivity(OneHandleChangeActivity.class);
    }
    /**
     * 一键通采集
     * */
    @OnClick(R.id.one_key_collect_ll)
    public void onClickKey(){
        jumpActivity(OneKeyCollectActivity.class);
    }
    /**
     * 一键通设置
     **/
    @OnClick(R.id.one_key_set_ll)
    public void onClickKeySet(){
        jumpActivity(OneKeySetActivity.class);
    }

    /**
     *  一键通子机名称设置
     * */
    @OnClick(R.id.one_key_set_child_ll)
    public void onSetChildName(){
        jumpActivity(SetChildNameActivity.class);
    }

    private void jumpActivity(Class clazz) {
        mContext.startActivity(new Intent(mContext,clazz));
    }

    private void initLeftMenu() {
        Fragment leftMenuFragment = new PaneFragment();
        setBehindContentView(R.layout.left_menu_frame);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_left_menu, leftMenuFragment).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(null);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(0.0f);

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