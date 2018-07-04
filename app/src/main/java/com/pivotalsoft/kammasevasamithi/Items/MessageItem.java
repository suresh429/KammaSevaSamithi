package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 11/23/2017.
 */

public class MessageItem {
    String messageid;
    String message;
    String currentdate;

    public MessageItem(String messageid, String message, String currentdate) {
        this.messageid = messageid;
        this.message = message;
        this.currentdate = currentdate;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }
}
