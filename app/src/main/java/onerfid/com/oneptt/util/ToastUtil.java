package onerfid.com.oneptt.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import onerfid.com.oneptt.R;

/**
 * Created by jiayong on 2018/4/16.
 */
public class ToastUtil{
    public static void show(Context activity, String massage, int show_length){
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(activity).inflate(R.layout.toast_layout, null);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.tv_toast_util);
        //设置显示的内容
        title.setText(massage);

        Toast toast = new Toast(activity);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 70);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM, 0, 300);
        //toast.setMargin(100,100);
        //设置显示时间
        toast.setDuration(show_length);

        toast.setView(view);
        toast.show();
    }

    public static void show(Context activity, int ResourceId){
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(activity).inflate(R.layout.toast_layout, null);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.tv_toast_util);
        //设置显示的内容
        title.setText(activity.getResources().getString(ResourceId));

        Toast toast = new Toast(activity);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 70);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM, 0, 300);
        //toast.setMargin(100,100);
        //设置显示时间
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(view);
        toast.show();
    }
}
