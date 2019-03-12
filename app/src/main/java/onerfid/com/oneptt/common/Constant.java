package onerfid.com.oneptt.common;

/**
 * Created by jiayong on 2018/4/27.
 */
public class Constant {
    public static final String ONEDTURL = "tcp://www.onedt.com.cn";//mqtt服务器
//    public static final String ONEDTURL = "tcp://221.176.156.231";//开封mqtt服务器
    public static final String MQTTURL = "tcp://iot.eclipse.org";//mqtt官网服务器

    //一键通Topic
    public static final String ONE_KEY_COLLECT   = "/onephone/+/caiji";  //一键通采集订阅topic
    public static final String ONE_HANDLE_COLLECT   = "/onehand/+/caiji";  //一键通采集订阅topic

    public static final String ONE_KEY_CHECK   = "/onephone/+/check";  //成功后返回校验
    public static final String ONE_HANDLE_CHECK   = "/onehand/+/check";  //成功后返回校验

    public static final String ONE_KEY_CHILD = "/onephone/+/sendslavename"; //订阅一键通子机名称
}
