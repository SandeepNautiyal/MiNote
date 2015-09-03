package com.gp.app.minote.layout.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.gp.app.minote.R;
import com.gp.app.minote.backend.messaging.Messaging;
import com.gp.app.minote.util.MiNoteParameters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareNoteActivity extends Activity
{
    String message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        message = getIntent().getStringExtra("MESSAGE");

        setContentView(R.layout.share_note_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void notifyFriend(View view)
    {
        EditText emailAddress = (EditText)findViewById(R.id.friendsEmailIdEditText);

        new MessagingAsyncTask(MiNoteParameters.getApplicationContext()).execute(message, emailAddress.getText().toString().toLowerCase());
    }

    //TODO to be removed.
    class MessagingAsyncTask extends AsyncTask<String, Void, String> {

        private GoogleCloudMessaging gcm;
        private Context context;

        // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
        private static final String SENDER_ID = "700276642861";

        public MessagingAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params)
        {
            Messaging.Builder messaqingBuilder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://minote-997.appspot.com/_ah/api/");

            Messaging messaging = messaqingBuilder.build();

            Messaging.MessagingEndpoint endpoint = messaging.messagingEndpoint();

            try
            {
                Messaging.MessagingEndpoint.SendMessage sentMessage = endpoint.sendMessage(params[0], params[1]);

                sentMessage.execute();
            }
            catch (IOException e)
            {
                System.out.println("failure in sending message");

                e.printStackTrace();
            }

            System.out.println("Succes in sending message");

            return "Success";
        }

        @Override
        protected void onPostExecute(String msg)
        {
            startNotesActivity();
        }
    }

    public void startNotesActivity()
    {
        Intent intent = new Intent(this, NotesLayoutManagerActivity.class);

        startActivity(intent);
    }
}
