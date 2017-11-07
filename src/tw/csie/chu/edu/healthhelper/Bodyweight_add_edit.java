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

public class Bodyweight_add_edit extends Activity implements OnClickListener {

	private EditText mEdit_BW_Height,mEdit_BW_Weight,mEdit_BW_Date,mEdit_BW_Time;
	private Button mBtn_BW_Submit,mBtn_BW_Cancel,mBtn_BW_Delete;
	private String BWID,Height,Weight,Date,Time,BMI,Url;
	private int mUID,method;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bodyweight_add_edit);
		
		mEdit_BW_Height = (EditText)findViewById(R.id.Edit_BW_Height);
		mEdit_BW_Weight = (EditText)findViewById(R.id.Edit_BW_Weight);
		mEdit_BW_Date = (EditText)findViewById(R.id.Edit_BW_Date);
		mEdit_BW_Time = (EditText)findViewById(R.id.Edit_BW_Time);
		
		mBtn_BW_Submit = (Button)findViewById(R.id.Btn_BW_Submit);
		mBtn_BW_Cancel = (Button)findViewById(R.id.Btn_BW_Cancel);
		mBtn_BW_Delete = (Button)findViewById(R.id.Btn_BW_Delete);
		
		mBtn_BW_Submit.setOnClickListener(this);
		mBtn_BW_Cancel.setOnClickListener(this);
		mBtn_BW_Delete.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		method = bundle.getInt("method");
		
		if(method==1){
			mBtn_BW_Delete.setVisibility(View.INVISIBLE);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BW/HealthHelper_BW_Add.php";
			
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date = sDateFormat.format(new java.util.Date());
			
			SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
			Time = sTimeFormat.format(new java.util.Date());
			
			mEdit_BW_Date.setText(Date);
			mEdit_BW_Time.setText(Time);
		}
		if(method==2){
			mBtn_BW_Delete.setVisibility(View.VISIBLE);
			BWID = bundle.getString("BWID");
			Height = bundle.getString("Height");
			Weight = bundle.getString("Weight");
			Date = bundle.getString("Date");
			Time = bundle.getString("Date");
			
			mEdit_BW_Height.setText(Height);
			mEdit_BW_Weight.setText(Weight);
			mEdit_BW_Date.setText(Date);
			mEdit_BW_Time.setText(Time);
			
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BW/HealthHelper_BW_Edit.php";
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_BW_Submit){
			Height = mEdit_BW_Height.getText().toString();
			Weight = mEdit_BW_Weight.getText().toString();
			Date = mEdit_BW_Date.getText().toString();
			Time = mEdit_BW_Time.getText().toString();
			BMI = Double.toString(Double.parseDouble(Weight)/((Double.parseDouble(Height)/100)*(Double.parseDouble(Height)/100)));
			if(method!=2) new AccessDBTask().execute(Integer.toString(mUID), Height, Weight, BMI, Date, Time);
			else new AccessDBTask().execute(BWID, Height, Weight, BMI, Date, Time);
		}
		if(id == R.id.Btn_BW_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Bodyweight_add_edit.this,Bodyweight.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_BW_Delete){
			method = 3;
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/BW/HealthHelper_BW_Delete.php";
			new AccessDBTask().execute(BWID);
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
			Intent go = new Intent(Bodyweight_add_edit.this,Bodyweight.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Bodyweight_add_edit.this.finish();
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(method==1){
        	nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Height", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Weight", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("BMI", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[4]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[5]));
        }
        if(method==2){
        	nameValuePairs.add(new BasicNameValuePair("BWID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Height", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Weight", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("BMI", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[4]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[5]));
        }
        if(method==3){
        	nameValuePairs.add(new BasicNameValuePair("BWID", inputStrings[0]));
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
