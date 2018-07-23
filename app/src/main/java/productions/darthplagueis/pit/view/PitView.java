package productions.darthplagueis.pit.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import productions.darthplagueis.pit.R;
import productions.darthplagueis.pit.view.util.PitPointComparator;
import productions.darthplagueis.pit.view.util.PitViewContract;
import productions.darthplagueis.pit.view.util.PitViewHelper;

/**
 * Class that renders the graph-like view in MainActivity. It is instantiated in the activity's
 * layout XML file. This class instantiates PitPoint objects and then sets their state and behavior.
 * Implements the PitViewContract interface that enables the user to add more points.
 */
public class PitView extends View implements PitViewContract {

    private Paint axisPaint;
    private Paint pointPaint;
    private Paint linePaint;
    private int canvasColor, axesColor, lineColor, pointColor;

    private int viewWidth;
    private int viewHeight;
    private float centerX;
    private float centerY;

    // Data structure that stores the PitPoint objects. Is instantiated with an
    // initial capacity of five since we know only five points will be created
    // at first. An arrayList is used due to it's speed of getting the objects it is storing.
    private final List<PitPoint> pointList = new ArrayList<>(5);

    // Point that will be draggable across PitView.
    private PitPoint selectedPoint = null;

    public PitView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Provides the view dimensions as imposed by the parent layout.
     * The width and height measurements are then passed into
     * setMeasuredDimension() so as to store the view dimensions.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        // Returns the smaller of the two values in order to create
        // a square. The final shape will be determined by the constraints
        // set in the layout file.
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    /**
     * This method allows you to catch the current height and width of the custom view
     * to properly adjust the rendering code. The values gotten here will be used
     * to create the PitPoint and canvas objects. It is called after onMeasure() and
     * is not called repeatedly like onMeasure() may be.
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        viewHeight = h;
        centerX = w / 2f;
        centerY = h / 2f;

        createInitialPitPoints();
    }

    /**
     * This method provides a canvas object that will be used to render PitView.
     * Here is where the PitView axes, points and interconnecting lines are rendered.
     * In order to optimize the view there is an attempt to allocate as few objects as
     * possible in the drawing process to prevent stutter from garbage collection events.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setUpPitGraph(canvas);
        setUpPitPoints(canvas);
        setUpPointsLine(canvas);
    }

    /**
     * Accepts user interaction in the form of touch events. These touch events are
     * then interpreted and used to make the PitPoints draggable across PitView.
     * Upon pressing down on a point the method getSelectedPoint() attempts to
     * return the point that was selected. If it is successful then the point can be
     * dragged around within the view bounds of the view. When the point is let go
     * the new position was already saved and the selected point is null once more.
     *
     * @param event
     * @return true
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // The selectedPoint is retrieved here so that more than one
                // point is not able to be dragged at a time.
                selectedPoint = getSelectedPoint(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectedPoint != null) {

                    // The value of 16 is used because that is the radius of the
                    // points on the screen. The code checks that the point does
                    // not go out of bounds.
                    if ((event.getX() > 16 && event.getX() < viewWidth - 16) &&
                            (event.getY() > 16 && event.getY() < viewHeight - 16)) {
                        selectedPoint.setXPosition((int) event.getX());
                        selectedPoint.setYPosition((int) event.getY());

                        // The arrayList is sorted by the X positions of the points
                        // in order to facilitate an interconnecting line between
                        // the points that would resemble a line graph.
                        Collections.sort(pointList, new PitPointComparator());

                        // Invalidate is used instead of invalidate(dirty Rect) because
                        // of the use of hardware acceleration starting in API 14.
                        // Hardware acceleration has reduced the importance of
                        // the dirty rectangle and the rectangle is ignored completely
                        // starting in API 21.
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                selectedPoint = null;
                break;
        }
        return true;
    }

    /**
     * This method is provided by the implemented interface and provides the
     * ability to add additional points to PitView. The centerX and centerY values
     * are used to place the new point at the intersection of the axes.
     */
    @Override
    public void addPitPoint() {
        pointList.add(new PitPoint(getContext(), (int) centerX, (int) centerY));
        Collections.sort(pointList, new PitPointComparator());
        invalidate();
    }

