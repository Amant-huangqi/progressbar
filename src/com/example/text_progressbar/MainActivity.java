package com.example.text_progressbar;

import com.example.view.HorizontalProgressbarWithProgress;

import android.R.anim;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private HorizontalProgressbarWithProgress mHProgress;
	
	private static final int MSG_UPDATE = 0X110;//定义消息常量
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			int progress = mHProgress.getProgress();
			mHProgress.setProgress(++progress);
			if(progress>=100){
				mHandler.removeMessages(MSG_UPDATE);
			}
			
			mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);//每次延迟100毫秒发出去
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHProgress = (HorizontalProgressbarWithProgress) findViewById(R.id.id_progress01);
		
		mHandler.sendEmptyMessage(MSG_UPDATE);
	}

	

}
