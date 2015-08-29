package com.gp.app.minote.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by dell on 7/25/2015.
 */
public class MiNoteInstanceIDListenerService extends InstanceIDListenerService
{
    public void onTokenRefresh()
    {
        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
    }
}
