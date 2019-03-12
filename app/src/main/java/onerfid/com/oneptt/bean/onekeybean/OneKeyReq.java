package onerfid.com.oneptt.bean.onekeybean;

import java.util.ArrayList;

/**
 * Created by jiayong on 2018/5/2.
 */
public class OneKeyReq{
    public String AccessToken;
    public String OneHandleId;
    public String OneKeyId;
    public int  UseOrgId;
    public String OneKeyDeviceName;
    public String SimCardNo;
    public ArrayList<ElevatorBean> ELes;

    @Override
    public String toString() {
        return "OneKeyReq{" +
                "AccessToken='" + AccessToken + '\'' +
                ", OneHandleId='" + OneHandleId + '\'' +
                ", OneKeyId='" + OneKeyId + '\'' +
                ", UseOrgId=" + UseOrgId +
                ", OneKeyDeviceName='" + OneKeyDeviceName + '\'' +
                ", SimCardNo='" + SimCardNo + '\'' +
                ", ELes=" + ELes +
                '}';
    }
}
