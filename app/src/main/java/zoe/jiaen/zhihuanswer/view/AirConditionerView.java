package zoe.jiaen.zhihuanswer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Liujilong on 2015/12/18.
 * liujilong.me@gmail.com
 */
public class AirConditionerView extends View {
    private float mTemperature = 24f;

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private float mMinSize;
    private float mGapWidth;
    private float mInnerRadius;
    private float mRadius;
    private float mCenter;

    private RectF mArcRect;
    private SweepGradient mSweepGradient;

    public AirConditionerView(Context context) {
        super(context, null);
    }

    public AirConditionerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirConditionerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mArcPaint = new Paint();
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xffdddddd);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xff64646f);
    }

    private void initSize(){
        mGapWidth = 56*mMinSize;
        mInnerRadius = 180*mMinSize;
        mCenter = 300*mMinSize;
        mRadius = 208*mMinSize;

        mArcRect = new RectF(mCenter-mRadius,mCenter-mRadius,mCenter+mRadius,mCenter+mRadius);
        int[] colors = {0xFFE5BD7D,0xFFFAAA64,
                0xFFFFFFFF, 0xFF6AE2FD,
                0xFF8CD0E5, 0xFFA3CBCB,
                0xFFBDC7B3, 0xFFD1C299, 0xFFE5BD7D, };
        float[] positions = {0,1f/8,2f/8,3f/8,4f/8,5f/8,6f/8,7f/8,1};
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors , positions);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw arc
        mArcPaint.setStrokeWidth(mGapWidth);
        int gapDegree = getDegree();
        mArcPaint.setShader(mSweepGradient);
        canvas.drawArc(mArcRect, -225, gapDegree + 225, false, mArcPaint);
        mArcPaint.setShader(null);
        mArcPaint.setColor(Color.WHITE);
        canvas.drawArc(mArcRect, gapDegree, 45 - gapDegree, false, mArcPaint);

        // draw line
        mLinePaint.setStrokeWidth(mMinSize*1.5f);
        for(int i = 0; i<120;i++){
            if(i<=45 || i >= 75){
                float top = mCenter-mInnerRadius-mGapWidth;
                if(i%15==0){
                    top = top - 20*mMinSize;
                }
                canvas.drawLine(mCenter, mCenter - mInnerRadius, mCenter, top, mLinePaint);
            }
            canvas.rotate(3,mCenter,mCenter);
        }
        // draw text
        mTextPaint.setTextSize(mMinSize*30);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for(int i = 16; i<29; i+=2){
            float r = mInnerRadius+mGapWidth + 40*mMinSize;
            float x = (float) (mCenter + r*Math.cos((26-i)/2*Math.PI/4));
            float y = (float) (mCenter - r*Math.sin((26-i)/2*Math.PI/4));
            canvas.drawText(""+i,x,y - ((mTextPaint.descent() + mTextPaint.ascent()) / 2),mTextPaint);
        }
    }

    private int getDegree(){
        checkTemperature(mTemperature);
        return -225 + (int)((mTemperature-16)/12*90+0.5f)*3;
    }
    private void checkTemperature(float t){
        if(t<16 || t > 28){
            throw new RuntimeException("Temperature out of range");
        }
    }

    public void setTemperature(float t){
        checkTemperature(t);
        mTemperature = t;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        double distance = Math.sqrt((x - mCenter) * (x - mCenter) + (y - mCenter) * (y - mCenter));
        if(distance < mInnerRadius || distance > mInnerRadius + mGapWidth){
            return false;
        }
        double degree = Math.atan2(-(y-mCenter),x-mCenter);
        if(-3*Math.PI/4<degree && degree < -Math.PI/4){
            return false;
        }
        if(degree < -3*Math.PI/4){
            degree = degree + 2*Math.PI;
        }
        float t = (float) (26 - degree*8/Math.PI);
        setTemperature(t);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = Integer.MAX_VALUE;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        mMinSize = width/600f;
        int size = width;
        initSize();
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(size, heightSize);
        } else {
            //Be whatever you want
            height = size;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }
}
