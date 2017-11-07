package tw.csie.chu.edu.healthhelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity implements OnClickListener {

	private EditText mEdit_SignUp_ID,mEdit_SignUp_Passwd,mEdit_SignUp_ConfPasswd,mEdit_SignUp_Name,mEdit_SignUp_Disease;
	private Button mBtn_SignUp_Submit,mBtn_SignUp_Cancel;
	private String ID,Passwd,ConfPasswd,Name,Disease,Date;
	private Thread signup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		this.mEdit_SignUp_ID = (EditText)findViewById(R.id.Edit_SignUp_ID);
		this.mEdit_SignUp_Passwd = (EditText)findViewById(R.id.Edit_SignUp_Passwd);
		this.mEdit_SignUp_ConfPasswd = (EditText)findViewById(R.id.Edit_SignUp_ConfPasswd);
		this.mEdit_SignUp_Name = (EditText)findViewById(R.id.Edit_SignUp_Name);
		this.mEdit_SignUp_Disease = (EditText)findViewById(R.id.Edit_SignUp_Disease);
		
		this.mBtn_SignUp_Submit = (Button)findViewById(R.id.Btn_SignUp_Submit);
		this.mBtn_SignUp_Cancel = (Button)findViewById(R.id.Btn_SignUp_Cancel);
		
		this.mBtn_SignUp_Submit.setOnClickListener(this);
		this.mBtn_SignUp_Cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_SignUp_Submit){
			ID = this.mEdit_SignUp_ID.getText().toString();
			Passwd = this.mEdit_SignUp_Passwd.getText().toString();
			ConfPasswd = this.mEdit_SignUp_ConfPasswd.getText().toString();
			Name = this.mEdit_SignUp_Name.getText().toString();
			Disease = this.mEdit_SignUp_Disease.getText().toString();
			
			if(ID.equals("")){
				Toast.makeText(this,"帳號不能為空",Toast.LENGTH_SHORT).show();
			}
			else if(Name.equals("")){
				Toast.makeText(this,"姓名不能為空",Toast.LENGTH_SHORT).show();
			}
			else if(Passwd.equals("")){
				Toast.makeText(this,"密碼不能為空",Toast.LENGTH_SHORT).show();
			}
			else if(!Passwd.equals(ConfPasswd)){
				Toast.makeText(this,"密碼不相符",Toast.LENGTH_SHORT).show();
			}
			else{
				ID = mEdit_SignUp_ID.getText().toString();
				Passwd = mEdit_SignUp_Passwd.getText().toString();
				ConfPasswd = mEdit_SignUp_ConfPasswd.getText().toString();
				Name = mEdit_SignUp_Name.getText().toString();
				Disease = mEdit_SignUp_Disease.getText().toString();
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date = sDateFormat.format(new java.util.Date());
				new AccessDBTask().execute(ID, Passwd, ConfPasswd, Name, Disease, Date);
			}
			
		}
		else if(id == R.id.Btn_SignUp_Cancel){
			Intent go = new Intent(this,Login.class);
			startActivity(go);
		}
	}
	
	private class AccessDBTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            return accessDatabase(args);
        }

        @Override
        protected void onPostExecute(String str) {
        	Intent go = new Intent(SignUp.this,Login.class);
        	startActivity(go);
        	SignUp.this.finish();
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("ID", inputStrings[0]));
        nameValuePairs.add(new BasicNameValuePair("Passwd", inputStrings[1]));
        nameValuePairs.add(new BasicNameValuePair("ConfPasswd", inputStrings[2]));
        nameValuePairs.add(new BasicNameValuePair("Name", inputStrings[3]));
        nameValuePairs.add(new BasicNameValuePair("Disease", inputStrings[4]));
        nameValuePairs.add(new BasicNameValuePair("Date", inputStrings[5]));
        
        InputStream is = null;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://web.csie.chu.edu.tw/~crane0911/health/User/HealthHelper_SignUp.php");
            
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