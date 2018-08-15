package com.work.nzqpdaili.utils;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

public class SpannableStringUtlis {
	/**
	 * 全局的置换字符串
	 * */
	public SpannableString SpannableString;

	public int start = 0;// 开始位置
	public int end = 0;// 结束位置

	public SpannableStringUtlis(String strs) {
		SpannableString = new SpannableString(strs);
	}

	/**
	 * 设置起始位置
	 * */
	public void setStartEnd(int start, int end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * 字体颜色设置
	 * */
	public void setForegroundColorSpan(int color) {
		// 字体颜色设置(ForegroundColorSpan)
		ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
		// 将这个Span应用于指定范围的字体
		SpannableString.setSpan(foregroundColorSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	}

	/**
	 * 字体背景颜色
	 * */
	public void setBackgroundColorSpan(int color) {
		// 字体背景颜色(BackgroundColorSpan)
		BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(color);
		SpannableString.setSpan(backgroundColorSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 字体大小
	 * */
	public void setAbsoluteSizeSpan(int size) {
		// 字体大小(AbsoluteSizeSpan)
		AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size);
		SpannableString.setSpan(absoluteSizeSpan, start, end,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	}

	/**
	 * 字体粗体、斜体
	 * */
	public void setStyleSpan() {
		// 字体粗体、斜体(StyleSpan)
		StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
		SpannableString.setSpan(styleSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 文字加删除线
	 * */
	public void StrikethroughSpan() {
		// 删除线(StrikethroughSpan)
		StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
		SpannableString.setSpan(strikethroughSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 文字加删除线
	 * */
	public void StrikethroughSpan(int start, int end) {
		// 删除线(StrikethroughSpan)
		StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
		SpannableString.setSpan(strikethroughSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 文字加下划线
	 * */
	public void setUnderlineSpan() {
		// 下划线（UnderlineSpan）
		UnderlineSpan span = new UnderlineSpan();
		SpannableString.setSpan(span, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 文字加下划线
	 * */
	public void setUnderlineSpan(int start, int end) {
		// 下划线（UnderlineSpan）
		UnderlineSpan span = new UnderlineSpan();
		SpannableString.setSpan(span, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 文字图片置换
	 * */
	public void setUnderlineSpan(Drawable mDrawable) {
		mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(),
				mDrawable.getIntrinsicHeight());
		ImageSpan imageSpan = new ImageSpan(mDrawable, ImageSpan.ALIGN_BASELINE);
		SpannableString.setSpan(imageSpan, start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

}
