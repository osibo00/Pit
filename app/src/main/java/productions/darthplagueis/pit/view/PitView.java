package productions.darthplagueis.pit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import productions.darthplagueis.pit.R;
import productions.darthplagueis.pit.util.PitPointComparator;
import productions.darthplagueis.pit.util.PitViewContract;
import productions.darthplagueis.pit.util.PitViewHelper;

public class PitView extends View implements PitViewContract {

    private Paint axisPaint, pointPaint, linePaint;
    private int axesColor, lineColor, pointColor;
    private float centerWidth, centerHeight;
    private boolean arePitPointsCreated;

    private final List<PitPoint> pointList = new ArrayList<>(5);

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

        createInitialPitPoints(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setUpPitGraph(canvas);
        setUpPitPoints(canvas);
        setUpPointsLine(canvas);
    }

    @Override
    public void addPitPoint() {
        pointList.add(new PitPoint(getContext(), centerWidth, centerHeight));
        Collections.sort(pointList, new PitPointComparator());
        invalidate();
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

        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(axesColor);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(4);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(8);
    }

    private void createInitialPitPoints(int widthMeasureSpec, int heightMeasureSpec) {
        if (!arePitPointsCreated) {
            PitViewHelper helper = PitViewHelper.getINSTANCE();
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            for (int i = 0; i < 5; i++) {
                PitPoint point = new PitPoint(
                        getContext(),
                        helper.randomFloat(0, width),
                        helper.randomFloat(0, height)
                );
                pointList.add(point);
            }

            Collections.sort(pointList, new PitPointComparator());
            arePitPointsCreated = true;
        }
    }

    private void setUpPitGraph(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        centerWidth = width / 2;
        centerHeight = height / 2;

        canvas.drawColor(Color.LTGRAY);
        canvas.drawLine(centerWidth, 0, centerWidth, height, axisPaint);
        canvas.drawLine(0, centerHeight, width, centerHeight, axisPaint);
    }

    private void setUpPitPoints(Canvas canvas) {
        for (PitPoint point : pointList) {
            canvas.drawCircle(point.getXPosition(), point.getYPosition(), 16, pointPaint);
        }
    }

    private void setUpPointsLine(Canvas canvas) {
        int n = pointList.size();

        if (n > 1) {
            Path linePath = new Path();

            for (int i = 1; i < n; i++) {
                linePath.moveTo(pointList.get(i - 1).getXPosition(), pointList.get(i - 1).getYPosition());
                linePath.lineTo(pointList.get(i).getXPosition(), pointList.get(i).getYPosition());
            }

            linePath.close();
            canvas.drawPath(linePath, linePaint);
        }
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
