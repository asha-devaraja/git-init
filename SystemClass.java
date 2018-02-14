package com.android_examples.Connect2ControlHome;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Asha.Devaraja on 11/10/2016.
 */
public class SystemClass extends Application {
    DatabaseHandler db;
    DatabaseParameters dbParams;
    communicationmodule comHandle;
    String macAddress;
    String phoneNo;
    String simSerial;
    Boolean isInitSuccess = false;
    Integer timeToWait = 7000;
    Boolean isConnected = false;
    systemClassCallbacks systemCallback = null;
    ArrayList<String> deviceList = new ArrayList<String>();
    ArrayList<String> GroupList = new ArrayList<String>();
    ArrayList<String> GroupStatus = new ArrayList<String>();
    ArrayList<String> deviceStatus = new ArrayList<String>();
    Integer deviceCount = 0;
    ArrayList<String> phoneNoList = new ArrayList<String>();
    ArrayList<String> scheduleList = new ArrayList<String>();

    ArrayList<String> ccCodeList = new ArrayList<String>();
    ArrayList<Boolean> adminList = new ArrayList<Boolean>();
    Integer GroupCount = 0;
    ArrayList<Boolean> SelectedList = new ArrayList<Boolean>();
    ArrayList<ArrayList<Integer>> groupDeviceList=new ArrayList<ArrayList<Integer>>();
    Integer phoneNosCount = 0;
    Integer schedulerDateTimeCount=0;
    String tempDeviceName="";
    String tempDeviceStatus="";
    Integer tabIndex = 0;
    String tempGroupName="";
    String tempGroupStatus="";
    private static SystemClass inst;

    public static SystemClass getInstance() {
        return inst;
    }

    public boolean systemInit (systemClassCallbacks callback)
    {
        inst = this;
        db = new DatabaseHandler(getApplicationContext());
        dbParams = db.getAllContents();
        Log.i(this.getClass().getCanonicalName(), "notification cnt no  " + db.getNotificationHistoryCount());

        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        simSerial = tMgr.getSimSerialNumber();
        Log.i(this.getClass().getCanonicalName(), "serial no  "+simSerial);

        macAddress = getMacAddr();
        Log.i(this.getClass().getCanonicalName(), "Mac address  "+macAddress);

        if (true != initCommunicationModule(dbParams) )
        {
            Log.i(this.getClass().getCanonicalName(), "communication module init error ");
            return false;
        }

        if(true == isDbEmpty(dbParams))
        {
            Log.i(this.getClass().getCanonicalName(), "database empty ");
            return false;
        }

        phoneNo = dbParams._Phone_no;

       // dbParams._RegStatus = false;

        //test_fillGroupList();

        if (true != dbParams._RegStatus )
        {
            Log.i(this.getClass().getCanonicalName(), "Mobile registration not done...");
            //return false;
        }

        if (true != connectToServer(callback))
        {
            Log.i(this.getClass().getCanonicalName(), "server Connect error...");
            return false;
        }

        Log.i(this.getClass().getCanonicalName(), "system init success ");
        return true;
       // return false;
    }

    public boolean isDbEmpty ( DatabaseParameters dbParams )
    {
        if(dbParams._name.isEmpty()||dbParams._IP.isEmpty()||
                dbParams._Password.isEmpty()||dbParams._clientid.isEmpty()||dbParams._Phone_no.isEmpty()||
                dbParams._port.isEmpty())
        {
            return true;
        }

        return false;
    }

    public ArrayList<String> getNotificationList()
    {
        return db.readNotificationsFromDb();
    }

    public Boolean setNotificationToDb(String text)
    {
        return db.saveNotificationToDb(text);
    }

    public void deleteNotificationsFromDb()
    {
        db.deleteNotificationHistory();
    }

    public boolean updateDatabase( DatabaseParameters oldDbParams, DatabaseParameters newdDbParams )
    {
        db.deleteContent(oldDbParams);
        db.addContent(newdDbParams);
        return true;
    }

    public boolean addToDatabase( DatabaseParameters Params )
    {
        db.addContent(Params);
        dbParams = Params;
        return true;
    }

    public boolean deleteFromDatabase( DatabaseParameters dbParams )
    {
        db.deleteContent(dbParams);
        return true;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public boolean initCommunicationModule (DatabaseParameters dbParams)
    {
        comHandle = new communicationmodule( simSerial, macAddress );

        if ( false == comHandle.init(getApplicationContext()))
        {
            return false;
        }

        return true;
    }

    public boolean connectToServer ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        comHandle._IP       = dbParams._IP;
        comHandle._port     = dbParams._port;
        comHandle._name     = dbParams._name;
        comHandle._clientid = dbParams._clientid;
        comHandle._Password = dbParams._Password;
        comHandle._phoneNo  = dbParams._Phone_no;
        Log.i(this.getClass().getCanonicalName(), "Connecting to server..");
        return comHandle.connect();
    }

