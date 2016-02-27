package me.kpr.nnp.back.nfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import me.kpr.nnp.back.callback.OnMessageReceivedCallback;
import timber.log.Timber;

/**
 * Created by dorosh on 2/27/16.
 */
public class NFCHelper implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {

    private String message;
    private NFCReceiver receiver;
    private NFCSender sender;
    private OnMessageReceivedCallback callback;

    private NfcAdapter adapter;

    public NFCHelper() {
        receiver = NFCReceiver.newInstance();
        sender = NFCSender.newInstance();
    }

    public void init(Activity activity) {
        adapter = NfcAdapter.getDefaultAdapter(activity);

        if (adapter != null) {
            Timber.e("ADAPTER NOT NULL");
            adapter.setNdefPushMessageCallback(this, activity);
            adapter.setOnNdefPushCompleteCallback(this, activity);
        } else {
            Timber.e("ADAPTER IS NULL");

            if (callback != null)
                callback.onFailure();
        }
    }

    public void onResume(Intent intent) {
        Timber.e("ON RESUME");
        receiver.onResume(intent);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        byte[] bytesOut = message.getBytes();
        return sender.createNdefNMessage(bytesOut, event);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        Timber.e("COMPLETED");

        // Cull, but not helpful
    }

    public void setMessage(String message) {
        Timber.e("SETTING MESSAGE : " + message);

        this.message = message;
    }

    public void setCallback(OnMessageReceivedCallback callback) {
        this.callback = callback;
        receiver.setCallback(callback);
    }
}
