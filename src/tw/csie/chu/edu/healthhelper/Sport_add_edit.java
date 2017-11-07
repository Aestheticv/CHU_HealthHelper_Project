package tw.csie.chu.edu.healthhelper;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sport_add_edit extends Activity implements OnClickListener {

	private EditText mEdit_Sport_Type,mEdit_Sport_Hour,mEdit_Sport_Date,mEdit_Sport_Time;
	private Button mBtn_Sport_Submit,mBtn_Sport_Cancel,mBtn_Sport_Delete;
	private String SID,Type,Hour,Date,Time,Url;
	private int mUID,method;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport_add_edit);
		
		mEdit_Sport_Type = (EditText)findViewById(R.id.Edit_Sport_Type);
		mEdit_Sport_Hour = (EditText)findViewById(R.id.Edit_Sport_Hour);
		mEdit_Sport_Date = (EditText)findViewById(R.id.Edit_Sport_Date);
		mEdit_Sport_Time = (EditText)findViewById(R.id.Edit_Sport_Time);
		
		mBtn_Sport_Submit = (Button)findViewById(R.id.Btn_Sport_Submit);
		mBtn_Sport_Cancel = (Button)findViewById(R.id.Btn_Sport_Cancel);
		mBtn_Sport_Delete = (Button)findViewById(R.id.Btn_Sport_Delete);
		
		mBtn_Sport_Submit.setOnClickListener(this);
		mBtn_Sport_Cancel.setOnClickListener(this);
		mBtn_Sport_Delete.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		method = bundle.getInt("method");
		
		if(method==1){
			mBtn_Sport_Delete.setVisibility(View.INVISIBLE);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/sport/HealthHelper_sport_Add.php";
			
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date = sDateFormat.format(new java.util.Date());
			
			SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
			Time = sTimeFormat.format(new java.util.Date());
			
			mEdit_Sport_Date.setText(Date);
			mEdit_Sport_Time.setText(Time);
		}
		if(method==2){
			mBtn_Sport_Delete.setVisibility(View.VISIBLE);
			SID = bundle.getString("SID");
			Type = bundle.getString("Type");
			Hour = bundle.getString("Hour");
			Date = bundle.getString("Date");
			Time = bundle.getString("Time");
			
			mEdit_Sport_Type.setText(Type);
			mEdit_Sport_Hour.setText(Hour);
			mEdit_Sport_Date.setText(Date);
			mEdit_Sport_Time.setText(Time);
			
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/sport/HealthHelper_sport_Edit.php";
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_Sport_Submit){
			Type = mEdit_Sport_Type.getText().toString();
			Hour = mEdit_Sport_Hour.getText().toString();
			Date = mEdit_Sport_Date.getText().toString();
			Time = mEdit_Sport_Time.getText().toString();
			if(method!=2) new AccessDBTask().execute(Integer.toString(mUID), Type, Hour, Date, Time);
			else new AccessDBTask().execute(SID, Type, Hour, Date, Time);
		}
		if(id == R.id.Btn_Sport_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Sport_add_edit.this,Sport.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_Sport_Delete){
			method = 3;
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/sport/HealthHelper_sport_Delete.php";
			new AccessDBTask().execute(SID);
		}
	}
	
	private class AccessDBTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            return accessDatabase(args);
        }

		@Override
        protected void onPostExecute(String str) {
        	Bundle bundle = new Bundle();
			Intent go = new Intent(Sport_add_edit.this,Sport.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Sport_add_edit.this.finish();
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(method==1){
        	nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Type", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Hour", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[4]));
        }
        if(method==2){
        	nameValuePairs.add(new BasicNameValuePair("SID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Type", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Hour", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[4]));
        }
        if(method==3){
        	nameValuePairs.add(new BasicNameValuePair("SID", inputStrings[0]));
        }
        
        InputStream is = null;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            
            HttpPost httppost = new HttpPost(Url);
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            //Toast toast = Toast.makeText(SignUpActivity.this,Log.e("log_tag", "Error in http connection "+e.toString()), Toast.LENGTH_LONG);
        }

        return resultStr;
    }
}
