package br.com.mobiltec.c4m.sampleapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import br.com.mobiltec.cloud4mobile.android.library.DeviceApi;

public class MainActivity extends Activity {
	
	private DeviceApi deviceApi;
	private static final String C4M_SAMPLE_TAG = "C4M_SAMPLE";
	private TextView statusText;
	private volatile boolean isWorking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		deviceApi = new DeviceApi(getApplicationContext());
		statusText = (TextView) findViewById(R.id.status_text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void doEnrollWithServer(final View view){
		if(!isWorking){
			statusText.setText("Enroll started... Wait for update..");
			
			//TODO: Change here with your own values
			new EnrollBackgroundTask().execute("you_app_secret", "enroll_code");
		}
	}
	
	private class EnrollBackgroundTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			isWorking = true;
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try{
				deviceApi.enrollDevice(params[0],params[1]);
				Log.d(C4M_SAMPLE_TAG, "Enroll with server ok.");
			} catch(Exception ex){
				Log.e(C4M_SAMPLE_TAG, "Error in enroll.", ex);
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		
		@Override
		protected void onPostExecute(Boolean isEnrollOk) {
			if(isEnrollOk){
				statusText.setText("Enroll with server finished.");
			} else {
				statusText.setText("Failed to enroll with server. Check logcat for details.");
			}
			
			isWorking = false;
		}
		
	}
}
