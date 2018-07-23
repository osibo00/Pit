package productions.darthplagueis.pit.view;

import android.content.Context;
import android.view.View;

/**
 * Class that is used create the point objects that will be manipulated
 * by PitView. The X and Y field variables hold values that correspond
 * to the point object's position in PitView.
 */
public class PitPoint extends View {

    private int xPosition;
    private int yPosition;

    public PitPoint(Context context) {
        super(context);
    }

    public PitPoint(Context context, int xPosition, int yPosition) {
        super(context);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}
