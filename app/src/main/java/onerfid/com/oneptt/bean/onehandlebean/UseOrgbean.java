package onerfid.com.oneptt.bean.onehandlebean;

import java.util.ArrayList;

/**
 * Created by jiayong on 2018/4/26.
 */
public class UseOrgbean {
    public int total;
    public ArrayList<OrgBean> orgList;

    @Override
    public String toString() {
        return "UseOrgbean{" +
                "total=" + total +
                ", orgList=" + orgList +
                '}';
    }
}

