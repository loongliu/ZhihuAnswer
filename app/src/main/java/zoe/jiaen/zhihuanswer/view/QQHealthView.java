package zoe.jiaen.zhihuanswer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liujilong on 2015/12/18.
 * liujilong.me@gmail.com
 */
public class QQHealthView extends View {

    private float mMinSize;
    private int mWidth;
    private int mHeight;
    private int mHeightUp;
    private int mBackgroundCorner;
    private int mStrokeWidth;

    private int mArcCenterX;
    private int mArcCenterY;
    private RectF mArcRect;


    private Paint mBackgroundPaint;
    private Paint mStrokePaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mBarPaint;
    private SweepGradient mSweepGradient;

    private int[] mSteps;


    public QQHealthView(Context context) {
        super(context, null);
    }

    public QQHealthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHealthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init();
    }

    private void init(){
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.RED);

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setDither(true);                    // set the dither to true
        mStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        mStrokePaint.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xFF77838F);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(new DashPathEffect(new float[] {10,3}, 0));

        mBarPaint = new Paint();
        mBarPaint.setAntiAlias(true);
        mBarPaint.setColor(0xFF63CFEC);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);

        mArcRect = new RectF();

        mSteps = new int[]{9000,12000,8900, 9200,7000,0,6000};

    }
    private void initSize(){
        mWidth = (int) (570*mMinSize);
        mHeight = (int) (650*mMinSize);
        mHeightUp = (int) (550*mMinSize);
        mBackgroundCorner = (int) (13*mMinSize);

        mStrokeWidth = (int) (20*mMinSize);

        mArcCenterX = mWidth/2;
        int arcRadius = (int) ((308 / 2) * mMinSize);
        mArcCenterY = (int) (200 * mMinSize);
        mArcRect.left = mArcCenterX - arcRadius;
        mArcRect.top = mArcCenterY - arcRadius;
        mArcRect.right = mArcCenterX + arcRadius;
        mArcRect.bottom = mArcCenterY + arcRadius;
        int[] colors = {0xFF9A9BF8,0xFF9AA2F7, 0xFF65CCD1,0xFF63D0CD,0xFF68CBD0,0xFF999AF6,0xFF9A9BF8};
        float[] positions = {0,1f/6,2f/6,3f/6,4f/6,5f/6,1};
        mSweepGradient = new SweepGradient(mArcCenterX, mArcCenterX, colors , positions);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 1.draw background
        mBackgroundPaint.setColor(0xFF4C5A67);
        drawUpRoundRect(0,0,mWidth,mHeightUp,mBackgroundCorner ,mBackgroundPaint,canvas);
        mBackgroundPaint.setColor(0xFF496980);
        drawBelowRoundRect(0,mHeightUp,mWidth,mHeight, mBackgroundCorner, mBackgroundPaint,canvas);

        // 2.draw arc
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setShader(mSweepGradient);
        canvas.drawArc(mArcRect,-240,300,false,mStrokePaint);

        // 3.draw text
        float xPos, yPos;
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(80*mMinSize);
        yPos = mArcCenterY-((mTextPaint.descent() + mTextPaint.ascent()) / 2);
        canvas.drawText("5599",mArcCenterX, yPos, mTextPaint);
        mTextPaint.setColor(0xFF828E98);
        mTextPaint.setTextSize(20*mMinSize);
        yPos = 140*mMinSize -((mTextPaint.descent() + mTextPaint.ascent()) / 2);
        canvas.drawText("截至22:23已走", mArcCenterX, yPos, mTextPaint);
        yPos = 260*mMinSize -((mTextPaint.descent() + mTextPaint.ascent()) / 2);
        canvas.drawText("好友平均3874步",mArcCenterX, yPos, mTextPaint);
        mTextPaint.setColor(Color.WHITE);
        xPos = mArcCenterX - 40*mMinSize;
        yPos = 380 * mMinSize;
        canvas.drawText("第", xPos, yPos, mTextPaint);
        xPos = mArcCenterX + 40*mMinSize;
        canvas.drawText("名", xPos, yPos, mTextPaint);
        mTextPaint.setTextSize(30*mMinSize);
        canvas.drawText("44", mArcCenterX, yPos, mTextPaint);


        mTextPaint.setTextSize(18*mMinSize);
        mTextPaint.setColor(0xFF77838F);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        xPos = 30*mMinSize;
        yPos = 420*mMinSize;
        canvas.drawText("最近7天",xPos, yPos, mTextPaint);

        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        xPos = 540 * mMinSize;
        yPos = 420*mMinSize;
        canvas.drawText("平均9341步/天", xPos, yPos, mTextPaint);

        // 4. draw dash line
        xPos = 30 * mMinSize;
        yPos = 440 * mMinSize;
        mLinePaint.setStrokeWidth(2*mMinSize);
        canvas.drawLine(xPos, yPos, mWidth - xPos, yPos,mLinePaint);

        // 5. draw bars and date
        mBarPaint.setStrokeWidth(mStrokeWidth);
        float halfBarGap = 510 * mMinSize / 14;
        float maxLength = 440 * mMinSize;
        yPos = 480 * mMinSize;
        int maxBar = 0;
        for(int step : mSteps){
            if(step > maxBar){
                maxBar = step;
            }
        }
        float length;
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(18*mMinSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for(int i = 0; i < mSteps.length; i++){
            xPos = (2*i+1)* halfBarGap + 30 *mMinSize;
            length = (yPos - maxLength) * mSteps[i]*1f/maxBar;
            canvas.drawLine(xPos, yPos, xPos, yPos -length, mBarPaint );
            String date = String.format("%02d日",8+i);
            canvas.drawText(date,xPos, 520*mMinSize, mTextPaint );
        }

        // 6.draw text below
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(23*mMinSize);
        canvas.drawText("这是被隐藏的内容",30 * mMinSize,610*mMinSize,mTextPaint);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(0xFF63CFEC);
        canvas.drawText("查看  >", 540*mMinSize, 610*mMinSize, mTextPaint);
    }

    private void drawUpRoundRect(float left, float top, float right, float bottom, float radius, Paint paint, Canvas canvas) {
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right-radius, top);
        path.quadTo(right,top,right, top+radius);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);
        canvas.drawPath(path, paint);
    }
    private void drawBelowRoundRect(float left, float top, float right, float bottom, float radius, Paint paint, Canvas canvas) {
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom-radius);
        path.quadTo(right, bottom,right-radius,bottom);
        path.lineTo(left+radius, bottom);
        path.quadTo(left,bottom,left,bottom-radius);
        path.lineTo(left, top);
        canvas.drawPath(path, paint);
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
        mMinSize = width/570.f;
        int desiredHeight = (int) (mMinSize*650);
        initSize();
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }


}