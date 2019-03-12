package onerfid.com.oneptt.bean.onekeyset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 一键通设置的基本信息
 */

public class OneKeyBasicInfo {

    /**
     * id : f763cdc8-9068-4ae6-bc2c-5cab62ab6deb
     * deviceId : dd2020606910ee2
     * deviceName : 一键通1144
     * pollingTimes : 0
     * useOrgName : 永城和平酒店
     * useEleName : useEleName
     * status : 0
     * wbTime : 0
     * divertWaitingTime : 0
     * onphonetime : 0
     * softwareVersion : softwareVersion
     * hardtwareVersion : hardtwareVersion
     * simCardNo : simCardNo
     * divertPhone1 : divertPhone1
     * divertPhone2 : divertPhone2
     * divertPhone3 : divertPhone3
     * divertPhone4 : divertPhone4
     * divertPhone5 : divertPhone5
     */
    public String id;
    public String deviceId;
    public String deviceName;
    public int pollingTimes;
    public String useOrgName;
    public String useEleName;
    public int status;
    public int wbTime;
    public int divertWaitingTime;
    public int onphonetime;
    public String softwareVersion;
    public String hardtwareVersion;
    public String simCardNo;
    public String divertPhone1;
    public String divertPhone2;
    public String divertPhone3;
    public String divertPhone4;
    public String divertPhone5;
    public String handID;

    @Override
    public String toString() {
        return "OneKeyBasicInfo{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", pollingTimes=" + pollingTimes +
                ", useOrgName='" + useOrgName + '\'' +
                ", useEleName='" + useEleName + '\'' +
                ", status=" + status +
                ", wbTime=" + wbTime +
                ", divertWaitingTime=" + divertWaitingTime +
                ", onphonetime=" + onphonetime +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", hardtwareVersion='" + hardtwareVersion + '\'' +
                ", simCardNo='" + simCardNo + '\'' +
                ", divertPhone1='" + divertPhone1 + '\'' +
                ", divertPhone2='" + divertPhone2 + '\'' +
                ", divertPhone3='" + divertPhone3 + '\'' +
                ", divertPhone4='" + divertPhone4 + '\'' +
                ", divertPhone5='" + divertPhone5 + '\'' +
                ", handID='" + handID + '\'' +
                '}';
    }
}
