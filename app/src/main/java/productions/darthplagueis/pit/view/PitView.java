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
import android.view.MotionEvent;
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
    private int canvasColor, axesColor, lineColor, pointColor;
    private int viewWidth, viewHeight, centerWidth, centerHeight;
    private boolean arePitPointsCreated;

    private final List<PitPoint> pointList = new ArrayList<>(5);
    private PitPoint selectedPoint = null;

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

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        centerWidth = viewWidth / 2;
        centerHeight = viewHeight / 2;

        createInitialPitPoints();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setUpPitGraph(canvas);
        setUpPitPoints(canvas);
        setUpPointsLine(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                selectedPoint = getSelectedPoint(event.getX(), event.getY());

                if (selectedPoint != null) {
                    if ((event.getX() > 16 && event.getX() < viewWidth - 16) &&
                            (event.getY() > 16 && event.getY() < viewHeight - 16)) {
                        selectedPoint.setXPosition(event.getX());
                        selectedPoint.setYPosition(event.getY());

                        Collections.sort(pointList, new PitPointComparator());
                        invalidate();
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                selectedPoint = null;
                return true;
            default:
                return false;
        }
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

            canvasColor = attributes.getColor(R.styleable.PitView_canvasColor, Color.LTGRAY);
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
            canvasColor = Color.LTGRAY;
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

    private void createInitialPitPoints() {
        if (!arePitPointsCreated) {
            PitViewHelper helper = PitViewHelper.getINSTANCE();

            for (int i = 0; i < 5; i++) {
                PitPoint point = new PitPoint(
                        getContext(),
                        helper.randomFloat(16, viewWidth),
                        helper.randomFloat(16, viewHeight)
                );
                pointList.add(point);
            }

            Collections.sort(pointList, new PitPointComparator());
            arePitPointsCreated = true;
        }
    }

    private void setUpPitGraph(Canvas canvas) {
        canvas.drawColor(canvasColor);
        canvas.drawLine(centerWidth, 0, centerWidth, viewHeight, axisPaint);
        canvas.drawLine(0, centerHeight, viewWidth, centerHeight, axisPaint);
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

    @Nullable
    private PitPoint getSelectedPoint(float x, float y) {
        for (PitPoint point : pointList) {
            float xPosition = point.getXPosition();
            float yPosition = point.getYPosition();
            if ((xPosition > x - 32 && xPosition < x + 32) &&
                    (yPosition > y - 32 && yPosition < y + 32)) {
                return point;
            }
        }
        return null;
    }

    public int getAxesColor() {
        return axesColor;
    }

    public void setAxesColor(int axesColor) {
        this.axesColor = axesColor;
        axisPaint.setColor(axesColor);
        invalidate();
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
        pointPaint.setColor(pointColor);
        invalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        linePaint.setColor(lineColor);
        invalidate();
    }
}
