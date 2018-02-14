package com.android_examples.Connect2ControlHome;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Asha.Devaraja on 11/10/2016.
 */
public class mqttcommunication implements IMqttActionListener, MqttCallback, MqttTraceHandler {
    String _IP = "";
    String _port = "";
    String _name = "";
    String _Password = "";
    String _clientid = "";
    String _macAddress = "";
    MqttAndroidClient _client=null;
    boolean isConnectSuccess = false;
    mqttCallbacks _mqttListener = null;
    Boolean sendConnectNotiification  = false;

    public mqttcommunication( ) {
        Log.i(this.getClass().getCanonicalName(), "Creating MQTT...");
    }

    public boolean open(Context context)
    {
        String Url = "tcp://" + _IP + ":" + _port;
        MemoryPersistence memPer = new MemoryPersistence();

        Log.i(this.getClass().getCanonicalName(), "MQTT URL : "+Url);

        if(_client!=null)
        {
            _client.unregisterResources();
            _client.close();
            _client = null;
        }

        _client = new MqttAndroidClient(context, Url, "mac_"+_macAddress, memPer);

        if(_client== null)
        {
            Log.i(this.getClass().getCanonicalName(), "MQTT init failed...");
            return false;
        }

        return true;
    }

    public boolean connect ( mqttCallbacks callback )
    {
        _mqttListener = callback;
        MqttConnectOptions authen = new MqttConnectOptions();

        authen.setCleanSession(true);
        authen.setConnectionTimeout(5);
        authen.setKeepAliveInterval(0);
        authen.setUserName(_name);
        authen.setPassword(_Password.toCharArray());

        sendConnectNotiification = true;

        try {
            _client.connect(authen, null, this);
        }catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to connect mqtt " + e);
            return false;
        }

        return true;
    }

    public boolean publish(String Topic, String data) {
        try {
            if(false == _client.isConnected())
            {
                Log.e(this.getClass().getCanonicalName(), "Client is not in connection " + Topic  );
                return false;
            }

            MqttMessage message = new MqttMessage(data.getBytes());
            message.setQos(0);
            message.setRetained(false);
            _client.setCallback(this);

            IMqttToken token = _client.publish(Topic, message);
            Log.e(this.getClass().getCanonicalName(), "MQTT Published to " + Topic  + " data " + data);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to publish " + e);
            return false;
        }

        return true;
    }

    public boolean subscribe(final String Topic ) {
        try {
            if(false == _client.isConnected())
            {
                Log.e(this.getClass().getCanonicalName(), "Client is not in connection " + Topic );
                return false;
            }
            _client.setCallback(this);
            IMqttToken token = _client.subscribe(Topic, 2);
            Log.e(this.getClass().getCanonicalName(), "MQTT subscribed to " + Topic );
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe " + e);
            return false;
        }

        return true;
    }

    public void unsubscribe (String topic)
    {
        try {
            _client.unsubscribe(topic);
            Log.e(this.getClass().getCanonicalName(), "MQTT unsubscribed to " + topic );
        }catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to unsubcribe " + e);
        }
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.i(this.getClass().getCanonicalName(), "$ MQTT Client connected");
        if(null != _mqttListener) {
            if(true==sendConnectNotiification) {
                _mqttListener.mqttCallback("connect", "success");
                sendConnectNotiification = false;
            }
        }
    }

    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        Log.i(this.getClass().getCanonicalName(), "$ MQTT Client connection error " + exception);
        if(null!=_mqttListener) {
            _mqttListener.mqttCallback("connect", "failure");
        }
    }

    public boolean isMqttConnected ( )
    {
        return _client.isConnected();
    }

    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        // Called when a message arrives from the server that matches any
        // subscription made by the client
        Log.i(this.getClass().getCanonicalName(), "MQTT msg recd : " + topic + " " + new String(message.getPayload()));
        if(null!=_mqttListener) {
            _mqttListener.mqttCallback(topic, new String(message.getPayload()));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public void connectionLost(Throwable cause) {
    }

    public void traceDebug(String arg0, String arg1) {
        Log.i(arg0, arg1);
    };

    public void traceError(String arg0, String arg1) {
        Log.e(arg0, arg1);
    };

    public void traceException(String arg0, String arg1,
                               Exception arg2) {
        Log.e(arg0, arg1, arg2);
    };
}
