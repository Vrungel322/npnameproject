package me.kpr.nnp.back.callback;

/**
 * Created by dorosh on 2/27/16.
 */
public interface OnMessageReceivedCallback {
    void onReceive(String message);
    void onFailure();
}
