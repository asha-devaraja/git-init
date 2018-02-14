package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Asha.Devaraja on 9/27/2016.
 */
public class communicationmodule extends Activity implements mqttCallbacks {
    String _Protocol = "mqtt";
    String _IP = "";
    String _port = "";
    String _name = "";
    String _Password = "";
    String _clientid = "";
    String _phoneNo = "";
    String _ScheduleDateTime="";
    String _macAddress = "";
    String _simSerial = "";
    boolean isConnected = false;
    mqttcommunication _mqttConnection;
    Context _context;


    public communicationmodule(String serialNo, String mac) {
        _simSerial  = serialNo;
        _macAddress = mac;

        _mqttConnection = new mqttcommunication( );
    }

    public boolean init (Context context)
    {
        _context = context;
        return true;
    }

    public boolean connect ( )
    {
        _mqttConnection._IP        = _IP;
        _mqttConnection._port      = _port;
        _mqttConnection._name      = _name;
        _mqttConnection._Password  = _Password;
        _mqttConnection._clientid  = _clientid;
        _mqttConnection._macAddress= _macAddress;

        if ( true != _mqttConnection.open(_context))
        {
            return false;
        }
        return _mqttConnection.connect(this);
    }

    public boolean isConnected ( )
    {
        return _mqttConnection.isMqttConnected();
    }

