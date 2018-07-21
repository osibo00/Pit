package productions.darthplagueis.pit.view;

import android.content.Context;
import android.view.View;

public class PitPoint extends View {

    private float xPosition;
    private float yPosition;

    public PitPoint(Context context) {
        super(context);
    }

    public PitPoint(Context context, float xPosition, float yPosition) {
        super(context);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public float getXPosition() {
        return xPosition;
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }
}
