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

public class Diet extends Activity {

	private ListView mList_Diet_Data;
	private int mUID,method;
	private String[] DID,Meal,Price,Date,Time;
	private String Res = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diet);
		
		mList_Diet_Data = (ListView)findViewById(R.id.List_Diet_Data);
		
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
			Intent go = new Intent(Diet.this,Diet_add_edit.class);
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
	    String [] meal,price,date,time;
	    public MyAdapter(Context c,String [] meal,String [] price, String[] date,String[] time){
	        inflater = LayoutInflater.from(c);
	        this.meal = meal;
	        this.price = price;
	        this.date = date;
	        this.time = time;
	    }
	    @Override
	    public int getCount() {
	        return meal.length;
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
	        view = inflater.inflate(R.layout.activity_diet_adapter,viewGroup,false);
	        TextView mText_Diet_Meal,mText_Diet_Price,mText_Diet_Date,mText_Diet_Time;
	        mText_Diet_Meal = (TextView) view.findViewById(R.id.Text_Diet_Meal);
	        mText_Diet_Price = (TextView) view.findViewById(R.id.Text_Diet_Price);
	        mText_Diet_Date = (TextView) view.findViewById(R.id.Text_Diet_Date);
	        mText_Diet_Time = (TextView) view.findViewById(R.id.Text_Diet_Time);
	        
	        mText_Diet_Meal.setText(meal[i]);
	        mText_Diet_Price.setText(price[i]);
	        mText_Diet_Date.setText(date[i]);
	        mText_Diet_Time.setText(time[i]);
	        
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
				Intent go = new Intent(Diet.this,Diet_add_edit.class);
				bundle.putInt("UID", mUID);
				bundle.putInt("method", method);
				bundle.putString("DID",DID[Integer.parseInt(content)]);
				bundle.putString("Meal",Meal[Integer.parseInt(content)]);
				bundle.putString("Price",Price[Integer.parseInt(content)]);
				bundle.putString("Date",Date[Integer.parseInt(content)]);
				bundle.putString("Time",Time[Integer.parseInt(content)]);
				go.putExtras(bundle);
				startActivity(go);
				Diet.this.finish();
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
	        mList_Diet_Data.setAdapter(new MyAdapter(Diet.this,Meal,Price,Date,Time));
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
            HttpPost httppost = new HttpPost("http://web.csie.chu.edu.tw/~crane0911/health/diet/HealthHelper_diet_Search.php");
            
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
    	  DID = new String[jArray.length()];
    	  Meal = new String[jArray.length()];
    	  Price = new String[jArray.length()];
    	  Date = new String[jArray.length()];
    	  Time = new String[jArray.length()];
    	  for (int i = 0; i < jArray.length(); i++) {
    	    JSONObject json_data = jArray.getJSONObject(i);
    	    DID[i] = json_data.getString("DID");
    	    Meal[i] = json_data.getString("Meal");
    	    Price[i] = json_data.getString("Price");
    	    Date[i] = json_data.getString("Date");
    	    Time[i] = json_data.getString("Time");
    	  };
    	} catch(JSONException e){
    	  Log.e("log_tag", "Error parsing data "+e.toString());
    	}
        return resultStr;
    }
}
