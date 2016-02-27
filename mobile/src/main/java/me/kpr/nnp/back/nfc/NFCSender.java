package me.kpr.nnp.back.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcEvent;

/**
 * Created by dorosh on 2/27/16.
 */
public class NFCSender {

    private static NFCSender instance;

    private NFCSender() {
    }

    public static NFCSender newInstance() {
        if (instance == null)
            instance = new NFCSender();
        return instance;
    }


    public NdefMessage createNdefNMessage(byte[] bytesOut, NfcEvent event) {
        NdefRecord ndefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/plain".getBytes(),
                new byte[] {},
                bytesOut);

        return new NdefMessage(ndefRecordOut);
    }
}