    public boolean getDeviceList() {
        if (_Protocol == "mqtt") {
            String topic = "deviceList/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "getDeviceList/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendRegistrationCommand ()
    {
        if (_Protocol == "mqtt") {
            String topic = "regPhone/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                //sendOtpCommand("180428");
                return true;
            }
        }

        return false;
    }

    public boolean sendOtpCommand (String otp )
    {
        if (_Protocol == "mqtt") {
            String topic = "regStatus/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "otp/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + otp;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendSetDeviceStateCommand(String deviceName, String Status)
    {
        if (_Protocol == "mqtt") {
            String topic = "deviceStatus/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "setDeviceState/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + deviceName + "," + Status;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendSetGroupStateCommand(String groupName, String Status)
    {
        if (_Protocol == "mqtt") {
            String topic = "groupStatus/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "setGroupState/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + groupName + "," + Status;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean getPhoneNoList( )
    {
        if (_Protocol == "mqtt") {
            String topic = "phoneNoList/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "getPhoneNoList/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }
    public boolean getscheduleList( )
    {
        if (_Protocol == "mqtt") {
            String topic = "ScheduleDateTimeList/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = " getScheduleList/" + _clientid;
            String msg = _ScheduleDateTime + "," + _macAddress  + "," + _simSerial;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendAddNewPhoneNoCommand (String phoneNo, Boolean isAdmin)
    {
        if (_Protocol == "mqtt") {
            String topic = "addNewPhone/" + _clientid;
            String admin = "not admin";

            if(isAdmin==true)
            {
                admin = "admin";
            }
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + phoneNo + "," + admin;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendAdminStatusCommand (String phoneNo, Boolean isAdmin)
    {
        if (_Protocol == "mqtt") {
            String topic = "changeAdmin/" + _clientid;
            String admin = "not admin";

            if(isAdmin==true)
            {
                admin = "admin";
            }
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + phoneNo + "," + admin;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendRemovePhoneNoCommand (String phoneNo)
    {
        if (_Protocol == "mqtt") {
            String topic = "removePhone/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + phoneNo;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean sendDeviceNameChangeCommand (String oldName, String newName )
    {
        if (_Protocol == "mqtt") {
            String topic = "changeName/" + _clientid;
            String msg = _phoneNo + "," + _macAddress  + "," + _simSerial + "," + oldName + "," + newName ;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean getGroupList() {
        if (_Protocol == "mqtt") {
            String topic = "groupList/" + _clientid + "/" + _simSerial;
            if (false == _mqttConnection.subscribe(topic)) {
                return false;
            }

            topic = "getGroupList/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean addGroup(String groupName, ArrayList<Integer> DeviceList) {
        if (_Protocol == "mqtt") {
            SystemClass appInstance = (SystemClass)_context;
            String topic = "addGroup/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial+ "," + groupName + "," + DeviceList.size();
            for(Integer i=0;i<DeviceList.size();++i)
            {
                Integer deviceIndex = DeviceList.get(i);
                msg = msg + "," + appInstance.deviceList.get(deviceIndex);
            }
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean deleteGroup(String groupName) {
        if (_Protocol == "mqtt") {
            String topic = "deleteGroup/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial+ "," + groupName ;
             if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public boolean addDeviceToGroup(String groupName, String deviceName) {
        if (_Protocol == "mqtt") {
            String topic = "addToGroup/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial+ "," + groupName + "," + deviceName;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }
    public boolean sendAddNewScheduleCommand (String ScheduleDateTime)
    {
        if (_Protocol == "mqtt") {
            String topic = "addNewSchedule/" + _clientid;

            String msg = _ScheduleDateTime + "," + _macAddress  + "," + _simSerial + "," + ScheduleDateTime ;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }
    public boolean sendRemoveScheduleCommand (String ScheduleDateTime)
    {
        if (_Protocol == "mqtt") {
            String topic = "removeSchedule/" + _clientid;
            String msg = _ScheduleDateTime + "," + _macAddress  + "," + _simSerial + "," + ScheduleDateTime;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }


    public boolean removeDeviceFromGroup(String groupName, String deviceName) {
        if (_Protocol == "mqtt") {
            String topic = "removeFromGroup/" + _clientid;
            String msg = _phoneNo + "," + _macAddress + "," + _simSerial+ "," + groupName + "," + deviceName;
            if (false == _mqttConnection.publish(topic, msg)) {
                return false;

            } else {
                return true;
            }
        }

        return false;
    }

    public void mqttCallback(String event, String msg)
    {
        SystemClass appInstance = (SystemClass)_context;
        String deviceListTopic = "deviceList/" + _clientid + "/" + _simSerial;
        String regStatusTopic = "regStatus/" + _clientid + "/" + _simSerial;
        String phoneNoListTopic =  "phoneNoList/" + _clientid + "/" + _simSerial;
        String scheduleListTopic =  "ScheduleDateTimeList/" + _clientid + "/" + _simSerial;
        String deviceStatusTopic = "deviceStatus/" + _clientid + "/" + _simSerial;
        String GroupListTopic =  "groupList/" + _clientid + "/" + _simSerial;
        String groupStatusTopic = "groupStatus/" + _clientid + "/" + _simSerial;

        if(event.equals("connect"))
        {
            if(null == appInstance.systemCallback)
            {
                return;
            }

            if(msg.equals("success"))
            {
                appInstance.isConnected = true;
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.CONNECT, true );
            }
            else
            {
                appInstance.isConnected = false;
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.CONNECT, false );
            }
        }
        else if(event.equals(deviceListTopic))
        {
             String tokens[] = msg.split(",");

            _mqttConnection.unsubscribe(deviceListTopic);

            if(null == appInstance.systemCallback)
            {
                return;
            }

            if ( tokens.length < 2 )
            {
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.DEVICELIST, false );
                return;
            }

            appInstance.deviceCount = 0;
            appInstance.deviceList.removeAll(appInstance.deviceList);
            appInstance.deviceStatus.removeAll(appInstance.deviceStatus);

            for(int j=0; j < tokens.length; j++){
                Log.i(this.getClass().getCanonicalName(), "Device["+j+"] name : " + tokens[j]);
                appInstance.deviceList.add(tokens[j]);
                Log.i(this.getClass().getCanonicalName(), "Device["+j+"] status : " + tokens[j+1]);
                appInstance.deviceStatus.add(tokens[j+1]);
                appInstance.deviceCount += 1;
                j = j + 1;
            }

            appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.DEVICELIST, true );
        }
        else if(event.equals(regStatusTopic))
        {
            //_mqttConnection.unsubscribe(regStatusTopic);

            Log.i(this.getClass().getCanonicalName(), "reg status : " + msg);
            String tokens[] = msg.split(",");
            if(tokens.length < 2)
            {
                return;
            }
            if(tokens[0].equals("success"))
            {
                if(tokens[1].equals("admin"))
                {
                    appInstance.deleteFromDatabase(appInstance.dbParams);
                    appInstance.dbParams._isAdmin = true;
                    appInstance.addToDatabase(appInstance.dbParams);
                    Log.i(this.getClass().getCanonicalName(), "registered as admin...");
                }
                else
                {
                    appInstance.deleteFromDatabase(appInstance.dbParams);
                    appInstance.dbParams._isAdmin = false;
                    appInstance.addToDatabase(appInstance.dbParams);
                    Log.i(this.getClass().getCanonicalName(), "registered as not admin...");
                }

                if( null!=appInstance.systemCallback)
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.REGISTRATION, true );
            }
            else
            {
                if( null!=appInstance.systemCallback)
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.REGISTRATION, false );
            }
        }
        else if(event.equals(scheduleListTopic))
        {
             _mqttConnection.unsubscribe(scheduleListTopic);
             String tokens[] = msg.split(",");
             appInstance.schedulerDateTimeCount = 0;
             appInstance.scheduleList.removeAll(appInstance.scheduleList);

            for(int j=0; j < tokens.length; j++)
            {
                Log.i(this.getClass().getCanonicalName(), "Scheduled["+j+"] DateTime : " + tokens[j]);
                appInstance.scheduleList.add(tokens[j]);
                appInstance.schedulerDateTimeCount += 1;
                j = j + 1;
            }

            appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.SCHEDULELIST, true );
        }



        else if(event.equals(phoneNoListTopic))
        {
            _mqttConnection.unsubscribe(phoneNoListTopic);

            String tokens[] = msg.split(",");

            appInstance.phoneNosCount = 0;
            appInstance.phoneNoList.removeAll(appInstance.phoneNoList);
            appInstance.ccCodeList.removeAll(appInstance.ccCodeList);
            appInstance.adminList.removeAll(appInstance.adminList);

            for(int j=0; j < tokens.length; j++){
                Log.i(this.getClass().getCanonicalName(), "Phone["+j+"] Number : " + tokens[j]);
                appInstance.phoneNoList.add(tokens[j]);
                Log.i(this.getClass().getCanonicalName(), "Admin["+j+"] status : " + tokens[j+1]);
                if(tokens[j+1].equals("admin")) {
                    appInstance.adminList.add(true);
                }
                else
                {
                    appInstance.adminList.add(false);
                }
                appInstance.phoneNosCount += 1;
                j = j + 1;
            }

            appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.PHONENOLIST, true );
        }
        else if(event.equals(deviceStatusTopic))
        {
            _mqttConnection.unsubscribe(deviceStatusTopic);
            Log.i(this.getClass().getCanonicalName(), "device status : " + msg);
            String tokens[] = msg.split(",");

            if(tokens.length < 2)
            {
                return;
            }

            Log.i(this.getClass().getCanonicalName(), "device name " + tokens[0]);
            Log.i(this.getClass().getCanonicalName(), "device status " + tokens[1]);
            for(int j=0; j < appInstance.deviceCount; j++){

               if(appInstance.deviceList.get(j).equals(tokens[0]))
               {
                   Log.i(this.getClass().getCanonicalName(), "device name found");
                   if(appInstance.deviceStatus.get(j).equals(tokens[1]))
                   {
                       Log.i(this.getClass().getCanonicalName(), "device status equal");
                       appInstance.tempDeviceName = appInstance.deviceList.get(j);
                       appInstance.tempDeviceStatus = appInstance.deviceStatus.get(j);
                       appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.ACKNOWLEDGMENT, true );
                       break;
                   }
               }
            }
        }
        else if(event.equals(GroupListTopic)) {

            String tokens[] = msg.split(",");

            _mqttConnection.unsubscribe(GroupListTopic);

            if(null == appInstance.systemCallback)
            {
                return;
            }

            if ( tokens.length < 4 )
            {
                appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.GROUPLIST, true );
                return;
            }


            appInstance.GroupList.removeAll(appInstance.GroupList);

            for(int j=1; j < tokens.length; ){
                Log.i(this.getClass().getCanonicalName(), "Group["+j+"] name : " + tokens[j]);
                appInstance.GroupList.add(tokens[j]);
                ++j;
                Integer nodevices=Integer.valueOf(tokens[j]);
                j++;
                Log.i(this.getClass().getCanonicalName(), "Group no of devices : " + nodevices);
                ArrayList<Integer> DeviceList = new ArrayList<Integer>();
                Integer groupStatus =0;
                for(int i=0;i<nodevices;i++){
                    String dn=tokens[j];
                    if((dn.isEmpty())||(dn.equals("")))
                    {
                        break;
                    }
                    Integer ga=0;
                    Log.i(this.getClass().getCanonicalName(), "Group device : " + dn);
                    for(int l=0;l<appInstance.deviceList.size();l++){
                        if(dn.equals(appInstance.deviceList.get(l))){
                            ga=l;
                            Log.i(this.getClass().getCanonicalName(), "Group device index : " + l);
                            DeviceList.add(ga);
                            if(appInstance.deviceStatus.get(l).equals("on"))
                            {
                                groupStatus = 1;
                            }
                        }
                    }
                    ++j;
                }
                appInstance.groupDeviceList.add(DeviceList);
                if(1==groupStatus)
                {
                    appInstance.GroupStatus.add("on");
                }
                else
                {
                    appInstance.GroupStatus.add("off");
                }
            }

            appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.GROUPLIST, true );
        }
        else if(event.equals(groupStatusTopic))
        {
            _mqttConnection.unsubscribe(groupStatusTopic);
            Log.i(this.getClass().getCanonicalName(), "group status : " + msg);
            String tokens[] = msg.split(",");

            if(tokens.length < 2)
            {
                return;
            }

            Log.i(this.getClass().getCanonicalName(), "group name " + tokens[0]);
            Log.i(this.getClass().getCanonicalName(), "group status " + tokens[1]);
            for(int j=0; j < appInstance.GroupList.size(); j++){

                if(appInstance.GroupList.get(j).equals(tokens[0]))
                {
                    Log.i(this.getClass().getCanonicalName(), "group name found");
                    if(appInstance.GroupStatus.get(j).equals(tokens[1]))
                    {
                        Log.i(this.getClass().getCanonicalName(), "group status equal");
                        appInstance.tempGroupName = appInstance.GroupList.get(j);
                        appInstance.tempGroupStatus = appInstance.GroupStatus.get(j);
                        appInstance.systemCallback.systemClassCallback(systemClassCallbacks.systemEvents.ACKNOWLEDGMENT_GROUP_STATUS, true );
                        break;
                    }
                }
            }
        }
        }
    }

