package onerfid.com.oneptt.bean.onekeybean;

/**
 * Created by jiayong on 2018/4/28.
 */
public class OneHandleReq {
    public String AccessToken;
    public String OrgName;
    public int OrgId;
    public String OrderBy;
    public String PageIndex;
    public String PageSize;

    public OneHandleReq(String accessToken,int orgId) {
        AccessToken = accessToken;
        OrgId = orgId;
        OrgName = "";
        OrderBy = "";
        PageIndex = " 1";
        PageSize = "1000";
    }

    @Override
    public String toString() {
        return "OneHandleReq{" +
                "AccessToken='" + AccessToken + '\'' +
                ", OrgName='" + OrgName + '\'' +
                ", OrgId=" + OrgId +
                ", OrderBy='" + OrderBy + '\'' +
                ", PageIndex='" + PageIndex + '\'' +
                ", PageSize='" + PageSize + '\'' +
                '}';
    }
}
