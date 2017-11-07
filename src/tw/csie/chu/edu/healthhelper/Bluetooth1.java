package tw.csie.chu.edu.healthhelper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Bluetooth1 extends Activity {

	private static final int REQUEST_ENABLE_BLUETOOTH = 1;
	   private ListView listDevices;
	   private TextView state;
	   private BluetoothAdapter btAdapter;
	   private ArrayAdapter<String> btArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth1);
		state = (TextView)findViewById(R.id.state);
		   listDevices = (ListView)findViewById(R.id.listdevices);
		   btArrayAdapter = new ArrayAdapter<String>(
		            Bluetooth1.this,
		            android.R.layout.simple_list_item_1);
		   listDevices.setAdapter(btArrayAdapter);
		   btAdapter = BluetoothAdapter.getDefaultAdapter();
		   checkBluetoothState();
		   registerReceiver(BluetoothFoundReceiver, 
		    new IntentFilter(BluetoothDevice.ACTION_FOUND));

	}
	@Override
	protected void onDestroy() {
	   super.onDestroy();
	   unregisterReceiver(BluetoothFoundReceiver);
	}
	public void button_Click(View view) {
		   btArrayAdapter.clear();
		   btAdapter.startDiscovery();
		}


		private void checkBluetoothState() {
		   if (btAdapter == null){
		      state.setText("行動裝置不支援藍牙...");
		   }
		   else {
			   if (btAdapter.isEnabled()) {
			         if (btAdapter.isDiscovering()) {
			            state.setText("目前正在掃描藍牙裝置...");
			         }
			         else {
			            state.setText("行動裝置藍牙已啟用...");
			            Button btnScan = (Button) findViewById(R.id.btnScan);
			            btnScan.setEnabled(true);
			         }
			      }
			   else {
			         state.setText("行動裝置藍牙沒有啟用...");
			         // 
			         Intent i = new Intent(BluetoothAdapter.
			               ACTION_REQUEST_ENABLE);
			         startActivityForResult(i, 
			            REQUEST_ENABLE_BLUETOOTH);
			      }
			   }
			}
		@Override
		protected void onActivityResult(int requestCode, 
		                     int resultCode, Intent data) {
		   if (requestCode == REQUEST_ENABLE_BLUETOOTH ) {
		      checkBluetoothState();
		   }
		}


		private final BroadcastReceiver BluetoothFoundReceiver 
		                 = new BroadcastReceiver(){
		   @Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();
		      if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		         BluetoothDevice device = intent.getParcelableExtra(
		                BluetoothDevice.EXTRA_DEVICE);
		         btArrayAdapter.add(device.getName() +
		                     "\n" + device.getAddress());
		         btArrayAdapter.notifyDataSetChanged();
		      }
		   }
		};

}
