package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 11/10/2016.
 */

public interface systemClassCallbacks {

    public enum systemEvents {
        CONNECT,
        REGISTRATION,
        DEVICELIST,
        ACKNOWLEDGMENT,
        PHONENOLIST,
        GROUPLIST,
        SCHEDULELIST,
        ACKNOWLEDGMENT_GROUP_STATUS
    }
    public void systemClassCallback(systemEvents event, Boolean status);
}
