package tw.csie.chu.edu.healthhelper;

import java.io.InputStream;
import java.util.ArrayList;

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

public class Account_Edit extends Activity implements OnClickListener {

	private TextView mText_AE_UID;
	private EditText mEdit_AE_OPasswd,mEdit_AE_NPasswd,mEdit_AE_CPasswd;
	private Button mBtn_AE_Submit,mBtn_AE_Cancel;
	private String OPasswd,NPasswd,CPasswd,Url;
	private int mUID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account__edit);
		
		mText_AE_UID = (TextView)findViewById(R.id.Text_AE_UID);
		mEdit_AE_OPasswd = (EditText)findViewById(R.id.Edit_AE_OPasswd);
		mEdit_AE_NPasswd = (EditText)findViewById(R.id.Edit_AE_NPasswd);
		mEdit_AE_CPasswd = (EditText)findViewById(R.id.Edit_AE_CPasswd);
		mBtn_AE_Submit = (Button)findViewById(R.id.Btn_AE_Submit);
		mBtn_AE_Cancel = (Button)findViewById(R.id.Btn_AE_Cancel);
		
		mBtn_AE_Submit.setOnClickListener(this);
		mBtn_AE_Cancel.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		
		mText_AE_UID.setText(Integer.toString(mUID));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_AE_Submit){
			OPasswd = mEdit_AE_OPasswd.getText().toString();
			NPasswd = mEdit_AE_NPasswd.getText().toString();
			CPasswd = mEdit_AE_CPasswd.getText().toString();
			Url = "";
			new AccessDBTask().execute(Integer.toString(mUID), OPasswd, NPasswd, CPasswd);
		}
		if(id == R.id.Btn_AE_Cancel){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Account_Edit.this,Home.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
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
			Intent go = new Intent(Account_Edit.this,Home.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			Account_Edit.this.finish();
        }
    }
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        
        nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        nameValuePairs.add(new BasicNameValuePair("OPasswd", inputStrings[1]));
        nameValuePairs.add(new BasicNameValuePair("NPasswd", inputStrings[2]));
        nameValuePairs.add(new BasicNameValuePair("CPasswd", inputStrings[3]));
        
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
