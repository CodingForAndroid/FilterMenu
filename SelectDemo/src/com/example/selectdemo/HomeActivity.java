package com.example.selectdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
	}
	
	public void type01(View view){
		//类型1
		startActivity(new Intent(HomeActivity.this, Type01Activity.class));
	}
	public void type02(View view){
		//类型2
		startActivity(new Intent(HomeActivity.this, Type02Activity.class));
	}
}
