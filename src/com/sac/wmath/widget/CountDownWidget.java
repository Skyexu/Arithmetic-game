package com.sac.wmath.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sac.wmath.R;

/**
 * 自定义倒计时控件
 *
 * 
 */
public class CountDownWidget extends View {
    private static final int MIN_HEIGHT = 10;
    private static final int MIN_WIDTH = 20;

    private int fullNumber = 100;// 最大值
    private int decreaseNumber = 5;// 每次缩减的值
    private int currentNumber = 100;// 当前值
    private int fullColor = Color.BLUE;// 值最大到警告之前的颜色
    private int alarmColor = Color.RED;// 到达警戒值之后的颜色

    Paint paint;

    RectF rectF;// 用于绘制圆角矩形的时候使用的矩形数据

    private int width = MIN_WIDTH;// 控件的宽度
    private int height = MIN_HEIGHT;// 控件的高度

    public CountDownWidget(Context context) {
        super(context);
        initialize();
    }

    public CountDownWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeAttribute(context, attrs);
        initialize();
    }

    public CountDownWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttribute(context, attrs);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentNumber * 2 >= fullNumber)
            paint.setColor(fullColor);
        else
            paint.setColor(alarmColor);
        int rectWidth = (int) (currentNumber * width * 1.0 / fullNumber);
        rectF.set(getPaddingLeft(), getPaddingTop(), rectWidth, height
                - getPaddingBottom());
        canvas.drawRect(rectF, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 测量宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = getPaddingLeft() + getPaddingRight() + MIN_WIDTH;
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(desired, widthSize);
            }
        }

        // 测量高度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = getPaddingTop() + getPaddingBottom() + MIN_HEIGHT;
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(desired, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    /**
     * 初始化倒计时控件的相关属性值
     *
     * @param context 控件的上下文
     * @param attrs   控件的属性集合
     */
    private void initializeAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CountDownWidget);
        //		fullNumber = typedArray.getInt(R.styleable.CountDownWidget_full_number,
        //				100);
        fullNumber = 100;
        //		decreaseNumber = typedArray.getInt(
        //				R.styleable.CountDownWidget_decrease_number, 5);
        decreaseNumber = 5;
        fullColor = typedArray.getColor(R.styleable.CountDownWidget_full_color,
                Color.BLUE);
        alarmColor = typedArray.getColor(
                R.styleable.CountDownWidget_alarm_color, Color.RED);
        typedArray.recycle();
    }

    /**
     * 参数初始化
     */
    private void initialize() {
        paint = new Paint();
        rectF = new RectF();
    }

    /**
     * 重置控件参数
     */
    public void reset() {
        currentNumber = fullNumber;
        invalidate();
    }

    /**
     * 改变(缩减)当前计数
     */
    public void changeCurrentNumber() {
        currentNumber = currentNumber - decreaseNumber;
        if (currentNumber < 0) {
            currentNumber = 0;
        }
        invalidate();
    }
}
