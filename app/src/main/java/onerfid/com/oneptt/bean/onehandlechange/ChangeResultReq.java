package onerfid.com.oneptt.bean.onehandlechange;

/**
 * Created by jiayong on 2018/5/7.
 */
public class ChangeResultReq {
    public String AccessToken;
    public String BatchId;

    public ChangeResultReq(String accessToken, String batchId) {
        AccessToken = accessToken;
        BatchId = batchId;
    }
}
