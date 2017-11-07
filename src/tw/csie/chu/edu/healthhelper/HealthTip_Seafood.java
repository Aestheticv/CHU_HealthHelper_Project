package tw.csie.chu.edu.healthhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HealthTip_Seafood extends Activity implements OnClickListener {

	private Button mBtn_HTS_Return;
	private int mUID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_tip__seafood);
		
		mBtn_HTS_Return = (Button)findViewById(R.id.Btn_HTS_Return);
		mBtn_HTS_Return.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();  
		mUID = bundle.getInt("UID");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		if(id == R.id.Btn_HTS_Return){
			Bundle bundle = new Bundle();
			Intent go = new Intent(HealthTip_Seafood.this,HealthTips.class);
			bundle.putInt("UID", mUID);
			go.putExtras(bundle);
			startActivity(go);
			this.finish();
		}
	}
}
