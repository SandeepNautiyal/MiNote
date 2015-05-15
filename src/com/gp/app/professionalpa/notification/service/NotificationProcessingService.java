package com.gp.app.professionalpa.notification.service;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPATools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationProcessingService extends Service 
{
	@Override
	public void onCreate() 
	{
		System.out.println("onCreate -> service");
		
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
    	System.out.println("onStartCommand -> intent="+intent);
    	
    	if(intent != null)
    	{
    		ProfessionalPAParameters.setApplicationContext(getApplicationContext());
    		
    		boolean  isNotification = intent.getBooleanExtra(ProfessionalPAConstants.IS_NOTIFICATION, true);
            String eventName = intent.getStringExtra(ProfessionalPAConstants.EVENT_NAME);
            String eventMessage = intent.getStringExtra(ProfessionalPAConstants.EVENT_MESSAGE);
            int notificationId = intent.getIntExtra(ProfessionalPAConstants.NOTIFICATION_ID, -1);
            
            new NotificationActionPerformer(notificationId, eventName, eventMessage, isNotification).execute(new String[]{});

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
