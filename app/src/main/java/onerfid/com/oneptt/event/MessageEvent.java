package onerfid.com.oneptt.event;

/**
 * Created by jiayong on 2018/4/27.
 */
public class MessageEvent {
    public String message;
    public String type;
    public String deviceId;

    public MessageEvent(String message, String type, String deviceId) {
        this.message = message;
        this.type = type;
        this.deviceId = deviceId;
    }
}
