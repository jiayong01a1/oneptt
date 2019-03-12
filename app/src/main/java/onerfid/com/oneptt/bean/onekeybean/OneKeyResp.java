package onerfid.com.oneptt.bean.onekeybean;

/**
 * Created by jiayong on 2018/5/3.
 */
public class OneKeyResp {
    public String oneHandleId;
    public String oneKeyId;
    public String mqttUser;
    public String mqttPwd;
    public String serverTime;

    @Override
    public String toString() {
        return "OneKeyResp{" +
                "oneHandleId='" + oneHandleId + '\'' +
                ", oneKeyId='" + oneKeyId + '\'' +
                ", mqttUser='" + mqttUser + '\'' +
                ", mqttPwd='" + mqttPwd + '\'' +
                ", serverTime='" + serverTime + '\'' +
                '}';
    }
}
