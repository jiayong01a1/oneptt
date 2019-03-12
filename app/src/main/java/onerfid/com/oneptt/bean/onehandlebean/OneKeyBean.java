package onerfid.com.oneptt.bean.onehandlebean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * onekey mqtt返回
 */

public class OneKeyBean {


    /**
     * ID : 一键通的ID号
     * code : 验证码
     * checksum : 123
     */

    private String ID;
    private String code;
    private int checksum;

    public static OneKeyBean objectFromData(String str) {
        return new Gson().fromJson(str, OneKeyBean.class);
    }

    public static List<OneKeyBean> arrayOneKeyBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<OneKeyBean>>() {

        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public String getID() {
        return ID;
    }

    public String getCode() {
        return code;
    }

    public int getChecksum() {
        return checksum;
    }
}
