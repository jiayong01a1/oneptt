package onerfid.com.oneptt.bean.onekeyset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 一键通设置 --> 维保人员电话bean
 */

public class OneMTPhoneBean {


    /**
     * mtId : 1
     * mtType : 1
     * mtPersonName : 永城和平酒店值班室
     * mtPersonPhone : 18822880157
     */

    private int mtId;
    private int mtType;
    private String mtPersonName;
    private String mtPersonPhone;

    public static OneMTPhoneBean objectFromData(String str) {

        return new Gson().fromJson(str, OneMTPhoneBean.class);
    }

    public static List<OneMTPhoneBean> arrayOneMTPhoneBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<OneMTPhoneBean>>() {

        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public int getMtId() {
        return mtId;
    }

    public void setMtId(int mtId) {
        this.mtId = mtId;
    }

    public int getMtType() {
        return mtType;
    }

    public void setMtType(int mtType) {
        this.mtType = mtType;
    }

    public String getMtPersonName() {
        return mtPersonName;
    }

    public void setMtPersonName(String mtPersonName) {
        this.mtPersonName = mtPersonName;
    }

    public String getMtPersonPhone() {
        return mtPersonPhone;
    }

    public void setMtPersonPhone(String mtPersonPhone) {
        this.mtPersonPhone = mtPersonPhone;
    }

    @Override
    public String toString() {
        return "OneMTPhoneBean{" +
                "mtId=" + mtId +
                ", mtType=" + mtType +
                ", mtPersonName='" + mtPersonName + '\'' +
                ", mtPersonPhone='" + mtPersonPhone + '\'' +
                '}';
    }
}
