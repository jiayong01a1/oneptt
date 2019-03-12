package onerfid.com.oneptt.bean;

/**
 * Created by jiayong on 2018/4/10.
 */
public class UserRequest{
    public String UserCode;
    public String Password;
    public String Org;
    public String APPId;

    public UserRequest(String userCode, String password, String org, String APPId) {
        UserCode = userCode;
        Password = password;
        Org = org;
        this.APPId = APPId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "UserCode='" + UserCode + '\'' +
                ", Password='" + Password + '\'' +
                ", Org='" + Org + '\'' +
                ", APPId='" + APPId + '\'' +
                '}';
    }
}
