package onerfid.com.oneptt.bean.onehandlechange;

/**
 * Created by jiayong on 2018/5/7.
 */
public class OneHandleOutput {
    public String id;
    public String deviceId;
    public int useOrgId;
    public String useOrgName;
    public String mqttUser ;
    public String mqttPwd ;
    public String serverTime;

    @Override
    public String toString() {
        return "OneHandleOutput{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", useOrgId=" + useOrgId +
                ", useOrgName='" + useOrgName + '\'' +
                ", mqttUser='" + mqttUser + '\'' +
                ", mqttPwd='" + mqttPwd + '\'' +
                ", serverTime='" + serverTime + '\'' +
                '}';
    }
}
