package onerfid.com.oneptt.bean.onekeyset;

import onerfid.com.oneptt.util.SharedPreferenceUtil;

/**
 * Created by jiayong on 2018/5/4.
 */
public class SubmitOKeySetReq {
    public String AccessToken;
    public int fun;//功能码  51 基本设置   52  删除号码   54 维保
    public String id;
    public String num;
    public String housename;
    public String liftname;
    public String count;
    public String waittime;
    public int onphonetime;
    public String username;
    public String password;
    public int wbstatus;
    public int wbtime;

    public SubmitOKeySetReq(int fun, String id, String num, String housename, String liftname, String count, String waittime, int onphonetime, int wbstatus, int wbtime) {
        AccessToken = SharedPreferenceUtil.getInstance().getUserResult().access_Token;
        this.fun = fun;
        this.id = id;
        this.num = num;
        this.housename = housename;
        this.liftname = liftname;
        this.count = count;
        this.waittime = waittime;
        this.onphonetime = onphonetime;
        this.username = SharedPreferenceUtil.getInstance().getUserResult().name;
        this.password = SharedPreferenceUtil.getInstance().getUser().Password;
        this.wbstatus = wbstatus;
        this.wbtime = wbtime;
    }
}
