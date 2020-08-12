package com.arcore.arcore_test.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawLineCanvas extends View {

    private Paint paintLine, paintBackground;
    private Path touchPath;

    private Bitmap b;

    public DrawLineCanvas(Context context) {
        super(context);
    }

    public DrawLineCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint(Paint.DITHER_FLAG);
        paintBackground.setColor(Color.TRANSPARENT);

        paintLine = new Paint();
        paintLine.setColor(Color.GREEN);
        paintLine.setAntiAlias(true);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(3);

        touchPath = new Path();
    }

    public DrawLineCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                touchPath.lineTo(touchX, touchY);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(b, 0, 0, paintBackground);
        canvas.drawPath(touchPath, paintLine);
    }
}
