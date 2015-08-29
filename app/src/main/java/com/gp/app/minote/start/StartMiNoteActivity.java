package com.gp.app.minote.start;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gp.app.minote.R;
import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notes.backup.NotesBackupManager;
import com.gp.app.minote.util.MiNoteParameters;

public class StartMiNoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		MiNoteParameters.setApplicationContext(getApplicationContext());
		
		NotesBackupManager.setInitialBackupTime();

		SharedPreferences sharedPreferences = getSharedPreferences("MiNoteSharedPref", MODE_PRIVATE);

		String userEmailId = sharedPreferences.getString("UserEmailId", "invalid");

		if(!userEmailId.equals("invalid"))
		{
			Intent startLayoutManager = new Intent(this, NotesLayoutManagerActivity.class);

			startLayoutManager.setAction("START_LAYOUT_MANAGER_ACTIVITY");

			startActivity(startLayoutManager);
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

		System.out.println("User userName="+userName+" email Id :"+emailId);

		edit.putString("UserName", userName);

		edit.putString("UserEmailId", emailId.toLowerCase());

		edit.commit();

		Intent startLayoutManager = new Intent(this, NotesLayoutManagerActivity.class);

		startLayoutManager.setAction("START_LAYOUT_MANAGER_ACTIVITY");

		startActivity(startLayoutManager);
	}
}
