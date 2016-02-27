package me.kpr.nnp.back.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;

import me.kpr.nnp.back.callback.OnMessageReceivedCallback;
import timber.log.Timber;

/**
 * Created by dorosh on 2/27/16.
 */
public class NFCReceiver {


    private static NFCReceiver instance;

    private NFCReceiver() {

    }

    public static NFCReceiver newInstance() {
        if (instance == null)
            instance = new NFCReceiver();
        return instance;
    }

    private OnMessageReceivedCallback callback;

    public void onResume(Intent intent) {
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(
                            NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord NdefRecord_0 = inNdefRecords[0];
            String msg = new String(NdefRecord_0.getPayload());

            Timber.e("MESSAGE : " + msg);

            if (callback != null) {
                Timber.e("CALLBACK NOT NULL");
                callback.onReceive(msg);

            }
            return;
        }

        if (callback != null)
            callback.onFailure();

    }

    public void setCallback(OnMessageReceivedCallback callback) {
        Timber.e("CALLBACK SETTED");
        this.callback = callback;
    }
}
