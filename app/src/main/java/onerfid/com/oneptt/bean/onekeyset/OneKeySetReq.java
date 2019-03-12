package onerfid.com.oneptt.bean.onekeyset;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/5/3.
 */
public class OneKeySetReq {
    public String AccessToken;
    public String OrgName;
    public String OneKeyName;
    public String OrderBy;
    public int PageIndex;
    public int PageSize;

    public OneKeySetReq(String orgName, String oneKeyName) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        OrgName = orgName;
        OneKeyName = oneKeyName;
        OrderBy = "";
        PageIndex = 1;
        PageSize =1000;
    }
}
