package me.kpr.nnp.back.callback;

import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

/**
 * Created by dorosh on 2/28/16.
 */
public interface OnNfcStateUpdateListener {
    void onUpdate(NfcAdapter adapter, int state);
}
