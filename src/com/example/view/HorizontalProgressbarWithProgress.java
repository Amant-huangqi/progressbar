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
	// ��ʼ��Ĭ��ֵ
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
	 * �����ļ��г�ʼ��ʹ��
	 * 
	 * @param context
	 * @param attrs
	 */
	public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * �û���ʼ��
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
		// ��ȡ�Զ�������
		obtainStyledAttrs(attrs);
	}

	/**
	 * ��ȡ�Զ�������
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
		mPaint.setTextSize(mTextSize);//���������size
	}
	
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//int widthMode = MeasureSpec.getMode(widthMeasureSpec);//��ȵ�ģʽ
		//�ڽ��������龰�� ��ȿ϶���һ����ȷ��ֵ
		int widthVal = MeasureSpec.getSize(widthMeasureSpec);//�õ����
		int height = measureHeight(heightMeasureSpec);//�õ��߶�
		
		setMeasuredDimension(widthVal, height);//����view�Ŀ��
		
		mRealWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();//��������Ŀ��
	}

	private int measureHeight(int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		
		if(mode == MeasureSpec.EXACTLY){//�������ģʽ�Ǿ�ȷֵ
			result = size;
		}else{
			int textHeight = (int) (mPaint.descent()-mPaint.ascent());//��������ĸ߶�
			//ȡ�� ����߶� �Ѿ���ʾ�������߶� δ��ʾ�������߶� ���� ȡ�����ֵ
			result = getPaddingTop()+getPaddingBottom()+Math.max(Math.max(mReachHeight, mUnReachHight), Math.abs(textHeight));
		
			if(mode == MeasureSpec.AT_MOST){//����ֵ���ܳ���������size
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
		int textWidth = (int) mPaint.measureText(text);//����ı��Ŀ��
		float radio = getProgress()*1.0f/getMax();//ReachBar�ĳ��ȣ�x%��ʽ��
		float progressX = radio*mRealWidth;//������ ReachBar��ʵ�ʳ���
		if(progressX+textWidth>mRealWidth){//���ReachBar�ĳ��ȵ��� ���� �������ĳ���
			progressX = mRealWidth - textWidth; //progressX������״̬
			noNeedUnRech = true;
		}
		
		float endX = progressX - mTextOffset/2;//ReachBar�������ĳ��ȣ���ȥ�ٷֱ���ֵռ�ݵĳ��ȣ�
		if(endX>0){
			mPaint.setColor(mReachColor);//����������ɫ
			mPaint.setStrokeWidth(mReachHeight);//�������ø߶�
			canvas.drawLine(0, 0, endX, 0, mPaint);//��һ��ֱ�ߣ���ȴ�0->endx �߶ȴ�0->0��
		}
		
		//draw text
		mPaint.setColor(mTextColor);
		int y =(int) -(mPaint.descent()+mPaint.ascent()/2);
		canvas.drawText(text, progressX, y, mPaint);
		
		//draw unreachbar
		if(!noNeedUnRech){//�ж�unreachbar�Ƿ���Ҫ����
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