    /**
     * An initializer method called by the constructor to allocate supplementary
     * objects before onDraw() is called. First it sets values to the variables that will
     * be used to style PitView using either default attributes or those that can set in the
     * inflated layout file. Then it instantiates the paint objects that will be used by the canvas
     * object.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.PitView,
                defStyleAttr,
                R.style.DefaultPitViewStyle
        );

        try {
            canvasColor = a.getColor(R.styleable.PitView_canvasColor, defStyleAttr);
            axesColor = a.getColor(R.styleable.PitView_axesColor, defStyleAttr);
            lineColor = a.getColor(R.styleable.PitView_lineColor, defStyleAttr);
            pointColor = a.getColor(R.styleable.PitView_pointColor, defStyleAttr);
        } finally {
            a.recycle();
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

    /**
     * Using a helper class called PitViewHelper the initial five PitPoint
     * objects are created using random X and Y positions located within
     * the view bounds. The points are added to the arrayList so that
     * they can be manipulated later.
     */
    private void createInitialPitPoints() {
        PitViewHelper helper = PitViewHelper.getINSTANCE();

        for (int i = 0; i < 5; i++) {
            PitPoint point = new PitPoint(
                    getContext(),

                    // The min and max values are offset by 16 so that
                    // the points are not placed at the very edge of the
                    // view bounds.
                    helper.randomInt(16, viewWidth - 16),
                    helper.randomInt(16, viewHeight - 16)
            );
            pointList.add(point);
        }

        Collections.sort(pointList, new PitPointComparator());
    }

    /**
     * Renders the graph-like background of PitView. Receives a canvas object
     * from onDraw() which colors the background, draws a vertical line and a
     * horizontal line.
     *
     * @param canvas
     */
    private void setUpPitGraph(@NonNull Canvas canvas) {
        canvas.drawColor(canvasColor);
        canvas.drawLine(centerX, 0, centerX, viewHeight, axisPaint);
        canvas.drawLine(0, centerY, viewWidth, centerY, axisPaint);
    }

    /**
     * Iterates through the arrayList and draws point based off of the
     * X and Y positions of the earlier created PitPoints.
     *
     * @param canvas
     */
    private void setUpPitPoints(@NonNull Canvas canvas) {

        // A for each loop is used to improve performance.
        for (PitPoint point : pointList) {
            canvas.drawCircle(point.getXPosition(), point.getYPosition(), 16, pointPaint);
        }
    }

    /**
     * Iterates through the arrayList to provide point positions and draws
     * the lines that interconnect the points in PitView using a path object.
     * Checks if the size of the arrayList is greater than one because if
     * there is less than two points then no lines can be rendered.
     *
     * @param canvas
     */
    private void setUpPointsLine(@NonNull Canvas canvas) {
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

    /**
     * Occurs after a touch event on the screen. This method is passed the
     * touch event X and Y positions. It then iterates through the arrayList
     * checking to see if a PitPoint is within range of that touch event.
     * If a point is within that event range then it is returned, otherwise null
     * is returned.
     *
     * @param x
     * @param y
     * @return pitpoint or null
     */
    @Nullable
    private PitPoint getSelectedPoint(float x, float y) {
        for (PitPoint point : pointList) {
            int xPosition = point.getXPosition();
            int yPosition = point.getYPosition();

            // The value 32 is used because that is twice the radius of the points
            // on screen. This makes them easier to select than if the radius value
            // was used.
            if ((xPosition > x - 32 && xPosition < x + 32) &&
                    (yPosition > y - 32 && yPosition < y + 32)) {
                return point;
            }
        }
        return null;
    }


    /**
     * Getters and setters provided for the styleable attributes as suggested
     * by the Android docs. If a new value is set then the view must
     * invalidated and redrawn.
     */
    public int getCanvasColor() {
        return canvasColor;
    }

    public void setCanvasColor(int canvasColor) {
        this.canvasColor = canvasColor;
        invalidate();
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
