package tw.csie.chu.edu.healthhelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.style.ParagraphStyle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Login extends Activity implements OnClickListener {

	private EditText mEdit_Login_ID,mEdit_Login_Passwd;
	private Button mBtn_Login_Submit,mBtn_Login_SignUp;
	private String ID,Passwd,Operator;
	private String SQL_ID,SQL_Passwd,SQL_Name,SQL_Disease,SQL_Date;
	private int SQL_UID;
	private String Res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		this.mEdit_Login_ID = (EditText)findViewById(R.id.Edit_Login_ID);
		this.mEdit_Login_Passwd = (EditText)findViewById(R.id.Edit_Login_Passwd);
		
		this.mBtn_Login_Submit = (Button)findViewById(R.id.Btn_Login_Submit);
		this.mBtn_Login_Submit.setOnClickListener(this);
		this.mBtn_Login_SignUp = (Button)findViewById(R.id.Btn_Login_SignUp);
		this.mBtn_Login_SignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.Btn_Login_Submit){
			this.ID = mEdit_Login_ID.getText().toString();
			this.Passwd = mEdit_Login_Passwd.getText().toString();
			new AccessDBTask().execute(ID, Passwd);
		}
		else if(id == R.id.Btn_Login_SignUp){
			Intent go = new Intent(this,SignUp.class);
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
        	
        	if(Res.equals("帳號或密碼錯誤"))
			{
        		Toast.makeText(Login.this,Res,Toast.LENGTH_SHORT).show();
			}						
			//否則只顯示回傳訊息		
			else
			{
				
				Bundle bundle = new Bundle();
				Intent go = new Intent(Login.this,Home.class);
				bundle.putInt("UID", SQL_UID);
				go.putExtras(bundle);
				startActivity(go);
				Login.this.finish();
			}
        }
    }
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("ID", inputStrings[0]));
        nameValuePairs.add(new BasicNameValuePair("Passwd", inputStrings[1]));
        
        InputStream is = null;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://web.csie.chu.edu.tw/~crane0911/health/User/HealthHelper_Login.php");
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            
            HttpClient client = new DefaultHttpClient();
            
            ResponseHandler<String> h=new BasicResponseHandler();
            Res=new String(client.execute(httppost,h).getBytes(),HTTP.UTF_8);
			
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            //Toast toast = Toast.makeText(SignUpActivity.this,Log.e("log_tag", "Error in http connection "+e.toString()), Toast.LENGTH_LONG);
        }
      //parse json data
    	try {
    	  JSONArray jArray = new JSONArray(Res);
    	  for (int i = 0; i < jArray.length(); i++) {
    	    JSONObject json_data = jArray.getJSONObject(i);
    	    SQL_UID = json_data.getInt("UID");
    	    SQL_ID = json_data.getString("ID");
    	    SQL_Passwd = json_data.getString("Passwd");
    	    SQL_Name = json_data.getString("Name");
    	    SQL_Disease = json_data.getString("Disease");
    	    SQL_Date = json_data.getString("Date");
    	  }
    	} catch(JSONException e){
    	  Log.e("log_tag", "Error parsing data "+e.toString());
    	}
        return resultStr;
    }
}