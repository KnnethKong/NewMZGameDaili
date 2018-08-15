/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package zuo.biao.library.ui.xlistview;

import zuo.biao.library.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private View mArrowImageView;
	private View mProgressBar;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;
	@SuppressLint("SimpleDateFormat")
	static private SimpleDateFormat sSDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private Date date = null;
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private static final int DEFAULT_HINT_COLOR = 0xFF333333;
	private static final int DEFAULT_TIME_COLOR = 0xFF666666;
	private final int ROTATE_ANIM_DURATION = 180;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	private TextView timeView;
	private long lastUpdateTime = 0L;
	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		date = new Date();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = findViewById(R.id.xlistview_header_progressbar);
		timeView = (TextView) findViewById(R.id.xlistview_header_time);
		timeView.setTextColor(DEFAULT_TIME_COLOR);
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
//			setTimeView(lastUpdateTime);
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				lastUpdateTime = System.currentTimeMillis();
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
//			setTimeView(lastUpdateTime);
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
//			setTimeView(lastUpdateTime);
			break;
			default:
		}
		
		mState = state;
	}
	
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	private void setTimeView(long lastUpdate) {
		if (lastUpdate > 0L) {
			date.setTime(lastUpdate);
			String time = getResources().getString(
					R.string.xlistview_header_last_time, sSDF.format(date));
			timeView.setVisibility(View.VISIBLE);
			timeView.setText(time);
		} else {
			timeView.setVisibility(View.GONE);
		}
	}
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
	public void setHintTextColor(int color) {
		mHintTextView.setTextColor(color);
	}

	public void setTimeTextColor(int color) {
		mHintTextView.setTextColor(color);
	}
}
