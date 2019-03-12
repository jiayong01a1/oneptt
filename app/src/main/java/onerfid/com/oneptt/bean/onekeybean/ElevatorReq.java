package onerfid.com.oneptt.bean.onekeybean;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/5/2.
 */
public class ElevatorReq {
    public String  AccessToken;
    public String EmCode;
    public String  EleName;
    public String Address;
    public String  MTOrgName;
    public String  UseOrgName;
    public int PageIndex;
    public int PageSize;

    public ElevatorReq( String eleName, String address) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        EmCode = "";
        EleName = eleName;
        Address = address;
        MTOrgName = "";
        UseOrgName = "";
        PageIndex = 1;
        PageSize = 1000;
    }
}
