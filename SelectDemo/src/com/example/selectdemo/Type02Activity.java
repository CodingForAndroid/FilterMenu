package com.example.selectdemo;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Type02Activity extends Activity implements OnClickListener {

	private TextView classify;
	private LinearLayout ll_filter;
	private GridView gridview;
	private GridAdapter adapter;

	private MyHandler mHandler=new MyHandler(this);
	// Ë¢ÐÂUI
	private static class MyHandler extends Handler {
		private final WeakReference<Type02Activity> mWeakAct;

		public MyHandler(Type02Activity activity) {
			mWeakAct = new WeakReference<Type02Activity>(activity);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			final Type02Activity activity = mWeakAct.get();
			if (activity == null) {
				return;
			}
			activity.handleMsg(msg);
		}
	}
	
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case GridAdapter.MSG_WHAT_ONITEM_CLICK:
			int position=msg.getData().getInt(GridAdapter.MSG_BUNDLE_KEY_POS);
			String value=msg.getData().getString(GridAdapter.MSG_BUNDLE_KEY_TAGID);
			classify.setText(value);
			ll_filter.setVisibility(View.GONE);
			adapter.notifyDataSetInvalidated();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type02);

		findViews();
		addListener();
		init();
	}



	private void init() {
		String[] selects=getResources().getStringArray(R.array.array_grid);
		adapter = new GridAdapter(selects);
		gridview.setAdapter(adapter);
	}

	private void addListener() {
		classify.setOnClickListener(this);
	}

	private void findViews() {
		classify = (TextView) findViewById(R.id.classify);
		ll_filter = (LinearLayout) findViewById(R.id.layout_grid_filterc);
		gridview = (GridView) findViewById(R.id.grid_filterc);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.classify:
			ll_filter.setVisibility(View.VISIBLE);
			break;
		
		default:
			break;
		}

	}
	
	private class GridAdapter extends BaseAdapter{
		public static final int MSG_WHAT_ONITEM_CLICK= 0x00011;
		public static final String MSG_BUNDLE_KEY_TAGID= "msg_bundle_key_tagid";
		
		public static final String MSG_BUNDLE_KEY_POS= "msg_bundle_key_pos";
		
		private String[] data;
		public GridAdapter(String[] str){
		data=str;
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
		  convertView=genrateItemLayout();
		  final TextView tv=(TextView) convertView.findViewById(R.id.id02);
		  RelativeLayout layout= (RelativeLayout) convertView.findViewById(R.id.id01);
		 
		  tv.setText(data[position]);
		  if(classify.getText().toString().equals(data[position])){
			  tv.setBackgroundResource(R.drawable.shape_rectangle_color_blue_0067be);
		  }
		  convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg=Message.obtain();
				msg.what=MSG_WHAT_ONITEM_CLICK;
				Bundle data = new Bundle() ; 
				data.putString(MSG_BUNDLE_KEY_TAGID, tv.getText().toString()) ; 
				data.putInt(MSG_BUNDLE_KEY_POS, position) ; 
				msg.setData(data) ; 
				mHandler.sendMessage(msg) ; 
				
			}
		});
			return convertView;
		}
		
		
		public View genrateItemLayout() {
			
			int width = getWindowManager().getDefaultDisplay().getWidth() /  getResources().getInteger(R.integer.classify_main_filter_header_num) ;
			
			RelativeLayout layout = new RelativeLayout(Type02Activity.this) ;
			layout.setId(R.id.id01) ; 
			
			TextView textView = new TextView(Type02Activity.this);
			textView.setGravity(Gravity.CENTER) ; 
			textView.setBackgroundResource(R.drawable.shape_rectangle_color_white_border_gray) ; 
			textView.setId(R.id.id02) ;
			RelativeLayout.LayoutParams textParams =new RelativeLayout.LayoutParams(width, dip2px(40));
			
			textParams.setMargins(dip2px(2), dip2px(1), dip2px(2), dip2px(1)); 
			layout.addView(textView, textParams) ; 
			
			return layout;
		}
	}
	
	public   int dip2px( float dpValue) 
    {
        final float scale = Type02Activity.this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
