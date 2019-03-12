package onerfid.com.oneptt.bean.updatebean;

/**
 * Created by jiayong on 2018/5/10.
 */
public class UpdateReq {
    public String PCID;
    public int UpdateType;

    public UpdateReq(int updateType) {
        this.PCID = "e47872dc-3154-e811-80e9-96b8ed11dd4e";
        UpdateType = updateType;
    }

    @Override
    public String toString() {
        return "Updatereq{" +
                "PCID='" + PCID + '\'' +
                ", UpdateType=" + UpdateType +
                '}';
    }
}
