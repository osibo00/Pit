package productions.darthplagueis.pit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import productions.darthplagueis.pit.R;

public class PitView extends View {

    private Paint axes, line, point;
    private int axesColor, lineColor, pointColor;

    public PitView(Context context) {
        super(context);
        init(context, null);
    }

    public PitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setUpPitGraph(canvas);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.PitView
            );

            axesColor = attributes.getColor(R.styleable.PitView_axesColor, Color.BLACK);
            lineColor = attributes.getColor(
                    R.styleable.PitView_lineColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)
            );
            pointColor = attributes.getColor(
                    R.styleable.PitView_pointColor,
                    ContextCompat.getColor(context, R.color.colorAccent)
            );

            attributes.recycle();
        } else {
            axesColor = Color.BLACK;
            lineColor = ContextCompat.getColor(context, R.color.colorPrimary);
            pointColor = ContextCompat.getColor(context, R.color.colorAccent);
        }

        axes = new Paint(Paint.ANTI_ALIAS_FLAG);
        axes.setColor(axesColor);
        axes.setStyle(Paint.Style.STROKE);
        axes.setStrokeWidth(4);

        line = new Paint(Paint.ANTI_ALIAS_FLAG);
        line.setColor(lineColor);
        line.setStrokeWidth(8);

        point = new Paint(Paint.ANTI_ALIAS_FLAG);
        point.setColor(pointColor);
        point.setStyle(Paint.Style.FILL);
    }

    private void setUpPitGraph(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int centerWidth = canvas.getWidth() / 2;
        int centerHeight = canvas.getHeight() / 2;

        canvas.drawColor(Color.LTGRAY);
        canvas.drawLine(centerWidth, 0, centerWidth, height, axes);
        canvas.drawLine(0, centerHeight, width, centerHeight, axes);
    }

    public int getAxesColor() {
        return axesColor;
    }

    public void setAxesColor(int axesColor) {
        this.axesColor = axesColor;
        invalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
        invalidate();
    }
}

