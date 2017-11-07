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
import android.widget.TextView;
import android.widget.Toast;

public class Bloodglucose_add_edit extends Activity implements OnClickListener {

	private EditText mEdit_BG_BG,mEdit_BG_Date,mEdit_BG_Time;
	private TextView mBtn_BG_Submit,mBtn_BG_Cancel,mBtn_BG_Delete;
	private int mUID,method;
	private String mBGID,mBG,mDate,mTime,Url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloodglucose_add_edit);
		
		mEdit_BG_BG = (EditText)findViewById(R.id.Edit_BG_BG);
		mEdit_BG_Date = (EditText)findViewById(R.id.Edit_BG_Date);
		mEdit_BG_Time = (EditText)findViewById(R.id.Edit_BG_Time);
		
		mBtn_BG_Submit = (Button)findViewById(R.id.Btn_BG_Submit);
		mBtn_BG_Cancel = (Button)findViewById(R.id.Btn_BG_Cancel);
		mBtn_BG_Delete = (Button)findViewById(R.id.Btn_BG_Delete);
		
		mBtn_BG_Submit.setOnClickListener(this);
		mBtn_BG_Cancel.setOnClickListener(this);
		mBtn_BG_Delete.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		method = bundle.getInt("method");
		
		if(method==1){
			mBtn_BG_Delete.setVisibility(View.INVISIBLE);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BG/HealthHelper_BG_Add.php";
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			mDate = sDateFormat.format(new java.util.Date());
			
			SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
			mTime = sTimeFormat.format(new java.util.Date());
			
			mEdit_BG_Date.setText(mDate);
			mEdit_BG_Time.setText(mTime);
			
		}
		if(method==2){
			mBtn_BG_Delete.setVisibility(View.VISIBLE);
			mBGID = bundle.getString("BGID");
			mBG = bundle.getString("BG");
			mDate = bundle.getString("Date");
			mTime = bundle.getString("Time");
			
			mEdit_BG_BG.setText(mBG);
			mEdit_BG_Date.setText(mDate);
			mEdit_BG_Time.setText(mTime);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BG/HealthHelper_BG_Edit.php";
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_BG_Submit){
			mBG = mEdit_BG_BG.getText().toString();
			mDate = mEdit_BG_Date.getText().toString();
			mTime = mEdit_BG_Time.getText().toString();
			if(method!=2) new AccessDBTask().execute(Integer.toString(mUID), mBG, mDate, mTime);
			else new AccessDBTask().execute(mBGID, mBG, mDate, mTime);
		}
		if(id == R.id.Btn_BG_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Bloodglucose_add_edit.this,Bloodpressure.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_BG_Delete){
			method = 3;
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BG/HealthHelper_BG_Delete.php";
			new AccessDBTask().execute(mBGID);
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
			Intent go = new Intent(Bloodglucose_add_edit.this,Bloodglucose.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Bloodglucose_add_edit.this.finish();
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(method==1){
        	nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("BG", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[3]));
        }
        if(method==2){
        	nameValuePairs.add(new BasicNameValuePair("BGID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("BG", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[3]));
        }
        if(method==3){
        	nameValuePairs.add(new BasicNameValuePair("BGID", inputStrings[0]));
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
