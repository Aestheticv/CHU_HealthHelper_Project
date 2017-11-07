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
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Bloodpressure extends Activity {

	private ListView mList_Bloodpressure_Data;
	private int mUID,method;
	private String[] SBP,DBP,Pluse,Date,BPID,UID,Time;
	private String Res = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloodpressure);
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
		new AccessDBTask().execute(Integer.toString(mUID));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			method = 1;
			Bundle bundle = new Bundle();
			Intent go = new Intent(Bloodpressure.this,Bloodpressure_add_edit.class);
			bundle.putInt("UID", mUID);
			bundle.putInt("method", method);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class MyAdapter extends BaseAdapter{
	    private LayoutInflater inflater;
	    String [] sbp,dbp,pluse,date;
	    public MyAdapter(Context c,String [] sbp,String [] dbp, String[] pluse, String[] date){
	        inflater = LayoutInflater.from(c);
	        this.sbp = sbp;
	        this.dbp = dbp;
	        this.pluse = pluse;
	        this.date = date;
	    }
	    @Override
	    public int getCount() {
	        return sbp.length;
	    }

	    @Override
	    public Object getItem(int i) {
	        return null;
	    }

	    @Override
	    public long getItemId(int i) {
	        return i;
	    }

	    @Override
	    public View getView(int i, View view, ViewGroup viewGroup) {
	        view = inflater.inflate(R.layout.activity_bloodpressure_adapter,viewGroup,false);
	        TextView mText_Bloodpressure_SBP,mText_Bloodpressure_DBP,
	                 mText_Bloodpressure_Pluse,mText_Bloodpressure_Date;
	        mText_Bloodpressure_SBP = (TextView) view.findViewById(R.id.Text_Bloodpressure_SBP);
	        mText_Bloodpressure_DBP = (TextView) view.findViewById(R.id.Text_Bloodpressure_DBP);
	        mText_Bloodpressure_Pluse = (TextView) view.findViewById(R.id.Text_Bloodpressure_Pluse);
	        mText_Bloodpressure_Date = (TextView) view.findViewById(R.id.Text_Bloodpressure_Date);
	        
	        mText_Bloodpressure_SBP.setText(sbp[i]);
	        mText_Bloodpressure_DBP.setText(dbp[i]);
	        mText_Bloodpressure_Pluse.setText(pluse[i]);
	        mText_Bloodpressure_Date.setText(date[i]);
	        
	        view.setOnLongClickListener(new LongClick(Integer.toString(i)));
	        return view;
	    }
	    class LongClick implements View.OnLongClickListener{
	        private String content;
	        public LongClick(String content){
	            this.content = content;
	        }//用建構子獲得該item 的值
	        @Override
	        public boolean onLongClick(View view) {
	            ClipboardManager clipboard = (ClipboardManager)
	            view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
	            
	            method = 2;
				Bundle bundle = new Bundle();
				Intent go = new Intent(Bloodpressure.this,Bloodpressure_add_edit.class);
				bundle.putInt("UID", mUID);
				bundle.putInt("method", method);
				bundle.putString("BPID",BPID[Integer.parseInt(content)]);
				bundle.putString("SBP",SBP[Integer.parseInt(content)]);
				bundle.putString("DBP",DBP[Integer.parseInt(content)]);
				bundle.putString("Pluse",Pluse[Integer.parseInt(content)]);
				bundle.putString("Date",Date[Integer.parseInt(content)]);
				bundle.putString("Time",Time[Integer.parseInt(content)]);
				go.putExtras(bundle);
				startActivity(go);
				Bloodpressure.this.finish();
	            return true;
	        }
	    }
	}
	
	private class AccessDBTask extends AsyncTask<String, String, String> { 

		@Override
	    protected String doInBackground(String... args) {
	      return accessDatabase(args);
	    }
	    
		@Override
	    protected void onPostExecute(String str){
	        mList_Bloodpressure_Data = (ListView)findViewById(R.id.List_Bloodpressure_Data);
	  		mList_Bloodpressure_Data.setAdapter(new MyAdapter(Bloodpressure.this,SBP,DBP,Pluse,Date));
	    }
	}
	
	
	public String accessDatabase(String... inputStrings) {
        String result = "", resultStr = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("UID", inputStrings[0]));
        
        InputStream is = null;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://web.csie.chu.edu.tw/~crane0911/health/BP/HealthHelper_BP_Search.php");
            
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
    	  BPID = new String[jArray.length()];
    	  UID = new String[jArray.length()];
    	  SBP = new String[jArray.length()];
    	  DBP = new String[jArray.length()];
    	  Pluse = new String[jArray.length()];
    	  Date = new String[jArray.length()];
    	  Time = new String[jArray.length()];
    	  for (int i = 0; i < jArray.length(); i++) {
    	    JSONObject json_data = jArray.getJSONObject(i);
    	    BPID[i] = json_data.getString("BPID");
    	    UID[i] = json_data.getString("UID");
    	    SBP[i] = json_data.getString("SBP");
    	    DBP[i] = json_data.getString("DBP");
    	    Pluse[i] = json_data.getString("Pluse");
    	    Date[i] = json_data.getString("Date");
    	    Time[i] = json_data.getString("Time");
    	  };
    	} catch(JSONException e){
    	  Log.e("log_tag", "Error parsing data "+e.toString());
    	}
        return resultStr;
    }
}
