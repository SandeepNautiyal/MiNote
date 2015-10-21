package com.gp.app.minote.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.gp.app.minote.R;
import com.gp.app.minote.backend.userdata.userRegistrationInfoApi.UserRegistrationInfoApi;
import com.gp.app.minote.backend.userdata.userRegistrationInfoApi.model.UserRegistrationInfo;
import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notes.backup.NotesBackupManager;
import com.gp.app.minote.util.MiNoteParameters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartMiNoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		MiNoteParameters.setApplicationContext(getApplicationContext());
		
		NotesBackupManager.setInitialBackupTime();

		SharedPreferences sharedPreferences = getSharedPreferences("MiNoteSharedPref", MODE_PRIVATE);

		String userEmailId = sharedPreferences.getString("UserEmailId", "invalid");

		boolean editEmailId = getIntent().getBooleanExtra("EDIT_EMAIL_ID", false);

		if(!editEmailId && !userEmailId.equals("invalid"))
		{
			Intent startLayoutManager = new Intent(this, NotesLayoutManagerActivity.class);

			startLayoutManager.setAction("START_LAYOUT_MANAGER_ACTIVITY");

			startActivity(startLayoutManager);

			finish();
		}

		setContentView(R.layout.activity_start_professional_pa_application);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_professional_pa_application, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void submitEmaidId(View view)
	{
		SharedPreferences sharedPreferences = getSharedPreferences("MiNoteSharedPref", MODE_PRIVATE);

		SharedPreferences.Editor edit = sharedPreferences.edit();

		String userName = ((EditText)findViewById(R.id.nameEditText)).getText().toString();

		String emailId = ((EditText)findViewById(R.id.emailEditText)).getText().toString();

		edit.putString("UserName", userName);

		edit.putString("UserEmailId", emailId.toLowerCase());

		edit.commit();

		new GcmRegistrationAsyncTask(this).execute();

		Intent startLayoutManager = new Intent(this, NotesLayoutManagerActivity.class);

		startLayoutManager.setAction("START_LAYOUT_MANAGER_ACTIVITY");

		startActivity(startLayoutManager);

        finish();
    }

	//TODO to be removed.
	class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String>
	{
		private GoogleCloudMessaging gcm;
		private Context context;

		// TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
		private static final String SENDER_ID = "700276642861";

		public GcmRegistrationAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(Void... params)
		{
			boolean isDeviceRegistered = false;

			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}

				String regId = gcm.register(SENDER_ID);

				msg = "Device registered, registration ID=" + regId;

				UserRegistrationInfo userRegistrationInfo = new UserRegistrationInfo();

				SharedPreferences sharedPreferences = getSharedPreferences("MiNoteSharedPref", MODE_PRIVATE);

				String userEmailId = sharedPreferences.getString("UserEmailId", "invalid");

				String userName = sharedPreferences.getString("UserName", "invalid");

				userRegistrationInfo.setDeviceRegistrationId(regId);

				if(!userEmailId.equals("invalid"))
				{
					userRegistrationInfo.setUserEmail(userEmailId);
				}

				if(!userName.equals("invalid"))
				{
					userRegistrationInfo.setUserName(userName);
				}

				userRegistrationInfo.setUserEmail(userEmailId);

				UserRegistrationInfoApi.Builder builder = new UserRegistrationInfoApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
						.setRootUrl("https://minote-997.appspot.com/_ah/api/");

				UserRegistrationInfoApi userRegistrationInfoApi = builder.build();

				UserRegistrationInfoApi.Insert insertUserInfoEntity = userRegistrationInfoApi.insert(userRegistrationInfo);

				insertUserInfoEntity.execute();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				msg = "Error: " + ex.getMessage();
			}

			return "Hello";
		}

		@Override
		protected void onPostExecute(String msg) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
		}
	}
}
