package com.example.view;

import com.example.text_progressbar.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class HorizontalProgressbarWithProgress extends ProgressBar {
	// 初始化默认值
	private static final int DEFAULT_TEXT_SIZE = 10;// sp
	private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
	private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
	private static final int DEFAULT_HEIGHT_UNREACH = 2;// dp
	private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
	private static final int DEFAULT_HEIGHT_REACH = 2;// dp
	private static final int DEFAULT_TEXT_OFFSET = 10;// dp
	
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
	protected int mTextColor = DEFAULT_TEXT_COLOR;
	protected int mUnReachColor = DEFAULT_COLOR_UNREACH;
	protected int mUnReachHight = dp2px(DEFAULT_HEIGHT_UNREACH);
	protected int mReachColor = DEFAULT_COLOR_REACH;
	protected int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
	protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
	
	protected Paint mPaint = new Paint();
	
	private int mRealWidth;

	/**
	 * 布局文件中初始化使用
	 * 
	 * @param context
	 * @param attrs
	 */
	public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * 用户初始化
	 * 
	 * @param context
	 */
	public HorizontalProgressbarWithProgress(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	
    
	public HorizontalProgressbarWithProgress(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		// 获取自定义属性
		obtainStyledAttrs(attrs);
	}

	/**
	 * 获取自定义属性
	 * @param attrs
	 */
	private void obtainStyledAttrs(AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.HorizontalProgressbarWithProgress);
		mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_size, mTextSize);
		mTextColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_text_color, mTextColor);
		mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_offset, mTextSize);
		mUnReachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color, mUnReachColor);
		mUnReachHight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height, mUnReachHight);
		mReachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_reach_color, mReachColor);
		mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_reach_height,mReachHeight);
		ta.recycle();
		mPaint.setTextSize(mTextSize);//设置字体的size
	}
	
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//int widthMode = MeasureSpec.getMode(widthMeasureSpec);//宽度的模式
		//在进度条的情景下 宽度肯定是一个明确的值
		int widthVal = MeasureSpec.getSize(widthMeasureSpec);//得到宽度
		int height = measureHeight(heightMeasureSpec);//得到高度
		
		setMeasuredDimension(widthVal, height);//设置view的宽高
		
		mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();//绘制区域的宽度
	}

	private int measureHeight(int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		
		if(mode == MeasureSpec.EXACTLY){//如果测量模式是精确值
			result = size;
		}else{
			int textHeight = (int) (mPaint.descent()-mPaint.ascent());//测量字体的高度
			//取出 字体高度 已经显示进度条高度 未显示进度条高度 三者 取出最大值
			result = getPaddingTop()+getPaddingBottom()+Math.max(Math.max(mReachHeight, mUnReachHight), Math.abs(textHeight));
		
			if(mode == MeasureSpec.AT_MOST){//测量值不能超过给定的size
				result = Math.min(result, size);
			}
		
		}
		
		
		return result;
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.save();
		canvas.translate(getPaddingLeft(), getHeight()/2);
		boolean noNeedUnRech = false;
		
		//deaw ReachBar
		String text = getProgress()+"%";
		int textWidth = (int) mPaint.measureText(text);//获得文本的宽度
		float radio = getProgress()*1.0f/getMax();//ReachBar的长度（x%形式）
		float progressX = radio*mRealWidth;//理论上 ReachBar的实际长度
		if(progressX+textWidth>mRealWidth){//如果ReachBar的长度等于 整个 进度条的长度
			progressX = mRealWidth - textWidth; //progressX结束的状态
			noNeedUnRech = true;
		}
		
		float endX = progressX - mTextOffset/2;//ReachBar的真正的长度（减去百分比数值占据的长度）
		if(endX>0){
			mPaint.setColor(mReachColor);//画笔设置颜色
			mPaint.setStrokeWidth(mReachHeight);//画笔设置高度
			canvas.drawLine(0, 0, endX, 0, mPaint);//花一条直线（宽度从0->endx 高度从0->0）
		}
		
		//draw text
		mPaint.setColor(mTextColor);
		int y =(int) -(mPaint.descent()+mPaint.ascent()/2);
		canvas.drawText(text, progressX, y, mPaint);
		
		//draw unreachbar
		if(!noNeedUnRech){//判断unreachbar是否需要绘制
			float start = progressX + mTextOffset/2 + textWidth;
			mPaint.setColor(mUnReachColor);
			mPaint.setStrokeWidth(mUnReachHight);
			canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
		}
	}

	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());

	}

}
