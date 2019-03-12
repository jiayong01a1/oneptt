package onerfid.com.oneptt.common;

import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.greenrobot.eventbus.EventBus;

import onerfid.com.oneptt.event.MessageEvent;

/**
 * 管理mqtt的连接,发布,订阅,断开连接, 断开重连等操作
 */
public class MqttManager {

    String tag = "MqttManager";

    // 单例
    private static MqttManager mInstance = null;
    private MqttClient client;
    private MqttConnectOptions conOpt;
    private boolean clean = true;

    public static MqttManager getInstance() {
        if (null == mInstance) {
            mInstance = new MqttManager();
        }
        return mInstance;
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 创建Mqtt 连接
     * @param brokerUrl Mqtt服务器地址(tcp://xxxx:1863)
     * @param userName  用户名
     * @param password  密码
     * @param clientId  clientId
     * @return
     */
    public boolean creatConnect(String brokerUrl, String userName, String password, String clientId) {
        boolean flag = false;
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

        try {
            conOpt = new MqttConnectOptions();
            conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            conOpt.setCleanSession(clean);
            if (password != null) {
                conOpt.setPassword(password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(userName);
            }

            client = new MqttClient(brokerUrl, clientId, dataStore);

            client.setCallback(mqttCallback);
            flag = doConnect();
        } catch (MqttException e) {
            Log.e(tag,e.getMessage());
        }

        return flag;
    }

    /**
     * 建立连接
     * @return
     */
    public boolean doConnect() {
        boolean flag = false;
        if (client != null) {
            try {
                client.connect(conOpt);
                Log.e(tag,"Connected to " + client.getServerURI() + " with client ID " + client.getClientId());
                flag = true;
            } catch (Exception e) {
                Log.e("MQTT连接失败原因",e.toString());
            }
        }
        return flag;
    }

    /**
     * Publish / send a message to an MQTT server
     * @param topicName the name of the topic to publish to
     * @param qos       the quality of service to delivery the message at (0,1,2)
     * @param payload   the set of bytes to send to the MQTT server
     * @return boolean
     */
    public boolean publish(String topicName, int qos, byte[] payload) {

        boolean flag = false;

        if (client != null && client.isConnected()) {
            Log.e(tag,"Publishing to topic \"" + topicName + "\" qos " + qos);

            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);

            try {
                client.publish(topicName, message);
                flag = true;
            } catch (MqttException e) {

            }

        }

        return flag;
    }

    /**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     *
     * @param topicName to subscribe to (can be wild carded)
     * @param qos       the maximum quality of service to receive messages at for this subscription
     * @return boolean
     */
    public boolean subscribe(String topicName, int qos) {

        boolean flag = false;

        if (client != null && client.isConnected()) {

            Log.e(tag,"Subscribing to topic \"" + topicName + "\" qos " + qos);

            // Logger.d("Subscribing to topic \"" + topicName + "\" qos " + qos);
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {

            }
        }

        return flag;

    }

    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String str1 = new String(message.getPayload());
            //   /onephone/05d7ff303435484d43136706/check
            Log.e("messageArrived",topic+":"+str1);
            String ids [] = topic.split("/");
            Log.e("messageArrived",ids[2]);
//            /onephone/05d7ff303435484d43136706/caiji:
// {"ID":"05d7ff303435484d43136706","code":"13632723651","checksum":2991}:
// {"ID":"05d7ff303435484d43136706","code":"13632723651","checksum":2991}
            EventBus.getDefault().post(new MessageEvent(str1,"success",ids[2]));
        }
        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }
        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            EventBus.getDefault().post(new MessageEvent("MQTT失去连接,重连","error",""));
        }
    };
}
