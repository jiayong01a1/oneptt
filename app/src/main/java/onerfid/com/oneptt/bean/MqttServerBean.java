package onerfid.com.oneptt.bean;

/**
 * Created by jiayong on 2018/4/27.
 */
public class MqttServerBean {
    public String serverAddress;
    public String serverName;
    public String serverPwd;

    public MqttServerBean(String serverAddress, String serverName, String serverPwd) {
        this.serverAddress = serverAddress;
        this.serverName = serverName;
        this.serverPwd = serverPwd;
    }
}
