package com.example.selectdemo;

import java.lang.ref.WeakReference;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Type01Activity extends Activity implements OnClickListener {

	/** 时间 */
	public static final int RANKTYPE_TIME = 0x0 ;
	/** 分类 */
	public static final int RANKTYPE_CLASSIIFY = 0x1 ;
	
	private TextView classify;
	private TextView time;
	private TextView group_classify;
	private TextView group_time;
	private ListView list_filter;
	private LinearLayout menu;
	private String[] classArrays;
	private String[] timeArrays;
	
	private FilterAdapter adapter;
	

	protected Handler mHandler = new MyHandler(this);
	private TextView category;
	
	// 刷新UI
	private static class MyHandler extends Handler {
		private final WeakReference<Type01Activity> mWeakAct;

		public MyHandler(Type01Activity activity) {
			mWeakAct = new WeakReference<Type01Activity>(activity);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			final Type01Activity activity = mWeakAct.get();
			if (activity == null) {
				return;
			}
			activity.handleMsg(msg);
		}
	}
	
	public void handleMsg(Message msg) {
		
		switch (msg.what) {
		case FilterAdapter.MSG_WHAT_ONITEM_CLICK:
			String tag_id = msg.getData().getString(FilterAdapter.MSG_BUNDLE_KEY_TAGID) ; 
			int type =  msg.getData().getInt(FilterAdapter.MSG_BUNDLE_KEY_RANKTYPE) ;
			if(type==RANKTYPE_CLASSIIFY){
				category.setText(tag_id);
			}else{
				time.setText(tag_id);
			}
			adapter.notifyDataSetInvalidated();
			menu.setVisibility(View.GONE);
		}
		
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type01);
		
	
		findViews();
		addListener();
		init();
	}


	private void init() {
		classArrays = getResources().getStringArray(R.array.rank_classify);
		timeArrays = getResources().getStringArray(R.array.rank_time);
		
		adapter=new FilterAdapter(classArrays,RANKTYPE_CLASSIIFY);
		list_filter.setAdapter(adapter);
		
		time.setText("日");
		category.setText("全部");
	}

	private void addListener() {
		classify.setOnClickListener(this);
		group_classify.setOnClickListener(this);
		group_time.setOnClickListener(this);
		
	}

	private void findViews() {
		classify = (TextView) findViewById(R.id.classify);
		category = (TextView) findViewById(R.id.category);
		time = (TextView) findViewById(R.id.time);
		group_classify = (TextView) findViewById(R.id.txt_classify_group);
		group_time = (TextView) findViewById(R.id.txt_time_group);
		list_filter = (ListView) findViewById(R.id.list_filterc);
		
		menu = (LinearLayout) findViewById(R.id.ll_menu);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.classify:
			//点击分类
			menu.setVisibility(View.VISIBLE);
			break;
		case R.id.txt_classify_group:
			// 选择分类
			adapter=new FilterAdapter(classArrays,RANKTYPE_CLASSIIFY);
			list_filter.setAdapter(adapter);
			adapter.notifyDataSetInvalidated();
			break;
		case R.id.txt_time_group:
			// 选择时间
			adapter=new FilterAdapter(timeArrays,RANKTYPE_TIME);
			list_filter.setAdapter(adapter);
			adapter.notifyDataSetInvalidated();
			break;
		default:
			break;
		}

	}
	
	
	private class FilterAdapter extends BaseAdapter{
		/** 选择器类型(时间,类型) */
		public static final String MSG_BUNDLE_KEY_RANKTYPE= "msg_bundle_key_ranktype";
		
		/** tag_id */
		public static final String MSG_BUNDLE_KEY_TAGID= "msg_bundle_key_tagid";
		
		public static final int MSG_WHAT_ONITEM_CLICK= 0x00011;
		private String[] data;
		/** 选择器类型(时间,类型) */
		private int key_ranktype  ; 

		public FilterAdapter(String[]  str,int type){
			super();
			data=str;
			key_ranktype=type;
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
		public View getView( int position, View convertView, ViewGroup parent) {
			convertView=genrateItemLayout();
			RelativeLayout layout=(RelativeLayout) convertView.findViewById(R.id.id01);
			final TextView tv=(TextView) convertView.findViewById(R.id.id02);
			ImageView image=(ImageView) convertView.findViewById(R.id.id03);
			image.setVisibility(View.GONE);
			if(time.getText().toString().equals(data[position])){
				image.setVisibility(View.VISIBLE);
			}else if(category.getText().toString().equals(data[position])){
				image.setVisibility(View.VISIBLE);
			}
			tv.setText(data[position]);
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Message msg = Message.obtain() ; 
					msg.what = MSG_WHAT_ONITEM_CLICK ; 
					Bundle data = new Bundle() ; 
					data.putString(MSG_BUNDLE_KEY_TAGID, tv.getText().toString()) ; 
					data.putInt(MSG_BUNDLE_KEY_RANKTYPE, key_ranktype) ; 
					msg.setData(data) ; 
					mHandler.sendMessage(msg) ; 
				}
			});
			return convertView;
		}
		
		
		public View genrateItemLayout() {
			
			RelativeLayout layout = new RelativeLayout(Type01Activity.this) ;
			layout.setBackgroundColor(getResources().getColor(android.R.color.white)) ; 
			layout.setId(R.id.id01) ; 
			
			//类别文字
			TextView textView=new TextView(Type01Activity.this);
			textView.setGravity(Gravity.CENTER_VERTICAL) ; 
			textView.setPadding(dip2px(15), 0, 0, 0) ;
			textView.setId(R.id.id02) ;
			RelativeLayout.LayoutParams textParams =new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, dip2px(40));
			textParams.setMargins(dip2px(2), dip2px(1), dip2px(2), dip2px(1)); 
			
			layout.addView(textView, textParams) ; 
			
			//右侧对勾
			ImageView nikeView = new ImageView(Type01Activity.this) ; 
			nikeView.setImageResource(R.drawable.img_tick) ; 
			RelativeLayout.LayoutParams nickParams =new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nickParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT) ; 
			nickParams.addRule(RelativeLayout.CENTER_VERTICAL) ; 
			nickParams.setMargins(0	, 0, dip2px(25), 0) ; 
			nikeView.setId(R.id.id03) ; 
			
			layout.addView(nikeView, nickParams) ; 
			
			//底线
			View doteView = new View(Type01Activity.this) ; 
			doteView.setId(R.id.id04) ; 
			doteView.setBackgroundResource(R.drawable.shape_dotted_line_gray) ; 
			RelativeLayout.LayoutParams doteParams =new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(1));
			doteParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) ; 
			layout.addView(doteView, doteParams) ; 
			
			return layout;
		}
	}
	
	
	public   int dip2px( float dpValue) 
    {
        final float scale = Type01Activity.this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
