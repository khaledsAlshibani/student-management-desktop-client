package com.kss.studentmanagementdesktopclient.app;

/**
 * Interface for controllers that need to receive data when switching scenes.
 * Classes implementing this interface can accept data through the setData method.
 */
public interface DataReceiver {
    /**
     * Sets the data to be received by the implementing controller.
     *
     * @param data the data to be passed to the controller
     */
    void setData(Object data);
}
