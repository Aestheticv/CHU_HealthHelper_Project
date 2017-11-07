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
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Bloodpressure_add_edit extends Activity implements OnClickListener {

	private EditText mEdit_BP_SBP,mEdit_BP_DBP,mEdit_BP_Pluse,mEdit_BP_Date,
		    mEdit_BP_Time;
	private Button mBtn_BP_Submit,mBtn_BP_Cancel,mBtn_BP_Delete;
	private int mUID,method;
	String mBPID;
	private String SBP,DBP,Pluse,Date,Time,Url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloodpressure_add_edit);
		mEdit_BP_SBP = (EditText)findViewById(R.id.Edit_BP_SBP);
		mEdit_BP_DBP = (EditText)findViewById(R.id.Edit_BP_DBP);
		mEdit_BP_Pluse = (EditText)findViewById(R.id.Edit_BP_Pluse);
		mEdit_BP_Date = (EditText)findViewById(R.id.Edit_BP_Date);
		mEdit_BP_Time = (EditText)findViewById(R.id.Edit_BP_Time);
		
		mBtn_BP_Submit = (Button)findViewById(R.id.Btn_BP_Submit);
		mBtn_BP_Cancel = (Button)findViewById(R.id.Btn_BP_Cancel);
		mBtn_BP_Delete = (Button)findViewById(R.id.Btn_BP_Delete);
		mBtn_BP_Submit.setOnClickListener(this);
		mBtn_BP_Cancel.setOnClickListener(this);
		mBtn_BP_Delete.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		method = bundle.getInt("method");
		if(method==1){
			mBtn_BP_Delete.setVisibility(View.INVISIBLE);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BP/HealthHelper_BP_Add.php";
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date = sDateFormat.format(new java.util.Date());
			
			SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
			Time = sTimeFormat.format(new java.util.Date());
			
			mEdit_BP_Date.setText(Date);
			mEdit_BP_Time.setText(Time);
		}
		if(method==2){
			mBtn_BP_Delete.setVisibility(View.VISIBLE);
			mBPID = bundle.getString("BPID");
			SBP = bundle.getString("SBP");
			DBP = bundle.getString("DBP");
			Pluse = bundle.getString("Pluse");
			Date = bundle.getString("Date");
			Time = bundle.getString("Time");
			
			mEdit_BP_SBP.setText(SBP);
			mEdit_BP_DBP.setText(DBP);
			mEdit_BP_Pluse.setText(Pluse);
			mEdit_BP_Date.setText(Date);
			mEdit_BP_Time.setText(Time);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BP/HealthHelper_BP_Edit.php";
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.Btn_BP_Submit){
			SBP = mEdit_BP_SBP.getText().toString();
			DBP = mEdit_BP_DBP.getText().toString();
			Pluse = mEdit_BP_Pluse.getText().toString();
			Date = mEdit_BP_Date.getText().toString();
			Time = mEdit_BP_Time.getText().toString();
			if(method!=2) new AccessDBTask().execute(Integer.toString(mUID), SBP, DBP, Pluse, Date, Time);
			else new AccessDBTask().execute(mBPID, SBP, DBP, Pluse, Date, Time);
		}
		if(id == R.id.Btn_BP_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Bloodpressure_add_edit.this,Bloodpressure.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_BP_Delete){
			method = 3;
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BP/HealthHelper_BP_Delete.php";
			new AccessDBTask().execute(mBPID);
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
			Intent go = new Intent(Bloodpressure_add_edit.this,Bloodpressure.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Bloodpressure_add_edit.this.finish();
        	
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(method==1){
        	nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("SBP", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("DBP", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Pluse", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[4]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[5]));
        }
        if(method==2){
        	nameValuePairs.add(new BasicNameValuePair("BPID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("SBP", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("DBP", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Pluse", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[4]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[5]));
        }
        if(method==3){
        	nameValuePairs.add(new BasicNameValuePair("BPID", inputStrings[0]));
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
