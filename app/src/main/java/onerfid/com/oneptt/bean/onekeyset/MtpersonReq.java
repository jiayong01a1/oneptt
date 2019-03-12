package onerfid.com.oneptt.bean.onekeyset;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/5/3.
 */
public class MtpersonReq {
    public String AccessToken;
    public int OrgId;

    public MtpersonReq(int orgId) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        OrgId = orgId;
    }
}