    public boolean registerMobileDevice ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        Log.i(this.getClass().getCanonicalName(), "Registering mobile..");
        if( true != comHandle.sendRegistrationCommand() )
        {
            Log.i(this.getClass().getCanonicalName(), "sendRegistrationCommand failed..");
            return false;
        }

        return true;
    }

    public void otpReceived (String otp )
    {
        Log.i(this.getClass().getCanonicalName(), "Sending otp..");
        if( true != comHandle.sendOtpCommand(otp) )
        {
            Log.i(this.getClass().getCanonicalName(), "sendRegistrationCommand failed..");
        }
    }

    public boolean getDeviceList ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        return comHandle.getDeviceList();
    }

    public boolean getGroupList ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        return comHandle.getGroupList();
    }
    public boolean addGroup (systemClassCallbacks callback,String groupName, ArrayList<Integer> DeviceList){
        systemCallback = callback;
        return comHandle.addGroup(groupName, DeviceList);
    }
    public boolean deleteGroup(systemClassCallbacks callback,String groupName){
        systemCallback = callback;
        return comHandle.deleteGroup(groupName);
    }
    public boolean addDeviceToGroup(systemClassCallbacks callback,String groupName, String deviceName){
        systemCallback = callback;
        return comHandle.addDeviceToGroup(groupName, deviceName);
    }
    public boolean removeDeviceFromGroup(systemClassCallbacks callback,String groupName, String deviceName){
        systemCallback = callback;
        return comHandle.removeDeviceFromGroup(groupName, deviceName);
    }



    public boolean sendPowerToggleCommand(systemClassCallbacks callback, String deviceName, String status )
    {
        systemCallback = callback;
        return comHandle.sendSetDeviceStateCommand(deviceName,status);
    }

    public boolean sendGroupStatusChangeCommand(systemClassCallbacks callback, String groupName, String status )
    {
        systemCallback = callback;
        return comHandle.sendSetGroupStateCommand(groupName,status);
    }

    public boolean getPhoneNoList ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        return comHandle.getPhoneNoList();
    }

    public boolean getscheduleList ( systemClassCallbacks callback )
    {
        systemCallback = callback;
        return comHandle.getscheduleList();
    }

    public boolean addPhoneNumberToEsp (systemClassCallbacks callback, String phoneNo, Boolean isAdmin )
    {
        systemCallback = callback;
        return comHandle.sendAddNewPhoneNoCommand(phoneNo,isAdmin);
    }
    public boolean changeAdminStatus (systemClassCallbacks callback, String phoneNo, Boolean isAdmin )
    {systemCallback = callback;

        return comHandle.sendAdminStatusCommand(phoneNo,isAdmin);
    }

    public boolean removePhoneNumber (systemClassCallbacks callback, String phoneNo )
    {
        systemCallback = callback;
        return comHandle.sendRemovePhoneNoCommand(phoneNo);
    }

    public boolean addScheduleToEsp (systemClassCallbacks callback, String ScheduleDateTime )
    {
        systemCallback = callback;
        return comHandle.sendAddNewScheduleCommand(ScheduleDateTime);
    }

    public boolean removeSchedule (systemClassCallbacks callback, String ScheduleDateTime )
    {
        systemCallback = callback;
        return comHandle.sendRemoveScheduleCommand(ScheduleDateTime);
    }

    public boolean editDeviceName (systemClassCallbacks callback, String oldName, String newName )
    {
        systemCallback = callback;
        return comHandle.sendDeviceNameChangeCommand(oldName, newName );
    }

  /* public boolean addGroupNameToEsp (systemClassCallbacks callback, String GroupName, Boolean isSelected )
    {
        systemCallback = callback;
       return comHandle.sendAddNewGroupNameCommand(GroupName,isSelected );
    }*/
    public void test_fillGroupList ()
    {
        GroupList.add("Hall");
        GroupStatus.add("on");
        GroupList.add("Kitchen");
        GroupStatus.add("off");

        ArrayList<Integer> tempDeviceList = new ArrayList<Integer>();
        tempDeviceList.add(0);
        groupDeviceList.add(tempDeviceList);

        ArrayList<Integer> tempDeviceList1 = new ArrayList<Integer>();
        tempDeviceList1.add(1);
        groupDeviceList.add(tempDeviceList1);

        setNotificationToDb("group is added");
        setNotificationToDb("device is turned on");
    }

}
