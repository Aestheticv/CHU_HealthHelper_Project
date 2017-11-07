package tw.csie.chu.edu.healthhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener{

	private TextView mText_Home_UID;
	private ImageButton mBtn_Home_Bloodpressure,mBtn_Home_Bloodglucose,mBtn_Home_BodyWeight,
						mBtn_Home_Diet,mBtn_Home_Sport,mBtn_Home_HealthTip,mBtn_Home_Notice,
						mBtn_Home_Record,mBtn_Home_Setting;
	private int mUID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		this.mBtn_Home_Bloodpressure = (ImageButton)findViewById(R.id.Btn_Home_Bloodpressure);
		this.mBtn_Home_Bloodglucose = (ImageButton)findViewById(R.id.Btn_Home_Bloodglucose);
		this.mBtn_Home_BodyWeight = (ImageButton)findViewById(R.id.Btn_Home_BodyWeight);
		this.mBtn_Home_Diet = (ImageButton)findViewById(R.id.Btn_Home_Diet);
		this.mBtn_Home_Sport = (ImageButton)findViewById(R.id.Btn_Home_Sport);
		this.mBtn_Home_HealthTip = (ImageButton)findViewById(R.id.Btn_Home_HealthTip);
		this.mBtn_Home_Notice = (ImageButton)findViewById(R.id.Btn_Home_Notice);
		this.mBtn_Home_Record = (ImageButton)findViewById(R.id.Btn_Home_Record);
		this.mBtn_Home_Setting = (ImageButton)findViewById(R.id.Btn_Home_Setting);
		
		this.mBtn_Home_Bloodpressure.setOnClickListener(this);
		this.mBtn_Home_Bloodglucose.setOnClickListener(this);
		this.mBtn_Home_BodyWeight.setOnClickListener(this);
		this.mBtn_Home_Diet.setOnClickListener(this);
		this.mBtn_Home_Sport.setOnClickListener(this);
		this.mBtn_Home_HealthTip.setOnClickListener(this);
		this.mBtn_Home_Notice.setOnClickListener(this);
		this.mBtn_Home_Record.setOnClickListener(this);
		this.mBtn_Home_Setting.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		
		if(id == R.id.Btn_Home_Bloodpressure){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Bloodpressure.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_Bloodglucose){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Bloodglucose.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_BodyWeight){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Bodyweight.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_Diet){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Diet.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
	
		}
		else if(id == R.id.Btn_Home_Sport){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Sport.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_HealthTip){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,HealthTips.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_Notice){
	
		}
		else if(id == R.id.Btn_Home_Record){
			Intent go = new Intent(Home.this,Bluetooth1.class);
			startActivity(go);
		}
		else if(id == R.id.Btn_Home_Setting){
			Bundle bundle = new Bundle();
			Intent go = new Intent(Home.this,Account_Edit.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
		}
	}
}
