package tw.csie.chu.edu.healthhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HealthTips extends Activity implements OnClickListener {

	private Button mBtn_HT_Meet,mBtn_HT_Seafood,mBtn_HT_Veg,mBtn_HT_Drink,mBtn_HT_Return;
	private int mUID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_tips);
		
		mBtn_HT_Meet = (Button)findViewById(R.id.Btn_HT_Meet);
		mBtn_HT_Seafood = (Button)findViewById(R.id.Btn_HT_Seafood);
		mBtn_HT_Veg = (Button)findViewById(R.id.Btn_HT_Veg);
		mBtn_HT_Drink = (Button)findViewById(R.id.Btn_HT_Drink);
		mBtn_HT_Return = (Button)findViewById(R.id.Btn_HT_Return);
		
		mBtn_HT_Meet.setOnClickListener(this);
		mBtn_HT_Seafood.setOnClickListener(this);
		mBtn_HT_Veg.setOnClickListener(this);
		mBtn_HT_Drink.setOnClickListener(this);
		mBtn_HT_Return.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id =v.getId();
		
		if(id == R.id.Btn_HT_Meet){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTips.this,HealthTip_Meet.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_HT_Seafood){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTips.this,HealthTip_Seafood.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_HT_Veg){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTips.this,HealthTip_Veg.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_HT_Drink){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTips.this,HealthTip_Drink.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
		if(id == R.id.Btn_HT_Return){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTips.this,Home.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
	}
}
