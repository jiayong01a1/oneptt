package onerfid.com.oneptt.bean.onekeyset;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onerfid.com.oneptt.R;

/**
 * 一键通设置 --> 维保人员电话
 */

public class OneMTPhoneAdapter extends BaseAdapter {

    private List<OneMTPhoneBean> items;
    private LayoutInflater inflater;
    private static Map<Integer, View> m = new HashMap<>();
    private Context context1;

    public OneMTPhoneAdapter(List<OneMTPhoneBean> items, Context context1) {
        this.items = items;
        this.context1 = context1;
    }

    @Override
    public int getCount()  {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position)  {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)  {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OneMTPhoneAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new OneMTPhoneAdapter.ViewHolder();
            convertView = inflater.from(parent.getContext()).inflate(R.layout.item_mt_phone, null);
            holder.tvMtPersonName = (TextView) convertView.findViewById(R.id.mt_person_name_tv);
            holder.tvMtPersonPhone = (TextView) convertView.findViewById(R.id.mt_person_phone_tv);
            convertView.setTag(holder);
        } else {
            holder = (OneMTPhoneAdapter.ViewHolder) convertView.getTag();
        }

        OneMTPhoneBean oneMTPhoneBean = items.get(position);

        if (!TextUtils.isEmpty(oneMTPhoneBean.getMtPersonName())) {
            holder.tvMtPersonName.setText(oneMTPhoneBean.getMtPersonName());
        }

        if (!TextUtils.isEmpty(oneMTPhoneBean.getMtPersonPhone())) {
            holder.tvMtPersonPhone.setText(oneMTPhoneBean.getMtPersonPhone());
        }

        m.put(position, convertView);
        return convertView;
    }


    class ViewHolder {

        public TextView tvMtPersonName;
        public TextView tvMtPersonPhone;


    }
}
