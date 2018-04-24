package kevinstar1.edu.cn.dagger2expresstest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MyCompassView extends View {

    private Paint paint;
    private float position = 0;

    public MyCompassView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
    }

    public MyCompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPoint = getMeasuredHeight()/2;
        int yPoint = getMeasuredHeight()/2;


        canvas.drawText("方向传感器",50,50,paint);

        float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
        canvas.drawCircle(xPoint, yPoint, radius, paint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        // 3.143 is a good approximation for the circle
        canvas.drawLine(
                xPoint,
                yPoint,
                (float) (xPoint + radius
                        * Math.sin((double) (-position) / 180 * Math.PI)),
                (float) (yPoint - radius
                        * Math.cos((double) (-position) / 180 * Math.PI)), paint);

        canvas.drawText(String.valueOf(position), xPoint, yPoint, paint);

    }

    public void updateData(float position) {
        this.position = position;
        invalidate();
    }
}
