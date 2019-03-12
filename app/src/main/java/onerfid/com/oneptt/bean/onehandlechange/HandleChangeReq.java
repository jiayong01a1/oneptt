package onerfid.com.oneptt.bean.onehandlechange;

/**
 * Created by jiayong on 2018/5/7.
 */
public class HandleChangeReq {
    public String AccessToken;
    public String DeviceId;
    public int UseOrgId;
    public String UseOrgName;
    public String OldDeviceId;

    public HandleChangeReq(String accessToken, String deviceId, int useOrgId, String useOrgName, String oldDeviceId) {
        AccessToken = accessToken;
        DeviceId = deviceId;
        UseOrgId = useOrgId;
        UseOrgName = useOrgName;
        OldDeviceId = oldDeviceId;
    }
}
