package productions.darthplagueis.pit.view;

import android.content.Context;
import android.view.View;

public class PitPoint extends View {

    private float xPosition;
    private float yPosition;

    public PitPoint(Context context, float xPosition, float yPosition) {
        super(context);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }
}
