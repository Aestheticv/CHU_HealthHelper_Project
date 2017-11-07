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

public class Diet_add_edit extends Activity implements OnClickListener {

	private EditText mEdit_Diet_Meal,mEdit_Diet_Price,mEdit_Diet_Date,mEdit_Diet_Time;
	private Button mBtn_Diet_Submit,mBtn_Diet_Cancel,mBtn_Diet_Delete;
	private String DID,Meal,Price,Date,Time,Url;
	private int mUID,method;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diet_add_edit);
		
		mEdit_Diet_Meal = (EditText)findViewById(R.id.Edit_Diet_Meal);
		mEdit_Diet_Price = (EditText)findViewById(R.id.Edit_Diet_Price);
		mEdit_Diet_Date = (EditText)findViewById(R.id.Edit_Diet_Date);
		mEdit_Diet_Time = (EditText)findViewById(R.id.Edit_Diet_Time);
		
		mBtn_Diet_Submit = (Button)findViewById(R.id.Btn_Diet_Submit);
		mBtn_Diet_Cancel = (Button)findViewById(R.id.Btn_Diet_Cancel);
		mBtn_Diet_Delete = (Button)findViewById(R.id.Btn_Diet_Delete);
		
		mBtn_Diet_Submit.setOnClickListener(this);
		mBtn_Diet_Cancel.setOnClickListener(this);
		mBtn_Diet_Delete.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		method = bundle.getInt("method");
		
		if(method==1){
			mBtn_Diet_Delete.setVisibility(View.INVISIBLE);
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/diet/HealthHelper_diet_Add.php";
			
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date = sDateFormat.format(new java.util.Date());
			
			SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
			Time = sTimeFormat.format(new java.util.Date());
			
			mEdit_Diet_Date.setText(Date);
			mEdit_Diet_Time.setText(Time);
		}
		if(method==2){
			mBtn_Diet_Delete.setVisibility(View.VISIBLE);
			DID = bundle.getString("DID");
			Meal = bundle.getString("Meal");
			Price = bundle.getString("Price");
			Date = bundle.getString("Date");
			Time = bundle.getString("Time");
			
			mEdit_Diet_Meal.setText(Meal);
			mEdit_Diet_Price.setText(Price);
			mEdit_Diet_Date.setText(Date);
			mEdit_Diet_Time.setText(Time);
			
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/diet/HealthHelper_diet_Edit.php";
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_Diet_Submit){
			Meal = mEdit_Diet_Meal.getText().toString();
			Price = mEdit_Diet_Price.getText().toString();
			Date = mEdit_Diet_Date.getText().toString();
			Time = mEdit_Diet_Time.getText().toString();
			if(method!=2) new AccessDBTask().execute(Integer.toString(mUID), Meal, Price, Date, Time);
			else new AccessDBTask().execute(DID, Meal, Price, Date, Time);
		}
		if(id == R.id.Btn_Diet_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Diet_add_edit.this,Diet.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_Diet_Delete){
			method = 3;
			Url = "http://web.csie.chu.edu.tw/~crane0911/health/diet/HealthHelper_diet_Delete.php";
			new AccessDBTask().execute(DID);
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
			Intent go = new Intent(Diet_add_edit.this,Diet.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Diet_add_edit.this.finish();
        }
    }
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(method==1){
        	nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Meal", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Price", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[4]));
        }
        if(method==2){
        	nameValuePairs.add(new BasicNameValuePair("DID", inputStrings[0]));
        	nameValuePairs.add(new BasicNameValuePair("Meal", inputStrings[1]));
        	nameValuePairs.add(new BasicNameValuePair("Price", inputStrings[2]));
        	nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[3]));
        	nameValuePairs.add(new BasicNameValuePair("Time", inputStrings[4]));
        }
        if(method==3){
        	nameValuePairs.add(new BasicNameValuePair("DID", inputStrings[0]));
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
