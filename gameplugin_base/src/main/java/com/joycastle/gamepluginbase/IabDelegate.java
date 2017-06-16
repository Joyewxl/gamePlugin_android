package com.joycastle.gamepluginbase;

/**
 * Created by gaoyang on 10/9/16.
 */

public interface IabDelegate extends LifeCycleDelegate {
    interface RestoreDelegate {
        void onResult(boolean result, String iapId, String message);
    }
    interface PurchaseDelegate {
        void onResult(boolean result, String message);
    }
    void setRestoreHandler(RestoreDelegate delegate);
    void purchase(String iapId, String payLoad, PurchaseDelegate delegate);
}
