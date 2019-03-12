package onerfid.com.oneptt.bean.onehandlebean;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/4/27.
 */
public class SimHandleReq {
    public String AccessToken;
    public String AppId;
    public String ValidCode;
    public String SimCardNo;

    public SimHandleReq(String validCode) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        AppId = SharedPreferenceUtil.getInstance().getUserResult().appId;
        ValidCode = validCode;
        SimCardNo = validCode;
    }
}
