package com.example.myclass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class FirstActivity extends Activity {
	String session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_first);
		SharedPreferences localdata=getSharedPreferences("localdata",0);
		localdata.edit().putString("host", "80").commit();
		localdata.edit().putString("ip", "139.129.119.93").commit();
		session=localdata.getString("session", "");
		Handler x=new Handler();
		x.postDelayed(new start(),1000);
	}
	class start implements Runnable{
		public void run() {
			if(session!="")
				startActivity(new Intent(FirstActivity.this,MainActivity.class));
			
			else
				startActivity(new Intent(FirstActivity.this,Login_Activity.class));
			FirstActivity.this.finish();
		}
	}
}
