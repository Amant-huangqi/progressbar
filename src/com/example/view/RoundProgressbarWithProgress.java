package com.example.view;

import com.example.text_progressbar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundProgressbarWithProgress extends
		HorizontalProgressbarWithProgress {

	private int mRadius = dp2px(30);

	private int mMaxPainWidth;

	public RoundProgressbarWithProgress(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressbarWithProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressbarWithProgress(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mReachHeight = (int) (mUnReachHight * 2.5f);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressbarWithProgress);
		mRadius = ta.getDimensionPixelOffset(
				R.styleable.RoundProgressbarWithProgress_radius, mRadius);
		ta.recycle();

		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeCap(Cap.ROUND);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mMaxPainWidth = Math.max(mReachHeight, mUnReachHight);
		// 默认四个padding 一致
		int expect = mRadius * 2 + mMaxPainWidth + getPaddingLeft()
				+ getPaddingRight();

		int width = resolveSize(expect, widthMeasureSpec);
		int height = resolveSize(expect, heightMeasureSpec);

		int readwidth = Math.min(width, height);

		mRadius = (readwidth - getPaddingLeft() - getPaddingRight() - mMaxPainWidth) / 2;

		setMeasuredDimension(readwidth, readwidth);

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		String text = getProgress() + "%";
		float textWidth = mPaint.measureText(text);
		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

		canvas.save();

		canvas.translate(getPaddingLeft() + mMaxPainWidth / 2, getPaddingTop()
				+ mMaxPainWidth / 2);
		mPaint.setStyle(Style.STROKE);

		// draw unreach bar
		mPaint.setColor(mUnReachColor);
		mPaint.setStrokeWidth(mUnReachHight);
		canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

		// draw reach bar
		mPaint.setColor(mUnReachColor);
		mPaint.setStrokeWidth(mUnReachHight);
		float sweepAngle = getProgress()*1.0f/getMax()*360;
		canvas.drawArc(new RectF(0, 0, mRadius*2, mRadius*2), 0, sweepAngle, false, mPaint);//绘制一个弧
		
		//draw text
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mTextColor);
		canvas.drawText(text, mRadius-textWidth/2, mRadius-textHeight, mPaint);
		

		canvas.restore();
	}
}
