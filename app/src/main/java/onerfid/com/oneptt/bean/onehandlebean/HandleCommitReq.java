package onerfid.com.oneptt.bean.onehandlebean;

/**
 * Created by jiayong on 2018/4/28.
 */
public class HandleCommitReq {
    public String AccessToken;
    public int UseOrgId;
    public String DeviceId;
    public String UseOrgName;
    public String PhoneNumber;

    public HandleCommitReq(String accessToken, int useOrgId, String deviceId, String useOrgName, String phoneNumber) {
        AccessToken = accessToken;
        UseOrgId = useOrgId;
        DeviceId = deviceId;
        UseOrgName = useOrgName;
        PhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "HandleCommitReq{" +
                "AccessToken='" + AccessToken + '\'' +
                ", UseOrgId=" + UseOrgId +
                ", DeviceId='" + DeviceId + '\'' +
                ", UseOrgName='" + UseOrgName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                '}';
    }
}
