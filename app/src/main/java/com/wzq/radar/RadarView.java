package com.wzq.radar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by windows on 2017/7/10.
 */

public class RadarView extends View {

    private int backgroundColor; //背景色
    private int radius;
    private int circleColor;
    private int startColor;
    private int endColor;
    private int lineColor;
    private int circleNum;
    private Paint backgroundPaint;
    private Paint circlePaint;
    private Shader gradient;
    private Matrix matrix;
    private int rotate = 0;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViewAttr(context, attrs);

        init();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rotate += 3;
                postInvalidate();
                matrix.reset();
                matrix.preRotate(rotate, 0, 0);
                handler.postDelayed(this, 20);
            }
        }, 2000);
    }


    Handler handler = new Handler();

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);


        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2);


        gradient = new SweepGradient(0, 0, startColor, endColor);


        matrix = new Matrix();



    }

    //初始化属性
    private void initViewAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.radar_style);
            backgroundColor = typeArr.getColor(R.styleable.radar_style_backgroundColor, Color.BLACK);
            radius = typeArr.getInteger(R.styleable.radar_style_radius, 200);
            circleColor = typeArr.getColor(R.styleable.radar_style_circleColor, Color.RED);
            startColor = typeArr.getColor(R.styleable.radar_style_startColor, Color.RED);
            endColor = typeArr.getColor(R.styleable.radar_style_endColor, Color.BLUE);
            lineColor = typeArr.getColor(R.styleable.radar_style_lineColor, Color.GREEN);
            circleNum = typeArr.getInteger(R.styleable.radar_style_circleNum, 9);
            typeArr.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.max(w / 2, h / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = Math.max(radius, widthSize);
        } else {
            width = radius + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = Math.max(radius, heightSize);
        } else {
            height = radius + getPaddingBottom() + getPaddingTop();
        }
        int maxVal = Math.min(height, width);
        setMeasuredDimension(maxVal, maxVal);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundPaint.setShader(null);
        canvas.translate(radius, radius);
        canvas.drawCircle(0, 0, radius, backgroundPaint);

        circlePaint.setColor(circleColor);
        for (int i = 0; i < circleNum; i++) {
            canvas.drawCircle(0, 0, (int)(radius * 1.0 / circleNum * (i + 1)), circlePaint);
        }


        circlePaint.setColor(lineColor);

        canvas.drawLine(-radius, 0, radius, 0, circlePaint);
        canvas.drawLine(0, - radius, 0, radius, circlePaint);

        backgroundPaint.setShader(gradient);
        canvas.concat(matrix);
        canvas.drawCircle(0, 0, radius, backgroundPaint);
    }
}
