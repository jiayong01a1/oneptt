package onerfid.com.oneptt.bean.onehandlebean;

/**
 * Created by jiayong on 2018/4/28.
 */
public class HandleCommitResponse {
    public String mqttUser;
    public String mqttPwd;
    public String serverTime;
    public String deviceId;
    public String phoneNumber;

    @Override
    public String toString() {
        return "HandleCommitResponse{" +
                "mqttUser='" + mqttUser + '\'' +
                ", mqttPwd='" + mqttPwd + '\'' +
                ", serverTime='" + serverTime + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
