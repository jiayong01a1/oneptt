package onerfid.com.oneptt.bean.onekeybean;

/**
 * Created by jiayong on 2018/4/28.
 */
public class OneHandleBean {
    /**
     * id : e9d1a893-440a-43a5-bc0a-7a5c39b6ebf7
     * deviceId : ab2034963691df
     * useOrgId : 136
     * useOrgName : 永城和平酒店
     * mqttUser : null
     * mqttPwd : null
     */

    public String id;
    public String deviceId;
    public int useOrgId;
    public String useOrgName;
    public String mqttUser;
    public String mqttPwd;
    public String phoneNumber;

    @Override
    public String toString() {
        return "OneHandleBean{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", useOrgId=" + useOrgId +
                ", useOrgName='" + useOrgName + '\'' +
                ", mqttUser='" + mqttUser + '\'' +
                ", mqttPwd='" + mqttPwd + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
