package customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SquareIndicatorView extends View {

    private static final int DEFAULT_SIZE = 200 ;

    private Paint paint;
    private Paint textPaint;
    private Paint clipPaint;

    private Paint indicatorPaint;

    private Path clipPath;
    private Path idicatorPath;

    private  int percent = 50;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);


        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setLetterSpacing(0.07F);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(100f);

        clipPaint = new Paint();
        clipPaint.setAntiAlias(true);
        clipPaint.setColor(Color.WHITE);


        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setColor(Color.RED);
        indicatorPaint.setStrokeCap(Paint.Cap.BUTT);
        indicatorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private static int getMeasurementSize(int measureSpec, int defaultSize) {

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return size;

            case MeasureSpec.AT_MOST:
                return Math.min(defaultSize, size);

            case MeasureSpec.UNSPECIFIED:
            default:
                return defaultSize;

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasurementSize(widthMeasureSpec, DEFAULT_SIZE);
        int height = getMeasurementSize(heightMeasureSpec, DEFAULT_SIZE);

        setMeasuredDimension(width, height);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

        int squareWidth = getWidth();
        if(getHeight() < squareWidth)squareWidth = getHeight();

        float left = (getWidth() - squareWidth)/2;
        float top = (getHeight() - squareWidth) / 2;
        float right = left + squareWidth;
        float bottom = top + squareWidth;

        if(clipPath == null){

            float clipWidth = squareWidth * 0.8f;

            float cLeft = (getWidth() - clipWidth) / 2;
            float cTop = (getHeight() - clipWidth) / 2;
            float cRight = cLeft  + clipWidth;
            float cBottom = cTop + clipWidth;

            clipPath = new Path();
            clipPath.addRect(cLeft, cTop, cRight, cBottom, Path.Direction.CW);

            this.idicatorPath = constructIndicatorPath(percent,left,top, squareWidth,squareWidth * 0.2f);
        }

        canvas.drawRect(left,top,right,bottom,paint);

        indicatorPaint.setStrokeWidth(squareWidth * 0.2f);
        canvas.drawPath(this.idicatorPath, indicatorPaint);

        canvas.drawPath(clipPath,clipPaint);

        canvas.drawText(percent + "%", getWidth()/2, getHeight()/2,textPaint);
    }

    private Path constructIndicatorPath(float percent, float left, float top, float size, float strokeWidth){

        float percentLength = (percent * (size*4)) / 100;

        Path iPath = new Path();

        float x = left;
        float y = top;
        int side;
        int sideNumber = (int)((percentLength / size)+0.5);

        iPath.moveTo(left, top);
        for(int i=0;i<sideNumber;i++){
            side = i+1;
            float sideSize = percentLength > size ? size : percentLength;
            percentLength -=sideSize;

            if(side == 1){ y = top + sideSize;}
            if(side == 2){iPath.moveTo(left,top + size - strokeWidth/2);x = left + sideSize; y = top +size - strokeWidth/2;}
            if(side == 3){y = sideSize; x = left + size;}
            if(side == 4){y = top; x = sideSize;}

            iPath.lineTo(x, y);

        }

        return iPath;
    }
}
