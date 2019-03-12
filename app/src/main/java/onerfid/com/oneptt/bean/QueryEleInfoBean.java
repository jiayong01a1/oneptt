package onerfid.com.oneptt.bean;

/**
 * 根据查询条件查询返回bean
 */

public class QueryEleInfoBean {
    public String id;
    public String equipmentName;
    public String equipmentCode;
    public String eleEMCode;
    public int mtOrgId;
    public String mtOrgName;
    public int useOrgId;
    public String useOrgName;
    public String mtLeaderName;
    public String mtLeaderPhone;
    public String eleSecurityMName;
    public String eleSecurityPhone;
    public int chinaAddressID;
    public String regionName;
    public String privateAddress;
    public String fullAddress;
    public String rimid;
    public double lat;
    public double lng;
    public String appId;
    public String serialNo;
    public String serialValid;
    public int liveType;
    public int equipmentStatus;
    public String statusDesc;
    public int eqipmentIoTStatus;
    public int eqipmentRegStatus;
    public String regStatusDesc;
    public boolean isRealIM;
    public boolean isActive;

    @Override
    public String toString() {
        return "QueryEleInfoBean{" +
                "id='" + id + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", equipmentCode='" + equipmentCode + '\'' +
                ", eleEMCode='" + eleEMCode + '\'' +
                ", mtOrgId=" + mtOrgId +
                ", mtOrgName='" + mtOrgName + '\'' +
                ", useOrgId=" + useOrgId +
                ", useOrgName='" + useOrgName + '\'' +
                ", mtLeaderName='" + mtLeaderName + '\'' +
                ", mtLeaderPhone='" + mtLeaderPhone + '\'' +
                ", eleSecurityMName='" + eleSecurityMName + '\'' +
                ", eleSecurityPhone='" + eleSecurityPhone + '\'' +
                ", chinaAddressID=" + chinaAddressID +
                ", regionName='" + regionName + '\'' +
                ", privateAddress='" + privateAddress + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", rimid='" + rimid + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", appId='" + appId + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", serialValid='" + serialValid + '\'' +
                ", liveType=" + liveType +
                ", equipmentStatus=" + equipmentStatus +
                ", statusDesc='" + statusDesc + '\'' +
                ", eqipmentIoTStatus=" + eqipmentIoTStatus +
                ", eqipmentRegStatus=" + eqipmentRegStatus +
                ", regStatusDesc='" + regStatusDesc + '\'' +
                ", isRealIM=" + isRealIM +
                ", isActive=" + isActive +
                '}';
    }
}
