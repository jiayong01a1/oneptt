package onerfid.com.oneptt.bean.onekeyset;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/5/3.
 */
public class OKeyBasicReq {
    public String AccessToken;
    public String DeviceId;

    public OKeyBasicReq(String deviceId) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        DeviceId = deviceId;
    }

    @Override
    public String toString() {
        return "OKeyBasicReq{" +
                "AccessToken='" + AccessToken + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                '}';
    }
}
