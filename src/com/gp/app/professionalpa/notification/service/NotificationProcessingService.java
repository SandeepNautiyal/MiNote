package com.gp.app.professionalpa.notification.service;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationProcessingService extends Service 
{
	@Override
	public void onCreate() 
	{
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
    	if(intent != null)
    	{
    		ProfessionalPAParameters.setApplicationContext(getApplicationContext());
    		
            new NotificationActionPerformer().execute(false);

            intent.addCategory(NotificationReceiver.CATEGORY);
    	}
    	else
    	{
    		stopSelf();
    	}
    	
    	return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		
//		stopService(new Intent(ProfessionalPAParameters.getApplicationContext(), NotificationProcessingService.class));
	}

	
}
