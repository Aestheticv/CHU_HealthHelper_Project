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
import tw.csie.chu.edu.healthhelper.Bloodpressure.MyAdapter;
import tw.csie.chu.edu.healthhelper.Bloodpressure.MyAdapter.LongClick;

public class Bodyweight extends Activity {

	private ListView mList_BW_Data;
	private int mUID,method;
	private String[] BWID,Height,Weight,BMI,Date,Time;
	private String Res = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bodyweight);
		
		mList_BW_Data = (ListView)findViewById(R.id.mList_BW_Data);
		
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
			Intent go = new Intent(Bodyweight.this,Bodyweight_add_edit.class);
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
	    String [] height,weight,bmi,date,time;
	    public MyAdapter(Context c,String [] height,String [] weight, String[] bmi, String[] date,String[] time){
	        inflater = LayoutInflater.from(c);
	        this.height = height;
	        this.weight = weight;
	        this.bmi = bmi;
	        this.date = date;
	        this.time = time;
	    }
	    @Override
	    public int getCount() {
	        return height.length;
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
	        view = inflater.inflate(R.layout.activity_bodyweight_adapter,viewGroup,false);
	        TextView mText_BW_Height,mText_BW_Weight,mText_BW_BMI,mText_BW_Date,mText_BW_Time;
	        mText_BW_Height = (TextView) view.findViewById(R.id.Text_BW_Height);
	        mText_BW_Weight = (TextView) view.findViewById(R.id.Text_BW_Weight);
	        mText_BW_BMI = (TextView) view.findViewById(R.id.Text_BW_BMI);
	        mText_BW_Date = (TextView) view.findViewById(R.id.Text_BW_Date);
	        mText_BW_Time = (TextView) view.findViewById(R.id.Text_BW_Time);
	        
	        mText_BW_Height.setText(height[i]);
	        mText_BW_Weight.setText(weight[i]);
	        mText_BW_BMI.setText(bmi[i]);
	        mText_BW_Date.setText(date[i]);
	        mText_BW_Time.setText(time[i]);
	        
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
				Intent go = new Intent(Bodyweight.this,Bodyweight_add_edit.class);
				bundle.putInt("UID", mUID);
				bundle.putInt("method", method);
				bundle.putString("BWID",BWID[Integer.parseInt(content)]);
				bundle.putString("Height",Height[Integer.parseInt(content)]);
				bundle.putString("Weight",Weight[Integer.parseInt(content)]);
				bundle.putString("BMI",BMI[Integer.parseInt(content)]);
				bundle.putString("Date",Date[Integer.parseInt(content)]);
				bundle.putString("Time",Time[Integer.parseInt(content)]);
				go.putExtras(bundle);
				startActivity(go);
				Bodyweight.this.finish();
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
	        mList_BW_Data.setAdapter(new MyAdapter(Bodyweight.this,Height,Weight,BMI,Date,Time));
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
            HttpPost httppost = new HttpPost("http://web.csie.chu.edu.tw/~crane0911/health/BW/HealthHelper_BW_Search.php");
            
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
    	  BWID = new String[jArray.length()];
    	  Height = new String[jArray.length()];
    	  Weight = new String[jArray.length()];
    	  BMI = new String[jArray.length()];
    	  Date = new String[jArray.length()];
    	  Time = new String[jArray.length()];
    	  for (int i = 0; i < jArray.length(); i++) {
    	    JSONObject json_data = jArray.getJSONObject(i);
    	    BWID[i] = json_data.getString("BWID");
    	    Height[i] = json_data.getString("Height");
    	    Weight[i] = json_data.getString("Weight");
    	    BMI[i] = json_data.getString("BMI");
    	    Date[i] = json_data.getString("Date");
    	    Time[i] = json_data.getString("Time");
    	  };
    	} catch(JSONException e){
    	  Log.e("log_tag", "Error parsing data "+e.toString());
    	}
        return resultStr;
    }
}
