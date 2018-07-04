package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by USER on 12/21/2017.
 */

public class AllFcmTokenItem {
    String fcmKey;

    public AllFcmTokenItem(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }
}
