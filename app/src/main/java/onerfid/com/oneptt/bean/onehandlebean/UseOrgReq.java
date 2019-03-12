package onerfid.com.oneptt.bean.onehandlebean;

/**
 * Created by jiayong on 2018/4/26.
 */
public class UseOrgReq {
    public String AccessToken;
    public String OrgName;
    public String OrderBy;
    public String PageIndex;
    public String PageSize;

    public UseOrgReq(String accessToken) {
        AccessToken = accessToken;
        OrgName = "";
        OrderBy = "";
        PageIndex = " 1";
        PageSize = "1000";
    }

    @Override
    public String toString() {
        return "UserOrgReq{" +
                "AccessToken='" + AccessToken + '\'' +
                ", OrgName='" + OrgName + '\'' +
                ", OrderBy='" + OrderBy + '\'' +
                ", PageIndex='" + PageIndex + '\'' +
                ", PageSize='" + PageSize + '\'' +
                '}';
    }
}
