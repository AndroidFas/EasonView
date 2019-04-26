package base.appUpdate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 * 水平进度条
 */
public class MyProgressBar extends View {
    private Context context;
    private float value = 0;
    private float tempValue = 0;
    private int color_fill = Color.parseColor("#0387d1");
    private int color_stroke = Color.parseColor("#b5000000");
    private int color_bg = Color.parseColor("#b5000000");//Color.WHITE;

    private boolean isRound = true; // 默认圆角

    private boolean needDownLoadText = false;
    public Paint mPaint;
    public Paint mPaint_downLoadText;
    public Paint.FontMetricsInt mFontMetrics;
    int rx = 25;
    int ry = 25;

    public void setNeedDownLoadText(boolean needDownLoadText) {
        this.needDownLoadText = needDownLoadText;
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (tempValue + 0.03 >= value) {
                tempValue = value;
                MyProgressBar.this.invalidate();
                return;
            }
            tempValue += 0.03;
            MyProgressBar.this.invalidate();
            handler.postDelayed(this, 4);
        }
    };

    /**
     * 值0-1 ( 带动画)
     *
     * @param value
     */
    public void setValue(float value) {
        handler.removeCallbacks(runnable);
        tempValue = 0;
        if (value > 1)
            value = 1;
        if (value < 0)
            value = 0;
        this.value = value;
        handler.post(runnable);

//		tempValue = value;
//		this.invalidate();
    }

    /**
     * 值0-1 ( 不带动画)
     *
     * @param value
     */
    public void setValue2(float value) {
        if (value > 1)
            value = 1;
        if (value < 0)
            value = 0;
        tempValue = value;
        this.invalidate();
    }

    public void setColor(int color_fill, int color_stroke, int color_bg) {
        this.color_fill = color_fill;
        this.color_stroke = color_stroke;
        this.color_bg = color_bg;
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProgressBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setWillNotDraw(false);//在onDraw函数始终不被调用添加
        mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint_downLoadText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_downLoadText.setStrokeWidth(4);
        mPaint_downLoadText.setTextSize(13);
        mPaint_downLoadText.setColor(Color.WHITE);
        mPaint_downLoadText.setTextAlign(Paint.Align.CENTER);
        mFontMetrics = mPaint_downLoadText.getFontMetricsInt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRound) {
            canvas.drawColor(getResources().getColor(android.R.color.transparent));

            mPaint.setColor(color_bg);
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), rx, ry, mPaint);
            mPaint.setColor(color_fill);
            canvas.drawRoundRect(new RectF(0, 0, getWidth() * tempValue, getHeight()), rx, ry, mPaint);
        } else {
            canvas.drawColor(color_bg);
            mPaint.setColor(color_fill);
            canvas.drawRect(0, 0, getWidth() * tempValue, getHeight(), mPaint);
        }
        if (needDownLoadText) {
            Rect targetRect = new Rect(0, 0, getWidth(), getHeight());
            int baseline = (targetRect.bottom + targetRect.top - mFontMetrics.bottom - mFontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            canvas.drawText("下载进度：" + (int) (tempValue * 100) + "%", targetRect.centerX(), baseline, mPaint_downLoadText);
        }


//		paint.setColor(color_stroke);
//		paint.setStyle(Style.STROKE);
//		paint.setStrokeWidth(4);
//		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

}
