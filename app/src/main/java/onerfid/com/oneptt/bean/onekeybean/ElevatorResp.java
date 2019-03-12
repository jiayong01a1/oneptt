package onerfid.com.oneptt.bean.onekeybean;

import java.util.ArrayList;

import onerfid.com.oneptt.bean.QueryEleInfoBean;

/**
 * Created by jiayong on 2018/5/2.
 */
public class ElevatorResp {
    public int total;
    public ArrayList<QueryEleInfoBean> eleList;
    @Override
    public String toString() {
        return "ElevatorResp{" +
                "total=" + total +
                ", eleList=" + eleList +
                '}';
    }
}
